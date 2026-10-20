package com.adarshpandey.testing.testing.module7.controllers;


import com.adarshpandey.testing.testing.module7.TestContainerConfiguration;
import com.adarshpandey.testing.testing.module7.dto.EmployeeDto;
import com.adarshpandey.testing.testing.module7.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {


    @Autowired
    WebTestClient webTestClient;

     Employee testEmployee = Employee.builder()
            //  .id(1L)
            .name("adarsh")
                .email("adarsh@gmail.com")
                .salary(200L)
                .build();

      EmployeeDto testEmployeeDto = EmployeeDto.builder()
            .id(testEmployee.getId())
            .name(testEmployee.getName())
            .email(testEmployee.getEmail())
            .salary(testEmployee.getSalary())
            .build();


}
