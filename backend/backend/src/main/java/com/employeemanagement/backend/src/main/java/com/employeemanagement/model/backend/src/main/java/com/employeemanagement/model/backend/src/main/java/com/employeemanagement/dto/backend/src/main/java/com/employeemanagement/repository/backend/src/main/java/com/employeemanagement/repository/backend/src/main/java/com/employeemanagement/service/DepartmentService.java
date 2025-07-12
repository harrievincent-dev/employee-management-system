package com.employeemanagement.service;

import com.employeemanagement.model.Department;
import com.employeemanagement.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }
    
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByDepartmentName(department.getDepartmentName())) {
            throw new RuntimeException("Department with this name already exists");
        }
        return departmentRepository.save(department);
    }
    
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        
        // Check if name is being changed and if new name already exists
        if (!department.getDepartmentName().equals(departmentDetails.getDepartmentName()) &&
            departmentRepository.existsByDepartmentName(departmentDetails.getDepartmentName())) {
            throw new RuntimeException("Department with this name already exists");
        }
        
        department.setDepartmentName(departmentDetails.getDepartmentName());
        department.setDescription(departmentDetails.getDescription());
        department.setLocation(departmentDetails.getLocation());
        department.setBudget(departmentDetails.getBudget());
        department.setManagerName(departmentDetails.getManagerName());
        
        return departmentRepository.save(department);
    }
    
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        
        if (department.getEmployees() != null && !department.getEmployees().isEmpty()) {
            throw new RuntimeException("Cannot delete department with existing employees");
        }
        
        departmentRepository.deleteById(id);
    }
}
