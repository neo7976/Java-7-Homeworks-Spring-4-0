package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Demo1ApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Container
    private final GenericContainer<?> myAppFirst = new GenericContainer<>("tcapp:1.0")
            .withExposedPorts(8080);
    @Container
    //static создает один контейнер на все тесты. Без него - 1 тест = 1 новый контейнер
    private static final GenericContainer<?> myAppSecond = new GenericContainer<>("tcapp:2.0")
            .withExposedPorts(8080);
// Не требуется за счёт аннотаций @Testcontainers
//    @BeforeEach
//    void setUp() {
//        myAppFirst.start();
//        myAppSecond.start();
//    }


    @Test
    void contextLoads() {
        Integer firstAppPort = myAppFirst.getMappedPort(8080);
        Integer secondAppPort = myAppSecond.getMappedPort(8080);
        ResponseEntity<String> entityFromFirst = restTemplate.getForEntity("http://localhost:" + firstAppPort, String.class);
        ResponseEntity<String> entityFromSecond = restTemplate.getForEntity("http://localhost:" + secondAppPort, String.class);

        System.out.println("First: " + entityFromFirst.getBody());
        System.out.println("Second: " + entityFromSecond.getBody());
    }

}
