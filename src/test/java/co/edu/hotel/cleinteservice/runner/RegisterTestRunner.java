import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",

        // ¡CORRECCIÓN CLAVE!
        // 1. "co.edu.hotel.cleinteservice.steps" -> Ubicación de tus Step Definitions
        // 2. "co.edu.hotel.cleinteservice" -> Ubicación de tu clase de contexto (MsServicesClienteApplicationTests.java)
        // 3. ¡IMPORTANTE! Se debe incluir el paquete que contiene los 'Hooks' de Cucumber-Spring.
        //    Cucumber-Spring necesita un paquete adicional para sus hooks internos.
        glue = {"co.edu.hotel.cleinteservice.steps", "co.edu.hotel.cleinteservice", "io.cucumber.spring"},

        plugin = {
                "pretty",
                "html:target/cucumber-reports/html-report.html",
                "json:target/cucumber-reports/cucumber.json"
        },

        snippets = CucumberOptions.SnippetType.CAMELCASE,
        monochrome = true,
        tags = ""
)
public class RegisterTestRunner {
    // ...
}