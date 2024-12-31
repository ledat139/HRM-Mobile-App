package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SearchEmployee extends AppCompatActivity {
    private Button cancel, search;
    private EditText id, name, gioitinh, birthday, phone, email, address,cccd, position, department;
    private DatabaseHandler dbHandler;
    private RadioGroup genderGroup;
    private RadioButton radioMale, radioFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_employee);
        findViews();

        dbHandler = new DatabaseHandler(this);

        cancel.setOnClickListener(v -> finish());

        search.setOnClickListener(v -> {
            // Lấy dữ liệu từ các trường nhập liệu
            String maNV = id.getText().toString().trim();
            String hoTen = name.getText().toString().trim();
            String gender;
            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            if (selectedGenderId != -1) {
                RadioButton selectedGenderRadioButton = findViewById(selectedGenderId);
                gender = selectedGenderRadioButton.getText().toString();
            } else {
                gender = null;
            }
            String ngSinh = birthday.getText().toString().trim();
            String sdt = phone.getText().toString().trim();
            String emailText = email.getText().toString().trim();
            String diaChi = address.getText().toString().trim();
            String cancuoc =  cccd.getText().toString().trim();
            String capBac = position.getText().toString().trim();
            String phongBan = department.getText().toString().trim();

            // Tạo intent và truyền thông tin tìm kiếm
            Intent resultIntent = new Intent();
            resultIntent.putExtra("maNV", id.getText().toString());
            resultIntent.putExtra("name", name.getText().toString());
            resultIntent.putExtra("gender", gender);
            resultIntent.putExtra("birthday", birthday.getText().toString());
            resultIntent.putExtra("phone", phone.getText().toString());
            resultIntent.putExtra("email", email.getText().toString());
            resultIntent.putExtra("address", address.getText().toString());
            resultIntent.putExtra("cccd", cccd.getText().toString());
            resultIntent.putExtra("position", position.getText().toString());
            resultIntent.putExtra("iddepartment", department.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }


    private void findViews() {
        cancel = findViewById(R.id.buttonCancel);
        search = findViewById(R.id.buttonSearch);
        id = findViewById(R.id.etId);
        name = findViewById(R.id.etName);
        genderGroup = findViewById(R.id.genderGroup);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        birthday = findViewById(R.id.etBirthday);
        phone = findViewById(R.id.etPhone);
        email = findViewById(R.id.etEmail);
        address = findViewById(R.id.etAddress);
        cccd = findViewById(R.id.etCCCD);
        position = findViewById(R.id.etPosition);
        department = findViewById(R.id.etDepartment);
    }
}
