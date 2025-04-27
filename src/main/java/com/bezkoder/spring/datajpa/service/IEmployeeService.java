package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.model.Employee;

import java.util.List;

public interface IEmployeeService {
    // them nhan vien
    public Employee addEmployee(Employee employee);

    // chinh sua thong tin nhan vien
    public Employee updateEmployee(long id, Employee employee);

    // xoa nhan vien
    public boolean deleteEmployee(long id);

    // lay ra danh sach nhan vien
    public List<Employee> getAllEmployee();

    // lay ra 1 nhan vien
    public Employee getOneEmployee(long id);
}
