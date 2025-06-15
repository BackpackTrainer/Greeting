package com.example.demo.controller;

import com.example.demo.dto.GreetingDto;
import com.example.demo.entity.Greeting;
import com.example.demo.service.GreetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/greet")
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> greet(@PathVariable String name) {
        try {
            String message = greetingService.greet(name);
            return ResponseEntity.ok(message);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @GetMapping("/all")
    public List<GreetingDto> getAllGreetings() {
        return greetingService.findAllGreetings();
    }


    @PostMapping
    public GreetingDto saveGreeting(@RequestBody GreetingDto dto) {
        return greetingService.saveGreeting(dto);
    }

    @PostMapping("/add")
    public ResponseEntity<GreetingDto> addGreeting(@RequestBody GreetingDto dto) {
        try {
            return ResponseEntity.ok(greetingService.addGreeting(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 409 Conflict
        }
    }

    @PutMapping
    public ResponseEntity<GreetingDto> updateGreeting(@RequestBody GreetingDto dto) {
        Optional<GreetingDto> updated = greetingService.updateGreeting(dto);
        return updated
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


}
