package com.example.demo.integrationtests.greeting;

import com.example.demo.dto.GreetingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql(statements = "DELETE FROM greeting", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GreetingControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void greetReturnsDefaultMessageWhenNotFound() throws Exception {
        String unknownUserName = "Bob";

        mockMvc.perform(get("/greet/" + unknownUserName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, " + unknownUserName + "!"));
    }

    @Test
    void greetReturnsStoredMessageForKnownUser() throws Exception {
        String knownUserName = "Alice";
        mockMvc.perform(get("/greet/" + knownUserName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome back, " +  knownUserName + "!"))
                .andExpect(content().string(String.format("Welcome back, %s!", knownUserName)));
    }

    @ParameterizedTest
    @CsvSource({"'Alice', 'Welcome back, Alice!'",
            "'Carol', 'Welcome back, Carol!'"})
    void greetReturnsStoredMessageForKnownUsers(String name, String message) throws Exception {
        mockMvc.perform(get("/greet/" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    @Test
    void getAllGreetingsReturnsTwoEntries() throws Exception {
        MvcResult result = mockMvc.perform(get("/greet/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println("RESPONSE: " + jsonResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        List<GreetingDto> greetings = objectMapper.readValue(
                jsonResponse, new TypeReference<List<GreetingDto>>() {});

        assertEquals(2, greetings.size());
    }
    @Test
    void getAllGreetingsReturnsCorrectContent() throws Exception {
        MvcResult result = mockMvc.perform(get("/greet/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<GreetingDto> actual = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        List<GreetingDto> expected = List.of(
                new GreetingDto("Alice", "Welcome back, Alice!"),
                new GreetingDto("Carol", "Welcome back, Carol!")
        );

        assertEquals(expected.size(), actual.size());
        assertTrue(actual.containsAll(expected) && expected.containsAll(actual));
    }

}

