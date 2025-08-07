package com.example.demo.unit;

import com.example.demo.controller.GreetingController;
import com.example.demo.dto.GreetingDto;
import com.example.demo.service.GreetingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @WebMvcTest(GreetingController.class)
    public class AlternateControllerUnitTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private GreetingService greetingService;

        private ObjectMapper objectMapper = new ObjectMapper();

        @Test
        void greetReturnsMessageWhenFound() throws Exception {
            when(greetingService.greet("Alice")).thenReturn("Hello Alice!");

            mockMvc.perform(get("/greet/Alice"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Hello Alice!"));
        }

        @Test
        void greetReturnsNotFoundWhenMissing() throws Exception {
            when(greetingService.greet("Ghost")).thenThrow(new NoSuchElementException("Not found"));

            mockMvc.perform(get("/greet/Ghost"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Not found"));
        }

        @Test
        void getAllGreetingsReturnsList() throws Exception {
            List<GreetingDto> dtos = List.of(new GreetingDto("A", "Hi"), new GreetingDto("B", "Hey"));
            when(greetingService.findAllGreetings()).thenReturn(dtos);

            mockMvc.perform(get("/greet/all"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].name").value("A"))
                    .andExpect(jsonPath("$[0].message").value("Hi"));
        }

        @Test
        void saveGreetingReturnsDto() throws Exception {
            GreetingDto input = new GreetingDto("Dave", "Hi Dave!");
            when(greetingService.saveGreeting(input)).thenReturn(input);

            mockMvc.perform(post("/greet")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Dave"))
                    .andExpect(jsonPath("$.message").value("Hi Dave!"));
        }

        @Test
        void addGreetingReturnsCreated() throws Exception {
            GreetingDto input = new GreetingDto("Eve", "Hello!");
            when(greetingService.addGreeting(input)).thenReturn(input);

            mockMvc.perform(post("/greet/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Eve"))
                    .andExpect(jsonPath("$.message").value("Hello!"));
        }

        @Test
        void addGreetingReturnsConflictWhenExists() throws Exception {
            GreetingDto input = new GreetingDto("Frank", "Hi again");
            when(greetingService.addGreeting(input)).thenThrow(new IllegalArgumentException("Exists"));

            mockMvc.perform(post("/greet/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isConflict())
                    .andExpect(content().string(""));
        }

        @Test
        void updateGreetingReturnsUpdatedDto() throws Exception {
            GreetingDto dto = new GreetingDto("George", "Updated!");
            when(greetingService.updateGreeting(dto)).thenReturn(Optional.of(dto));

            mockMvc.perform(put("/greet")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("George"))
                    .andExpect(jsonPath("$.message").value("Updated!"));
        }

        @Test
        void updateGreetingReturnsNotFound() throws Exception {
            GreetingDto dto = new GreetingDto("Helen", "Not found");
            when(greetingService.updateGreeting(dto)).thenReturn(Optional.empty());

            mockMvc.perform(put("/greet")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString("No update made")));
        }
    }


