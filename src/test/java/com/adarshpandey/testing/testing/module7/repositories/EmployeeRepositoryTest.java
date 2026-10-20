package com.adarshpandey.testing.testing.module7.repositories;


import com.adarshpandey.testing.testing.module7.TestContainerConfiguration;
import com.adarshpandey.testing.testing.module7.entities.Employee;
import com.adarshpandey.testing.testing.module7.services.EmployeeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;


//@SpringBootTest   //this load whole application but we dont need it we only need repository

@Import(TestContainerConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//this above will apply h2 database but h2 database is not as same to production database so we will configure it further

class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setup(){
        employee = Employee.builder()
//               .id(1L)
                .name("adarsh")
                .email("adarsh@gmail.com")
                .salary(100L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee(){
//        employeeRepository.findByEmail("");

        //arrange , given
        employeeRepository.save(employee);

        //act , when
        List<Employee> employeeList = employeeRepository.findByEmail(employee.getEmail());

        //assert , then;
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());


    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmployeeList(){

        //given
        String emial = "notPresent.12@gmail.com";

        //when
        List<Employee> employeeList = employeeRepository.findByEmail(emial);

        //then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();

    }




}