package com.projet12.server_spring.controller;

import com.projet12.server_spring.model.Employee;
import com.projet12.server_spring.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository repository;

    @GetMapping
    public List<Employee> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Employee add(@RequestBody Employee employee) {
        // Générer automatiquement la valeur de obs en fonction du salaire
        employee.setObs(generateObs(employee.getSalaire()));
        return repository.save(employee);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable int id, @RequestBody Employee employee) {
        employee.setNumEmp(id);
        // Générer automatiquement la valeur de obs en fonction du salaire
        employee.setObs(generateObs(employee.getSalaire()));
        return repository.save(employee);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    @GetMapping("/stats")
    public double[] getSalaryStats() {
        List<Employee> employees = repository.findAll();
        double total = employees.stream().mapToDouble(Employee::getSalaire).sum();
        double min = employees.stream().mapToDouble(Employee::getSalaire).min().orElse(0);
        double max = employees.stream().mapToDouble(Employee::getSalaire).max().orElse(0);
        return new double[]{total, min, max};
    }

    // Méthode pour générer la valeur de obs
    private String generateObs(double salaire) {
        if (salaire < 1000) {
            return "mediocre";
        } else if (salaire >= 1000 && salaire <= 5000) {
            return "moyen";
        } else {
            return "grand";
        }
    }
}