package com.employeemanagement.repository;

import com.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeId(String employeeId);
    Optional<Employee> findByEmail(String email);
    List<Employee> findByDepartmentId(Long departmentId);
    
    @Query("SELECT e FROM Employee e WHERE e.firstName LIKE %:name% OR e.lastName LIKE %:name%")
    List<Employee> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT e FROM Employee e WHERE e.position LIKE %:position%")
    List<Employee> findByPosition(@Param("position") String position);
    
    List<Employee> findByStatus(Employee.EmploymentStatus status);
    
    boolean existsByEmployeeId(String employeeId);
    boolean existsByEmail(String email);
}
