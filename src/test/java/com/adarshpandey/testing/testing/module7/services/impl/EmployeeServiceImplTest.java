package com.adarshpandey.testing.testing.module7.services.impl;

import com.adarshpandey.testing.testing.module7.TestContainerConfiguration;
import com.adarshpandey.testing.testing.module7.dto.EmployeeDto;
import com.adarshpandey.testing.testing.module7.entities.Employee;
import com.adarshpandey.testing.testing.module7.exceptions.ResourceNotFoundException;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {


    @Mock
    private EmployeeRepository employeeRepository;


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
                .email("adarsh@gmail.com")
                .salary(200L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee,EmployeeDto.class);
    }







    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto(){

        //assign
        Long id = mockEmployee.getId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee)); //stubbing

        //act
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

        //assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository,times(1)).findById(id);
    }





    @Test
    void testGetEmployeeUd_whenEmployeeIsNotPresent_theThrowException(){
        //arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        //act and assert
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee not found with id: 1");


        verify(employeeRepository).findById(1L);


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

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());


        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee captureEmployee = employeeArgumentCaptor.getValue();
        assertThat(captureEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());


    }






    @Test
    void testCreateNewEmployee_whenAttemptingToCreateEmployeeWithExistingEmail_thenThrowException(){

        //arrange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));

        //act and assert
        assertThatThrownBy(() -> employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+mockEmployee.getEmail());


        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository,never()).save(any());
    }





    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExist_thenThrowException(){
        //assert
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        //assert and act
        assertThatThrownBy(() -> employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository,never()).save(any());
    }




    @Test
    void testUpdateEmployee_whenEmployeeAttemptingToUpdateEmail_thenThrowException(){
        //arrange
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setName("hello");
        mockEmployeeDto.setEmail("hello@gmail.com");

        //assert and act
        assertThat(mockEmployeeDto.getEmail()).isNotEqualTo(mockEmployee.getEmail());

        assertThatThrownBy(() -> employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");


        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository,never()).save(any());

    }


    @Test
    void testUpdateEmployee_whenValidEmployee_thenUpdateEmployee(){
        //arrange
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setName("hello");
        mockEmployeeDto.setSalary(199L);

        Employee newEmployee = modelMapper.map(mockEmployeeDto, Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        //act
        EmployeeDto updateEmployeeDto = employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);


        //assert
        assertThat(updateEmployeeDto).isEqualTo(mockEmployeeDto);
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any(Employee.class));


    }


    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExist_thenThrowException(){
        when(employeeRepository.existsById(1L)).thenReturn(false);

        //act
        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: "+1L);


        verify(employeeRepository,never()).deleteById(anyLong());
    }

    @Test
    void testDeleteEmployee_whenEmployeeIsValid_thenDeleteEmployee(){

        //arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);


        assertThatCode(() -> employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();

        verify(employeeRepository).deleteById(1L);

    }




}