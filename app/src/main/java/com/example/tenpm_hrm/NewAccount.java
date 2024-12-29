package com.example.tenpm_hrm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class NewAccount extends AppCompatActivity {
    private TextInputEditText inputEmployeeId, inputUsername, inputPassword;
    private Button btnAddAccount;
    private DatabaseHandler dbHelper;
    private RadioGroup radioGroupAccountType;
    private RadioButton radioEmployee, radioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_account);
        inputEmployeeId = findViewById(R.id.inputEmployeeId);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        radioGroupAccountType = findViewById(R.id.radioGroupAccountType);
        radioEmployee = findViewById(R.id.radioEmployee);
        radioManager = findViewById(R.id.radioManager);
        btnAddAccount = findViewById(R.id.button);

        dbHelper = new DatabaseHandler(this);
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int employeeId = Integer.parseInt(inputEmployeeId.getText().toString().trim());
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String accountType = radioEmployee.isChecked() ? "nhân viên" : "quản lý";

                // Kiểm tra dữ liệu đầu vào
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(NewAccount.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lưu thông tin vào database
                boolean isInserted = dbHelper.addAccount(employeeId, username, password, accountType);
                if (isInserted) {
                    Toast.makeText(NewAccount.this, "Thêm tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(NewAccount.this, "Thêm tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }


}