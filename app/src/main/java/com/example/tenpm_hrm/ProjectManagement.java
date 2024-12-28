package com.example.tenpm_hrm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import models.Project;

public class ProjectManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project);

        LinearLayout projectContainer = findViewById(R.id.projectContainer);
        Button btnProjectAdd = findViewById(R.id.btnProjectAdd);

        btnProjectAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectManagement.this, NewProject.class);
                startActivity(intent);
            }
        });



        // Create and add three project items
//        Project project1 = new Project("1", "Phát triển web TENPM", "Huỳnh Nhật Duy", "Chưa hoàn thành");
//        Project project2 = new Project("2", "Phát triển web bán hàng", "Huỳnh Nhật Duy", "Hoàn thành");
//        Project project3 = new Project("3", "Phát triển web bán hàng", "Huỳnh Nhật Duy", "Hoàn thành");
//
//        addProjectToContainer(projectContainer, project1);
//        addProjectToContainer(projectContainer, project2);
//        addProjectToContainer(projectContainer, project3);
    }

    private void addProjectToContainer(ViewGroup container, Project project) {
        View projectItem = LayoutInflater.from(this).inflate(R.layout.item_project, container, false);

        TextView tvProjectName = projectItem.findViewById(R.id.tvProjectName);
        TextView tvProjectParticipant = projectItem.findViewById(R.id.tvProjectParticipant);
        TextView tvProjectStatus = projectItem.findViewById(R.id.tvProjectStatus);
        ImageView ivProjectDelete = projectItem.findViewById(R.id.ivProjectDelete);

//        tvProjectName.setText(project.getName());
//        tvProjectParticipant.setText(project.getParticipant());
//        tvProjectStatus.setText(project.getStatus());
//        if (project.getStatus() == "Hoàn thành") {
//            tvProjectStatus.setBackgroundResource(R.drawable.employee_type_shape);
//            tvProjectStatus.setTextColor(getResources().getColor(R.color.green));
//        }

        ivProjectDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProjectManagement.this)
                        .setTitle("Xóa dự án")
                        .setMessage("Bạn có chắc chắn muốn xóa dự án này không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Code to delete the project
                                container.removeView(projectItem);
                            }
                        })
                        .setNegativeButton("Không", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        container.addView(projectItem);
    }
}