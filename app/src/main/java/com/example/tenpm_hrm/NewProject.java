package com.example.tenpm_hrm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

import customlistview.ParticipantAdapter;
import models.Department;
import models.NhanVien;
import models.Project;
import models.Project_NhanVien;

public class NewProject extends AppCompatActivity {

    private RecyclerView participantContainer;
    private Button btnAddProject, btnAddParticipant;
    private EditText inputAddProjectName, inputAddProjectSInitialDate, inputAddProjectSEndingDate;
    private Spinner  inputAddProjectSDepartmentID;
    private EditText inputAddProjectDesc;
    ArrayList<Project_NhanVien> participantList;

    ParticipantAdapter participantAdapter;

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_project);

        inputAddProjectName = findViewById(R.id.inputAddProjectName);
        inputAddProjectSInitialDate = findViewById(R.id.inputAddProjectSInitialDate);
        inputAddProjectSEndingDate = findViewById(R.id.inputAddProjectSEndingDate);
        inputAddProjectDesc = findViewById(R.id.inputAddProjectDesc);
        inputAddProjectSDepartmentID = findViewById(R.id.inputAddProjectSDepartmentID);
        btnAddProject = findViewById(R.id.btnAddProject);
        btnAddParticipant = findViewById(R.id.buttonAddParticipant);
        participantContainer = findViewById(R.id.participantContainer);

        participantList = new ArrayList<>();
        participantAdapter = new ParticipantAdapter(this ,participantList);

        participantContainer.setLayoutManager(new LinearLayoutManager(this));;
        participantContainer.setAdapter(participantAdapter);

        dbHandler = new DatabaseHandler(this);

        ArrayList<Department> phongBanList = new ArrayList<>();
        phongBanList.add(new Department(1, "Phòng Kỹ Thuật", "2020-01-01", 1, "Nguyễn Văn A", "path_to_it_avatar"));
        phongBanList.add(new Department(2, "Phòng Kinh Doanh", "2019-05-15", 2, "Trần Thị B", "path_to_sale_avatar"));
        phongBanList.add(new Department(3, "Phòng Nhân Sự", "2018-03-20", 3, "Lê Thị C", "path_to_hr_avatar"));
        phongBanList.add(new Department(4, "Phòng Marketing", "2021-06-10", 4, "Phạm Minh D", "path_to_marketing_avatar"));

        ArrayList<String> tenPhongBanList = new ArrayList<>();
        tenPhongBanList.add("Chọn phòng ban");
        for (Department phongBan : phongBanList) {
            tenPhongBanList.add(phongBan.getDepartmentName() + " (ID: " + phongBan.getDepartmentId() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenPhongBanList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputAddProjectSDepartmentID.setAdapter(adapter);

        inputAddProjectSInitialDate.setOnClickListener(view -> showDatePickerDialog(inputAddProjectSInitialDate));
        inputAddProjectSEndingDate.setOnClickListener(view -> showDatePickerDialog(inputAddProjectSEndingDate));


        btnAddParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddParticipantDialog();
            }
        });

        btnAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String projectName = inputAddProjectName.getText().toString().trim();
                String startDate = inputAddProjectSInitialDate.getText().toString().trim();
                String endDate = inputAddProjectSEndingDate.getText().toString().trim();
                String description = inputAddProjectDesc.getText().toString().trim();

                int selectedPosition = inputAddProjectSDepartmentID.getSelectedItemPosition();

                if (projectName.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || selectedPosition == 0) {
                    Toast.makeText(NewProject.this, "Vui lòng nhập đầy đủ thông tin dự án!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Department selectedPhongBan = phongBanList.get(selectedPosition - 1);

                if (participantList.isEmpty()) {
                    Toast.makeText(NewProject.this, "Vui lòng thêm ít nhất một người vào dự án!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Project project = new Project(projectName, startDate, endDate, description, selectedPhongBan.getDepartmentId());
                    int projectId = dbHandler.addProject(project);
                    for (Project_NhanVien participant : participantList) {
                        participant.setMaDA(projectId);
                        dbHandler.addProjectNhanVien(participant);
                    }
                }catch (Exception e) {
                    Toast.makeText(NewProject.this, "Thêm dự án mới không thành công!", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(NewProject.this, "Đã thêm dự án mới!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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
        nhanVienList.add(new NhanVien(1, "Trần Thị B", "Nữ", "1992-08-20", "0907654321", "tranthib@example.com", "Hà Nội", "123456789", "Nhân viên", 1));
        nhanVienList.add(new NhanVien(2, "Phạm Minh C", "Nam", "1988-11-10", "0912345678", "phamminhc@example.com", "Hồ Chí Minh", "987654321", "Quản lý", 1));
        nhanVienList.add(new NhanVien(3, "Lê Thị D", "Nữ", "1995-02-25", "0934567890", "lethid@example.com", "Đà Nẵng", "112233445", "Trưởng phòng", 1));

        ArrayList<String> tenNhanVienList = new ArrayList<>();
        tenNhanVienList.add("Chọn nhân viên");
        for (NhanVien nhanVien : nhanVienList) {
            tenNhanVienList.add(nhanVien.getHoTen() + " (ID: " + nhanVien.getMaNV() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenNhanVienList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputParticipantID.setAdapter(adapter);


        builder.setPositiveButton("Thêm", (dialog, which) -> {
            int selectedPosition = inputParticipantID.getSelectedItemPosition();
            String role = inputParticipantRole.getText().toString().trim();
            String joiningDate = inputParticipantJoiningDate.getText().toString().trim();

            if (role.isEmpty() || joiningDate.isEmpty() || selectedPosition == 0) {
                Toast.makeText(NewProject.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            NhanVien selectedNhanVien = nhanVienList.get(selectedPosition - 1);

            Project_NhanVien participant = new Project_NhanVien(selectedNhanVien.getMaNV(), role, joiningDate);
            participantList.add(participant);

            participantAdapter.notifyDataSetChanged();

            Toast.makeText(NewProject.this, "Đã thêm người tham gia", Toast.LENGTH_SHORT).show();

        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}