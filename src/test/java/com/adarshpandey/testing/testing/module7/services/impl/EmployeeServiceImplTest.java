package com.adarshpandey.testing.testing.module7.services.impl;

import com.adarshpandey.testing.testing.module7.TestContainerConfiguration;
import com.adarshpandey.testing.testing.module7.dto.EmployeeDto;
import com.adarshpandey.testing.testing.module7.entities.Employee;
import com.adarshpandey.testing.testing.module7.repositories.EmployeeRepository;
import com.adarshpandey.testing.testing.module7.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



//@SpringBootTest
//@Import(TestContainerConfiguration.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class EmployeeServiceImplTest {
//
//    @Autowired
//    private EmployeeService employeeService;
//
//    @Test
//    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto(){
//        employeeService.getEmployeeById(1L);
//    }
//}






@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {


    @Mock
    private EmployeeRepository employeeRepository;

//    @Mock
//    private ModelMapper modelMapper;

    @Spy
    private ModelMapper modelMapper;


    @InjectMocks
    private EmployeeServiceImpl employeeService;



    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;
    @BeforeEach
    void setUp(){
         mockEmployee = Employee.builder()
                .id(1L)
                .name("adarsh")
                .email("adarsh@gmai.com")
                .salary(200L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee,EmployeeDto.class);
    }







    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto(){
//        Long id = 1L;
//        Employee mockEmployee = Employee.builder()
//                .id(id)
//                .name("adarsh")
//                .email("adarsh@gmail.com")
//                .salary(200L)
//                .build();

        //assign
        Long id = mockEmployee.getId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee)); //stubbing

        //act
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

        //assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
//        verify(employeeRepository).findById(id);
//        verify(employeeRepository).save(null);
        verify(employeeRepository,times(1)).findById(id);
        //atleast
        //atmost

    }







    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee(){
        //assign
//        when(employeeRepository.findByEmail("adarsh@gmail.com"))
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);


        //act

        EmployeeDto employeeDto = employeeService.createNewEmployee(mockEmployeeDto);


        //assert


//        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);


        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
//        verify(employeeRepository).save(any(Employee.class));


        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee captureEmployee = employeeArgumentCaptor.getValue();
        assertThat(captureEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());


    }




}