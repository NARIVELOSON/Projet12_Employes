package com.projet12.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projet12.model.Employee;
import okhttp3.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class EmployeeService {
    private static final String BASE_URL = "http://localhost:8080/api/employees"; // URL de l'API (à ajuster)
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    // Récupérer tous les employés
    public List<Employee> getAllEmployees() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Type listType = new TypeToken<List<Employee>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    // Ajouter un employé
    public void addEmployee(Employee employee) throws IOException {
        String json = gson.toJson(employee);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();
        client.newCall(request).execute();
    }

    // Modifier un employé
    public void updateEmployee(Employee employee) throws IOException {
        String json = gson.toJson(employee);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + employee.getNumEmp())
                .put(body)
                .build();
        client.newCall(request).execute();
    }

    // Supprimer un employé
    public void deleteEmployee(int numEmp) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + numEmp)
                .delete()
                .build();
        client.newCall(request).execute();
    }

    // Statistiques (salaire total, min, max)
    public double[] getSalaryStats() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/stats")
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        return gson.fromJson(json, double[].class); // [total, min, max]
    }
}