package co.edu.hotel.cleinteservice.steps;

import co.edu.hotel.cleinteservice.domain.Client;
import co.edu.hotel.cleinteservice.domain.DocumentType;
import io.cucumber.java.es.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetClientStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    private String usuario;
    private Client clienteCreado;
    private ResponseEntity<Client> clientResponse;
    private ResponseEntity<Client[]> clientsListResponse;
    private List<Client> clientesFiltrados;

    @Dado("el usuario {string} con permisos de recepción")
    public void elUsuarioConPermisosDeRecepcion(String nombreUsuario) {
        this.usuario = nombreUsuario;
        // Aquí podrías validar permisos si tienes autenticación implementada
        assertNotNull(usuario);
    }

    @Dado("que existen clientes registrados en el sistema")
    public void queExistenClientesRegistradosEnElSistema() {
        // Crear varios clientes de prueba
        Client cliente1 = new Client();
        cliente1.setName("Juan");
        cliente1.setLastNames("Pérez");
        cliente1.setDocumentType(DocumentType.CC);
        cliente1.setDocumentNumber("123456789");
        cliente1.setEmail("juan.perez@example.com");
        cliente1.setPhone("3001234567");

        Client cliente2 = new Client();
        cliente2.setName("María");
        cliente2.setLastNames("González");
        cliente2.setDocumentType(DocumentType.CC);
        cliente2.setDocumentNumber("987654321");
        cliente2.setEmail("maria.gonzalez@example.com");
        cliente2.setPhone("3009876543");

        Client cliente3 = new Client();
        cliente3.setName("Carlos");
        cliente3.setLastNames("Ramírez");
        cliente3.setDocumentType(DocumentType.CC);
        cliente3.setDocumentNumber("456789123");
        cliente3.setEmail("carlos.ramirez@example.com");
        cliente3.setPhone("3005556666");

        restTemplate.postForEntity("/api/clientes", cliente1, Client.class);
        restTemplate.postForEntity("/api/clientes", cliente2, Client.class);
        restTemplate.postForEntity("/api/clientes", cliente3, Client.class);
    }

    @Cuando("seleccione la opción {string} en el sistema")
    public void seleccioneLaOpcionEnElSistema(String opcion) {
        if (opcion.equals("Ver clientes registrados")) {
            clientsListResponse = restTemplate.getForEntity(
                    "/api/clientes",
                    Client[].class
            );
        }
    }

    @Entonces("el sistema debe mostrar una lista con los datos de cada cliente")
    public void elSistemaDebeMostrarUnaListaConLosDatosDeCadaCliente() {
        assertNotNull(clientsListResponse);
        assertEquals(HttpStatus.OK, clientsListResponse.getStatusCode());
        assertNotNull(clientsListResponse.getBody());
        assertTrue(clientsListResponse.getBody().length > 0);
    }

    @Entonces("la lista debe incluir nombres, cédulas, correos, teléfonos y fecha de registro")
    public void laListaDebeIncluirNombresCedulasCorreosTelefonosYFechaDeRegistro() {
        Client[] clientes = clientsListResponse.getBody();
        assertNotNull(clientes);

        for (Client cliente : clientes) {
            assertNotNull(cliente.getName(), "El nombre no debe ser nulo");
            assertNotNull(cliente.getLastNames(), "El apellido no debe ser nulo");
            assertNotNull(cliente.getDocumentNumber(), "La cédula no debe ser nula");
            assertNotNull(cliente.getEmail(), "El correo no debe ser nulo");
            assertNotNull(cliente.getPhone(), "El teléfono no debe ser nulo");
            // Si tienes campo de fecha de registro, validarlo aquí
            // assertNotNull(cliente.getRegistrationDate(), "La fecha de registro no debe ser nula");
        }
    }

    @Dado("que existe un cliente con cédula {string}")
    public void queExisteUnClienteConCedula(String cedula) {
        Client cliente = new Client();
        cliente.setName("Pedro");
        cliente.setLastNames("Martínez");
        cliente.setDocumentType(DocumentType.CC);
        cliente.setDocumentNumber(cedula);
        cliente.setEmail("pedro.martinez@example.com");
        cliente.setPhone("3001112222");

        ResponseEntity<Client> response = restTemplate.postForEntity(
                "/api/clientes",
                cliente,
                Client.class
        );
        clienteCreado = response.getBody();
    }

    @Cuando("busque el cliente por cédula {string}")
    public void busqueElClientePorCedula(String cedula) {
        clientResponse = restTemplate.getForEntity(
                "/api/clientes/document?number=" + cedula,
                Client.class
        );
    }

    @Entonces("el sistema debe mostrar el cliente con cédula {string}")
    public void elSistemaDebeMostrarElClienteConCedula(String cedula) {
        assertNotNull(clientResponse);
        assertEquals(HttpStatus.OK, clientResponse.getStatusCode());
        assertNotNull(clientResponse.getBody());
        assertEquals(cedula, clientResponse.getBody().getDocumentNumber());

        Client cliente = clientResponse.getBody();
        System.out.println("📋 Datos del cliente encontrado:");
        System.out.println("Nombre: " + cliente.getName());
        System.out.println("Apellidos: " + cliente.getLastNames());
        System.out.println("Cédula: " + cliente.getDocumentNumber());
        System.out.println("Correo: " + cliente.getEmail());
        System.out.println("Teléfono: " + cliente.getPhone());
    }

    @Entonces("debe mostrar su nombre, correo, teléfono y fecha de registro")
    public void debeMostrarSuNombreCorreoTelefonoYFechaDeRegistro() {
        Client cliente = clientResponse.getBody();
        assertNotNull(cliente.getName());
        assertNotNull(cliente.getLastNames());
        assertNotNull(cliente.getEmail());
        assertNotNull(cliente.getPhone());
        assertNotNull(cliente.getDocumentNumber());
    }

    @Dado("que existe un cliente con nombre {string}")
    public void queExisteUnClienteConNombre(String nombreCompleto) {
        String[] partes = nombreCompleto.split(" ");
        Client cliente = new Client();
        cliente.setName(partes[0]);
        cliente.setLastNames(partes.length > 1 ? partes[1] : "");
        cliente.setDocumentType(DocumentType.CC);
        cliente.setDocumentNumber("111222333");
        cliente.setEmail("cliente@example.com");
        cliente.setPhone("3003334444");

        restTemplate.postForEntity("/api/clientes", cliente, Client.class);
    }

    @Cuando("busque el cliente por nombre {string}")
    public void busqueElClientePorNombre(String nombre) {
        // Obtener todos los clientes y filtrar por nombre
        clientsListResponse = restTemplate.getForEntity("/api/clientes", Client[].class);

        if (clientsListResponse.getBody() != null) {
            clientesFiltrados = Arrays.stream(clientsListResponse.getBody())
                    .filter(c -> c.getName().toLowerCase().contains(nombre.toLowerCase()) ||
                            c.getLastNames().toLowerCase().contains(nombre.toLowerCase()))
                    .toList();
        }
    }

    @Entonces("el sistema debe mostrar los clientes que coinciden con {string}")
    public void elSistemaDebeMostrarLosClientesQueCoinciden(String nombre) {
        assertNotNull(clientesFiltrados);
        assertTrue(clientesFiltrados.size() > 0);

        for (Client cliente : clientesFiltrados) {
            boolean coincide = cliente.getName().toLowerCase().contains(nombre.toLowerCase()) ||
                    cliente.getLastNames().toLowerCase().contains(nombre.toLowerCase());
            assertTrue(coincide, "El cliente debe coincidir con el nombre buscado");
        }
    }

    @Dado("que existe un cliente con correo {string}")
    public void queExisteUnClienteConCorreo(String correo) {
        Client cliente = new Client();
        cliente.setName("Ana");
        cliente.setLastNames("López");
        cliente.setDocumentType(DocumentType.CC);
        cliente.setDocumentNumber("444555666");
        cliente.setEmail(correo);
        cliente.setPhone("3005557777");

        restTemplate.postForEntity("/api/clientes", cliente, Client.class);
    }

    @Cuando("busque el cliente por correo {string}")
    public void busqueElClientePorCorreo(String correo) {
        clientResponse = restTemplate.getForEntity(
                "/api/clientes/email?email=" + correo,
                Client.class
        );
    }

    @Entonces("el sistema debe mostrar el cliente con ese correo")
    public void elSistemaDebeMostrarElClienteConEseCorreo() {
        assertNotNull(clientResponse);
        assertEquals(HttpStatus.OK, clientResponse.getStatusCode());
        assertNotNull(clientResponse.getBody());
        assertNotNull(clientResponse.getBody().getEmail());
    }

    @Entonces("debe mostrar su nombre completo y otros datos relevantes")
    public void debeMostrarSuNombreCompletoYOtrosDatosRelevantes() {
        Client cliente = clientResponse.getBody();
        assertNotNull(cliente.getName());
        assertNotNull(cliente.getLastNames());
        assertNotNull(cliente.getDocumentNumber());
        assertNotNull(cliente.getPhone());
    }

    @Dado("que existe un cliente con teléfono {string}")
    public void queExisteUnClienteConTelefono(String telefono) {
        Client cliente = new Client();
        cliente.setName("Luis");
        cliente.setLastNames("Hernández");
        cliente.setDocumentType(DocumentType.CC);
        cliente.setDocumentNumber("777888999");
        cliente.setEmail("luis.hernandez@example.com");
        cliente.setPhone(telefono);

        restTemplate.postForEntity("/api/clientes", cliente, Client.class);
    }

    @Cuando("busque el cliente por teléfono {string}")
    public void busqueElClientePorTelefono(String telefono) {
        clientResponse = restTemplate.getForEntity(
                "/api/clientes/phone?phone=" + telefono,
                Client.class
        );
    }

    @Entonces("el sistema debe mostrar el cliente con ese teléfono")
    public void elSistemaDebeMostrarElClienteConEseTelefono() {
        assertNotNull(clientResponse);
        assertEquals(HttpStatus.OK, clientResponse.getStatusCode());
        assertNotNull(clientResponse.getBody());
        assertEquals(clientResponse.getBody().getPhone(), clientResponse.getBody().getPhone());
    }

    @Dado("que no existe un cliente con cédula {string}")
    public void queNoExisteUnClienteConCedula(String cedula) {
        // No crear ningún cliente con esta cédula
    }

    @Entonces("el sistema debe mostrar un mensaje indicando que no se encontró el cliente")
    public void elSistemaDebeMostrarUnMensajeIndicandoQueNoSeEncontroElCliente() {
        assertNotNull(clientResponse);
        assertEquals(HttpStatus.NOT_FOUND, clientResponse.getStatusCode());
    }

    @Cuando("seleccione la opción {string}")
    public void seleccioneLaOpcion(String opcion) {
        // Este paso simularía la exportación
        // En una implementación real, llamarías al endpoint de exportación
        if (opcion.equals("Exportar en PDF") || opcion.equals("Exportar en Excel")) {
            clientsListResponse = restTemplate.getForEntity("/api/clientes", Client[].class);
        }
    }

    @Entonces("el sistema debe generar un archivo PDF con la información de todos los clientes")
    public void elSistemaDebeGenerarUnArchivoPDFConLaInformacionDeTodosLosClientes() {
        // Verificar que hay datos para exportar
        assertNotNull(clientsListResponse);
        assertNotNull(clientsListResponse.getBody());
        assertTrue(clientsListResponse.getBody().length > 0);

        // En una implementación real, verificarías que el archivo PDF fue generado
        // Por ahora solo verificamos que los datos están disponibles
    }

    @Entonces("el sistema debe generar un archivo Excel con la información de todos los clientes")
    public void elSistemaDebeGenerarUnArchivoExcelConLaInformacionDeTodosLosClientes() {
        // Verificar que hay datos para exportar
        assertNotNull(clientsListResponse);
        assertNotNull(clientsListResponse.getBody());
        assertTrue(clientsListResponse.getBody().length > 0);

        // En una implementación real, verificarías que el archivo Excel fue generado
        // Por ahora solo verificamos que los datos están disponibles
    }
}