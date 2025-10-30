package co.edu.hotel.cleinteservice;

// 1. Corregir imports de Spring Boot y Context
import co.edu.hotel.cleinteservice.config.TestSecurityConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment; // Para WebEnvironment
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration; // Para ContextConfiguration
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;// Para el hook @Before de Cucumber

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {MsServicesClienteApplication.class})
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")

public class MsServicesClienteApplicationTests {

    @Before
    public void setup() {
        // Este hook se ejecuta antes de cada escenario BDD.
    }

}