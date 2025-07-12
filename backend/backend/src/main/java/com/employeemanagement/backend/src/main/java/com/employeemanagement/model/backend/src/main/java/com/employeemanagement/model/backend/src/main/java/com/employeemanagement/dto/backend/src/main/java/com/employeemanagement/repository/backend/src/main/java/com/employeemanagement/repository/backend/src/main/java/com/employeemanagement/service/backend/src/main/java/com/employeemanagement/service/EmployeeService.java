package com.employeemanagement.service;

import com.employeemanagement.dto.EmployeeRequest;
import com.employeemanagement.model.Department;
import com.employeemanagement.model.Employee;
import com.employeemanagement.repository.DepartmentRepository;
import com.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
    
    public Optional<Employee> getEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }
    
    public List<Employee> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }
    
    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByNameContaining(name);
    }
    
    public List<Employee> getEmployeesByPosition(String position) {
        return employeeRepository.findByPosition(position);
    }
    
    public List<Employee> getEmployeesByStatus(Employee.EmploymentStatus status) {
        return employeeRepository.findByStatus(status);
    }
    
    public Employee createEmployee(EmployeeRequest request) {
        // Check if employee ID already exists
        if (employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new RuntimeException("Employee with this ID already exists");
        }
        
        // Check if email already exists
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Employee with this email already exists");
        }
        
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        
        Employee employee = new Employee(
                request.getEmployeeId(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getDateOfBirth(),
                request.getAddress(),
                request.getPosition(),
                request.getSalary(),
                request.getHireDate(),
                request.getGender(),
                department
        );
        
        employee.setEmergencyContact(request.getEmergencyContact());
        employee.setEmergencyPhone(request.getEmergencyPhone());
        
        return employeeRepository.save(employee);
    }
    
    public Employee updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        // Check if employee ID is being changed and if new ID already exists
        if (!employee.getEmployeeId().equals(request.getEmployeeId()) &&
            employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new RuntimeException("Employee with this ID already exists");
        }
        
        // Check if email is being changed and if new email already exists
        if (!employee.getEmail().equals(request.getEmail()) &&
            employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Employee with this email already exists");
        }
        
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        
        employee.setEmployeeId(request.getEmployeeId());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setAddress(request.getAddress());
        employee.setPosition(request.getPosition());
        employee.setSalary(request.getSalary());
        employee.setHireDate(request.getHireDate());
        employee.setGender(request.getGender());
        employee.setDepartment(department);
        employee.setEmergencyContact(request.getEmergencyContact());
        employee.setEmergencyPhone(request.getEmergencyPhone());
        
        return employeeRepository.save(employee);
    }
    
    public Employee updateEmployeeStatus(Long id, Employee.EmploymentStatus status) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        employee.setStatus(status);
        return employeeRepository.save(employee);
    }
    
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
