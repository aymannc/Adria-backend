package com.ardia.backend;

import com.ardia.backend.services.DummyInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class VirementMultipleAppApplication implements CommandLineRunner {
    @Autowired
    DummyInitializer dummyInitializer;

    public static void main(String[] args) {
        SpringApplication.run(VirementMultipleAppApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder getBCPE(){
        return  new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {
        dummyInitializer.initAllData();
    }
}
