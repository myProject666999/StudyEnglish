package com.english;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.english.mapper")
public class StudyEnglishApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyEnglishApplication.class, args);
    }

}
