package com.example.demo.integrationtests.greeting;

import com.example.demo.dto.GreetingDto;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

//Note:  This class duplicates the tests in the GreetingControllerMockMvcTest class,
// but uses assertAll to group assertions together for better readability and organization.
// This is useful for testing scenarios where multiple assertions are made on the same response.
//In addition, it uses parameterized tests to reduce code duplication for similar test cases.


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql(statements = "DELETE FROM greeting", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GreetingControllerMockMvcAssertAllTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({"Bob, No greeting found for name: Bob"})
    void greetReturnsNotFoundForUnknownUser(String name, String expectedMessage) throws Exception {
        MvcResult result = mockMvc.perform(get("/greet/" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertAll(
                () -> assertEquals(404, result.getResponse().getStatus()),
                () -> assertEquals(expectedMessage, result.getResponse().getContentAsString())
        );
    }

    @ParameterizedTest
    @CsvSource({"'Alice', 'Welcome back, Alice!'", "'Carol', 'Welcome back, Carol!'"})
    void greetReturnsStoredMessageForKnownUsers(String name, String expectedMessage) throws Exception {
        MvcResult result = mockMvc.perform(get("/greet/" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(expectedMessage, result.getResponse().getContentAsString())
        );
    }

    @Test
    void getAllGreetingsReturnsExpectedUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/greet/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<GreetingDto> greetings = mapper.readValue(jsonResponse, new TypeReference<>() {});
        List<GreetingDto> expected = List.of(
                new GreetingDto("Alice", "Welcome back, Alice!"),
                new GreetingDto("Carol", "Welcome back, Carol!")
        );

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(2, greetings.size()),
                () -> assertTrue(greetings.containsAll(expected)),
                () -> assertTrue(expected.containsAll(greetings))
        );
    }

    @ParameterizedTest
    @CsvSource({"Fred, Good morning, Fred!"})
    void postNewGreetingAddsUserToDatabase(String name, String message) throws Exception {
        GreetingDto dto = new GreetingDto(name, message);
        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(post("/greet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertTrue(response.contains("\"name\":\"" + name + "\"")),
                () -> assertTrue(response.contains("\"message\":\"" + message + "\""))
        );
    }

    @Test
    void postGreetingFailsWhenUserExists() throws Exception {
        String payload = """
            {
                \"name\": \"Alice\",
                \"message\": \"Hello again!\"
            }
            """;

        MvcResult result = mockMvc.perform(post("/greet/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andReturn();

        assertEquals(409, result.getResponse().getStatus());
    }

    @Test
    void updateGreetingForExistingUserReturnsOk() throws Exception {
        String payload = """
            {
                \"name\": \"Alice\",
                \"message\": \"Updated message!\"
            }
            """;

        MvcResult result = mockMvc.perform(put("/greet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andReturn();

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals("application/json", result.getResponse().getContentType())
        );
    }

    @Test
    void updateGreetingForUnknownUserReturnsNotFound() throws Exception {
        String payload = """
            {
                \"name\": \"Zorro\",
                \"message\": \"He leaves his mark.\"
            }
            """;

        MvcResult result = mockMvc.perform(put("/greet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andReturn();

        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void getAllGreetingsContainsKnownUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/greet/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals("application/json", result.getResponse().getContentType()),
                () -> assertTrue(response.contains("Alice")),
                () -> assertTrue(response.contains("Carol"))
        );
    }
}

