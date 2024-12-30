package com.example.tenpm_hrm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import customlistview.ParticipantEmployeeAdapter;
import models.Department;
import models.NhanVien;
import models.Project;
import models.Project_NhanVien;

public class ProjectDetail extends AppCompatActivity {

    private TextView tvLabel;
    private Project project;
    private int projectID;
    private RecyclerView participantContainer;
    private EditText projectName, projectSInitialDate, projectSEndingDate, projectDesc, projectDepartmentName, projectStatus;
    List<Project_NhanVien> participantList;

    ParticipantEmployeeAdapter participantEmployeeAdapter;

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.project_detail);

        tvLabel = findViewById(R.id.tvLabel);
        projectName = findViewById(R.id.projectName);
        projectSInitialDate = findViewById(R.id.projectSInitialDate);
        projectSEndingDate = findViewById(R.id.projectSEndingDate);
        projectDesc = findViewById(R.id.projectDesc);
        projectDepartmentName = findViewById(R.id.projectDepartmentName);
        participantContainer = findViewById(R.id.participantContainer);
        projectStatus = findViewById(R.id.projectStatus);

        dbHandler = new DatabaseHandler(this);

        projectID = getIntent().getIntExtra("ProjectID", -1);
        project = dbHandler.getProject(projectID);

        participantList = dbHandler.getAllProjectsNhanVien(projectID);
        participantEmployeeAdapter = new ParticipantEmployeeAdapter(this ,participantList);

        participantContainer.setLayoutManager(new LinearLayoutManager(this));
        participantContainer.setAdapter(participantEmployeeAdapter);
        
        Department department = dbHandler.getDepartment(project.getMaPB());

        tvLabel.setText("Thông tin chi tiết dự án: " + projectID);
        projectName.setText(project.getTenDA());
        projectSInitialDate.setText(project.getNgayBD());
        projectSEndingDate.setText(project.getNgayKT());
        projectDepartmentName.setText(department.getDepartmentName());
        projectStatus.setText(project.getTrangThai());
        projectDesc.setText(project.getMoTa());
    }
}