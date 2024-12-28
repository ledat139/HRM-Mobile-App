package com.example.tenpm_hrm.attendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tenpm_hrm.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import models.Attendance;
import models.NhanVien;

public class AttendanceDbAdapter {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TenPM_HRM_DB";
    // Contacts table name
    private static final String TABLE_ATTENDANCE = "CHAMCONG";
    private static final String TABLE_EMPLOYEE= "NHANVIEN";
    private final Context context;
    private DatabaseHandler dbHelper;
    private SQLiteDatabase db;

    public AttendanceDbAdapter(Context context) {
        this.context = context;
    }
    public long insertCheckIn(Attendance attendance) {
        dbHelper = new DatabaseHandler(context);
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NGAYLAMVIEC", attendance.getWorkDate());
        values.put("MANV", attendance.getEmployeeId());
        values.put("GIOBATDAU", attendance.getCheckinTime());
        values.put("TRANGTHAI", attendance.getStatus());
        long id = db.insert(TABLE_ATTENDANCE, null, values);
        db.close();
        return id;
    }

    public Attendance getAttendance(int employeeId, String workDate){
        dbHelper = new DatabaseHandler(context);
        db = dbHelper.getWritableDatabase();
        String queryString = "SELECT * FROM " + TABLE_ATTENDANCE + " WHERE MANV = ? AND NGAYLAMVIEC = ?";
        String[] whereArgs  = { String.valueOf(employeeId),  workDate};
        Cursor cursor = db.rawQuery(queryString, whereArgs);
        if (cursor == null) return null;
        Attendance attendance = new Attendance();
        while (cursor.moveToNext()){
            attendance.setId(Integer.parseInt(cursor.getString(0)));
            attendance.setEmployeeId(Integer.parseInt(cursor.getString(1)));
            attendance.setWorkDate(cursor.getString(2));
            attendance.setCheckinTime(cursor.getString(3));
            attendance.setStatus(cursor.getString(5));
        }
        return attendance;
    }

    public List<CustomAttendance> getAttendanceList(String status, String workDate){
        dbHelper = new DatabaseHandler(context);
        db = dbHelper.getWritableDatabase();
        String queryString = "SELECT CHAMCONG.MANV, HOTEN, SDT, TENPB, TRANGTHAI\n" +
                "FROM CHAMCONG, NHANVIEN, PHONGBAN\n" +
                "WHERE CHAMCONG.MANV = NHANVIEN.MANV\n" +
                "AND PHONGBAN.MAPB = NHANVIEN.MAPB\n" +
                "AND TRANGTHAI = ? AND NGAYLAMVIEC = ?";
        String[] whereArgs  = {status, workDate};
        Cursor cursor = db.rawQuery(queryString, whereArgs);
        List<CustomAttendance> attendancesList = new ArrayList<>();
        while (cursor.moveToNext()){
            CustomAttendance attendance = new CustomAttendance();
            attendance.setEmployeeId(Integer.parseInt(cursor.getString(0)));
            attendance.setName(cursor.getString(1));
            attendance.setPhone(cursor.getString(2));
            attendance.setDepartment(cursor.getString(3));
            attendance.setStatus(cursor.getString(4));
            attendancesList.add(attendance);
        }
        return attendancesList;
    }

    public NhanVien getEmployee(int employeeId){
        dbHelper = new DatabaseHandler(context);
        db = dbHelper.getWritableDatabase();
        String queryString = "SELECT * FROM NHANVIEN WHERE MANV = ?";
        String[] whereArgs  = { String.valueOf(employeeId)};
        Cursor cursor = db.rawQuery(queryString, whereArgs);
        if (cursor == null) return null;
        NhanVien nhanVien = new NhanVien();
        while (cursor.moveToNext()){
            nhanVien.setMaNV(Integer.parseInt(cursor.getString(0)));
            nhanVien.setHoTen(cursor.getString(1));
            nhanVien.setNgSinh(cursor.getString(2));
            nhanVien.setSdt(cursor.getString(3));
        }
        return nhanVien;
    }

}
