package com.example.tenpm_hrm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tenpm_hrm.attendance.AttendanceManagement;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.components.Legend;

import java.util.ArrayList;

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
    private PieChart pieChart;
    private DatabaseHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homepage_admin);

        drawerLayout = findViewById(R.id.drawer_layout);
        imgSidebar = findViewById(R.id.imgSidebar);
        btnLogout = findViewById(R.id.btnLogout);
        progressBar = findViewById(R.id.progressBar); // Khởi tạo ProgressBar
        pieChart = findViewById(R.id.pieChart);
        dbHelper = new DatabaseHandler(this);

        // Khởi tạo các CardView
        cardRequest = findViewById(R.id.cardRequest);
        cardEmployee = findViewById(R.id.cardEmployee);
        cardDepartment = findViewById(R.id.cardDepartment);
        cardAttendance = findViewById(R.id.cardAttendance);
        cardFacilities = findViewById(R.id.cardFacilities);
        cardProject = findViewById(R.id.cardProject);
        cardAccount = findViewById(R.id.cardAccount);

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

        if (nhanVien != null) {
            txtFullName.setText(nhanVien.getHoTen());
            txtPosition.setText(nhanVien.getCapBac());

            btnPersonalInfo.setOnClickListener(v -> {
                Intent newRequestIntent = new Intent(HomepageAdmin.this, EmployeeInfo.class);
                newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi đối tượng NhanVien qua Intent
                startActivity(newRequestIntent);
            });

            btnChangePassword.setOnClickListener(v -> {
                Intent newRequestIntent = new Intent(HomepageAdmin.this, ChangePassword.class);
                newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi đối tượng NhanVien qua Intent
                startActivity(newRequestIntent);
            });
        } else {
            Log.e("HomePageAdmin", "NhanVien is null");
        }

        imgSidebar.setOnClickListener(v -> {
            // Mở hoặc đóng sidebar
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        btnLogout.setOnClickListener(v -> {
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
        });

        setupPieChart(); // Set up PieChart
    }

    private void setupCardClickListener(CardView card, Class<?> targetActivity) {
        card.setOnClickListener(v -> {
            // Hiện ProgressBar trước khi chuyển đến Activity mới
            progressBar.setVisibility(View.VISIBLE);
            // Bắt đầu Activity mới
            Intent newActivityIntent = new Intent(HomepageAdmin.this, targetActivity);
            startActivity(newActivityIntent);
            // Ẩn ProgressBar sau một khoảng thời gian ngắn
            v.postDelayed(() -> progressBar.setVisibility(View.GONE), 300);
        });
    }

    private void setupPieChart() {
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Lấy cơ sở dữ liệu ở chế độ chỉ đọc

        // Truy vấn dữ liệu để lấy số lượng nhân viên trong từng phòng ban
        String query = "SELECT PHONGBAN.TENPB, COUNT(NHANVIEN.MAPB) AS num_employees " +
                "FROM NHANVIEN " +
                "JOIN PHONGBAN ON NHANVIEN.MAPB = PHONGBAN.MAPB " +
                "GROUP BY NHANVIEN.MAPB";

        Cursor cursor = db.rawQuery(query, null);

        // Lưu trữ dữ liệu để đưa vào PieChart
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        // Màu sắc tùy chỉnh
        int[] chartColors = {
                Color.parseColor("#3ac5e0"),
                Color.parseColor("#8acdea"),
                Color.parseColor("#bcdaee"),
                Color.parseColor("#ede6f2"),
                Color.parseColor("#fff6fc")
        };

        int colorIndex = 0;
        while (cursor.moveToNext()) {
            String departmentName = cursor.getString(cursor.getColumnIndex("TENPB"));
            int numEmployees = cursor.getInt(cursor.getColumnIndex("num_employees"));

            pieEntries.add(new PieEntry(numEmployees, departmentName));
            colors.add(chartColors[colorIndex % chartColors.length]);  // Sử dụng màu tùy chỉnh từ chartColors
            colorIndex++;
        }
        cursor.close();
        db.close();

        // Kiểm tra nếu không có dữ liệu
        if (pieEntries.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu để hiển thị biểu đồ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo DataSet cho PieChart
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(colors);

        // Tạo PieData từ DataSet
        PieData data = new PieData(dataSet);
        dataSet.setValueFormatter(new PercentFormatter()); // Hiển thị phần trăm

        // Cấu hình PieChart
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);  // Hiển thị tỷ lệ phần trăm
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(40f); // kích thước lỗ
        pieChart.setTransparentCircleRadius(45f); // lỗ trong suốt

        data.setValueTextSize(14f);  // Kích thước chữ cho giá trị
        data.setValueTextColor(Color.BLACK);  // Màu chữ giá trị
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(10f);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);  // Đặt hướng ngang cho các chú thích
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);  // Căn chỉnh theo chiều dọc từ trên xuống
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);  // Căn chỉnh theo chiều ngang bên phải
        legend.setDrawInside(false);  // Đặt vị trí chú thích ra ngoài biểu đồ
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(10f);

        pieChart.setRotationEnabled(false);  // Tắt tính năng xoay
        pieChart.setHighlightPerTapEnabled(false);  // Tắt tính năng bấm vào

        // Cập nhật PieChart
        pieChart.invalidate();
    }
}
