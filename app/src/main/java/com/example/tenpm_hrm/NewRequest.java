package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class NewRequest extends AppCompatActivity {

    private EditText edtChuDe;
    private EditText edtNoiDung;
    private Button submitBtn;
    private DatabaseHandler dbHandler;

    private NhanVien nhanVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_request_client);

        dbHandler = new DatabaseHandler(this);
        edtChuDe = findViewById(R.id.edtChuDe);
        edtNoiDung = findViewById(R.id.edtNoiDung);
        submitBtn = findViewById(R.id.button);

        // Lấy Intent và extra
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nhanVien")) {
            nhanVien = intent.getParcelableExtra("nhanVien"); // Lấy đối tượng NhanVien từ Intent
            if (nhanVien != null) {
                // Sử dụng nhanVien ở đây nếu cần
                // Ví dụ: Log.d("RequestManagementClient", "Received NhanVien: " + nhanVien.toString());
            } else {
                Log.e("RequestManagementClient", "NhanVien is null");
            }
        } else {
            Log.e("RequestManagementClient", "Intent is null or does not have extra 'nhanVien'");
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHandler.addRequest(nhanVien.getMaNV(), edtChuDe.getText().toString(), edtNoiDung.getText().toString())) {
                    Intent newRequestIntent = new Intent(NewRequest.this, RequestManagementClient.class);
                    newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi đối tượng NhanVien qua Intent
                    startActivity(newRequestIntent);
                    Toast.makeText(NewRequest.this, "Gửi yêu cầu mới thành công!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(NewRequest.this, "Gửi yêu cầu mới thất bại :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}
