package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import customlistview.DepartmentAdapter;
import models.Department;

public class DepartmentManagement extends AppCompatActivity {
private ImageButton addEmployeeButton;
    private GridView departmentGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.department_management);

        departmentGridView = findViewById(R.id.departmentGridView);

        DatabaseHandler dbHandler = new DatabaseHandler(this);
        List<Department> departmentList = dbHandler.getAllDepartment();
        Log.d("departmentList", String.valueOf(departmentList.size()));

        DepartmentAdapter adapter = new DepartmentAdapter(this, departmentList);
        departmentGridView.setAdapter(adapter);
    }
}