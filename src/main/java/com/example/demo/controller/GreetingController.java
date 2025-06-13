package com.example.demo.controller;

import com.example.demo.dto.GreetingDto;
import com.example.demo.entity.Greeting;
import com.example.demo.service.GreetingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/greet")
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/{name}")
    public String greet(@PathVariable String name) {
        return greetingService.greet(name);
    }

    @GetMapping("/all")
    public List<GreetingDto> getAllGreetings() {
        return greetingService.findAllGreetings();
    }


    @PostMapping
    public GreetingDto saveGreeting(@RequestBody GreetingDto dto) {
        return greetingService.saveGreeting(dto);
    }
}
