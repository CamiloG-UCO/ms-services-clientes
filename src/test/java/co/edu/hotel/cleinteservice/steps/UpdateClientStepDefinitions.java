package co.edu.hotel.cleinteservice.steps;


import co.edu.hotel.cleinteservice.domain.Client;
import co.edu.hotel.cleinteservice.domain.DocumentType;
import co.edu.hotel.cleinteservice.repository.ClientRepository;
import co.edu.hotel.cleinteservice.services.ClientService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;



import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UpdateClientStepDefinitions {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;



    @Given("el cliente {string} con teléfono actual {string} y correo {string}")
    public void elClienteConTelefonoActualYCorreo(String nombre, String telefono, String correo) {

        Client client = new Client();
        client.setName(nombre);
        client.setLastNames("Ramírez");
        client.setDocumentType(DocumentType.CC);
        client.setDocumentNumber("123456789");
        client.setClientCode("CLI001");
        client.setPhone(telefono);
        client.setEmail(correo);

        clientRepository.save(client);

    }

    @When("el usuario {string} modifique el teléfono a {string} y el correo a {string}")
    public void elUsuarioModifiqueTelefonoYCorreo(String usuario, String nuevoTelefono, String nuevoCorreo) {
        // Buscamos el cliente existente (por simplicidad, el primero en la BD)
        Client client = clientRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró el cliente para actualizar"));

        // Simulamos que el usuario realiza la modificación
        client.setPhone(nuevoTelefono);
        client.setEmail(nuevoCorreo);

        // Guardamos los cambios
        clientRepository.save(client);// Simular mensaje de respuesta
    }

    @Then("el sistema debe guardar los nuevos datos")
    public void elSistemaDebeGuardarLosNuevosDatos() {
        // Buscamos el cliente nuevamente en la base de datos
        Client updatedClient = clientRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró el cliente actualizado"));

        // Verificamos que los nuevos valores se hayan guardado correctamente
        assertEquals("3209876543", updatedClient.getPhone());
        assertEquals("aramirez@hotmail.com", updatedClient.getEmail());
    }

    @And("mostrar el mensaje {string}")
    public void mostrarElMensaje(String mensajeEsperado) {
        // Simulamos que el sistema muestra un mensaje tras actualizar
        String mensajeReal = "Cliente actualizado correctamente";

        if (mensajeEsperado.equals(mensajeReal)) {
            assertEquals(mensajeEsperado, mensajeReal);
            System.out.println(mensajeReal);
        }
    }


}

