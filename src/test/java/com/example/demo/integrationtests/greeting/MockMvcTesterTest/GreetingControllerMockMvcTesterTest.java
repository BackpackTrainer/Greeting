package com.example.demo.integrationtests.greeting.MockMvcTesterTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql(statements = "DELETE FROM greeting", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class GreetingControllerMockMvcTesterTest {

@Autowired
MockMvcTester mockMvcTester;

    @Test
    void greetReturnsStatusCodeOKWhenUserNotFound() {
        String unknownUserName = "Bob";
        String url = "/greet/" + unknownUserName;

        var response = mockMvcTester.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON);

        assertThat(response)
                .hasStatusOk();
    }

    @Test
    void greetReturnsDefaultMessageWhenUserNotFound() {
        String unknownUserName = "Bob";
        String unknownUserMessage = "Hello, " + unknownUserName + "!";
        String url = "/greet/" + unknownUserName;

        MvcTestResult result = mockMvcTester.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .hasBodyTextEqualTo(unknownUserMessage)
                .hasContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void greetReturnsDefaultMessageWhenUserFound() {
        String knownUserName = "Alice";
        String knownUserMessage = "Welcome back, " + knownUserName + "!";
        String url = "/greet/" + knownUserName;

        MvcTestResult result = mockMvcTester.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .hasBodyTextEqualTo(knownUserMessage)
                .hasContentType(MediaType.APPLICATION_JSON);
    }

    @ParameterizedTest
    @CsvSource({"'Alice', 'Welcome back, Alice!'",
            "'Carol', 'Welcome back, Carol!'"})
    void greetReturnsStoredMessageForKnownUsers(String name, String message) throws Exception {
        String url = "/greet/" + name;
        MvcTestResult result = mockMvcTester.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .hasBodyTextEqualTo(message)
                .hasContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void postNewGreetingAddsFredToDatabase_MockMvcTester() throws UnsupportedEncodingException {
        String testName = "Fred";
        String testMessage = "Good morning, Fred!";
        String payload = String.format("""
            {
              "name": "%s",
              "message": "%s"
            }
    """, testName, testMessage);

        MvcTestResult result = mockMvcTester.post()
                .uri("/greet")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON);

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("\"name\":\"" + testName + "\"");
        assertThat(responseBody).contains("\"message\":\"" + testMessage + "\"");

        //alternative way to check the response body
        String expectedNameFragment = String.format("\"name\":\"%s\"", testName);
        String expectedMessageFragment = String.format("\"message\":\"%s\"", testMessage);

        assertThat(responseBody).contains(expectedNameFragment);
        assertThat(responseBody).contains(expectedMessageFragment);

    }
    @Test
    void greetReturnsDefaultMessageForNewUnknownUser() throws Exception {
        String name = "Zelda";
        String expectedMessage = "Hello, Zelda!";

        MvcTestResult result = mockMvcTester.get()
                .uri("/greet/" + name)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON);

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo(expectedMessage);
    }
}
