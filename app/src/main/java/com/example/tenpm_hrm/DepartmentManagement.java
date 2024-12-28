package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
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

        addEmployeeButton = findViewById(R.id.addEmployeeButton);

        // Create sample department list
        List<Department> departmentList = new ArrayList<>();

        departmentList.add(new Department("001", "IT", "001", "Nguyen Van A", 10, R.drawable.it));
        departmentList.add(new Department("002", "HR", "002", "Tran Thi B", 5, R.drawable.hr));
        departmentList.add(new Department("003", "Sales", "003", "Le Van C", 8, R.drawable.sale));
        departmentList.add(new Department("004", "Marketing", "004", "Pham Thi D", 6, R.drawable.marketing));

        // Initialize adapter and set to GridView
        DepartmentAdapter adapter = new DepartmentAdapter(this, departmentList);
        departmentGridView.setAdapter(adapter);

        addEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show ProgressBar before starting the new activity
//                progressBar.setVisibility(View.VISIBLE);

                // Start the new activity
                Intent newRequestIntent = new Intent(DepartmentManagement.this, NewDepartment.class);
                startActivity(newRequestIntent);

                // Optionally hide the ProgressBar after a short delay
                // This is just to simulate loading; adjust as needed
//                v.postDelayed(() -> progressBar.setVisibility(View.GONE), 300);
            }
        });
    }
}