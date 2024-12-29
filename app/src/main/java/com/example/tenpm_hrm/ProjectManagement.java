package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import customlistview.ProjectAdapter;
import models.Project;

public class ProjectManagement extends AppCompatActivity {

    private ListView projectContainer;
    private ImageView ivProjectSearch;
    private Button btnProjectAdd;
    private DatabaseHandler dbHandler;
    private ProjectAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project);

        btnProjectAdd = findViewById(R.id.btnProjectAdd);
        ivProjectSearch = findViewById(R.id.ivProjectSearch);
        projectContainer = findViewById(R.id.projectContainer);

        dbHandler = new DatabaseHandler(this);
        List<Project> projectList = dbHandler.getAllProjects();

        projectAdapter = new ProjectAdapter(this, projectList);
        projectContainer.setAdapter(projectAdapter);

        btnProjectAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectManagement.this, NewProject.class);
                startActivityForResult(intent, 2);
            }
        });

        ivProjectSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectManagement.this, SearchProject.class);
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
                List<Project> newProjectList = dbHandler.searchProject(id, name, startingDate, endingDate, status, departmentId);

                if (newProjectList.isEmpty()) {
                    Toast.makeText(this, "Không có dự án có thông tin đang tìm kiếm.", Toast.LENGTH_SHORT).show();
                }else {
                    projectAdapter.updateProjectList(newProjectList);
                }
            }
        }

        if (requestCode == 2 || requestCode == 3) {
            dbHandler = new DatabaseHandler(this);
            List<Project> newProjectList = dbHandler.getAllProjects();

            projectAdapter.updateProjectList(newProjectList);
        }
    }
}