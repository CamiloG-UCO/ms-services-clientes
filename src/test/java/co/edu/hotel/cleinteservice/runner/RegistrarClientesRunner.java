package co.edu.hotel.cleinteservice.runner;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/register.feature") // o "features/register.feature"
@ConfigurationParameter(
        key = io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME,
        value = "co.edu.hotel.cleinteservice.steps,co.edu.hotel.cleinteservice.config"
)
public class RegistrarClientesRunner { }
