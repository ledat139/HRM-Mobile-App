package com.example.tenpm_hrm.attendance;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tenpm_hrm.R;

import java.util.List;

import models.Attendance;

public class AttendanceAdapter  extends ArrayAdapter<CustomAttendance> {
    private Context context;

    public AttendanceAdapter(Context context, int resource, List<CustomAttendance> objects) {
        super(context, resource, objects);
        this.context = context;
    }
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_emp_attendance, null,false);
        }
        CustomAttendance customAttendance = getItem(position);
        TextView tvAttendanceName = (TextView) convertView.findViewById(R.id.tvAttendanceName);
        TextView tvAttendanceId = (TextView) convertView.findViewById(R.id.tvAttendanceId);
        TextView tvAttendancePhone = (TextView) convertView.findViewById(R.id.tvAttendancePhone);
        TextView tvAttendanceDep = (TextView) convertView.findViewById(R.id.tvAttendanceDep);
        TextView tvAttendanceStatus = (TextView) convertView.findViewById(R.id.tvAttendanceStatus);
        tvAttendanceName.setText(customAttendance.getName());
        tvAttendanceId.setText(String.valueOf(customAttendance.getEmployeeId()));
        tvAttendancePhone.setText(customAttendance.getPhone());
        tvAttendanceDep.setText(customAttendance.getDepartment());
        tvAttendanceStatus.setText(customAttendance.getStatus());

        if (customAttendance.getStatus().equals("Đúng giờ")) {
            tvAttendanceStatus.setBackgroundResource(R.drawable.employee_type_shape);
            tvAttendanceStatus.setTextColor(R.color.text_green);
        }
        else if(customAttendance.getStatus().equals("Xin nghỉ") || customAttendance.getStatus().equals("Có phép")){
            tvAttendanceStatus.setBackgroundResource(R.drawable.blue_bg);
            tvAttendanceStatus.setTextColor(R.color.text_blue);
        }
        else if(customAttendance.getStatus().equals("Chưa chấm công") || customAttendance.getStatus().equals("Không phép")){
            tvAttendanceStatus.setBackgroundResource(R.drawable.red_bg);
            tvAttendanceStatus.setTextColor(R.color.text_red);
        }
        else if(customAttendance.getStatus().equals("Trễ giờ")){
            tvAttendanceStatus.setBackgroundResource(R.drawable.yellow_bg);
            tvAttendanceStatus.setTextColor(R.color.text_yellow);
        }

        return convertView;
    }
}
