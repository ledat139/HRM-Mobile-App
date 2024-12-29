package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import customlistview.ProjectAdapter;
import customlistview.ProjectEmployeeAdapter;
import models.Project;

public class ProjectEmployee extends AppCompatActivity {

    private ListView projectContainer;
    private int employeeID;
    private ImageView ivProjectSearch;
    private DatabaseHandler dbHandler;
    private ProjectEmployeeAdapter projectEmployeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_employee);

        ivProjectSearch = findViewById(R.id.ivProjectSearch);
        projectContainer = findViewById(R.id.projectContainer);

        dbHandler = new DatabaseHandler(this);
        employeeID = getIntent().getIntExtra("employeeID", -1);
        List<Project> projectList = dbHandler.getProjectsByEmployee(employeeID);

        projectEmployeeAdapter = new ProjectEmployeeAdapter(this, projectList);
        projectContainer.setAdapter(projectEmployeeAdapter);

        ivProjectSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectEmployee.this, SearchProject.class);
                startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");
            String startingDate = data.getStringExtra("startingDate");
            String endingDate = data.getStringExtra("endingDate");
            String departmentId = data.getStringExtra("departmentId");
            String status = data.getStringExtra("status");

            if (requestCode == 1) {
                List<Project> newProjectList = dbHandler.searchProjectByEmployee(id, name, startingDate, endingDate, status, departmentId, String.valueOf(employeeID));

                if (newProjectList.isEmpty()) {
                    Toast.makeText(this, "Không có dự án có thông tin đang tìm kiếm.", Toast.LENGTH_SHORT).show();
                }else {
                    projectEmployeeAdapter.updateProjectList(newProjectList);
                }
            }
        }
    }
}