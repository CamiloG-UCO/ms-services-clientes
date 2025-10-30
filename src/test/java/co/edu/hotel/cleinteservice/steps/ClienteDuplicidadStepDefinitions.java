package co.edu.hotel.cleinteservice.steps;

import co.edu.hotel.cleinteservice.domain.Client;
import co.edu.hotel.cleinteservice.domain.DocumentType;
import co.edu.hotel.cleinteservice.repository.ClientRepository;
import co.edu.hotel.cleinteservice.services.ClientService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class ClienteDuplicidadStepDefinitions {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    private Client clienteExistente;
    private Exception excepcionCapturada;

    @Before
    public void setup() {
        clientRepository.deleteAll(); // limpiar base de datos antes de cada escenario
        excepcionCapturada = null;
    }

    @Given("existe un cliente registrado con los siguientes datos:")
    public void existeUnClienteRegistradoConLosSiguientesDatos(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> datos = rows.get(0);

        clienteExistente = new Client();
        clienteExistente.setName(datos.get("nombre"));
        clienteExistente.setLastNames(datos.get("apellidos"));
        clienteExistente.setDocumentType(DocumentType.valueOf(datos.get("documentoTipo")));
        clienteExistente.setDocumentNumber(datos.get("documentoNumero"));
        clienteExistente.setEmail(datos.get("correo"));
        clienteExistente.setPhone(datos.get("telefono"));
        clienteExistente.setClientCode("CL0001");

        clientRepository.save(clienteExistente);
    }

    @When("intento registrar un nuevo cliente con el mismo documento {string} y número {string}")
    public void intentoRegistrarUnNuevoClienteConElMismoDocumentoYNúmero(String tipo, String numero) {
        Client nuevo = new Client();
        nuevo.setName("Catalina López");
        nuevo.setDocumentType(DocumentType.valueOf(tipo));
        nuevo.setDocumentNumber(numero);
        nuevo.setEmail("catalina@hotel.com");
        nuevo.setPhone("3007654321");

        try {
            clientService.create(nuevo);
        } catch (Exception e) {
            excepcionCapturada = e;
        }
    }

    @Then("el sistema debe mostrar el mensaje {string}")
    public void validarMensaje(String mensajeEsperado) {
        assertNotNull(excepcionCapturada, "Se esperaba una excepción por duplicado, pero no ocurrió ninguna");
        assertTrue(
                excepcionCapturada.getMessage().contains(mensajeEsperado),
                "El mensaje esperado era: '" + mensajeEsperado + "', pero fue: '" + excepcionCapturada.getMessage() + "'"
        );
    }
}
