package com.acme.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входу commerce-сервісу. Потрібна, зокрема, щоб @WebMvcTest
 * міг знайти @SpringBootConfiguration під час старту web-slice.
 */
@SpringBootApplication
public class CommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceApplication.class, args);
    }
}