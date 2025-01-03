package com.example.tenpm_hrm.attendance;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tenpm_hrm.R;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import customlistview.CalendarRecycleViewAdapterAdmin;
import models.Attendance;
import models.NhanVien;

public class AttendanceManagement extends AppCompatActivity implements CalendarRecycleViewAdapterAdmin.OnItemListener {

    private TextView tvMonthYear;
    private RecyclerView rvCalendar;
    private LocalDate selectedDate;
    private ListView lvAttendance;
    private AttendanceDbAdapter db;
    private String status = "Đúng giờ";
    private String dateTemp;
    private LocalDate currentDate;
    private TextView tvDateValue;
    private Button dungGioBtn;
    private Button treGioBtn;
    private Button coPhepBtn;
    private Button khongPhepBtn;
    private Button danhDauBtn;
    private boolean checkdate = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.attendance_management_view);

        rvCalendar = findViewById(R.id.rvCalendar);
        tvMonthYear = findViewById(R.id.tvMonthYear);
        lvAttendance = findViewById(R.id.lvAttendace);
        tvDateValue = findViewById(R.id.dateValue);
        dungGioBtn = findViewById(R.id.dungGioBtn);
        treGioBtn = findViewById(R.id.treGioBtn);
        coPhepBtn = findViewById(R.id.coPhepBtn);
        khongPhepBtn = findViewById(R.id.khongPhepBtn);
        danhDauBtn = findViewById(R.id.danhDauBtn);

        danhDauBtn.setVisibility(View.GONE);
        db = new AttendanceDbAdapter(this);
        selectedDate = LocalDate.now();
        currentDate = selectedDate;
        setTvMonthYear();
        dateTemp = customDate(selectedDate);

        for(int i = 20; i <= 31; i++){
            if (i % 2 == 0){
                db.insertCheckIn(new Attendance(2, 1, i + "/12/2024", "08:01", null, "Đúng giờ"));
                db.insertCheckIn(new Attendance(2, 2, i + "/12/2024", "08:16", null, "Trễ giờ"));
                db.insertCheckIn(new Attendance(2, 2, i + "/12/2024", "08:01", null, "Đúng giờ"));
                db.insertCheckIn(new Attendance(2, 3, i + "/12/2024", "08:01", null, "Đúng giờ"));
                db.insertCheckIn(new Attendance(2, 4, i + "/12/2024", "08:17", null, "Trễ giờ"));
                db.insertCheckIn(new Attendance(2, 5, i + "/12/2024", "08:01", null, "Đúng giờ"));
                db.insertCheckIn(new Attendance(2, 6, i + "/12/2024", "", null, "Xin nghỉ"));
            }
            else {
                db.insertCheckIn(new Attendance(2, 1, i + "/12/2024", "08:16", null, "Trễ giờ"));
                db.insertCheckIn(new Attendance(2, 8, i + "/12/2024", "08:20", null, "Trễ giờ"));
                db.insertCheckIn(new Attendance(2, 9, i + "/12/2024", "08:01", null, "Đúng giờ"));
                db.insertCheckIn(new Attendance(2, 12, i + "/12/2024", "", null, "Xin nghỉ"));
                db.insertCheckIn(new Attendance(2, 14, i + "/12/2024", "08:17", null, "Trễ giờ"));
                db.insertCheckIn(new Attendance(2, 13, i + "/12/2024", "08:01", null, "Đúng giờ"));
            }
        }



        db.insertCheckIn(new Attendance(2, 1, "01/01/2025", "08:16", null, "Trễ giờ"));
        db.insertCheckIn(new Attendance(2, 2, "01/01/2025", "08:01", null, "Đúng giờ"));
        db.insertCheckIn(new Attendance(2, 3, "01/01/2025", "08:01", null, "Đúng giờ"));
        db.insertCheckIn(new Attendance(2, 4, "02/01/2025", "08:17", null, "Trễ giờ"));
        db.insertCheckIn(new Attendance(2, 5, "02/01/2025", "08:01", null, "Đúng giờ"));
        db.insertCheckIn(new Attendance(2, 6, "02/01/2025", "", null, "Xin nghỉ"));
//        db.insertCheckIn(new Attendance(2, 1, "02/01/2025", "08:01", null, "Đúng giờ"));
        db.insertCheckIn(new Attendance(2, 8, "02/01/2025", "08:01", null, "Đúng giờ"));
        db.insertCheckIn(new Attendance(2, 9, "02/01/2025", "08:20", null, "Trễ giờ"));
        db.insertCheckIn(new Attendance(2, 7, "02/01/2025", "08:01", null, "Đúng giờ"));




        tvDateValue.setText(getDateValue(selectedDate));

        List<CustomAttendance> attendanceList = db.getAttendanceList(status, dateTemp);
        AttendanceAdapter adapter = new AttendanceAdapter(this, android.R.layout.simple_list_item_1, attendanceList);
        lvAttendance.setAdapter(adapter);


        dungGioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                danhDauBtn.setVisibility(View.GONE);
                status = "Đúng giờ";
                List<CustomAttendance> attendanceList = db.getAttendanceList(status, dateTemp);
                AttendanceAdapter adapter = new AttendanceAdapter(AttendanceManagement.this, android.R.layout.simple_list_item_1, attendanceList);
                lvAttendance.setAdapter(adapter);
            }
        });
        treGioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                danhDauBtn.setVisibility(View.GONE);
                status = "Trễ giờ";
                List<CustomAttendance> attendanceList = db.getAttendanceList(status, dateTemp);
                AttendanceAdapter adapter = new AttendanceAdapter(AttendanceManagement.this, android.R.layout.simple_list_item_1, attendanceList);
                lvAttendance.setAdapter(adapter);
            }
        });
        coPhepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                danhDauBtn.setVisibility(View.GONE);
                status = "Xin nghỉ";
                List<CustomAttendance> attendanceList = db.getAttendanceList(status, dateTemp);
                AttendanceAdapter adapter = new AttendanceAdapter(AttendanceManagement.this, android.R.layout.simple_list_item_1, attendanceList);
                lvAttendance.setAdapter(adapter);
            }
        });
        khongPhepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkdate == true)
                danhDauBtn.setVisibility(View.GONE);
                else danhDauBtn.setVisibility(View.VISIBLE);
                List<CustomAttendance> attendanceList = new ArrayList<>();
                List<CustomAttendance> absentList = db.getAbsenseList(dateTemp);
                List<CustomAttendance> absentSavedList = db.getAttendanceList("Không phép", dateTemp);
                for (CustomAttendance temp : absentList){
                    attendanceList.add(temp);
                }
                for (CustomAttendance temp : absentSavedList){
                    attendanceList.add(temp);
                }
                AttendanceAdapter adapter = new AttendanceAdapter(AttendanceManagement.this, android.R.layout.simple_list_item_1, attendanceList);
                lvAttendance.setAdapter(adapter);
            }
        });
        danhDauBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<CustomAttendance> attendanceList = db.getAbsenseList(dateTemp);
                for (CustomAttendance temp : attendanceList){
                    Attendance attendance = new Attendance();
                    attendance.setEmployeeId(temp.getEmployeeId());
                    attendance.setWorkDate(customDate(currentDate));
                    attendance.setStatus(temp.getStatus());
                    db.insertCheckIn(attendance);
                }
                danhDauBtn.setVisibility(View.GONE);
            }
        });

    }

    private String getDateValue(LocalDate selectedDate) {
        String dayOfWeek = getDayOfWeek(selectedDate);
        return dayOfWeek + ", ngày " + dayFromdate(selectedDate) + " tháng " + monthFromDate(selectedDate) + " năm " + yearFromDate(selectedDate);
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
    private String dayFromdate(LocalDate selectedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
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

        CalendarRecycleViewAdapterAdmin calendarRecycleViewAdapterAdmin = new CalendarRecycleViewAdapterAdmin(daysInMonth, this, selectedDate.getMonthValue(), selectedDate.getYear());
        rvCalendar.setLayoutManager(new GridLayoutManager(getApplicationContext(), 7));
        rvCalendar.setAdapter(calendarRecycleViewAdapterAdmin);
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setTvMonthYear();
    }

    public void prevMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setTvMonthYear();
    }

    public String getDayOfWeek(LocalDate date){
        String dayOfWeek = "";
        switch(date.getDayOfWeek().toString()){
            case "SUNDAY":
                dayOfWeek = "Chủ nhật";
                break;
            case "MONDAY":
                dayOfWeek = "Thứ hai";
                break;
            case "TUESDAY":
                dayOfWeek = "Thứ ba";
                break;
            case "WEDNESDAY":
                dayOfWeek = "Thứ tư";
                break;
            case "THURSDAY":
                dayOfWeek = "Thứ năm";
                break;
            case "FRIDAY":
                dayOfWeek = "Thứ sáu";
                break;
            case "SATURDAY":
                dayOfWeek = "Thứ bảy";
                break;
        }
        return dayOfWeek;
    }

    @Override
    public void onClick(int position, String dayText) {
        LocalDate temp = LocalDate.of(Integer.parseInt(yearFromDate(selectedDate)), Integer.parseInt(monthFromDate(selectedDate)), Integer.parseInt(dayText));
        dateTemp = customDate(temp);
        if(!dayText.equals("") && Integer.parseInt(dayText) < Integer.parseInt(dayFromdate(currentDate))
                || Integer.parseInt(monthFromDate(selectedDate)) < Integer.parseInt(monthFromDate(currentDate))
        || Integer.parseInt(yearFromDate(selectedDate)) < Integer.parseInt(yearFromDate(currentDate)))
        {
            checkdate = true;

            Toast.makeText(this, getDateValue(temp), Toast.LENGTH_SHORT).show();
            tvDateValue.setText(getDateValue(temp));
            status = "Đúng giờ";
            List<CustomAttendance> attendanceList = db.getAttendanceList(status, dateTemp);
            AttendanceAdapter adapter = new AttendanceAdapter(AttendanceManagement.this, android.R.layout.simple_list_item_1, attendanceList);
            lvAttendance.setAdapter(adapter);
            danhDauBtn.setVisibility(View.GONE);
        }
        else if(!dayText.equals("") && Integer.parseInt(dayText) == Integer.parseInt(dayFromdate(currentDate))) {
            checkdate = false;

            Toast.makeText(this, getDateValue(temp), Toast.LENGTH_SHORT).show();
            tvDateValue.setText(getDateValue(temp));
            status = "Đúng giờ";
            List<CustomAttendance> attendanceList = db.getAttendanceList(status, dateTemp);
            AttendanceAdapter adapter = new AttendanceAdapter(AttendanceManagement.this, android.R.layout.simple_list_item_1, attendanceList);
            lvAttendance.setAdapter(adapter);
            danhDauBtn.setVisibility(View.GONE);
        }
    }

}