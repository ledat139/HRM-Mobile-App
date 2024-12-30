package com.example.tenpm_hrm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import models.Department;
import models.NhanVien;

public class NewDepartment extends AppCompatActivity {

    private Button btnAddProject;
    private TextInputEditText inputAddDepartmentName, inputAddDepartmentEstablishmentDate, inputAddManagerAppointmentDate;
    private Spinner inputAddManagerID;
    private ImageView ibAddDepartmentAvatar;
    private static final int PICK_IMAGE_REQUEST = 100;
    private DatabaseHandler dbHandler;
    private String avatarPath;

    private Bitmap bitmap;

    private void findViewsByIds() {
        btnAddProject = (Button) findViewById(R.id.btnAddProject);
        inputAddDepartmentName = (TextInputEditText) findViewById(R.id.inputAddDepartmentName);
        inputAddDepartmentEstablishmentDate = (TextInputEditText) findViewById(R.id.inputAddDepartmentEstablishmentDate);
        inputAddManagerAppointmentDate = (TextInputEditText) findViewById(R.id.inputAddManagerAppointmentDate);
        inputAddManagerID = (Spinner) findViewById(R.id.inputAddManagerID);
        ibAddDepartmentAvatar = (ImageView) findViewById(R.id.ibAddDepartmentAvatar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                ibAddDepartmentAvatar.setImageBitmap(bitmap);

                avatarPath = selectedImageUri.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_department_admin);

        findViewsByIds();

        ArrayList<NhanVien> nhanVienList = new ArrayList<>();
        nhanVienList.add(new NhanVien(1, "Nguyễn Văn A", "Nam", "1990-01-01", "0123456789", "vana@example.com", "123 Đường ABC", "012345678", "Nhân viên", 1));
        nhanVienList.add(new NhanVien(2, "Trần Thị B", "Nữ", "1992-02-15", "0987654321", "thib@example.com", "456 Đường DEF", "876543210", "Trưởng phòng", 2));
        nhanVienList.add(new NhanVien(3, "Lê Thị C", "Nữ", "1985-03-20", "0912345678", "thic@example.com", "789 Đường GHI", "123456789", "Quản lý", 3));
        nhanVienList.add(new NhanVien(4, "Phạm Minh D", "Nam", "1995-06-10", "0908765432", "minhd@example.com", "101 Đường JKL", "654321098", "Nhân viên", 4));

        ArrayList<String> tenNhanVienList = new ArrayList<>();
        for (NhanVien nhanVien : nhanVienList) {
            tenNhanVienList.add(nhanVien.getHoTen() + " (ID: " + nhanVien.getMaNV() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenNhanVienList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputAddManagerID.setAdapter(adapter);

        ibAddDepartmentAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btnAddProject.setOnClickListener(view -> {
            String departmentName = Objects.requireNonNull(inputAddDepartmentName.getText()).toString().trim();
            String establishmentDate = Objects.requireNonNull(inputAddDepartmentEstablishmentDate.getText()).toString().trim();
            String managerAppointmentDate = Objects.requireNonNull(inputAddManagerAppointmentDate.getText()).toString().trim();

            int selectedPosition = inputAddManagerID.getSelectedItemPosition();
            NhanVien selectedNhanVien = nhanVienList.get(selectedPosition);

            if (departmentName.isEmpty() || establishmentDate.isEmpty() || managerAppointmentDate.isEmpty() || avatarPath == null) {
                Toast.makeText(NewDepartment.this, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            Department department = new Department(departmentName, establishmentDate, selectedNhanVien.getMaNV(), managerAppointmentDate, avatarPath);

            dbHandler = new DatabaseHandler(this);
            dbHandler.addDepartment(department);

            Toast.makeText(NewDepartment.this, "Thêm phòng ban thành công!", Toast.LENGTH_SHORT).show();

            inputAddDepartmentName.setText("");
            inputAddDepartmentEstablishmentDate.setText("");
            inputAddManagerAppointmentDate.setText("");
            inputAddManagerID.setSelection(0);
            ibAddDepartmentAvatar.setImageResource(R.drawable.img_upload); // Reset ảnh
            avatarPath = null;
        });

    }
}