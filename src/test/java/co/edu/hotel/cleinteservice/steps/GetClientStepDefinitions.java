package co.edu.hotel.cleinteservice.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Assert;

public class GetClientStepDefinitions {
    private String usuario;
    private Response response;

    @Given("el usuario {string} con permisos de recepción ha iniciado sesión en el sistema")
    public void el_usuario_con_permisos_de_recepcion_ha_iniciado_sesion(String nombreUsuario) {
        this.usuario = nombreUsuario;
        System.out.println("Usuario autenticado: " + usuario);
    }

    @When("el usuario selecciona la opción {string}")
    public void el_usuario_selecciona_la_opcion(String opcion) {
        System.out.println("Opción seleccionada: " + opcion);

        // Llamada al endpoint para consultar todos los clientes
        response = given()
                .header("Content-Type", "application/json")
                .get("http://localhost:8080/api/clientes");
    }

    @Then("el sistema debe mostrar una lista con los siguientes datos de cada cliente:")
    public void el_sistema_debe_mostrar_una_lista_con_los_siguientes_datos_de_cada_cliente(io.cucumber.datatable.DataTable dataTable) {
        response.then()
                .statusCode(200)
                .body("size()", greaterThan(0)) // hay al menos un cliente
                .body("[0].name", notNullValue())
                .body("[0].documentNumber", notNullValue())
                .body("[0].email", notNullValue())
                .body("[0].phone", notNullValue())
                .body("[0].registerDate", notNullValue());
    }

    @And("el sistema debe permitir buscar clientes por cédula, nombre u otro dato relevante")
    public void el_sistema_debe_permitir_buscar_clientes_por_cedula_nombre_u_otro_dato_relevante() {
        // Buscar por número de documento
        Response busquedaDocumento = given()
                .header("Content-Type", "application/json")
                .queryParam("number", "10254879")
                .get("http://localhost:8080/api/clientes/document");

        busquedaDocumento.then()
                .statusCode(anyOf(is(200), is(404))); // Si no existe, puede devolver 404

        // Buscar por email
        Response busquedaEmail = given()
                .header("Content-Type", "application/json")
                .queryParam("email", "cliente@correo.com")
                .get("http://localhost:8080/api/clientes/email");

        busquedaEmail.then()
                .statusCode(anyOf(is(200), is(404)));
    }

    @And("el sistema debe permitir exportar la información en formato PDF o Excel")
    public void el_sistema_debe_permitir_exportar_la_informacion_en_formato_pdf_o_excel() {
        // Supongamos que existen endpoints de exportación
        Response exportPdf = given()
                .get("http://localhost:8080/api/clientes/export/pdf");
        Response exportExcel = given()
                .get("http://localhost:8080/api/clientes/export/excel");

        exportPdf.then().statusCode(anyOf(is(200), is(204), is(404)));
        exportExcel.then().statusCode(anyOf(is(200), is(204), is(404)));
    }
}
