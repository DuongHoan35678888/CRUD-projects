package com.bezkoder.spring.datajpa.controller;

import com.bezkoder.spring.datajpa.model.Employee;
import com.bezkoder.spring.datajpa.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService iEmployeeService;

    // Test
    @GetMapping("/")
    public String test () {
        return "Hello Hoan";
    }

    // API add employee
    @PostMapping("/add")
    public Employee addEmployee(@RequestBody Employee employee) {
        return iEmployeeService.addEmployee(employee);
    }

    // API update employee
    @PutMapping("/update")
    public Employee updateEmployee(@RequestParam("id") long id, @RequestBody Employee employee) {
        return iEmployeeService.updateEmployee(id, employee);
    }

    // API delete employee
    @DeleteMapping("/delete/{id}")
    private boolean deleteEmployee(@PathVariable("id") long id) {
        return iEmployeeService.deleteEmployee(id);
    }

    // API lay danh sach employee
    @GetMapping("/list")
    public List<Employee> getAllEmployee() {
        return iEmployeeService.getAllEmployee();
    }
}
