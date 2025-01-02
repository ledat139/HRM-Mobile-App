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

import customlistview.ParticipantAdapter;
import models.Department;
import models.NhanVien;
import models.Project;
import models.Project_NhanVien;

public class UpdateProject extends AppCompatActivity {

    private TextView tvLabel;
    private Project project;
    private String updatedStatus;
    private int projectID, updatedDepartmentID;
    private RecyclerView participantContainer;
    private Button btnUpdateProject, btnAddParticipant;
    private EditText inputUpdateProjectName, inputUpdateProjectSInitialDate, inputUpdateProjectSEndingDate;
    private Spinner inputUpdateProjectSDepartmentID, inputUpdateProjectStatus;
    private EditText inputUpdateProjectDesc;
    List<Project_NhanVien> participantList;

    ParticipantAdapter participantAdapter;

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_project);

        inputUpdateProjectName = findViewById(R.id.inputUpdateProjectName);
        inputUpdateProjectSInitialDate = findViewById(R.id.inputUpdateProjectSInitialDate);
        inputUpdateProjectSEndingDate = findViewById(R.id.inputUpdateProjectSEndingDate);
        inputUpdateProjectDesc = findViewById(R.id.inputUpdateProjectDesc);
        inputUpdateProjectSDepartmentID = findViewById(R.id.inputUpdateProjectSDepartmentID);
        btnUpdateProject = findViewById(R.id.btnUpdateProject);
        tvLabel = findViewById(R.id.tvLabel);
        btnAddParticipant = findViewById(R.id.btnAddParticipant);
        participantContainer = findViewById(R.id.participantContainer);
        inputUpdateProjectStatus = findViewById(R.id.inputUpdateProjectStatus);

        dbHandler = new DatabaseHandler(this);

        projectID = getIntent().getIntExtra("ProjectID", -1);
        project = dbHandler.getProject(projectID);

        participantList = dbHandler.getAllProjectsNhanVien(projectID);
        participantAdapter = new ParticipantAdapter(this ,participantList);

        participantContainer.setLayoutManager(new LinearLayoutManager(this));
        participantContainer.setAdapter(participantAdapter);



        List<Department> phongBanList = dbHandler.getAllDepartment();

        ArrayList<String> tenPhongBanList = new ArrayList<>();
        for (Department phongBan : phongBanList) {
            tenPhongBanList.add(phongBan.getDepartmentName() + " (ID: " + phongBan.getDepartmentId() + ")");
        }

        int selectedStatusPosition = 0;
        int selectedDepartmentPosition = project.getMaPB() - 1;

        for (int i = 0; i < getResources().getStringArray(R.array.project_status_options).length; i++) {
            if (getResources().getStringArray(R.array.project_status_options)[i].equals(project.getTrangThai())) {
                selectedStatusPosition = i;
                break;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenPhongBanList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputUpdateProjectSDepartmentID.setAdapter(adapter);
        inputUpdateProjectSDepartmentID.setSelection(selectedDepartmentPosition);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.project_status_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputUpdateProjectStatus.setAdapter(spinnerAdapter);
        inputUpdateProjectStatus.setSelection(selectedStatusPosition);


        updatedStatus = project.getTrangThai();
        updatedDepartmentID = project.getMaPB();

        tvLabel.setText("Cập nhật dự án: " + projectID);
        inputUpdateProjectName.setText(project.getTenDA());
        inputUpdateProjectSInitialDate.setText(project.getNgayBD());
        inputUpdateProjectSEndingDate.setText(project.getNgayKT());
        inputUpdateProjectDesc.setText(project.getMoTa());

        inputUpdateProjectSInitialDate.setOnClickListener(view -> showDatePickerDialog(inputUpdateProjectSInitialDate, project.getNgayBD()));
        inputUpdateProjectSEndingDate.setOnClickListener(view -> showDatePickerDialog(inputUpdateProjectSEndingDate, project.getNgayKT()));

        btnAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddParticipantDialog();
            }
        });

        btnUpdateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String projectName = inputUpdateProjectName.getText().toString().trim();
                String startDate = inputUpdateProjectSInitialDate.getText().toString().trim();
                String endDate = inputUpdateProjectSEndingDate.getText().toString().trim();
                String description = inputUpdateProjectDesc.getText().toString().trim();

                int selectedDepartmentPosition = inputUpdateProjectSDepartmentID.getSelectedItemPosition();
                Department selectedPhongBan = phongBanList.get(selectedDepartmentPosition);

                int selectedStatusPosition = inputUpdateProjectStatus.getSelectedItemPosition();
                String projectStatus = getResources().getStringArray(R.array.project_status_options)[selectedStatusPosition];

                if (projectName.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || selectedPhongBan == null || projectStatus.isEmpty()) {
                    Toast.makeText(UpdateProject.this, "Vui lòng nhập đầy đủ thông tin dự án!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (participantList.isEmpty()) {
                    Toast.makeText(UpdateProject.this, "Vui lòng thêm ít nhất một người vào dự án!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Project project = new Project(projectID ,projectName, startDate, endDate, projectStatus, description, selectedPhongBan.getDepartmentId());
                    dbHandler.updateProject(project);
                    for (Project_NhanVien participant : participantList) {
                        participant.setMaDA(projectID);
                        dbHandler.updateProjectNhanVien(participant);
                    }
                }catch (Exception e) {
                    Toast.makeText(UpdateProject.this, "Cập nhật dự án không thành công", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(UpdateProject.this, "Đã cập nhật dự án", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showDatePickerDialog(final EditText editText, String date) {
        String[] day_month_year = date.split("/");
        int day = Integer.parseInt(day_month_year[0]);
        int month = Integer.parseInt(day_month_year[1]) - 1;
        int year = Integer.parseInt(day_month_year[2]);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                editText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                editText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showAddParticipantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_participant, null);
        builder.setView(dialogView);

        Spinner inputParticipantID = dialogView.findViewById(R.id.inputParticipantID);
        TextInputEditText inputParticipantRole = dialogView.findViewById(R.id.inputParticipantRole);
        TextInputEditText inputParticipantJoiningDate = dialogView.findViewById(R.id.inputParticipantJoiningDate);

        inputParticipantJoiningDate.setOnClickListener(view -> showDatePickerDialog(inputParticipantJoiningDate));

        ArrayList<NhanVien> nhanVienList = new ArrayList<>();
        nhanVienList.add(new NhanVien(1, "Trần Thị B", "Nữ", "27/12/2024", "0907654321", "tranthib@example.com", "Hà Nội", "123456789", "Nhân viên", 1));
        nhanVienList.add(new NhanVien(2, "Phạm Minh C", "Nam", "27/12/2024", "0912345678", "phamminhc@example.com", "Hồ Chí Minh", "987654321", "Quản lý", 1));
        nhanVienList.add(new NhanVien(3, "Lê Thị D", "Nữ", "27/12/2024", "0934567890", "lethid@example.com", "Đà Nẵng", "112233445", "Trưởng phòng", 1));

        ArrayList<String> tenNhanVienList = new ArrayList<>();
        for (NhanVien nhanVien : nhanVienList) {
            tenNhanVienList.add(nhanVien.getHoTen() + " (ID: " + nhanVien.getMaNV() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenNhanVienList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputParticipantID.setAdapter(adapter);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            int selectedPosition = inputParticipantID.getSelectedItemPosition();
            NhanVien selectedNhanVien = nhanVienList.get(selectedPosition);
            String role = inputParticipantRole.getText().toString().trim();
            String joiningDate = inputParticipantJoiningDate.getText().toString().trim();

            if (role.isEmpty() || joiningDate.isEmpty()) {
                Toast.makeText(UpdateProject.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }else {
                Project_NhanVien participant = new Project_NhanVien(selectedNhanVien.getMaNV(), role, joiningDate);
                participantList.add(participant);
                participantAdapter.notifyDataSetChanged();
                Toast.makeText(UpdateProject.this, "Đã thêm người tham gia", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}