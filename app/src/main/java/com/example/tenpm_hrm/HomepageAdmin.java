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

import com.example.tenpm_hrm.attendance.AttendanceManagement;

import models.NhanVien;

public class HomepageAdmin extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView imgSidebar;
    private Button btnLogout;
    private CardView cardEmployee;
    private CardView cardDepartment;
    private CardView cardAttendance;
    private CardView cardFacilities;
    private CardView cardProject;
    private CardView cardRequest;
    private CardView cardAccount;
    private ProgressBar progressBar; // ProgressBar for better UX

    private Button btnChangePassword;

    private Button btnPersonalInfo;

    private TextView txtFullName;

    private TextView txtPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homepage_admin);

        drawerLayout = findViewById(R.id.drawer_layout);
        imgSidebar = findViewById(R.id.imgSidebar);
        btnLogout = findViewById(R.id.btnLogout);

        // Khởi tạo các CardView
        cardRequest = findViewById(R.id.cardRequest);
        cardEmployee = findViewById(R.id.cardEmployee);
        cardDepartment = findViewById(R.id.cardDepartment);
        cardAttendance = findViewById(R.id.cardAttendance);
        cardFacilities = findViewById(R.id.cardFacilities);
        cardProject = findViewById(R.id.cardProject);
        cardAccount = findViewById(R.id.cardAccount);
        progressBar = findViewById(R.id.progressBar); // Khởi tạo ProgressBar

        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnPersonalInfo = findViewById(R.id.btnPersonalInfo);

        txtFullName = findViewById(R.id.txtFullName);

        txtPosition = findViewById(R.id.txtPosition);

        // Thiết lập sự kiện click cho các CardView
        setupCardClickListener(cardRequest, RequestManagementAdmin.class);
        setupCardClickListener(cardEmployee, EmployeeManagement.class);
        setupCardClickListener(cardDepartment, DepartmentManagement.class);
        setupCardClickListener(cardAttendance, AttendanceManagement.class);
        setupCardClickListener(cardFacilities, CSVCManagement.class);
        setupCardClickListener(cardProject, ProjectManagement.class);
        setupCardClickListener(cardAccount, AccountManagement.class);


        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        NhanVien nhanVien = intent.getParcelableExtra("nhanVien");

        txtFullName.setText(nhanVien.getHoTen());
        txtPosition.setText(nhanVien.getCapBac());

        btnPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRequestIntent = new Intent(HomepageAdmin.this, EmployeeInfo.class);
                newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi đối tượng NhanVien qua Intent
                startActivity(newRequestIntent);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nhanVien != null) {
                    // Tạo Intent để chuyển đến Activity RequestManagementClient
                    Intent newRequestIntent = new Intent(HomepageAdmin.this, ChangePassword.class);
                    newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi đối tượng NhanVien qua Intent
                    startActivity(newRequestIntent); // Khởi động Activity mới
                } else {
                    Log.e("HomePageClient", "NhanVien is null, cannot start RequestManagementClient");
                }
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
                Intent loginIntent = new Intent(HomepageAdmin.this, Login.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Để xóa tất cả các Activity cũ
                startActivity(loginIntent);
                finish(); // Kết thúc Activity hiện tại
            }
        });
    }

    private void setupCardClickListener(CardView card, Class<?> targetActivity) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiện ProgressBar trước khi chuyển đến Activity mới
                progressBar.setVisibility(View.VISIBLE);

                // Bắt đầu Activity mới
                Intent newActivityIntent = new Intent(HomepageAdmin.this, targetActivity);
                startActivity(newActivityIntent);
                // Ẩn ProgressBar sau một khoảng thời gian ngắn
                v.postDelayed(() -> progressBar.setVisibility(View.GONE), 300);
            }
        });
    }
}
