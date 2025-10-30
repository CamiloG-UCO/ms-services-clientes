package co.edu.hotel.cleinteservice.test.steps;

import co.edu.hotel.cleinteservice.domain.Client;
import co.edu.hotel.cleinteservice.domain.DocumentType;
import co.edu.hotel.cleinteservice.repository.ClientRepository;
import co.edu.hotel.cleinteservice.services.ClientService;
import co.edu.hotel.cleinteservice.exceptions.ClientAlreadyExistsException; // ¡Importar la nueva excepción!
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RegisterNewClientStepDefinitions {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    private Client clientToCreate;
    private Client createdClient;
    private Exception caughtException;

    private void cleanUp(String documentNumber) {
        clientRepository.findByDocumentNumber(documentNumber)
                .ifPresent(clientRepository::delete);
    }

    // --- GIVEN Steps ---

    @Given("un servicio para el registro de clientes está en funcionamiento")
    public void el_servicio_de_registro_de_clientes_esta_en_funcionamiento() {
        assertNotNull(clientService, "El ClientService no debería ser nulo.");
    }

    @Given("un servicio para el registro de clientes está en funcionamiento y no existe previamente un cliente con CC {string}")
    public void no_existe_cliente_previo(String documentNumber) {
        cleanUp(documentNumber);
        assertFalse(clientService.existsByDocumentNumber(documentNumber), "El cliente ya existe, la prueba no es limpia.");
    }

    @Given("ya existe un cliente registrado con tipo y número de documento {string} y {string}")
    public void ya_existe_un_cliente_registrado(String documentType, String documentNumber) {
        cleanUp(documentNumber);

        Client existing = new Client();
        existing.setDocumentType(DocumentType.valueOf(documentType));
        existing.setDocumentNumber(documentNumber);
        existing.setName("Existing");
        existing.setLastNames("User");
        existing.setEmail("existing@email.com");
        existing.setPhone("3000000000");

        clientService.create(existing);

        assertTrue(clientService.existsByDocumentNumber(documentNumber), "Fallo al crear el cliente de precondición.");
    }

    // --- WHEN Steps ---

    @When("el sistema cliente solicita la creación de un nuevo cliente con los siguientes datos:")
    public void el_sistema_solicita_creacion_cliente(DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        clientToCreate = new Client();
        String documentTypeString = data.get("documentType");

        try {
            clientToCreate.setDocumentType(DocumentType.valueOf(documentTypeString));
            clientToCreate.setDocumentNumber(data.get("documentNumber"));
            clientToCreate.setName(data.get("name"));
            clientToCreate.setLastNames(data.get("lastNames"));
            clientToCreate.setEmail(data.get("email"));
            clientToCreate.setPhone(data.get("phone"));

            // Si el cliente existe, esto lanzará ClientAlreadyExistsException
            createdClient = clientService.create(clientToCreate);
            caughtException = null;

        } catch (ClientAlreadyExistsException e) {
            // Captura la excepción de negocio esperada para el escenario de Falla
            caughtException = e;
            createdClient = null;
        } catch (Exception e) {
            // Captura cualquier otra excepción no esperada
            caughtException = e;
            createdClient = null;
        }
    }

    // --- THEN Steps ---

    @Then("el registro del cliente debe ser exitoso \\(Código 201 Created)")
    public void el_registro_del_cliente_es_exitoso() {
        assertNotNull(createdClient, "El objeto Cliente creado no debería ser nulo.");
        assertNull(caughtException, "No debería haber ocurrido una excepción en el registro exitoso.");
        cleanUp(clientToCreate.getDocumentNumber());
    }

    @And("el cliente recién creado debe tener el nombre completo {string}")
    public void el_cliente_tiene_nombre_completo(String nombreCompletoEsperado) {
        String nombreReal = createdClient.getName() + " " + createdClient.getLastNames();
        assertEquals(nombreCompletoEsperado, nombreReal, "El nombre completo no coincide.");
    }

    @And("la respuesta del sistema debe incluir un código de cliente que empiece por {string}")
    public void la_respuesta_incluye_codigo_cliente(String prefix) {
        String clientCode = createdClient.getClientCode();
        assertNotNull(clientCode, "El código de cliente no debe ser nulo.");
        assertTrue(clientCode.startsWith(prefix), "El código debe empezar por " + prefix);
    }

    @And("el cliente debe quedar almacenado en la base de datos")
    public void el_cliente_queda_almacenado() {
        Optional<Client> found = clientRepository.findByDocumentNumber(createdClient.getDocumentNumber());
        assertTrue(found.isPresent(), "El cliente debe ser encontrado en la base de datos.");
    }

    // --- THEN Steps para FALLA ---

    @Then("el registro del cliente debe fallar \\(Código 409 Conflict o 400 Bad Request)")
    public void el_registro_debe_fallar() {
        assertNotNull(caughtException, "Se esperaba una excepción de fallo.");
        // Verificación BDD: Asegurar que el fallo fue por la razón de negocio esperada (duplicidad)
        assertTrue(caughtException instanceof ClientAlreadyExistsException,
                "Se esperaba ClientAlreadyExistsException, se encontró: " + caughtException.getClass().getSimpleName());
    }

    @And("la respuesta del sistema debe contener un mensaje de error indicando que el cliente ya existe")
    public void la_respuesta_contiene_mensaje_de_error() {
        String errorMessage = caughtException.getMessage();
        assertTrue(errorMessage.contains("Ya existe un cliente registrado"),
                "El mensaje de error no indica duplicidad.");
    }

    @And("no se debe crear un nuevo cliente en la base de datos")
    public void no_se_debe_crear_un_nuevo_cliente() {
        // En este punto, solo el cliente de precondición debe existir con ese número de documento.
        Optional<Client> foundAttempts = clientRepository.findByDocumentNumber(clientToCreate.getDocumentNumber());

        // Se asume que el repositorio solo devuelve el cliente de precondición.
        assertTrue(foundAttempts.isPresent(), "El cliente de precondición debe seguir existiendo.");
    }
}
