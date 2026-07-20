package com.example.demo.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import com.example.demo.dto.GreetingDto;
import com.example.demo.entity.Greeting;
import com.example.demo.mapper.GreetingMapper; // Imported new MyBatis Mapper
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GreetingServiceImpl implements GreetingService {

    private final GreetingMapper greetingMapper; // Swapped out GreetingRepository

    @Autowired
    public GreetingServiceImpl(GreetingMapper mapper) {
        this.greetingMapper = mapper;
    }

    @Override
    public String greet(String name) {
        // Uses Optional.ofNullable to handle cases where findByName returns null
        return Optional.ofNullable(greetingMapper.findByName(name))
                .map(Greeting::getMessage)
                .orElseThrow(() -> new NoSuchElementException("No greeting found for name: " + name));
    }

    @Override
    public List<GreetingDto> findAllGreetings() {
        return greetingMapper.findAll()
                .stream()
                .map(g -> new GreetingDto(g.getName(), g.getMessage()))
                .collect(Collectors.toList());
    }

    @Override
    public GreetingDto saveGreeting(GreetingDto dto) {
        Greeting existingGreeting = greetingMapper.findByName(dto.name());

        if (existingGreeting != null) {
            // Update existing greeting using explicit update query
            existingGreeting.setMessage(dto.message());
            greetingMapper.update(existingGreeting);
            return mapToDto(existingGreeting);
        } else {
            // Create and insert new greeting
            Greeting newGreeting = new Greeting();
            newGreeting.setName(dto.name());
            newGreeting.setMessage(dto.message());
            greetingMapper.insert(newGreeting);
            return mapToDto(newGreeting);
        }
    }

    @Override
    public GreetingDto addGreeting(GreetingDto dto) {
        if (greetingMapper.findByName(dto.name()) != null) {
            throw new IllegalArgumentException("Name already exists: " + dto.name());
        }

        Greeting newGreeting = new Greeting();
        newGreeting.setName(dto.name());
        newGreeting.setMessage(dto.message());
        greetingMapper.insert(newGreeting); // MyBatis automatically sets the ID back onto newGreeting via useGeneratedKeys
        return mapToDto(newGreeting);
    }

    @Override
    public Optional<GreetingDto> updateGreeting(GreetingDto dto) {
        Greeting existingGreeting = greetingMapper.findByName(dto.name());

        if (existingGreeting != null) {
            existingGreeting.setMessage(dto.message());
            greetingMapper.update(existingGreeting);
            return Optional.of(mapToDto(existingGreeting));
        }
        return Optional.empty();
    }

    private GreetingDto mapToDto(Greeting greeting) {
        return new GreetingDto(greeting.getName(), greeting.getMessage());
    }
}
