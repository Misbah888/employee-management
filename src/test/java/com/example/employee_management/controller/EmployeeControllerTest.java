package com.example.employee_management.controller;

import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Test
    void shouldCreateEmployee() throws Exception{
        String requestJson= """
                {
                "name":"Bumrah",
                "email":"bumrah.test88@gmail.com",
                "department":"Cricket",
                "salary":55000
                }
                """;
        mockMvc.perform(
                post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(requestJson)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Bumrah"))
                .andExpect(jsonPath("$.email").value("bumrah.test88@gmail.com"))
                .andExpect(jsonPath("$.department").value("Cricket"));
    }

    @Test
    void shouldReturnEmployeeById() throws Exception{
        Employee employee=new Employee();
        employee.setName("Virat");
        employee.setEmail("virat.test@gmail.com");
        employee.setSalary(new BigDecimal("70000"));
        employee.setDepartment("Cricket");

        Employee savedEmployee=employeeRepository.save(employee);

        mockMvc.perform(
                get("/api/employees/"+savedEmployee.getId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Virat"))
                .andExpect(jsonPath("$.email").value("virat.test@gmail.com"));
    }

    @Test
    void shouldReturn404WhenEmployeeNotExist() throws Exception{
        mockMvc.perform(
                get("/api/employees/9999")
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllEmployees() throws Exception{
        Employee employee1=new Employee();
        employee1.setName("Abhishek");
        employee1.setEmail("abhi.t20@gmail.com");
        employee1.setSalary(new BigDecimal("66000"));
        employee1.setDepartment("Cricket");

        Employee employee2=new Employee();
        employee2.setName("Kumar");
        employee2.setEmail("kumar.k@gmail.com");
        employee2.setSalary(new BigDecimal("70000"));
        employee2.setDepartment("Hockey");

        Employee savedEmployee1=employeeRepository.save(employee1);
        Employee savedEmployee2=employeeRepository.save(employee2);

        mockMvc.perform(
                get("/api/employees")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(2)));
    }

    @Test
    void shouldUpdateEmployee() throws Exception{
        Employee existing=new Employee();
        existing.setName("Rahul");
        existing.setEmail("rahul@gmail.com");
        existing.setDepartment("ECE");
        existing.setSalary(new BigDecimal("30000"));

        Employee savedEmployee=employeeRepository.save(existing);
        String requestJson= """
                {
                "name":"Rahul singh",
                "email":"Rahul.singh@gmail.com",
                "department":"ECE",
                "salary":35000
                }
                """;

        mockMvc.perform(
                put("/api/employees/"+savedEmployee.getId())
                        .contentType(APPLICATION_JSON)
                        .content(requestJson)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rahul singh"))
                .andExpect(jsonPath("$.email").value("Rahul.singh@gmail.com"));
    }

    @Test
    void shouldDeleteEmployee() throws Exception{
        Employee employee=new Employee();
        employee.setName("Sohail");
        employee.setDepartment("CSE");
        employee.setEmail("sohail@gmail.com");
        employee.setSalary(new BigDecimal("44000"));

        Employee savedEmployee=employeeRepository.save(employee);

        mockMvc.perform(
                delete("/api/employees/"+savedEmployee.getId())
        )
                .andExpect(status().isNoContent());
                assertFalse(employeeRepository.existsById(savedEmployee.getId()));
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception{
        String requestJson= """
                {
                "name":"  ",
                "email":"asman.singh@gmail.com",
                "department":"MEC",
                "salary":35000
                }
                """;

        mockMvc.perform(
                post("/api/employees")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenEmailIsInValid() throws Exception{
            String requestjson=
                    """
                {
                "name":"Misbah",
                "email":"misbah.khan",
                "department":"CSE",
                "salary":70000
                }
                """;

            mockMvc.perform(
                    post("/api/employees")
                            .contentType(APPLICATION_JSON)
                            .content(requestjson)
            )

                    .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {

        Employee employee = new Employee();
        employee.setName("First Employee");
        employee.setEmail("duplicate2.test.2026@example.com");
        employee.setDepartment("IT");
        employee.setSalary(new BigDecimal("50000"));

        employeeRepository.save(employee);

        String requestJson = """
           {
                "name": "Second Employee",
                "email": "duplicate2.test.2026@example.com",
                "department": "CSE",
                "salary": 60000
            }
            """;

        mockMvc.perform(post("/api/employees")
                       .contentType(APPLICATION_JSON)
                       .content(requestJson))
                .andExpect(status().isConflict());
   }
}
