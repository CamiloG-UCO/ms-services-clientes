package co.edu.hotel.cleinteservice.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", // Ruta de tus archivos .feature
        glue = {
                "co.edu.hotel.cleinteservice.steps",  // Step definitions
                "co.edu.hotel.cleinteservice.config"  // Configuración Spring para Cucumber
        },
        plugin = {
                "pretty", // Salida legible en consola
                "html:target/cucumber-reports/html-report.html", // Reporte HTML
                "json:target/cucumber-reports/cucumber.json"   // Reporte JSON para herramientas externas
        },
        snippets = CucumberOptions.SnippetType.CAMELCASE, // Formato de snippets generados
        monochrome = true, // Consola más legible
        tags = ""          // Filtrado por tags si se desea
)
public class ClienteDuplicidadRunner {
}
