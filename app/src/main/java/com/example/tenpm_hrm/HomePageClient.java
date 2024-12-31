package com.example.tenpm_hrm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tenpm_hrm.attendance.AttendanceDetails;

import models.NhanVien;

public class HomePageClient extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView imgSidebar;
    private Button btnLogout;
    private CardView cardInfo;
    private CardView cardAttendance;
    private CardView cardProject;
    private CardView cardRequest;

    private Button btnChangePassword;

    private Button btnPersonalInfo;

    private TextView txtFullName;

    private TextView txtPosition;
    private ProgressBar progressBar; // Add a ProgressBar for better UX

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homepage_client);

        drawerLayout = findViewById(R.id.drawer_layout);
        imgSidebar = findViewById(R.id.imgSidebar);
        btnLogout = findViewById(R.id.btnLogout);

        cardRequest = findViewById(R.id.cardRequest);
        cardInfo = findViewById(R.id.cardInfo);
        cardAttendance = findViewById(R.id.cardAttendance);
        cardProject = findViewById(R.id.cardProject);
//        progressBar = findViewById(R.id.progressBar);

        btnPersonalInfo = findViewById(R.id.btnPersonalInfo);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        txtFullName = findViewById(R.id.txtFullName);

        txtPosition = findViewById(R.id.txtPosition);

        Intent intent = getIntent();
        NhanVien nhanVien = intent.getParcelableExtra("nhanVien");

        txtFullName.setText(nhanVien.getHoTen());
        txtPosition.setText(nhanVien.getCapBac());

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nhanVien != null) {
                    // Tạo Intent để chuyển đến Activity RequestManagementClient
                    Intent newRequestIntent = new Intent(HomePageClient.this, ChangePassword.class);
                    newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi đối tượng NhanVien qua Intent
                    startActivity(newRequestIntent); // Khởi động Activity mới
                } else {
                    Log.e("HomePageClient", "NhanVien is null, cannot start RequestManagementClient");
                }
            }

        });

        btnPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRequestIntent = new Intent(HomePageClient.this, EmployeeInfo.class);
                newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi đối tượng NhanVien qua Intent
                startActivity(newRequestIntent);
            }
        });

        imgSidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở hoặc đóng sidebar
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa dữ liệu người dùng từ SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Xóa tất cả dữ liệu
                editor.apply();

                // Chuyển hướng về màn hình đăng nhập
                Intent loginIntent = new Intent(HomePageClient.this, Login.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Để xóa tất cả các Activity cũ
                startActivity(loginIntent);
                finish(); // Kết thúc Activity hiện tại
            }
        });


        cardRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem nhanVien có khác null không
                if (nhanVien != null) {
                    // Tạo Intent để chuyển đến Activity RequestManagementClient
                    Intent newRequestIntent = new Intent(HomePageClient.this, RequestManagementClient.class);
                    newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi đối tượng NhanVien qua Intent
                    startActivity(newRequestIntent); // Khởi động Activity mới
                } else {
                    Log.e("HomePageClient", "NhanVien is null, cannot start RequestManagementClient");
                }

                // Nếu bạn cần hiển thị ProgressBar, hãy làm như sau
                // progressBar.setVisibility(View.VISIBLE);
                // v.postDelayed(() -> progressBar.setVisibility(View.GONE), 300);
            }
        });
        cardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show ProgressBar before starting the new activity
//                progressBar.setVisibility(View.VISIBLE);

                // Start the new activity
                Intent newRequestIntent = new Intent(HomePageClient.this, EmployeeInfo.class);
                newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi đối tượng NhanVien qua Intent
                startActivity(newRequestIntent);

                // Optionally hide the ProgressBar after a short delay
                // This is just to simulate loading; adjust as needed
//                v.postDelayed(() -> progressBar.setVisibility(View.GONE), 300);
            }
        });

        cardAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show ProgressBar before starting the new activity
//                progressBar.setVisibility(View.VISIBLE);

                if (nhanVien != null) {
                    // Tạo Intent để chuyển đến Activity RequestManagementClient
                    Intent newRequestIntent = new Intent(HomePageClient.this, AttendanceDetails.class);
                    newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi đối tượng NhanVien qua Intent
                    startActivity(newRequestIntent); // Khởi động Activity mới
                } else {
                    Log.e("HomePageClient", "NhanVien is null, cannot start RequestManagementClient");
                }


                // Optionally hide the ProgressBar after a short delay
                // This is just to simulate loading; adjust as needed
//                v.postDelayed(() -> progressBar.setVisibility(View.GONE), 300);
            }
        });

        cardProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show ProgressBar before starting the new activity
//                progressBar.setVisibility(View.VISIBLE);

                // Start the new activity
                Intent newRequestIntent = new Intent(HomePageClient.this, ProjectEmployee.class);
                newRequestIntent.putExtra("employeeID", nhanVien.getMaNV());
                startActivity(newRequestIntent);

                // Optionally hide the ProgressBar after a short delay
                // This is just to simulate loading; adjust as needed
//                v.postDelayed(() -> progressBar.setVisibility(View.GONE), 300);
            }
        });
    }
}
