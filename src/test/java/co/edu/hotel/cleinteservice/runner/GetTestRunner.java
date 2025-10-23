package co.edu.hotel.cleinteservice.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/Get.feature",
        glue = {
                "co.edu.hotel.cleinteservice.steps",   // tus Step Definitions
                "co.edu.hotel.cleinteservice",         // contexto Spring Boot
                "io.cucumber.spring"                   // hooks de Cucumber-Spring
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/consultar-clientes-report.html",
                "json:target/cucumber-reports/consultar-clientes.json"
        },
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        monochrome = true,
        tags = "@ConsultarClientes" // puedes cambiarlo según el tag del feature
)
public class GetTestRunner {
    // Runner específico para el escenario de consulta
}