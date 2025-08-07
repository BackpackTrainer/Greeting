package com.example.demo.unit;

import com.example.demo.dto.GreetingDto;
import com.example.demo.entity.Greeting;
import com.example.demo.repository.GreetingRepository;
import com.example.demo.service.GreetingService;
import com.example.demo.service.GreetingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GreetingServiceTest {

    @Mock
    private GreetingRepository greetingRepository;

    @InjectMocks
    private GreetingServiceImpl greetingService;

    @Test
    void testGreetReturnsDefaultWhenNotFound() {
        String testName = "John";
        when(greetingRepository.findByName(testName)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(
                NoSuchElementException.class,
                () -> greetingService.greet(testName)
        );

        assertEquals("No greeting found for name: " + testName, ex.getMessage());
    }


    @Test
    void testGreetReturnsStoredMessageWhenFound() {
        Greeting greeting = new Greeting();
        greeting.setName("Carol");
        greeting.setMessage("Welcome back, Carol!");

        when(greetingRepository.findByName("Carol")).thenReturn(Optional.of(greeting));

        String result = greetingService.greet("Carol");

        assertEquals("Welcome back, Carol!", result);
    }

    @Test
    void testFindAllGreetingsReturnsDtoList() {
        Greeting g1 = new Greeting("Alice", "Hi Alice!");
        Greeting g2 = new Greeting("Bob", "Hey Bob!");

        when(greetingRepository.findAll()).thenReturn(List.of(g1, g2));

        var results = greetingService.findAllGreetings();

        assertAll("Verify greeting list contents",
                () -> assertEquals(2, results.size()),
                () -> assertEquals("Alice", results.get(0).name()),
                () -> assertEquals("Hi Alice!", results.get(0).message())
        );
    }
    @Test
    void testAddGreetingWhenNameExistsThrows() {
        when(greetingRepository.findByName("Dana")).thenReturn(Optional.of(new Greeting()));

        var ex = assertThrows(IllegalArgumentException.class,
                () -> greetingService.addGreeting(new GreetingDto("Dana", "Hello again")));

        assertEquals("Name already exists: Dana", ex.getMessage());
    }
    @Test
    void testUpdateGreetingWhenNotExists() {
        when(greetingRepository.findByName("Frank")).thenReturn(Optional.empty());

        Optional<GreetingDto> result = greetingService.updateGreeting(new GreetingDto("Frank", "Hello"));

        assertTrue(result.isEmpty());
    }


}
