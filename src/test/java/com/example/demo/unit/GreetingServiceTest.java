package com.example.demo.unit;

import com.example.demo.entity.Greeting;
import com.example.demo.repository.GreetingRepository;
import com.example.demo.service.GreetingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GreetingServiceTest {

    @Mock
    private GreetingRepository greetingRepository;

    @InjectMocks
    private GreetingService greetingService;

    @Test
    void testGreetReturnsDefaultWhenNotFound() {
        when(greetingRepository.findByName("John")).thenReturn(Optional.empty());

        String result = greetingService.greet("John");

        assertEquals("Hello, John!", result);
    }

    @Test
    void testGreetReturnsStoredMessageWhenFound() {
        Greeting greeting = new Greeting();
        greeting.setName("John");
        greeting.setMessage("Hi there, John!");

        when(greetingRepository.findByName("John")).thenReturn(Optional.of(greeting));

        String result = greetingService.greet("John");

        assertEquals("Hi there, John!", result);
    }
}
