package com.example.tenpm_hrm.attendance;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenpm_hrm.DatabaseHandler;
import com.example.tenpm_hrm.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import customlistview.CalendarRecycleViewAdapterClient;
import models.Attendance;
import models.NhanVien;

public class AttendanceDetails extends AppCompatActivity implements CalendarRecycleViewAdapterClient.OnItemListener {

    private TextView tvMonthYear;
    private RecyclerView rvCalendar;
    private LocalDate selectedDate;
    private LocalDate currentDate;
    private LocalTime checkinTime;
    private TextView workDay;
    private TextView checkInTime;
    private TextView status;
    private Button checkInBtn;
    private Button absentBtn;
    private AttendanceDbAdapter db;
    private NhanVien nhanVien;
    private int maNV;
    private boolean check;


    private FusedLocationProviderClient fusedLocationClient;

    //10.980899, 106.756021 - nhà
    //10.869951, 106.803116 - UIT
    // Tọa độ công ty (VD: vị trí công ty ở Hồ Chí Minh)
    private static final double COMPANY_LATITUDE = 10.980899; // Vĩ độ
    private static final double COMPANY_LONGITUDE = 106.756021; // Kinh độ
    private static final float COMPANY_RADIUS = 100; // Bán kính cho phép chấm công (100 mét)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.attendance_employee_detail);

        rvCalendar = findViewById(R.id.rvCalendar);
        tvMonthYear = findViewById(R.id.tvMonthYear);
        checkInBtn = findViewById(R.id.checkInBtn);
        workDay = findViewById(R.id.workDaysValue);
        checkInTime = findViewById(R.id.checkInTimeValue);
        status = findViewById(R.id.statusValue);
        absentBtn = findViewById(R.id.absentBtn);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nhanVien")) {
            nhanVien = intent.getParcelableExtra("nhanVien"); // Lấy đối tượng NhanVien từ Intent
            if (nhanVien != null) {
                maNV = nhanVien.getMaNV();
            }
        }

        // Khởi tạo FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Kiểm tra và yêu cầu quyền truy cập vị trí
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        db = new AttendanceDbAdapter(this);
        db.insertCheckIn(new Attendance(2, 2, "19/12/2024", "08:01", null, "Đúng giờ"));
        db.insertCheckIn(new Attendance(3, 2, "18/11/2024", "08:16", null, "Đi trễ"));
        selectedDate = LocalDate.now();
        currentDate = selectedDate;
        workDay.setText(customDate(currentDate));
        checkinTime = LocalTime.now();
        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationAndCheckAttendance();

            }
        });
        absentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attendance attendance = new Attendance();
                attendance.setEmployeeId(maNV);
                attendance.setWorkDate(customDate(currentDate));
                attendance.setStatus("Xin nghỉ");
                status.setBackgroundResource(R.drawable.blue_bg);
                status.setText(attendance.getStatus());
                db.insertCheckIn(attendance);
                checkInBtn.setVisibility(View.GONE);
                check = true;
                checkInBtn.setVisibility(View.GONE);
                absentBtn.setVisibility(View.GONE);
                Toast.makeText(AttendanceDetails.this, "Xin nghỉ thành công!", Toast.LENGTH_SHORT).show();
            }
        });
        setTvMonthYear();
        Attendance attendance = db.getAttendance(maNV, customDate(currentDate));
        if (attendance.getWorkDate() != null) {
            workDay.setText(attendance.getWorkDate());
            checkInTime.setText(attendance.getCheckinTime());
            status.setText(attendance.getStatus());
            checkInBtn.setVisibility(View.GONE);
            absentBtn.setVisibility(View.GONE);
            if (attendance.getStatus().equals("Không phép"))
                status.setBackgroundResource(R.drawable.red_bg);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndCheckAttendance();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền vị trí để sử dụng chức năng này!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Lấy vị trí hiện tại và kiểm tra khoảng cách với công ty
    private void getLocationAndCheckAttendance() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude(); // Vĩ độ
                            double longitude = location.getLongitude(); // Kinh độ

                            // Hiển thị tọa độ
                            Toast.makeText(AttendanceDetails.this,
                                    "Vị trí hiện tại: \nLatitude: " + latitude + "\nLongitude: " + longitude,
                                    Toast.LENGTH_SHORT).show();
                            checkAttendance(latitude, longitude);
                        } else {
                            Toast.makeText(AttendanceDetails.this, "Không thể lấy vị trí, thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkAttendance(double latitude, double longitude) {
        float[] results = new float[1];
        Location.distanceBetween(latitude, longitude, COMPANY_LATITUDE, COMPANY_LONGITUDE, results);

        if (results[0] <= COMPANY_RADIUS) {
            checkInTime.setText(hoursFromTime(checkinTime));
            Attendance attendance = new Attendance();
            attendance.setEmployeeId(maNV);
            attendance.setWorkDate(customDate(currentDate));
            attendance.setCheckinTime(hoursFromTime(checkinTime));

            int hour = Integer.parseInt(hoursFromTime(checkinTime).substring(0,2));
            int minute = Integer.parseInt(hoursFromTime(checkinTime).substring(3,5));
            if(hour == 8 && minute <= 15){
                attendance.setStatus("Đúng giờ");
                status.setBackgroundResource(R.drawable.employee_type_shape);
                Toast.makeText(AttendanceDetails.this, "1h", Toast.LENGTH_SHORT).show();
            }
            else if((hour == 8 && minute > 15) || (hour > 8)){
                attendance.setStatus("Trễ giờ");
                status.setBackgroundResource(R.drawable.late);
            }
            status.setText(attendance.getStatus());
            db.insertCheckIn(attendance);
            checkInBtn.setVisibility(View.GONE);
            check = true;
            checkInBtn.setVisibility(View.GONE);
            absentBtn.setVisibility(View.GONE);
            Toast.makeText(this, "Bạn đang ở gần công ty. Chấm công thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Bạn không ở gần công ty. Chấm công thất bại!", Toast.LENGTH_SHORT).show();
        }
    }


    private String hoursFromTime(LocalTime checkinTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return checkinTime.format(formatter);
    }
    private String dayFromdate(LocalDate selectedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        return selectedDate.format(formatter);
    }

    private String customDate(LocalDate selectedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return selectedDate.format(formatter);
    }
    private String monthFromDate(LocalDate selectedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return selectedDate.format(formatter);
    }
    private String yearFromDate(LocalDate selectedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return selectedDate.format(formatter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = (firstOfMonth.getDayOfWeek().getValue() % 7);

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > (daysInMonth + dayOfWeek)) {
                daysInMonthArray.add("");
            }else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    public void setTvMonthYear() {
        String txt = "Tháng " + monthFromDate(selectedDate) + " - " + yearFromDate(selectedDate);
        tvMonthYear.setText(txt);
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarRecycleViewAdapterClient calendarRecycleViewAdapterClient = new CalendarRecycleViewAdapterClient(daysInMonth, this, selectedDate.getMonthValue(), selectedDate.getYear());
        rvCalendar.setLayoutManager(new GridLayoutManager(getApplicationContext(), 7));
        rvCalendar.setAdapter(calendarRecycleViewAdapterClient);
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setTvMonthYear();
    }

    public void prevMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setTvMonthYear();
    }

    @Override
    public void onClick(int position, String dayText) {
        LocalDate temp = LocalDate.of(Integer.parseInt(yearFromDate(selectedDate)), Integer.parseInt(monthFromDate(selectedDate)), Integer.parseInt(dayText));
        if( !dayText.equals("") && (Integer.parseInt(dayText) < Integer.parseInt(dayFromdate(currentDate))
                || Integer.parseInt(monthFromDate(selectedDate)) < Integer.parseInt(monthFromDate(currentDate)) ))
        {

            Toast.makeText(this, customDate(temp), Toast.LENGTH_SHORT).show();
            Attendance attendance = db.getAttendance(maNV, customDate(temp));
            if (attendance.getWorkDate() != null){
                workDay.setText(attendance.getWorkDate());
                checkInTime.setText(attendance.getCheckinTime());
                status.setText(attendance.getStatus());
                checkInBtn.setVisibility(View.GONE);
                absentBtn.setVisibility(View.GONE);
                if(attendance.getStatus().equals("Đúng giờ"))
                    status.setBackgroundResource(R.drawable.employee_type_shape);
                else if(attendance.getStatus().equals("Đi trễ"))
                    status.setBackgroundResource(R.drawable.late);
                else if(attendance.getStatus().equals("Xin nghỉ"))
                    status.setBackgroundResource(R.drawable.blue_bg);
                else if(attendance.getStatus().equals("Không phép"))
                    status.setBackgroundResource(R.drawable.red_bg);
            }
        }
        else if(!dayText.equals("") && Integer.parseInt(dayText) == Integer.parseInt(dayFromdate(currentDate))) {
            workDay.setText(customDate(currentDate));
            checkInTime.setText("");
            status.setText("");
        }
    }
}