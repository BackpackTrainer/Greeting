package com.example.demo.bdd;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty")
@ConfigurationParameter(
        key = "cucumber.glue",
        value = "com.example.demo.bdd.steps.selenium.basic,com.example.demo.bdd.config"
)
public class CucumberSeleniumTest {
}
