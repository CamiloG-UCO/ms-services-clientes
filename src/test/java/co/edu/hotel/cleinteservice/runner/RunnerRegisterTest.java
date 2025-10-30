package co.edu.hotel.cleinteservice.test;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/registro_clientes.feature", // Ruta a tu archivo .feature
        glue = "co.edu.hotel.cleinteservice.test.steps", // Paquete donde se encuentran tus Step Definitions
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        tags = "@Clientes" // Ejecuta todos los escenarios etiquetados con @Clientes
)
public class RunnerRegisterTest {
    // Clase vacía, solo se usa para las anotaciones de Cucumber y JUnit
}