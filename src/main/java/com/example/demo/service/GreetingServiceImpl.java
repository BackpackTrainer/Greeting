package com.example.demo.service;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.GreetingDto;
import com.example.demo.entity.Greeting;
import com.example.demo.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GreetingServiceImpl implements GreetingService {

    private final GreetingRepository greetingRepository;


    @Autowired
    public GreetingServiceImpl(GreetingRepository repository) {
        this.greetingRepository = repository;
    }


    @Override
    public String greet(String name) {
        return greetingRepository.findByName(name)
                .map(Greeting::getMessage)
                .orElseThrow(() -> new NoSuchElementException("No greeting found for name: " + name));
    }


    @Override
    public List<GreetingDto> findAllGreetings() {
        return greetingRepository.findAll()
                .stream()
                .map(g -> new GreetingDto(g.getName(), g.getMessage()))
                .collect(Collectors.toList());
    }

@Override

    public GreetingDto saveGreeting(GreetingDto dto) {
        // Check if greeting with the same name exists
        Optional<Greeting> existingGreetingOpt = greetingRepository.findByName(dto.getName());

        Greeting savedGreeting;

        if (existingGreetingOpt.isPresent()) {
            // Update existing greeting
            Greeting existingGreeting = existingGreetingOpt.get();
            existingGreeting.setMessage(dto.getMessage());
            savedGreeting = greetingRepository.save(existingGreeting);
        } else {
            // Create new greeting
            Greeting newGreeting = new Greeting();
            newGreeting.setName(dto.getName());
            newGreeting.setMessage(dto.getMessage());
            savedGreeting = greetingRepository.save(newGreeting);
        }

        // Convert to DTO and return
        return mapToDto(savedGreeting);
    }

    @Override
    public GreetingDto addGreeting(GreetingDto dto) {
        if (greetingRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException("Name already exists: " + dto.getName());
        }

        Greeting newGreeting = new Greeting();
        newGreeting.setName(dto.getName());
        newGreeting.setMessage(dto.getMessage());
        Greeting saved = greetingRepository.save(newGreeting);
        return mapToDto(saved);
    }

    @Override
    public Optional<GreetingDto> updateGreeting(GreetingDto dto) {
        Optional<Greeting> existingGreeting = greetingRepository.findByName(dto.getName());

        if (existingGreeting.isPresent()) {
            Greeting greeting = existingGreeting.get();
            greeting.setMessage(dto.getMessage());
            Greeting saved = greetingRepository.save(greeting);
            return Optional.of(mapToDto(saved));
        }

        return Optional.empty(); // No member found to update
    }


    private GreetingDto mapToDto(Greeting greeting) {
        GreetingDto dto = new GreetingDto();
        dto.setName(greeting.getName());
        dto.setMessage(greeting.getMessage());
        return dto;
    }
}
