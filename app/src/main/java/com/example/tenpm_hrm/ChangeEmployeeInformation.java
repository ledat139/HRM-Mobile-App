package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import models.NhanVien;

public class ChangeEmployeeInformation extends AppCompatActivity {

    private EditText etName, etBirthday, etPhone, etEmail, etAddress, etCCCD, etPosition, etDepartment;
    private RadioGroup genderGroup;
    private RadioButton radioMale, radioFemale;
    private Button btnSave;
    private DatabaseHandler dbHandler;
    private int employeeId;
    private NhanVien nhanVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_infomation_employee);

        // Ánh xạ các view
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
        btnSave = findViewById(R.id.button);

        // Khởi tạo DatabaseHandler
        dbHandler = new DatabaseHandler(this);

        // Lấy ID nhân viên từ Intent
        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeId");
        employeeId = Integer.parseInt(employeeID);

        // Load thông tin nhân viên
        if (employeeId != -1) {
            nhanVien = dbHandler.getEmployeeById(employeeId);
            if (nhanVien != null) {
                FillInformation(nhanVien);
            } else {
                showToast("Không tìm thấy thông tin nhân viên.");
            }
        }

        // Xử lý sự kiện lưu
        btnSave.setOnClickListener(v -> {
                saveEmployeeInfo();
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
                Intent resultIntent = new Intent();
                resultIntent.putExtra("maNV", String.valueOf(employeeId));
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
                finish(); // Quay lại
        });
    }

    private void FillInformation(NhanVien nhanVien) {
        etName.setText(nhanVien.getHoTen());
        if ("Nam".equals(nhanVien.getGioiTinh())) {
            radioMale.setChecked(true);
        } else {
            radioFemale.setChecked(true);
        }
        etName.setText(nhanVien.getHoTen());
        etBirthday.setText(nhanVien.getNgSinh());
        etPhone.setText(nhanVien.getSdt());
        etEmail.setText(nhanVien.getEmail());
        etAddress.setText(nhanVien.getDiaChi());
        etCCCD.setText(nhanVien.getCccd());
        etPosition.setText(nhanVien.getCapBac());
        etDepartment.setText(String.valueOf(nhanVien.getMaPB()));
    }

    public void saveEmployeeInfo() {
        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(etName.getText())) {
            showToast("Vui lòng nhập tên nhân viên");
            return;
        }
        if (TextUtils.isEmpty(etPhone.getText())) {
            showToast("Vui lòng nhập số điện thoại");
            return;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            showToast("Vui lòng nhập email");
            return;
        }

        // Cập nhật đối tượng NhanVien với dữ liệu từ các trường nhập liệu
        nhanVien.setHoTen(etName.getText().toString().trim());
        int selectedGenderId = genderGroup.getCheckedRadioButtonId();
        String gender = (selectedGenderId == R.id.radioMale) ? "Nam" : "Nữ";
        nhanVien.setGioiTinh(gender);
        nhanVien.setNgSinh(etBirthday.getText().toString().trim());
        nhanVien.setSdt(etPhone.getText().toString().trim());
        nhanVien.setEmail(etEmail.getText().toString().trim());
        nhanVien.setDiaChi(etAddress.getText().toString().trim());
        nhanVien.setCccd(etCCCD.getText().toString().trim());
        nhanVien.setCapBac(etPosition.getText().toString().trim());
        int iddepartment = Integer.parseInt(etDepartment.getText().toString());

        // Gọi phương thức updateEmployee để lưu thay đổi
        boolean isUpdated = dbHandler.updateEmployee(nhanVien);
        if (isUpdated) {
            showToast("Cập nhật thông tin thành công!");
        } else {
            showToast("Cập nhật thất bại. Vui lòng thử lại.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
