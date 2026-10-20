package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

public class EmployeeServiceTest {
    //@Mock creates a mock EmployeeRepository. It doesn't connect to MySQL.
    @Mock
    private EmployeeRepository employeeRepository;

    //@InjectMocks creates an EmployeeService and injects the mock repository into it.
    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldSaveEmployeeAndReturnResponse() {
        //Arrange
        // 1. Prepare the request
        EmployeeRequest request = new EmployeeRequest();
        request.setName("Rahul");
        request.setEmail("rahul@example.com");
        request.setDepartment("IT");
        request.setSalary(new BigDecimal("55000"));

        // 2. Prepare the employee that the repository will return
        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setName("Rahul");
        savedEmployee.setEmail("rahul@example.com");
        savedEmployee.setDepartment("IT");
        savedEmployee.setSalary(new BigDecimal("55000"));

        // 3. Configure the mock repository
        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        //Act
        // 4. Call the method being tested
        EmployeeResponse response =
                employeeService.saveEmployee(request);

        //Assert
        // 5. Verify the returned response
        assertEquals(1L, response.getId());
        assertEquals("Rahul", response.getName());
        assertEquals("rahul@example.com", response.getEmail());
        assertEquals("IT", response.getDepartment());
        assertEquals(new BigDecimal("55000"), response.getSalary());
    }

    @Test
    void shouldReturnEmptyWhenEmployeeDoesNotExist(){
        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        Optional<EmployeeResponse> response=employeeService.getEmployeeById(999L);

        assertTrue(response.isEmpty());
    }

    @Test
    void shouldReturnAllEmployees(){
        Employee employee1=new Employee();
        employee1.setId(1L);
        employee1.setName("Rohit");
        employee1.setEmail("rohit@gmail.com");
        employee1.setDepartment("CSE");
        employee1.setSalary(new BigDecimal("55000"));

        Employee employee2=new Employee();
        employee2.setId(2L);
        employee2.setName("Ravi");
        employee2.setEmail("ravi@gmail.com");
        employee2.setDepartment("IT");
        employee2.setSalary(new BigDecimal("60000"));

        List<Employee> employees = List.of(employee1, employee2);

        Pageable pageable = PageRequest.of(0, 10);

        when(employeeRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(employees, pageable, employees.size()));

        Page<EmployeeResponse> responses =
                employeeService.getAllEmployees(pageable);

        assertEquals(2, responses.getContent().size());
        assertEquals("Rohit", responses.getContent().get(0).getName());
        assertEquals("Ravi", responses.getContent().get(1).getName());
    }

    @Test
    void shouldUpdateEmployeeWhenEmployeeExist(){
        Employee existing=new Employee();
        existing.setId(1L);
        existing.setName("Sami");
        existing.setSalary(new BigDecimal("40000"));
        existing.setDepartment("Civil");
        existing.setEmail("sami@gmail.com");

        EmployeeRequest request=new EmployeeRequest();
        request.setName("Sami khan");
        request.setEmail("sami.khan@gmail.com");
        request.setDepartment("Civil engg.");
        request.setSalary(new BigDecimal("40000"));

        Employee updated=new Employee();
        updated.setId(1L);
        updated.setName("Sami khan");
        updated.setEmail("sami.khan@gmail.com");
        updated.setDepartment("Civil engg.");
        updated.setSalary(new BigDecimal("40000"));

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(updated);

        Optional<EmployeeResponse> response=
                employeeService.updateEmployee(1L,request);

        assertTrue(response.isPresent());
        assertEquals("Sami khan",response.get().getName());
        assertEquals("Civil engg.",response.get().getDepartment());
    }

    @Test
    void shouldReturnEmptyWhenEmployeeNotExist(){

        EmployeeRequest request=new EmployeeRequest();
        request.setName("Mohsin");
        request.setEmail("mohsin.gmail.com");
        request.setDepartment("EEE");
        request.setSalary(new BigDecimal("56000"));

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        Optional<EmployeeResponse> result=employeeService.updateEmployee(999L,request);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDeleteEmployeeWhenEmployeeExist(){
        when(employeeRepository.existsById(1L))
                .thenReturn(true);
        boolean result=employeeService.deleteEmployee(1L);
        assertTrue(result);
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void shouldNotDeleteEmployeeWhenEmployeeNotExist(){
        when(employeeRepository.existsById(1L))
                .thenReturn(false);

        boolean result=employeeService.deleteEmployee(1L);
        assertFalse(result);
        verify(employeeRepository,never()).deleteById(1L);
    }




}
