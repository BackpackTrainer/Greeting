package com.example.demo.service;

import com.example.demo.dto.GreetingDto;

import java.util.List;

public interface GreetingService {
    public String greet(String name);

    public List<GreetingDto> findAllGreetings();

    public GreetingDto saveGreeting(GreetingDto dto);
}
