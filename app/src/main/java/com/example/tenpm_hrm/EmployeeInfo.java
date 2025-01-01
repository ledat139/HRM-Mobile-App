package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import models.NhanVien;

public class EmployeeInfo extends AppCompatActivity {

    private EditText manvEdt;
    private EditText hotenEdt;
    private EditText gioitinhEdt;
    private EditText ngaysinhEdt;
    private EditText chucvuEdt;
    private EditText emailEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_information);

        manvEdt = findViewById(R.id.manvEdt);
        hotenEdt = findViewById(R.id.hotenEdt);
        gioitinhEdt = findViewById(R.id.gioitinhEdt);
        ngaysinhEdt = findViewById(R.id.ngaysinhEdt);
        chucvuEdt = findViewById(R.id.chucvuEdt);
        emailEdt = findViewById(R.id.emailEdt);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        NhanVien nhanVien = intent.getParcelableExtra("nhanVien");


        manvEdt.setText(nhanVien.getMaNV() + "");
        hotenEdt.setText(nhanVien.getHoTen());
        gioitinhEdt.setText(nhanVien.getGioiTinh());
        ngaysinhEdt.setText(nhanVien.getNgSinh());
        chucvuEdt.setText(nhanVien.getCapBac());
        emailEdt.setText(nhanVien.getEmail());
    }
}