package co.edu.hotel.cleinteservice.config;

import co.edu.hotel.cleinteservice.MsServicesClienteApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(classes = MsServicesClienteApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {
    // Esta clase no necesita código dentro.
    // Sirve para inicializar el contexto de Spring para Cucumber.
}
