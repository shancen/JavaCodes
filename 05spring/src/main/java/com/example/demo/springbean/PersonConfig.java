package com.example.demo.springbean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonConfig {

    @Bean
    public Person person() {
        return new Person("liuzhongshan3", 26);
    }
}
