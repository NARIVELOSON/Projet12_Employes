package com.projet12.view;

import com.projet12.model.Employee;
import com.projet12.service.EmployeeService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField numEmpField, nomField, salaireField;
    private JLabel totalLabel, minLabel, maxLabel; // Labels pour les statistiques
    private EmployeeService employeeService;

    public MainFrame() {
        employeeService = new EmployeeService();
        setTitle("Gestion des Employés");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Panneau pour les champs d'entrée et boutons
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        numEmpField = new JTextField();
        nomField = new JTextField();
        salaireField = new JTextField();

        inputPanel.add(new JLabel("NumEmp:"));
        inputPanel.add(numEmpField);
        inputPanel.add(new JLabel("Nom:"));
        inputPanel.add(nomField);
        inputPanel.add(new JLabel("Salaire:"));
        inputPanel.add(salaireField);

        // Boutons
        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton statsButton = new JButton("Afficher Statistiques");
        JButton histogramButton = new JButton("Afficher Histogramme");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(statsButton);
        buttonPanel.add(histogramButton);

        // Panneau pour les champs et boutons (au-dessus du tableau)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Tableau des employés
        String[] columns = {"NumEmp", "Nom", "Salaire", "Obs"};
        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);
        employeeTable.setPreferredScrollableViewportSize(new Dimension(600, 400)); // Taille préférée pour occuper plus de place
        employeeTable.setFillsViewportHeight(true);
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        // Panneau pour afficher les statistiques en bas
        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        totalLabel = new JLabel("Salaire Total: ");
        minLabel = new JLabel("Salaire Minimal: ");
        maxLabel = new JLabel("Salaire Maximal: ");
        statsPanel.add(totalLabel);
        statsPanel.add(minLabel);
        statsPanel.add(maxLabel);

        // Ajout des panneaux
        add(topPanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.SOUTH);

        // Charger les données au démarrage
        loadEmployees();

        // Actions des boutons
        addButton.addActionListener(e -> addEmployee());
        updateButton.addActionListener(e -> updateEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        statsButton.addActionListener(e -> showStats());
        histogramButton.addActionListener(e -> showHistogram());

        // Sélection dans le tableau
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                numEmpField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nomField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                salaireField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            }
        });
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            System.out.println("Nombre d'employés récupérés : " + employees.size()); // Débogage
            // Trier la liste par numEmp en ordre croissant
            Collections.sort(employees, new Comparator<Employee>() {
                @Override
                public int compare(Employee e1, Employee e2) {
                    return Integer.compare(e1.getNumEmp(), e2.getNumEmp());
                }
            });
            tableModel.setRowCount(0); // Vider le tableau
            for (Employee emp : employees) {
                tableModel.addRow(new Object[]{emp.getNumEmp(), emp.getNom(), emp.getSalaire(), emp.getObs()});
                System.out.println("Employé ajouté au tableau : " + emp.getNom()); // Débogage
            }
            updateStats(); // Mettre à jour les stats après chargement
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des employés: " + e.getMessage());
            e.printStackTrace(); // Affiche l'erreur complète dans la console
        }
    }

    private void addEmployee() {
        try {
            Employee emp = new Employee();
            emp.setNumEmp(Integer.parseInt(numEmpField.getText()));
            emp.setNom(nomField.getText());
            emp.setSalaire(Double.parseDouble(salaireField.getText()));
            employeeService.addEmployee(emp);
            loadEmployees();
            clearFields();
            JOptionPane.showMessageDialog(this, "L'insertion a réussi avec succès !");
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            Employee emp = new Employee();
            emp.setNumEmp(Integer.parseInt(numEmpField.getText()));
            emp.setNom(nomField.getText());
            emp.setSalaire(Double.parseDouble(salaireField.getText()));
            employeeService.updateEmployee(emp);
            loadEmployees();
            clearFields();
            JOptionPane.showMessageDialog(this, "La modification a réussi avec succès !");
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            int numEmp = Integer.parseInt(numEmpField.getText());
            employeeService.deleteEmployee(numEmp);
            loadEmployees();
            clearFields();
            JOptionPane.showMessageDialog(this, "La suppression a réussi avec succès !");
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage());
        }
    }

    private void showStats() {
        try {
            double[] stats = employeeService.getSalaryStats();
            JOptionPane.showMessageDialog(this,
                    "Salaire Total: " + stats[0] + "\n" +
                            "Salaire Minimum: " + stats[1] + "\n" +
                            "Salaire Maximum: " + stats[2]);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage());
        }
    }

    private void updateStats() {
        try {
            double[] stats = employeeService.getSalaryStats();
            totalLabel.setText("Salaire Total: " + stats[0]);
            minLabel.setText("<html>Salaire Minimal: <font color='red'>" + stats[1] + "</font></html>");
            maxLabel.setText("<html>Salaire Maximal: <font color='green'>" + stats[2] + "</font></html>");
        } catch (IOException e) {
            totalLabel.setText("Salaire Total: Erreur");
            minLabel.setText("Salaire Minimal: Erreur");
            maxLabel.setText("Salaire Maximal: Erreur");
        }
    }

    private void showHistogram() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Employee emp : employees) {
                dataset.addValue(emp.getSalaire(), "Salaire", emp.getNom());
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Distribution des Salaires",
                    "Employés",
                    "Salaire",
                    dataset
            );

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 300));

            JFrame histogramFrame = new JFrame("Histogramme des Salaires");
            histogramFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            histogramFrame.add(chartPanel);
            histogramFrame.pack();
            histogramFrame.setLocationRelativeTo(this);
            histogramFrame.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création de l'histogramme: " + e.getMessage());
        }
    }

    private void clearFields() {
        numEmpField.setText("");
        nomField.setText("");
        salaireField.setText("");
    }
}