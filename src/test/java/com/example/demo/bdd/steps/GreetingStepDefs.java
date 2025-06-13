package com.example.demo.bdd.steps;

import com.example.demo.service.GreetingService;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;
//
//@Component
public class GreetingStepDefs {

    @Autowired
    private GreetingService greetingService;

    private String name;
    private String response;

    @Given("a user named {string}")
    public void a_user_named(String name) {
        this.name = name;
    }

    @When("the user is greeted")
    public void the_user_is_greeted() {
        response = greetingService.greet(name);
    }

    @Then("the message should be {string}")
    public void the_message_should_be(String expected) {
        assertEquals(expected, response);
    }
}