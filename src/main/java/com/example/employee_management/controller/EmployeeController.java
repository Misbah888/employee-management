package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request){
        EmployeeResponse savedEmployee=employeeService.saveEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(Pageable pageable){

        return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id){
        Optional<EmployeeResponse> employee=employeeService.getEmployeeById(id);

        if(employee.isPresent()){
            return ResponseEntity.ok(employee.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updatedEmployee(@PathVariable Long id,
                                                            @Valid @RequestBody EmployeeRequest request){
        Optional<EmployeeResponse>  updatedEmployee=employeeService.updateEmployee(id,request);
        if(updatedEmployee.isPresent()){
            return ResponseEntity.ok(updatedEmployee.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
        boolean deleted=employeeService.deleteEmployee(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
