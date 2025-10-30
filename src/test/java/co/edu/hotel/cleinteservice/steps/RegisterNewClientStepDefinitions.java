package co.edu.hotel.cleinteservice.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import org.springframework.boot.test.web.server.LocalServerPort; // <--- NUEVO IMPORT
import io.cucumber.spring.CucumberContextConfiguration;
import static io.restassured.RestAssured.port;

public class RegisterNewClientStepDefinitions {

    @LocalServerPort
    private int localServerPort;

    private String endpointPath = "/api/clientes"; // Guardamos solo el path
    private Response response;

    @Given("un endpoint de registro de clientes está disponible en \"{string}\"")
    public void unEndpointDeRegistroDeClientesEstaDisponibleEn(String url) {
        port = localServerPort;
        System.out.println("DEBUG: Aplicación corriendo en puerto: " + localServerPort);
    }

    @When("se envía una solicitud POST con el cuerpo:")
    public void seEnviaUnaSolicitudPOSTConElCuerpo(DataTable datosTabla) {
        // Mapear los datos de la tabla Gherkin a un mapa
        Map<String, String> clienteDataTabla = datosTabla.asMaps().get(0);

        // Adaptar los datos para el JSON, ya que 'name' y 'lastNames' están separados
        Map<String, String> bodyJson = new HashMap<>();
        bodyJson.put("documentType", clienteDataTabla.get("documentType"));
        bodyJson.put("documentNumber", clienteDataTabla.get("documentNumber"));
        bodyJson.put("name", clienteDataTabla.get("name"));
        bodyJson.put("lastNames", clienteDataTabla.get("lastNames"));
        bodyJson.put("email", clienteDataTabla.get("email"));
        bodyJson.put("phone", clienteDataTabla.get("phone"));

        // Llamada al ENDPOINT REAL
        response = given()
                .contentType(ContentType.JSON)
                .body(bodyJson)
                .when()
                .post(endpointPath);
    }

    // --- Pasos de Verificación (Then) ---

    @Then("el código de respuesta debe ser {int} \\(Created)")
    public void elCodigoDeRespuestaDebeSerCreated(int statusCode) {
        Assert.assertEquals("El código de respuesta HTTP no es 201 (Created).",
                statusCode, response.getStatusCode());
    }

    @Then("el cliente debe ser guardado con el nombre \"{string}\"")
    public void elClienteDebeSerGuardadoConElNombre(String nombreCompletoEsperado) {
        // Verificar que la respuesta contiene el nombre y apellido correcto
        String nombre = response.jsonPath().getString("name");
        String apellido = response.jsonPath().getString("lastNames");
        String nombreCompletoActual = nombre + " " + apellido;

        Assert.assertTrue("El nombre retornado no coincide o el registro falló. Respuesta: " + response.asString(),
                nombreCompletoEsperado.contains(nombre) && nombreCompletoEsperado.contains(apellido));

        // Opcional: Podrías usar tu ClienteService (inyectado) para buscar en la BD y verificar.
    }

    @Then("el sistema debe retornar el campo \"{word}\" con un valor que inicie con \"{word}\"")
    public void elSistemaDebeRetornarElCampoConUnValorQueInicieCon(String campo, String prefijo) {
        // Verificar la existencia y el formato del código generado automáticamente
        String valorCampo = response.jsonPath().getString(campo);

        Assert.assertNotNull("El campo '" + campo + "' no fue retornado en la respuesta.", valorCampo);
        Assert.assertTrue("El valor del campo '" + campo + "' debe iniciar con '" + prefijo + "'.",
                valorCampo.startsWith(prefijo));
    }
}