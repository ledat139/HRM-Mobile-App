package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

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

        DatabaseHandler dbHandler = new DatabaseHandler(this);
        List<Department> departmentList = dbHandler.getAllDepartment();

//        departmentList.add(new Department(1, "IT", "2020-01-01", 1, "2020-02-15", "path_to_it_avatar"));
//        departmentList.add(new Department(2, "HR", "2019-05-15", 2, "2019-06-10", "path_to_hr_avatar"));
//        departmentList.add(new Department(3, "Sales", "2018-03-20", 3, "2018-04-05", "path_to_sales_avatar"));
//        departmentList.add(new Department(4, "Marketing", "2021-06-10", 4, "2021-07-01", "path_to_marketing_avatar"));

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