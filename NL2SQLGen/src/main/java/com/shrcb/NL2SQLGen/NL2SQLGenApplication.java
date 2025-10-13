package com.shrcb.NL2SQLGen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shrcb.NL2SQLGen.mapper")
public class NL2SQLGenApplication {

    public static void main(String[] args) {
        SpringApplication.run(NL2SQLGenApplication.class, args);
        System.out.println("=== Spring Boot Application Started ===");

    }
}
