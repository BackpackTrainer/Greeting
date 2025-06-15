package com.example.demo.service;

import com.example.demo.dto.GreetingDto;

import java.util.List;
import java.util.Optional;

public interface GreetingService {
    public String greet(String name);

    public List<GreetingDto> findAllGreetings();

    public GreetingDto saveGreeting(GreetingDto dto);

    public GreetingDto addGreeting(GreetingDto dto);

    public Optional<GreetingDto> updateGreeting(GreetingDto dto);
}
