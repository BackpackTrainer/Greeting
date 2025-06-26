package com.example.demo.unit;

import com.example.demo.controller.GreetingController;
import com.example.demo.dto.GreetingDto;
import com.example.demo.service.GreetingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GreetingControllerTest {

    @Mock
    private GreetingService greetingService;

    @InjectMocks
    private GreetingController controller;

    @Test
    void greetReturnsMessageWhenFound() {
        when(greetingService.greet("Alice")).thenReturn("Hello Alice!");

        ResponseEntity<String> response = controller.greet("Alice");

        assertAll("Verify greet() success response",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("Hello Alice!", response.getBody())
        );
    }

    @Test
    void greetReturnsNotFoundWhenMissing() {
        when(greetingService.greet("Ghost")).thenThrow(new NoSuchElementException("Not found"));

        ResponseEntity<String> response = controller.greet("Ghost");

        assertAll("Verify greet() not found response",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertEquals("Not found", response.getBody())
        );
    }

    @Test
    void getAllGreetingsReturnsList() {
        List<GreetingDto> dtos = List.of(new GreetingDto("A", "Hi"), new GreetingDto("B", "Hey"));
        when(greetingService.findAllGreetings()).thenReturn(dtos);

        List<GreetingDto> result = controller.getAllGreetings();

        assertAll("Verify getAllGreetings() result",
                () -> assertEquals(2, result.size()),
                () -> assertEquals("A", result.get(0).getName()),
                () -> assertEquals("Hi", result.get(0).getMessage())
        );
    }

    @Test
    void saveGreetingReturnsDto() {
        GreetingDto input = new GreetingDto("Dave", "Hi Dave!");
        when(greetingService.saveGreeting(input)).thenReturn(input);

        GreetingDto result = controller.saveGreeting(input);

        assertAll("Verify saveGreeting() result",
                () -> assertEquals("Dave", result.getName()),
                () -> assertEquals("Hi Dave!", result.getMessage())
        );
    }

    @Test
    void addGreetingReturnsCreated() {
        GreetingDto input = new GreetingDto("Eve", "Hello!");
        when(greetingService.addGreeting(input)).thenReturn(input);

        ResponseEntity<GreetingDto> response = controller.addGreeting(input);

        assertAll("Verify addGreeting() success response",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("Eve", response.getBody().getName()),
                () -> assertEquals("Hello!", response.getBody().getMessage())
        );
    }

    @Test
    void addGreetingReturnsConflictWhenExists() {
        GreetingDto input = new GreetingDto("Frank", "Hi again");
        when(greetingService.addGreeting(input)).thenThrow(new IllegalArgumentException("Exists"));

        ResponseEntity<GreetingDto> response = controller.addGreeting(input);

        assertAll("Verify addGreeting() conflict response",
                () -> assertEquals(HttpStatus.CONFLICT, response.getStatusCode()),
                () -> assertNull(response.getBody())
        );
    }

    @Test
    void updateGreetingReturnsUpdatedDto() {
        GreetingDto dto = new GreetingDto("George", "Updated!");
        when(greetingService.updateGreeting(dto)).thenReturn(Optional.of(dto));

        ResponseEntity<?> response = controller.updateGreeting(dto);

        assertAll("Verify updateGreeting() success",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(dto, response.getBody())
        );
    }

    @Test
    void updateGreetingReturnsNotFound() {
        GreetingDto dto = new GreetingDto("Helen", "Not found");
        when(greetingService.updateGreeting(dto)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.updateGreeting(dto);

        assertAll("Verify updateGreeting() not found",
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertTrue(((String) response.getBody()).contains("No update made"))
        );
    }
}
