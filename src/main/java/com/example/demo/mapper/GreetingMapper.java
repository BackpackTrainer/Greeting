package com.example.demo.mapper;

import com.example.demo.entity.Greeting;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper // This marks it for MyBatis and allows Spring to inject it into services
public interface GreetingMapper {

    // Find a greeting by its unique ID
    Greeting findById(Long id);

    // Fetch all greetings from the table
    List<Greeting> findAll();

    // Insert a new greeting
    void insert(Greeting greeting);

    Greeting findByName(String name);
    void update(Greeting greeting);

}

