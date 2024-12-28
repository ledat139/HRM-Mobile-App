package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import models.NhanVien;

public class NewEmployee extends AppCompatActivity {
    private EditText etName, etBirthday, etPhone, etEmail, etAddress, etCCCD, etPosition, etDepartment;
    private Button btnAddEmployee;
    private RadioGroup genderGroup;
    private RadioButton radioMale, radioFemale;
    private DatabaseHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_employee);
        //Khởi tạo các trường EditText
        etName = findViewById(R.id.Name);
        genderGroup = findViewById(R.id.genderGroup);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        etBirthday = findViewById(R.id.Birthday);
        etPhone = findViewById(R.id.Phone);
        etEmail = findViewById(R.id.Email);
        etAddress = findViewById(R.id.Address);
        etCCCD = findViewById(R.id.CCCD);
        etPosition = findViewById(R.id.Position);
        etDepartment = findViewById(R.id.idDepartment);

        //Khởi tạo Button
        btnAddEmployee = findViewById(R.id.button);

        //Khởi tạo DatabaseHandler
        dbHandler = new DatabaseHandler(this);

        //Xử lý sự kiện khi nhấn vào Button
        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewEmployee();
            }
        });
    }
    private void addNewEmployee() {
        // Lấy dữ liệu từ các trường EditText
        String name = etName.getText().toString();
        int selectedGenderId = genderGroup.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender.getText().toString();
        String birthday = etBirthday.getText().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        String address = etAddress.getText().toString();
        String cccd = etCCCD.getText().toString();
        String position = etPosition.getText().toString();
        int iddepartment = Integer.parseInt(etDepartment.getText().toString());


        // Tạo đối tượng NhanVien
        int maxMaNV = dbHandler.getMaxMaNV() + 1;
        NhanVien nhanVien = new NhanVien(maxMaNV, name, gender, birthday, phone, email, address, cccd, position, iddepartment);        // Thêm nhân viên vào database
        dbHandler.addNhanVien(nhanVien);

        // Hiển thị thông báo
        Toast.makeText(this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();

        //làm mới các trường
        etName.setText("");
        genderGroup.check(R.id.radioMale);
        etBirthday.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etAddress.setText("");
        etCCCD.setText("");
        etPosition.setText("");
        etDepartment.setText("");

        // Tạo intent và truyền thông tin nhân viên
        Intent resultIntent = new Intent();
        resultIntent.putExtra("maNV", String.valueOf(maxMaNV));
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("gender", gender);
        resultIntent.putExtra("birthday", birthday);
        resultIntent.putExtra("phone", phone);
        resultIntent.putExtra("email", email);
        resultIntent.putExtra("address", address);
        resultIntent.putExtra("cccd", cccd);
        resultIntent.putExtra("position", position);
        resultIntent.putExtra("iddepartment", String.valueOf(iddepartment));
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}