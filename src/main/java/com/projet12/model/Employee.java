package com.projet12.model;

public class Employee {
    private int numEmp;
    private String nom;
    private double salaire;
    private String obs;

    // Constructeurs
    public Employee() {}

    public Employee(int numEmp, String nom, double salaire, String obs) {
        this.numEmp = numEmp;
        this.nom = nom;
        this.salaire = salaire;
        this.obs = obs;
    }

    // Getters et setters
    public int getNumEmp() { return numEmp; }
    public void setNumEmp(int numEmp) { this.numEmp = numEmp; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getSalaire() { return salaire; }
    public void setSalaire(double salaire) { this.salaire = salaire; }
    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }
}