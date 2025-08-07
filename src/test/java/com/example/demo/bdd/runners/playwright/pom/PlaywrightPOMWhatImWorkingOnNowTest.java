package com.example.demo.bdd.runners.playwright.pom;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty")
@ConfigurationParameter(
        key = "cucumber.glue",
        value = "com.example.demo.bdd.steps.playwright.pom,com.example.demo.bdd.config"
)
@ConfigurationParameter(
        key = "cucumber.filter.tags",
        value = "@WhatImWorkingOnNow"
)
public class PlaywrightPOMWhatImWorkingOnNowTest {
}
