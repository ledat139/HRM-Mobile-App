package com.example.tenpm_hrm.attendance;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenpm_hrm.DatabaseHandler;
import com.example.tenpm_hrm.R;

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
    private AttendanceDbAdapter db;
    private NhanVien nhanVien;
    private int maNV;



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
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nhanVien")) {
            nhanVien = intent.getParcelableExtra("nhanVien"); // Lấy đối tượng NhanVien từ Intent
            if (nhanVien != null) {
                maNV = nhanVien.getMaNV();
            }
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
//                Toast.makeText(AttendanceDetails.this, "Check-in thành công", Toast.LENGTH_SHORT).show();
            }
        });

        setTvMonthYear();
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
        String date;
        if (Integer.parseInt(dayText) < 10)
            date =  "0" + dayText + "/" + monthFromDate(selectedDate) + "/" + yearFromDate(selectedDate);
        else date =  dayText + "/" + monthFromDate(selectedDate) + "/" + yearFromDate(selectedDate);
        if( !dayText.equals("") && (Integer.parseInt(dayText) <= Integer.parseInt(dayFromdate(currentDate))
                || Integer.parseInt(monthFromDate(selectedDate)) < Integer.parseInt(monthFromDate(currentDate)) ))
        {
            Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
            Attendance attendance = db.getAttendance(maNV, date);
            if (attendance.getWorkDate() != null){
                workDay.setText(attendance.getWorkDate());
                checkInTime.setText(attendance.getCheckinTime());
                status.setText(attendance.getStatus());
                checkInBtn.setVisibility(View.GONE);
                if(attendance.getStatus().equals("Đúng giờ"))
                    status.setBackgroundResource(R.drawable.employee_type_shape);
                else if(attendance.getStatus().equals("Đi trễ"))
                    status.setBackgroundResource(R.drawable.late);
            }
        }
        else if(!dayText.equals("") && Integer.parseInt(dayText) == Integer.parseInt(dayFromdate(currentDate)))
        {
            workDay.setText(customDate(currentDate));
            checkInTime.setText("");
            status.setText("");
            status.setBackgroundResource(R.drawable.trans);
            checkInBtn.setVisibility(View.VISIBLE);
        }
    }
}