package me.vukas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Extractor {
    public static void main(String[] args) {
        SpringApplication.run(Extractor.class, args);
    }
}
