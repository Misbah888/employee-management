package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository=employeeRepository;
    }

    public EmployeeResponse saveEmployee(EmployeeRequest request){
        Employee employee=new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee savedEmployee=employeeRepository.save(employee);

        return toResponse(savedEmployee);

    }

    public Page<EmployeeResponse> getAllEmployees(Pageable pageable){
        return employeeRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public Optional<EmployeeResponse> getEmployeeById(Long id){
        return employeeRepository.findById(id)
                .map(this::toResponse);
    }

    public Optional<EmployeeResponse> updateEmployee(Long id,EmployeeRequest request){
        Optional<Employee> existingEmployee=employeeRepository.findById(id);

        if(existingEmployee.isEmpty()){
            return Optional.empty();
        }

        Employee employee=existingEmployee.get();
        employee.setName(request.getName());
        employee.setDepartment(request.getDepartment());
        employee.setEmail(request.getEmail());
        employee.setSalary(request.getSalary());

        Employee savedEmployee=employeeRepository.save(employee);
        return Optional.of(toResponse(savedEmployee));
    }

    public boolean deleteEmployee(Long id){
        if(!employeeRepository.existsById(id)){
            return false;
        }
        employeeRepository.deleteById(id);
        return true;
    }

    private EmployeeResponse toResponse(Employee employee){
        return new EmployeeResponse(employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary());
    }

}
