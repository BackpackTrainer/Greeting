package com.example.demo.repository;

import com.example.demo.entity.Greeting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {
    Optional<Greeting> findByName(String name);
}
