package com.example.employee_management.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class EmployeeRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 80)
    @NotBlank
    @Email
    private String email;

    @Size(max = 80)
    @NotBlank
    private String department;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal salary;

    public EmployeeRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
