package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    int a[] = {1,2,3};
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}