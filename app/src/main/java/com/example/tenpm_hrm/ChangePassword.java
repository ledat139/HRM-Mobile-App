package com.example.tenpm_hrm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import models.NhanVien;

public class ChangePassword extends AppCompatActivity {

    private Button button2;
    private EditText edtOldPw;
    private EditText edtNewPw;
    private EditText edtReNewPw;

    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        // Khởi tạo các view
        button2 = findViewById(R.id.button2);
        edtOldPw = findViewById(R.id.edtOldPw);
        edtNewPw = findViewById(R.id.edtNewPw);
        edtReNewPw = findViewById(R.id.edtReNewPw);

        // Khởi tạo DatabaseHandler
        databaseHandler = new DatabaseHandler(this);

        Intent intent = getIntent();
        NhanVien nhanVien = intent.getParcelableExtra("nhanVien");

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = edtOldPw.getText().toString().trim();
                String newPassword = edtNewPw.getText().toString().trim();
                String reNewPassword = edtReNewPw.getText().toString().trim();

                if (oldPassword.isEmpty() || newPassword.isEmpty() || reNewPassword.isEmpty()) {
                    Toast.makeText(ChangePassword.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(reNewPassword)) {
                    Toast.makeText(ChangePassword.this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase db = databaseHandler.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM TAIKHOAN WHERE MANV = ?", new String[]{String.valueOf(nhanVien.getMaNV())});
                if (cursor.moveToFirst()) {
                    String currentPassword = cursor.getString(3); // Mật khẩu hiện tại

                    if (!oldPassword.equals(currentPassword)) {
                        Toast.makeText(ChangePassword.this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Cập nhật mật khẩu mới
                        db.execSQL("UPDATE TAIKHOAN SET MATKHAU = ? WHERE MANV = ?", new Object[]{newPassword, nhanVien.getMaNV()});
                        Toast.makeText(ChangePassword.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                        // Xóa dữ liệu người dùng từ SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear(); // Xóa tất cả dữ liệu
                        editor.apply();

                        // Chuyển hướng về màn hình đăng nhập
                        Intent loginIntent = new Intent(ChangePassword.this, Login.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the task stack
                        startActivity(loginIntent);
                        finish(); // Kết thúc Activity hiện tại
                    }
                } else {
                    Toast.makeText(ChangePassword.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                db.close();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
