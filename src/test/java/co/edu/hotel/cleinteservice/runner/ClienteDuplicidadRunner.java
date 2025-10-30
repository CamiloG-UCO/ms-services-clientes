package co.edu.hotel.cleinteservice.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "co.edu.hotel.cleinteservice.steps",
                "co.edu.hotel.cleinteservice.config"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/html-report.html",
                "json:target/cucumber-reports/cucumber.json"
        },
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        monochrome = true,
        tags = ""
)
public class ClienteDuplicidadRunner {
}
