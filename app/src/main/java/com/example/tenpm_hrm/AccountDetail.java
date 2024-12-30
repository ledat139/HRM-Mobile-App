package com.example.tenpm_hrm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import models.Account;

public class AccountDetail extends AppCompatActivity {
    private TextInputEditText inputEmployeeId, inputUsername, inputPassword;
    private Button btnUpdateAccount;
    private DatabaseHandler dbHelper;
    private RadioGroup radioGroupAccountType;
    private RadioButton radioEmployee, radioManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.account_detail);

        int accountId = getIntent().getIntExtra("accountId", -1);
        Log.d("DEBUG", "Received Account ID: " + accountId);
        inputEmployeeId = findViewById(R.id.inputEmployeeId);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        radioGroupAccountType = findViewById(R.id.radioGroupAccountType);
        radioEmployee = findViewById(R.id.radioEmployee);
        radioManager = findViewById(R.id.radioManager);
        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        dbHelper = new DatabaseHandler(this);

        Account account= dbHelper.getAccountById(accountId);
        if (account == null) {
            Toast.makeText(AccountDetail.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
            finish();  // Hoặc có thể quay lại màn hình trước đó nếu tài khoản không tìm thấy
        }
        inputEmployeeId.setText( String.valueOf(account.getMaNV()));
        inputUsername.setText(account.getTenTK());
        inputPassword.setText(account.getMatKhau());
        if (account.getLoaiTK().equals("nhân viên")) {
            radioEmployee.setChecked(true);
        } else if (account.getLoaiTK().equals("quản lý")) {
            radioManager.setChecked(true);
        }
        btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int employeeId = Integer.parseInt(inputEmployeeId.getText().toString().trim());
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String accountType = radioEmployee.isChecked() ? "nhân viên" : "quản lý";
                // Lưu thông tin vào database
                account.setMaNV(employeeId);
                account.setTenTK(username);
                account.setMatKhau(password);
                account.setLoaiTK(accountType);
                boolean isUpdated = dbHelper.updateAccount(account);
                if (isUpdated) {
                    Toast.makeText(AccountDetail.this, "Cập nhật tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AccountDetail.this, "Cập nhật tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });


    }
}
