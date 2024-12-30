package com.example.tenpm_hrm;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.Department;
import models.Facility;
import models.NhanVien;

public class UpdateDepartment extends AppCompatActivity {

    private TextView tvDepartmentID, tvEmployeeNumber;
    private EditText inputAddDepartmentName, inputAddDepartmentEstablishmentDate, inputAddManagerAppointmentDate;
    private Button btnUpdateDepartment;
    private DatabaseHandler dbHandler;
    private int departmentID;
    private Department department;
    private Spinner inputUpdateManagerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_department);

        dbHandler = new DatabaseHandler(this);

        tvEmployeeNumber = findViewById(R.id.tvEmployeeNumber);
        tvDepartmentID = findViewById(R.id.tvDepartmentID);
        inputAddDepartmentName = findViewById(R.id.inputAddDepartmentName);
        inputAddDepartmentEstablishmentDate = findViewById(R.id.inputAddDepartmentEstablishmentDate);
        inputAddManagerAppointmentDate = findViewById(R.id.inputAddManagerAppointmentDate);
        btnUpdateDepartment = findViewById(R.id.btnUpdateDepartment);
        inputUpdateManagerID = findViewById(R.id.inputUpdateManagerID);


        departmentID = getIntent().getIntExtra("departmentID", -1);
        department = dbHandler.getDepartment(departmentID);
        int employeeCounting = dbHandler.countEmployeesByDepartment(departmentID);

        List<NhanVien> nhanVienList = dbHandler.getAllEmployes();

        ArrayList<String> tenNhanVienList = new ArrayList<>();
        for (NhanVien nhanVien : nhanVienList) {
            tenNhanVienList.add(nhanVien.getHoTen() + " (ID: " + nhanVien.getMaNV() + ")");
        }

        int selectedManagerPosition = department.getManagerId() - 1;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenNhanVienList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputUpdateManagerID.setAdapter(adapter);
        inputUpdateManagerID.setSelection(selectedManagerPosition);


        tvDepartmentID.setText("Cập nhật phòng ban: " + String.valueOf(departmentID));
        inputAddDepartmentName.setText(department.getDepartmentName());
        inputAddDepartmentEstablishmentDate.setText(String.valueOf(department.getEstablishmentDate()));
        inputAddManagerAppointmentDate.setText(department.getManagerAppointmentDate());
        tvEmployeeNumber.setText("Số lượng nhân viên: " + String.valueOf(employeeCounting));

        inputAddManagerAppointmentDate.setOnClickListener(view -> showDatePickerDialog(inputAddManagerAppointmentDate, department.getManagerAppointmentDate()));

        btnUpdateDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputAddDepartmentName.getText().toString();
                String establishmentDate = inputAddDepartmentEstablishmentDate.getText().toString();
                String managerAppointmentDate = inputAddManagerAppointmentDate.getText().toString();

                int selectedManagerPosition = inputUpdateManagerID.getSelectedItemPosition();
                NhanVien nhanVien = nhanVienList.get(selectedManagerPosition);

                if (name.isEmpty() || establishmentDate.isEmpty() || managerAppointmentDate.isEmpty() || nhanVien == null) {
                    Toast.makeText(UpdateDepartment.this, "Thông tin phòng ban không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Department newDepartment = new Department(departmentID, name, establishmentDate, nhanVien.getMaNV(), managerAppointmentDate, department.getAvatarPath());
                    dbHandler.updateDepartment(newDepartment);
                }catch (Exception e) {
                    Toast.makeText(UpdateDepartment.this, "Cập nhật phòng ban không thành công", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(UpdateDepartment.this, "Cập nhật phòng ban thành công", Toast.LENGTH_SHORT).show();
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
}
