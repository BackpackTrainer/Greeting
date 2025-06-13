package com.example.demo.service;
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
                .orElse("Hello, " + name + "!");
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
        Greeting g = new Greeting();
        g.setName(dto.getName());
        g.setMessage(dto.getMessage());
        Greeting saved = greetingRepository.save(g);
        return new GreetingDto(saved.getName(), saved.getMessage());
    }
}
