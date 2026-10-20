package com.adarshpandey.testing.testing.module7.controllers;

import com.adarshpandey.testing.testing.module7.TestContainerConfiguration;
import com.adarshpandey.testing.testing.module7.dto.EmployeeDto;
import com.adarshpandey.testing.testing.module7.entities.Employee;
import com.adarshpandey.testing.testing.module7.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
class EmployeeControllerTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee;
    private EmployeeDto testEmployeeDto;
    @BeforeEach
    void setUp(){
        testEmployee = Employee.builder()
  //              .id(1L)
                .name("adarsh")
                .email("adarsh@gmail.com")
                .salary(200L)
                .build();

        testEmployeeDto = EmployeeDto.builder()
                .id(testEmployee.getId())
                .name(testEmployee.getName())
                .email(testEmployee.getEmail())
                .salary(testEmployee.getSalary())
                .build();

        employeeRepository.deleteAll();
    }

    @Test
    void getEmployeeById_success(){
        Employee savedEmployee = employeeRepository.save(testEmployee);
//        testEmployeeDto.setId(savedEmployee.getId());

        webTestClient.get()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
//                .expectBody(EmployeeDto.class)
//                .isEqualTo(testEmployeeDto);
////                .value(employeeDto -> {
////                    assertThat(employeeDto.getEmail()).isEqualTo(savedEmployee.getEmail());
////                   assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());
////                });

                .expectBody()
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());;


    }

    @Test
    void  testGetEmployeeById_Failure(){
        webTestClient.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExists_thenThrowException() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setId(savedEmployee.getId());

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExists_thenCreateEmployee() {
       webTestClient.post()
               .uri("/employees")
               .bodyValue(testEmployeeDto)
               .exchange()
               .expectStatus().isCreated()
               .expectBody()
               .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
               .jsonPath("$.name").isEqualTo(testEmployeeDto.getName());
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        webTestClient.put()
                .uri("/employees/999")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateEmployee_whenAttemptingToUpdateTheEmail_thenThrowException() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setName("hello");
        testEmployeeDto.setEmail("hello@gmail.com");

        webTestClient.put()
                .uri("employees/{id}" , savedEmployee.getEmail())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testUpdateEmployee_whenEmployeeIsValid_thenUpdateEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setId(savedEmployee.getId());
        testEmployeeDto.setName("hello");
        testEmployeeDto.setSalary(250L);

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto);
    }


    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        webTestClient.delete()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
//        testEmployeeDto.setId(savedEmployee.getId());

        webTestClient.delete()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

        webTestClient.delete()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

}