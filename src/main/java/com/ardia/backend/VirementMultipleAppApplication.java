package com.ardia.backend;

import com.ardia.backend.services.DummyInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VirementMultipleAppApplication implements CommandLineRunner {
    @Autowired
    DummyInitializer dummyInitializer;

    public static void main(String[] args) {
        SpringApplication.run(VirementMultipleAppApplication.class, args);
    }

    @Override
    public void run(String... args) {
        dummyInitializer.initAllData();
    }
}
