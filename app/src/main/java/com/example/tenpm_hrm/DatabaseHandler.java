package com.example.tenpm_hrm;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import models.Account;
import models.Department;
import models.Facility;
import models.NhanVien;
import models.Project;
import models.Project_NhanVien;
import models.Request;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TenPM_HRM_DB";
    private SQLiteDatabase db;
    private static final String CREATE_TABLE_PHONGBAN =
            "CREATE TABLE PHONGBAN (" +
                    "MAPB INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TENPB TEXT NOT NULL, " +
                    "NGTHANHLAP TEXT NOT NULL, " +
                    "MATRUONGPHONG INTEGER, " +
                    "NGAYNHANCHUC TEXT, " +
                    "AVATAR_PATH TEXT " +
                    ");";
    private static final String CREATE_TABLE_NHANVIEN =
            "CREATE TABLE NHANVIEN (" +
                    "MANV INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "HOTEN TEXT NOT NULL, " +
                    "GIOITINH TEXT NOT NULL CHECK (GIOITINH IN ('Nam', 'Nu')), " +
                    "NGSINH TEXT NOT NULL, " +
                    "SDT TEXT NOT NULL UNIQUE, " +
                    "EMAIL TEXT NOT NULL UNIQUE, " +
                    "DIACHI TEXT, " +
                    "CCCD TEXT NOT NULL UNIQUE, " +
                    "CAPBAC TEXT NOT NULL, " +
                    "MAPB INTEGER NOT NULL, " +
                    "FOREIGN KEY (MAPB) REFERENCES PHONGBAN(MAPB)" +
                    ");";

    private static final String CREATE_TABLE_CHAMCONG =
            "CREATE TABLE CHAMCONG (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "MANV INTEGER NOT NULL, " +
                    "NGAYLAMVIEC TEXT NOT NULL, " +
                    "GIOBATDAU TEXT, " +
                    "GIOKETTHUC TEXT, " +
                    "TRANGTHAI TEXT,"  +
                    "CONSTRAINT chamcong_fk FOREIGN KEY (MANV) REFERENCES NHANVIEN (MANV)" +
                    ");";
    private static final String CREATE_TABLE_TAIKHOAN =
            "CREATE TABLE TAIKHOAN (" +
                    "MATK INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "MANV INTEGER, " +
                    "TENTK TEXT NOT NULL UNIQUE, " +
                    "MATKHAU TEXT NOT NULL, " +
                    "LOAITAIKHOAN TEXT NOT NULL CHECK (LOAITAIKHOAN IN ('quản lý', 'nhân viên')), " +
                    "FOREIGN KEY (MANV) REFERENCES NHANVIEN(MANV)" +
                    ");";
    private static final String CREATE_TABLE_YEUCAU =
            "CREATE TABLE YEUCAU (" +
                    "MAYC INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "MANV INTEGER, " +
                    "NOIDUNG TEXT, " +
                    "CHUDE TEXT, " +
                    "TRANGTHAI INTEGER CHECK (TRANGTHAI IN (-1, 0, 1)), " +
                    "FOREIGN KEY (MANV) REFERENCES NHANVIEN(MANV)" +
                    ");";
    private static final String CREATE_TABLE_COSOVATCHAT =
            "CREATE TABLE COSOVATCHAT (" +
                    "MACSVC INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TENCSVC TEXT NOT NULL, " +
                    "SOLUONG INTEGER NOT NULL CHECK (SOLUONG > 0), " +
                    "NGAYMUA TEXT, " +
                    "TRANGTHAI TEXT NOT NULL CHECK (TRANGTHAI IN ('Sử dụng', 'Hư hỏng', 'Bảo trì')), " +
                    "MAPB INTEGER, " +
                    "FOREIGN KEY (MAPB) REFERENCES PHONGBAN(MAPB)" +
                    ");";
    private static final String CREATE_TABLE_DUAN =
            "CREATE TABLE DUAN (" +
                    "MADA INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TENDUAN TEXT NOT NULL, " +
                    "NGAYBATDAU TEXT NOT NULL, " +
                    "NGAYKETTHUC TEXT, " +
                    "TRANGTHAI TEXT NOT NULL CHECK (TRANGTHAI IN ('Đang thực hiện', 'Hoàn thành', 'Bị hủy')), " +
                    "MOTA TEXT, " +
                    "MAPB INTEGER, " +
                    "FOREIGN KEY (MAPB) REFERENCES PHONGBAN(MAPB)" +
                    ");";
    private static final String CREATE_TABLE_NHANVIEN_DUAN =
            "CREATE TABLE NHANVIEN_DUAN (" +
                    "MANV INTEGER, " +
                    "MADA INTEGER, " +
                    "VAITRO TEXT NOT NULL, " +
                    "NGAYTHAMGIA TEXT NOT NULL, " +
                    "PRIMARY KEY (MANV, MADA), " +
                    "FOREIGN KEY (MANV) REFERENCES NHANVIEN(MANV), " +
                    "FOREIGN KEY (MADA) REFERENCES DUAN(MADA)" +
                    ");";
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PHONGBAN);
        db.execSQL(CREATE_TABLE_NHANVIEN);
        db.execSQL(CREATE_TABLE_TAIKHOAN);
        db.execSQL(CREATE_TABLE_CHAMCONG);
        db.execSQL(CREATE_TABLE_YEUCAU);
        db.execSQL(CREATE_TABLE_COSOVATCHAT);
        db.execSQL(CREATE_TABLE_DUAN);
        db.execSQL(CREATE_TABLE_NHANVIEN_DUAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS NHANVIEN_DUAN");
        db.execSQL("DROP TABLE IF EXISTS DUAN");
        db.execSQL("DROP TABLE IF EXISTS COSOVATCHAT");
        db.execSQL("DROP TABLE IF EXISTS YEUCAU");
        db.execSQL("DROP TABLE IF EXISTS CHAMCONG");
        db.execSQL("DROP TABLE IF EXISTS TAIKHOAN");
        db.execSQL("DROP TABLE IF EXISTS NHANVIEN");
        db.execSQL("DROP TABLE IF EXISTS PHONGBAN");
        onCreate(db);
    }
    public void close() {
        db.close();
    }
    public void addDe() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TENPB", "Phòng Kỹ thuật");
        values.put("NGTHANHLAP", "27/12/2024");
        values.put("MATRUONGPHONG", 1);
        values.put("NGAYNHANCHUC", "27/12/2024");
        values.put("AVATAR_PATH", "content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F1000000036/ORIGINAL/NONE/image%2Fjpeg/1615531904");
        long rowId = db.insert("PHONGBAN", null, values);
    }

    public void addFacilities() {
        db = this.getWritableDatabase();
        // Thêm phần tử 1
        ContentValues values1 = new ContentValues();
        values1.put("TENCSVC", "Máy tính xách tay");
        values1.put("SOLUONG", 10);
        values1.put("NGAYMUA", "27/12/2024");
        values1.put("TRANGTHAI", "Sử dụng");
        values1.put("MAPB", 1);
        long rowId1 = db.insert("COSOVATCHAT", null, values1);

        // Thêm phần tử 2
        ContentValues values2 = new ContentValues();
        values2.put("TENCSVC", "Máy chiếu");
        values2.put("SOLUONG", 5);
        values2.put("NGAYMUA", "27/12/2024");
        values2.put("TRANGTHAI", "Bảo trì");
        values2.put("MAPB", 2);
        long rowId2 = db.insert("COSOVATCHAT", null, values2);

        // Thêm phần tử 3
        ContentValues values3 = new ContentValues();
        values3.put("TENCSVC", "Điều hòa");
        values3.put("SOLUONG", 3);
        values3.put("NGAYMUA", "27/12/2024");
        values3.put("TRANGTHAI", "Hư hỏng");
        values3.put("MAPB", 3);
        long rowId3 = db.insert("COSOVATCHAT", null, values3);
        db.close();
    }

    public void addDepartmentsData() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Thêm phòng ban IT
        ContentValues values1 = new ContentValues();
        values1.put("TENPB", "IT");
        values1.put("NGTHANHLAP", "01/01/2020");
        values1.put("MATRUONGPHONG", 1);
        values1.put("NGAYNHANCHUC", "15/02/2020");
        values1.put("AVATAR_PATH", "it");
        db.insert("PHONGBAN", null, values1);

        // Thêm phòng ban HR
        ContentValues values2 = new ContentValues();
        values2.put("TENPB", "HR");
        values2.put("NGTHANHLAP", "15/05/2021");
        values2.put("MATRUONGPHONG", 2);
        values2.put("NGAYNHANCHUC", "10/06/2019");
        values2.put("AVATAR_PATH", "hr");
        db.insert("PHONGBAN", null, values2);

        // Thêm phòng ban Sales
        ContentValues values3 = new ContentValues();
        values3.put("TENPB", "Sales");
        values3.put("NGTHANHLAP", "20/03/2018");
        values3.put("MATRUONGPHONG", 3);
        values3.put("NGAYNHANCHUC", "05/04/2018");
        values3.put("AVATAR_PATH", "sale");
        db.insert("PHONGBAN", null, values3);

        // Thêm phòng ban Marketing
        ContentValues values4 = new ContentValues();
        values4.put("TENPB", "Marketing");
        values4.put("NGTHANHLAP", "10/06/2017");
        values4.put("MATRUONGPHONG", 4);
        values4.put("NGAYNHANCHUC", "01/07/2021");
        values4.put("AVATAR_PATH", "marketing");
        db.insert("PHONGBAN", null, values4);

        db.close();
    }

    public void add20Facilities() {
        db = this.getWritableDatabase();
        for (int i = 1; i <= 20; i++) {
            ContentValues values = new ContentValues();
            values.put("TENCSVC", "Cơ sở vật chất " + i);
            values.put("SOLUONG", (i % 10) + 1);
            values.put("NGAYMUA", "27/12/2024");
            values.put("TRANGTHAI", (i % 3 == 0) ? "Sử dụng" : (i % 3 == 1) ? "Bảo trì" : "Hư hỏng");
            values.put("MAPB", (i % 4) + 1);

            long rowId = db.insert("COSOVATCHAT", null, values);
        }

        db.close();
    }




    public void addProjectDe() {
        db = this.getWritableDatabase();
        ContentValues projectValues = new ContentValues();
        projectValues.put("TENDUAN", "Dự án XYZ");
        projectValues.put("NGAYBATDAU", "28/12/2024");
        projectValues.put("NGAYKETTHUC", "31/12/2024");
        projectValues.put("TRANGTHAI", "Đang thực hiện");
        projectValues.put("MOTA", "Mô tả dự án XYZ");
        projectValues.put("MAPB", 1);
        long projectId = db.insert("DUAN", null, projectValues);

        if (projectId != -1) {
            addEmployeeToProject((int) projectId);
        }
    }

    private void addEmployeeToProject(int projectId) {
        ContentValues employeeValues1 = new ContentValues();
        employeeValues1.put("MANV", 1);
        employeeValues1.put("MADA", projectId);
        employeeValues1.put("VAITRO", "Quản lý");
        employeeValues1.put("NGAYTHAMGIA", "28/12/2024");

        ContentValues employeeValues2 = new ContentValues();
        employeeValues2.put("MANV", 2);
        employeeValues2.put("MADA", projectId);
        employeeValues2.put("VAITRO", "Thành viên");
        employeeValues2.put("NGAYTHAMGIA", "28/12/2024");

        db.insert("NHANVIEN_DUAN", null, employeeValues1);
        db.insert("NHANVIEN_DUAN", null, employeeValues2);
    }

    public void add20Projects() {
        db = this.getWritableDatabase();

        for (int i = 1; i <= 20; i++) {
            ContentValues projectValues = new ContentValues();
            projectValues.put("TENDUAN", "Dự án XYZ " + i);
            projectValues.put("NGAYBATDAU", "28/12/2024");
            projectValues.put("NGAYKETTHUC", "31/12/2024");
            projectValues.put("TRANGTHAI", "Đang thực hiện");
            projectValues.put("MOTA", "Mô tả dự án XYZ " + i);
            projectValues.put("MAPB", (i % 4) + 1);

            long projectId = db.insert("DUAN", null, projectValues);

            if (projectId != -1) {
                addEmployeesToProject((int) projectId, i);
            }
        }
    }

    private void addEmployeesToProject(int projectId, int index) {
        ContentValues employeeValues1 = new ContentValues();
        employeeValues1.put("MANV", (index % 5) + 1);
        employeeValues1.put("MADA", projectId);
        employeeValues1.put("VAITRO", "Quản lý");
        employeeValues1.put("NGAYTHAMGIA", "28/12/2024");

        ContentValues employeeValues2 = new ContentValues();
        employeeValues2.put("MANV", ((index + 1) % 5) + 1);
        employeeValues2.put("MADA", projectId);
        employeeValues2.put("VAITRO", "Thành viên");
        employeeValues2.put("NGAYTHAMGIA", "28/12/2024");

        db.insert("NHANVIEN_DUAN", null, employeeValues1);
        db.insert("NHANVIEN_DUAN", null, employeeValues2);
    }


    public void addAdminAccount() {
        db = this.getWritableDatabase();

        // Nhân viên 1
        ContentValues values1 = new ContentValues();
        values1.put("HOTEN", "Nguyễn Văn A");
        values1.put("GIOITINH", "Nam");
        values1.put("NGSINH", "2004-02-15");
        values1.put("SDT", "123123123");
        values1.put("EMAIL", "lanhmdk@gmail.com");
        values1.put("DIACHI", "Long Thành, Đồng Nai");
        values1.put("CCCD", "001004075822");
        values1.put("CAPBAC", "MANAGER");
        values1.put("MAPB", 1);

        long rowId1 = db.insert("NHANVIEN", null, values1);
        if (rowId1 == -1) {
            Log.e("DatabaseHandler", "Error inserting first employee data");
        } else {
            ContentValues accountValues1 = new ContentValues();
            accountValues1.put("MANV", rowId1);
            accountValues1.put("TENTK", "admin");
            accountValues1.put("MATKHAU", "admin");
            accountValues1.put("LOAITAIKHOAN", "quản lý");

            long accountRowId1 = db.insert("TAIKHOAN", null, accountValues1);
            if (accountRowId1 == -1) {
                Log.e("DatabaseHandler", "Error inserting first admin account data");
            }
        }

        // Nhân viên 2
        ContentValues values2 = new ContentValues();
        values2.put("HOTEN", "Nguyễn Văn B");
        values2.put("GIOITINH", "Nu");
        values2.put("NGSINH", "2000-05-20");
        values2.put("SDT", "321321321");
        values2.put("EMAIL", "nuhoang@gmail.com");
        values2.put("DIACHI", "Hồ Chí Minh");
        values2.put("CCCD", "001004075823");
        values2.put("CAPBAC", "MANAGER");
        values2.put("MAPB", 2); // Đảm bảo MAPB đã tồn tại trong bảng PHONGBAN

        long rowId2 = db.insert("NHANVIEN", null, values2);
        if (rowId2 == -1) {
            Log.e("DatabaseHandler", "Error inserting second employee data");
        } else {
            ContentValues accountValues2 = new ContentValues();
            accountValues2.put("MANV", rowId2);
            accountValues2.put("TENTK", "client");
            accountValues2.put("MATKHAU", "client");
            accountValues2.put("LOAITAIKHOAN", "nhân viên");

            long accountRowId2 = db.insert("TAIKHOAN", null, accountValues2);
            if (accountRowId2 == -1) {
                Log.e("DatabaseHandler", "Error inserting second admin account data");
            }
        }

        // Nhân viên 3
        ContentValues values3 = new ContentValues();
        values3.put("HOTEN", "Nguyê Văn C");
        values3.put("GIOITINH", "Nam");
        values3.put("NGSINH", "1995-11-12");
        values3.put("SDT", "456456456");
        values3.put("EMAIL", "thuquyen@gmail.com");
        values3.put("DIACHI", "Hà Nội");
        values3.put("CCCD", "001004075824");
        values3.put("CAPBAC", "STAFF");
        values3.put("MAPB", 3);

        long rowId3 = db.insert("NHANVIEN", null, values3);
        if (rowId3 == -1) {
            Log.e("DatabaseHandler", "Error inserting third employee data");
        } else {
            ContentValues accountValues3 = new ContentValues();
            accountValues3.put("MANV", rowId3);
            accountValues3.put("TENTK", "staff1");
            accountValues3.put("MATKHAU", "staff123");
            accountValues3.put("LOAITAIKHOAN", "nhân viên");

            long accountRowId3 = db.insert("TAIKHOAN", null, accountValues3);
            if (accountRowId3 == -1) {
                Log.e("DatabaseHandler", "Error inserting third admin account data");
            }
        }

        // Nhân viên 4
        ContentValues values4 = new ContentValues();
        values4.put("HOTEN", "Nguyễn Văn Hòa");
        values4.put("GIOITINH", "Nu");
        values4.put("NGSINH", "1998-03-25");
        values4.put("SDT", "789789789");
        values4.put("EMAIL", "xuanro@gmail.com");
        values4.put("DIACHI", "Đà Nẵng");
        values4.put("CCCD", "001004075825");
        values4.put("CAPBAC", "STAFF");
        values4.put("MAPB", 4);

        long rowId4 = db.insert("NHANVIEN", null, values4);
        if (rowId4 == -1) {
            Log.e("DatabaseHandler", "Error inserting fourth employee data");
        } else {
            ContentValues accountValues4 = new ContentValues();
            accountValues4.put("MANV", rowId4);
            accountValues4.put("TENTK", "staff2");
            accountValues4.put("MATKHAU", "staff456");
            accountValues4.put("LOAITAIKHOAN", "nhân viên");

            long accountRowId4 = db.insert("TAIKHOAN", null, accountValues4);
            if (accountRowId4 == -1) {
                Log.e("DatabaseHandler", "Error inserting fourth admin account data");
            }
        }
        db.close();
    }


    // ============Cơ sở vật chất===================
    public void addFacility(Facility facility) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TENCSVC", facility.getFacilityName());
        values.put("SOLUONG", facility.getFacilityQuantity());
        values.put("NGAYMUA", facility.getFacilityBuyingDate());
        values.put("TRANGTHAI", facility.getFacilityStatus());
        values.put("MAPB", facility.getDepartmentID());
        long rowId = db.insert("COSOVATCHAT", null, values);
    }

    public Facility getFacility(int id) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query("COSOVATCHAT", null, "MACSVC" + " = ?", new String[] { String.valueOf(id) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Facility facility = new Facility(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
        cursor.close();
        return facility;
    }

    public List<Facility> getAllFacility() {
        List<Facility> facilityList = new ArrayList<>();
        String query = "SELECT * FROM COSOVATCHAT";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Facility facility = new Facility(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
            facilityList.add(facility);
            cursor.moveToNext();
        }
        cursor.close();
        return facilityList;
    }

    public boolean updateFacility(Facility facility) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MACSVC", facility.getFacilityID());
        values.put("TENCSVC", facility.getFacilityName());
        values.put("SOLUONG", facility.getFacilityQuantity());
        values.put("NGAYMUA", facility.getFacilityBuyingDate());
        values.put("TRANGTHAI", facility.getFacilityStatus());
        values.put("MAPB", facility.getDepartmentID());
        return db.update("COSOVATCHAT", values, "MACSVC" + " = ?", new String[]{String.valueOf(facility.getFacilityID())}) > 0;
    }

    public void deleteFacility(Facility facility) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("COSOVATCHAT", "MACSVC" + " = ?", new String[] { String.valueOf(facility.getFacilityID())});
        db.close();
    }

    public List<Facility> searchFacility(String facilityID, String facilityName, String facilityQuantity, String facilityStatus, String facilityBuyingDate, String departmentID) {
        List<Facility> facilityList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM COSOVATCHAT WHERE 1=1");

        if (facilityID != null && !facilityID.isEmpty()) queryBuilder.append(" AND MACSVC = ").append(facilityID);
        if (facilityName != null && !facilityName.isEmpty()) queryBuilder.append(" AND TENCSVC LIKE '%").append(facilityName).append("%'");
        if (facilityQuantity != null && !facilityQuantity.isEmpty()) queryBuilder.append(" AND SOLUONG = ").append(facilityQuantity);
        if (facilityBuyingDate != null && !facilityBuyingDate.isEmpty()) queryBuilder.append(" AND NGAYMUA LIKE '%").append(facilityBuyingDate).append("%'");
        if (facilityStatus != null && !facilityStatus.isEmpty()) queryBuilder.append(" AND TRANGTHAI LIKE '%").append(facilityStatus).append("%'");
        if (departmentID != null && !departmentID.isEmpty()) queryBuilder.append(" AND MAPB LIKE '%").append(departmentID).append("%'");

        queryBuilder.append(" ORDER BY MACSVC ASC");

        Cursor cursor = db.rawQuery(queryBuilder.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                Facility facility = new Facility(
                        cursor.getInt(cursor.getColumnIndexOrThrow("MACSVC")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TENCSVC")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("SOLUONG")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NGAYMUA")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TRANGTHAI")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("MAPB"))
                );
                facilityList.add(facility);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        for (Facility facility : facilityList) {
            Log.d("FacilityList", "MACSVC: " + facility.getFacilityID() +
                    ", TENCSVC: " + facility.getFacilityName() +
                    ", SOLUONG: " + facility.getFacilityQuantity() +
                    ", NGAYMUA: " + facility.getFacilityBuyingDate() +
                    ", TRANGTHAI: " + facility.getFacilityStatus() +
                    ", MAPB: " + facility.getDepartmentID());
        }
        return facilityList;
    }


    //  ================Project================
    public int addProject(Project project) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TENDUAN", project.getTenDA());
        values.put("NGAYBATDAU", project.getNgayBD());
        values.put("NGAYKETTHUC", project.getNgayKT());
        values.put("TRANGTHAI", project.getTrangThai());
        values.put("MOTA", project.getMoTa());
        values.put("MAPB", project.getMaPB());
        long rowId = db.insert("DUAN", null, values);
        return (int) rowId;
    }

    public Project getProject(int id) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query("DUAN", null, "MADA" + " = ?", new String[] { String.valueOf(id) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Project project = new Project(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
        cursor.close();
        return project;
    }

    public List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        String query = "SELECT * FROM DUAN";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Project project = new Project(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
            projectList.add(project);
            cursor.moveToNext();
        }
        cursor.close();
        return projectList;
    }

    public boolean updateProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MADA", project.getMaDA());
        values.put("TENDUAN", project.getTenDA());
        values.put("NGAYBATDAU", project.getNgayBD());
        values.put("NGAYKETTHUC", project.getNgayKT());
        values.put("TRANGTHAI", project.getTrangThai());
        values.put("MOTA", project.getMoTa());
        values.put("MAPB", project.getMaPB());
        return db.update("DUAN", values, "MADA" + " = ?", new String[]{String.valueOf(project.getMaDA())}) > 0;
    }

    public void deleteProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("DUAN", "MADA" + " = ?", new String[] { String.valueOf(project.getMaDA())});
        db.close();
    }

    public List<Project> searchProject(String projectID, String projectName, String startingDate, String endingDate, String projectStatus, String departmentID) {
        List<Project> projectList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM DUAN WHERE 1=1");

        if (projectID != null && !projectID.isEmpty()) queryBuilder.append(" AND MADA = ").append(projectID);
        if (projectName != null && !projectName.isEmpty()) queryBuilder.append(" AND TENDUAN LIKE '%").append(projectName).append("%'");
        if (startingDate != null && !startingDate.isEmpty()) queryBuilder.append(" AND NGAYBATDAU LIKE '%").append(startingDate).append("%'");
        if (endingDate != null && !endingDate.isEmpty()) queryBuilder.append(" AND NGAYKETTHUC LIKE '%").append(endingDate).append("%'");
        if (projectStatus != null && !projectStatus.isEmpty()) queryBuilder.append(" AND TRANGTHAI LIKE '%").append(projectStatus).append("%'");
        if (departmentID != null && !departmentID.isEmpty()) queryBuilder.append(" AND MAPB = ").append(departmentID);

        queryBuilder.append(" ORDER BY MADA ASC");

        Cursor cursor = db.rawQuery(queryBuilder.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                Project project = new Project(
                        cursor.getInt(cursor.getColumnIndexOrThrow("MADA")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TENDUAN")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NGAYBATDAU")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NGAYKETTHUC")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TRANGTHAI")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MOTA")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("MAPB"))
                );
                projectList.add(project);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return projectList;
    }

    public List<Project> getProjectsByEmployee(int maNV) {
        List<Project> projectList = new ArrayList<>();

        String query = "SELECT d.MADA, d.TENDUAN, d.NGAYBATDAU, d.NGAYKETTHUC, d.TRANGTHAI, d.MOTA, d.MAPB " +
                "FROM DUAN d " +
                "INNER JOIN NHANVIEN_DUAN nd ON d.MADA = nd.MADA " +
                "WHERE nd.MANV = ?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maNV)});

        if (cursor.moveToFirst()) {
            do {
                Project project = new Project(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
                projectList.add(project);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return projectList;
    }

    public List<Project> searchProjectByEmployee(String projectID, String projectName, String startingDate, String endingDate, String projectStatus, String departmentID, String maNV) {
        List<Project> projectList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder queryBuilder = new StringBuilder(
                "SELECT d.MADA, d.TENDUAN, d.NGAYBATDAU, d.NGAYKETTHUC, d.TRANGTHAI, d.MOTA, d.MAPB " +
                        "FROM DUAN d " +
                        "INNER JOIN NHANVIEN_DUAN nd ON d.MADA = nd.MADA " +
                        "WHERE 1=1");

        if (maNV != null && !maNV.isEmpty()) queryBuilder.append(" AND nd.MANV = ").append(maNV);
        if (projectID != null && !projectID.isEmpty()) queryBuilder.append(" AND MADA = ").append(projectID);
        if (projectName != null && !projectName.isEmpty()) queryBuilder.append(" AND TENDUAN LIKE '%").append(projectName).append("%'");
        if (startingDate != null && !startingDate.isEmpty()) queryBuilder.append(" AND NGAYBATDAU LIKE '%").append(startingDate).append("%'");
        if (endingDate != null && !endingDate.isEmpty()) queryBuilder.append(" AND NGAYKETTHUC LIKE '%").append(endingDate).append("%'");
        if (projectStatus != null && !projectStatus.isEmpty()) queryBuilder.append(" AND TRANGTHAI LIKE '%").append(projectStatus).append("%'");
        if (departmentID != null && !departmentID.isEmpty()) queryBuilder.append(" AND MAPB = ").append(departmentID);

        queryBuilder.append(" ORDER BY d.MADA ASC");

        Cursor cursor = db.rawQuery(queryBuilder.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                Project project = new Project(
                        cursor.getInt(cursor.getColumnIndexOrThrow("MADA")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TENDUAN")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NGAYBATDAU")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NGAYKETTHUC")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TRANGTHAI")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MOTA")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("MAPB"))
                );
                projectList.add(project);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return projectList;
    }



    public void addProjectNhanVien(Project_NhanVien projectNhanVien) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MANV", projectNhanVien.getMaNV());
        values.put("MADA", projectNhanVien.getMaDA());
        values.put("VAITRO", projectNhanVien.getVaiTro());
        values.put("NGAYTHAMGIA", projectNhanVien.getNgayTG());
        long rowId = db.insert("NHANVIEN_DUAN", null, values);
    }

    public Project_NhanVien getProjectNhanVien(int mada, int manv) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "NHANVIEN_DUAN", null,"MADA = ? AND MANV = ?", new String[] { String.valueOf(mada), String.valueOf(manv) }, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Project_NhanVien projectNhanVien = new Project_NhanVien(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3));
        cursor.close();
        return projectNhanVien;
    }

    public List<Project_NhanVien> getAllProjectsNhanVien(int mada) {
        List<Project_NhanVien> projectNhanVienList = new ArrayList<>();
        String query = "SELECT * FROM NHANVIEN_DUAN WHERE MADA = " + mada;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Project_NhanVien projectNhanVien = new Project_NhanVien(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3));
            projectNhanVienList.add(projectNhanVien);
            cursor.moveToNext();
        }
        cursor.close();
        return projectNhanVienList;
    }

    public boolean updateProjectNhanVien(Project_NhanVien projectNhanVien) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MANV", projectNhanVien.getMaNV());
        values.put("MADA", projectNhanVien.getMaDA());
        values.put("VAITRO", projectNhanVien.getVaiTro());
        values.put("NGAYTHAMGIA", projectNhanVien.getNgayTG());
        long result = db.insertWithOnConflict("NHANVIEN_DUAN", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public void deleteProjectNhanVien(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("DUAN", "MADA" + " = ?", new String[] { String.valueOf(project.getMaDA())});
        db.close();
    }

    // ============Phòng ban===================
    public void addDepartment(Department department) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TENPB", department.getDepartmentName());
        values.put("NGTHANHLAP", department.getEstablishmentDate());
        values.put("MATRUONGPHONG", department.getManagerId());
        values.put("NGAYNHANCHUC", department.getManagerAppointmentDate());
        values.put("AVATAR_PATH", department.getAvatarPath());
        long rowId = db.insert("PHONGBAN", null, values);

        if (rowId == -1) {
            Log.e("DatabaseHandler", "Error adding department: " + department.getDepartmentName());
        } else {
            Log.d("DatabaseHandler", "Department added with ID: " + rowId);
        }
    }

    public Department getDepartment(int id) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query("PHONGBAN", null, "MAPB" + " = ?", new String[] { String.valueOf(id) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Department department = new Department(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5));
        cursor.close();
        return department;
    }

    public List<Department> getAllDepartment() {
        List<Department> departmentList = new ArrayList<>();
        String query = "SELECT * FROM PHONGBAN";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Department department = new Department(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5));
            departmentList.add(department);
            cursor.moveToNext();
        }
        cursor.close();
        return departmentList;
    }

    public boolean updateDepartment(Department department) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MAPB", department.getDepartmentId());
        values.put("TENPB", department.getDepartmentName());
        values.put("NGTHANHLAP", department.getEstablishmentDate());
        values.put("MATRUONGPHONG", department.getManagerId());
        values.put("NGAYNHANCHUC", department.getManagerAppointmentDate());
        values.put("AVATAR_PATH", department.getAvatarPath());
        return db.update("PHONGBAN", values, "MAPB" + " = ?", new String[]{String.valueOf(department.getDepartmentId())}) > 0;
    }

    public int countEmployeesByDepartment(int departmentID) {
        SQLiteDatabase db = this.getReadableDatabase();
        int employeeCount = 0;

        String query = "SELECT COUNT(*) FROM NHANVIEN WHERE MAPB = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(departmentID)});

        if (cursor.moveToFirst()) {
            employeeCount = cursor.getInt(0);
        }
        cursor.close();
        return employeeCount;
    }



    //    ============================= Request
    public boolean addRequest(int manv, String chude, String noidung) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MANV", manv);
        values.put("CHUDE", chude);
        values.put("NOIDUNG", noidung);
        values.put("TRANGTHAI", 0);
        long rowId = db.insert("YEUCAU", null, values);
        if(rowId != 0){
            return true;
        }
        return false;
    }

    public ArrayList<Request> getRequestsByNVId(int NVid) {
        ArrayList<Request> requestList = new ArrayList<>();
        db = this.getReadableDatabase();

        // Define the selection criteria
        String selection = "MANV = ?";
        String[] selectionArgs = { String.valueOf(NVid) };

        // Query the YEUCAU table for requests by MANV
        Cursor cursor = db.query("YEUCAU", null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            try {
                // Loop through all records in the cursor
                while (cursor.moveToNext()) {
                    // Retrieve data from cursor
                    int mayc = cursor.getInt(cursor.getColumnIndex("MAYC")); // Use exact column name
                    String noidung = cursor.getString(cursor.getColumnIndex("NOIDUNG"));
                    String chude = cursor.getString(cursor.getColumnIndex("CHUDE"));
                    int trangthai = cursor.getInt(cursor.getColumnIndex("TRANGTHAI"));

                    // Create Request object and add to the list
                    Request request = new Request(mayc, NVid, chude, noidung, trangthai);
                    requestList.add(request);
                }
            } finally {
                cursor.close(); // Ensure cursor is closed in the finally block
            }
        }

        return requestList;
    }

    public Request getRequestsByRqId(int RqId) {
        Request request = new Request();
        db = this.getReadableDatabase();

        // Define the selection criteria
        String selection = "MAYC = ?";
        String[] selectionArgs = { String.valueOf(RqId) };

        // Query the YEUCAU table for requests by MANV
        Cursor cursor = db.query("YEUCAU", null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            try {
                // Loop through all records in the cursor
                while (cursor.moveToNext()) {
                    // Retrieve data from cursor
                    int mayc = cursor.getInt(cursor.getColumnIndex("MAYC")); // Use exact column name
                    int NVid = cursor.getInt(cursor.getColumnIndex("MANV")); // Use exact column name
                    String noidung = cursor.getString(cursor.getColumnIndex("NOIDUNG"));
                    String chude = cursor.getString(cursor.getColumnIndex("CHUDE"));
                    int trangthai = cursor.getInt(cursor.getColumnIndex("TRANGTHAI"));

                    // Create Request object and add to the list
                    request = new Request(mayc, NVid, chude, noidung, trangthai);
                }
            } finally {
                cursor.close(); // Ensure cursor is closed in the finally block
            }
        }

        return request;
    }

    public ArrayList<Request> getAllRequests() {
        ArrayList<Request> requestList = new ArrayList<>();
        db = this.getReadableDatabase();

        // Truy vấn tất cả các cột từ bảng YEUCAU
        Cursor cursor = db.query("YEUCAU", null, null, null, null, null, null);

        if (cursor != null) {
            try {
                // Lặp qua tất cả các bản ghi trong cursor
                while (cursor.moveToNext()) {
                    // Lấy dữ liệu từ cursor
                    int mayc = cursor.getInt(cursor.getColumnIndex("MAYC")); // Sử dụng tên cột chính xác
                    int manv = cursor.getInt(cursor.getColumnIndex("MANV"));
                    String noidung = cursor.getString(cursor.getColumnIndex("NOIDUNG"));
                    String chude = cursor.getString(cursor.getColumnIndex("CHUDE"));
                    int trangthai = cursor.getInt(cursor.getColumnIndex("TRANGTHAI"));

                    // Tạo đối tượng Request và thêm vào danh sách
                    Request request = new Request(mayc, manv, chude, noidung, trangthai);
                    requestList.add(request);
                }
            } finally {
                cursor.close(); // Đảm bảo đóng cursor trong khối finally
            }
        }

        return requestList;
    }

    public boolean approveRequest(int mayc) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TRANGTHAI", 1); // Assuming 1 indicates approved

        // Update the request in the database
        int rowsAffected = db.update("YEUCAU", values, "MAYC = ?", new String[]{String.valueOf(mayc)});
        db.close();

        return rowsAffected > 0; // Return true if at least one row was updated
    }

    public boolean rejectRequest(int mayc) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TRANGTHAI", -1); // Assuming 1 indicates approved

        // Update the request in the database
        int rowsAffected = db.update("YEUCAU", values, "MAYC = ?", new String[]{String.valueOf(mayc)});
        db.close();

        return rowsAffected > 0; // Return true if at least one row was updated
    }

    public boolean approveAllRequests() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TRANGTHAI", 1); // Assuming 1 indicates approved

        // Update all requests in the database
        int rowsAffected = db.update("YEUCAU", values, null, null); // No condition to update all rows
        db.close();

        return rowsAffected > 0; // Return true if at least one row was updated
    }
    public boolean deleteRequest(int maYC) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define the where clause for the deletion
        String whereClause = "MAYC = ?";
        String[] whereArgs = { String.valueOf(maYC) };

        // Execute the delete operation
        int rowsAffected = db.delete("YEUCAU", whereClause, whereArgs);
        db.close();

        // Return true if at least one row was deleted
        return rowsAffected > 0;
    }

    public boolean editRequest(int maYC, String newChuDe, String newNoiDung) {
        SQLiteDatabase db = this.getWritableDatabase(); // Open the database for writing

        ContentValues values = new ContentValues();
        values.put("CHUDE", newChuDe); // Set the new topic
        values.put("NOIDUNG", newNoiDung); // Set the new information
        values.put("TRANGTHAI", 0); // Set the new information

        // Update the request in the database where the id matches maYC
        int rowsAffected = db.update("YEUCAU", values, "MAYC = ?", new String[]{String.valueOf(maYC)});

        db.close(); // Close the database after operation

        return rowsAffected > 0; // Return true if the update was successful, false otherwise
    }

    // =====================Toan lam NHANVIEN ====================
    public int getMaxMaNV() {
        int maxMaNV = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(maNV) FROM NHANVIEN", null);
        if (cursor.moveToFirst()) {
            maxMaNV = cursor.getInt(0);
        }
        cursor.close();
        return maxMaNV;
    }
    public void addNhanVien(NhanVien nhanVien) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HOTEN", nhanVien.getHoTen());
        values.put("GIOITINH", nhanVien.getGioiTinh());
        values.put("NGSINH", nhanVien.getNgSinh());
        values.put("SDT", nhanVien.getSdt());
        values.put("EMAIL", nhanVien.getEmail());
        values.put("DIACHI", nhanVien.getDiaChi());
        values.put("CCCD", nhanVien.getCccd());
        values.put("CAPBAC", nhanVien.getCapBac());
        values.put("MAPB", nhanVien.getMaPB());
        db.insert("NHANVIEN", null, values);
        db.close();
    }
    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM NHANVIEN", null);
    }

    public List<NhanVien> getAllEmployes() {
        List<NhanVien> nhanVienList = new ArrayList<>();
        db = this.getReadableDatabase();

        String query = "SELECT * FROM NHANVIEN";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            NhanVien nhanVien = new NhanVien(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9));
            nhanVienList.add(nhanVien);
            cursor.moveToNext();
        }
        cursor.close();
        return nhanVienList;
    }
    // Define column names
    public static final String COLUMN_NAME = "HOTEN";
    public static final String COLUMN_DEPARTMENT = "MAPB";
    public static final String COLUMN_POSITION = "CAPBAC";
    public static final String COLUMN_EMPLOYEE_ID = "MANV";


    public boolean deleteEmployee(String employeeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "MANV = ?";
        String[] whereArgs = { employeeId };

        int rowsAffected = db.delete("NHANVIEN", whereClause, whereArgs);
        return rowsAffected > 0;
    }
    public NhanVien getEmployeeById(int employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("NHANVIEN", null, "MANV = ?", new String[]{String.valueOf(employeeId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setMaNV(cursor.getInt(cursor.getColumnIndex("MANV")));
            nhanVien.setHoTen(cursor.getString(cursor.getColumnIndex("HOTEN")));
            nhanVien.setGioiTinh(cursor.getString(cursor.getColumnIndex("GIOITINH")));
            nhanVien.setNgSinh(cursor.getString(cursor.getColumnIndex("NGSINH")));
            nhanVien.setSdt(cursor.getString(cursor.getColumnIndex("SDT")));
            nhanVien.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            nhanVien.setDiaChi(cursor.getString(cursor.getColumnIndex("DIACHI")));
            nhanVien.setCccd(cursor.getString(cursor.getColumnIndex("CCCD")));
            nhanVien.setCapBac(cursor.getString(cursor.getColumnIndex("CAPBAC")));
            nhanVien.setMaPB(cursor.getInt(cursor.getColumnIndex("MAPB")));
            cursor.close();
            return nhanVien;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public boolean updateEmployee(NhanVien nhanVien) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("HOTEN", nhanVien.getHoTen());
        values.put("GIOITINH", nhanVien.getGioiTinh());
        values.put("NGSINH", nhanVien.getNgSinh());
        values.put("SDT", nhanVien.getSdt());
        values.put("EMAIL", nhanVien.getEmail());
        values.put("DIACHI", nhanVien.getDiaChi());
        values.put("CCCD", nhanVien.getCccd());
        values.put("CAPBAC", nhanVien.getCapBac());
        values.put("MAPB", nhanVien.getMaPB());

        int rowsUpdated = db.update("NHANVIEN", values, "MANV = ?", new String[]{String.valueOf(nhanVien.getMaNV())});
        return rowsUpdated > 0;
    }
    public List<NhanVien> searchEmployees(String maNV, String hoTen,String gioiTinh, String ngSinh, String sdt, String email, String diaChi, String cccd, String capBac, String phongBan) {
        List<NhanVien> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM NHANVIEN WHERE 1=1");

        if (maNV != null && !maNV.isEmpty()) queryBuilder.append(" AND MANV = ").append(maNV);
        if (hoTen != null && !hoTen.isEmpty()) queryBuilder.append(" AND HOTEN LIKE '%").append(hoTen).append("%'");
        if (gioiTinh != null && !gioiTinh.isEmpty()) queryBuilder.append(" AND GIOITINH LIKE '%").append(gioiTinh).append("%'");
        if (ngSinh != null && !ngSinh.isEmpty()) queryBuilder.append(" AND NGSINH LIKE '%").append(ngSinh).append("%'");
        if (sdt != null && !sdt.isEmpty()) queryBuilder.append(" AND SDT LIKE '%").append(sdt).append("%'");
        if (email != null && !email.isEmpty()) queryBuilder.append(" AND EMAIL LIKE '%").append(email).append("%'");
        if (diaChi != null && !diaChi.isEmpty()) queryBuilder.append(" AND DIACHI LIKE '%").append(diaChi).append("%'");
        if (cccd != null && !cccd.isEmpty()) queryBuilder.append(" AND CCCD LIKE '%").append(cccd).append("%'");
        if (capBac != null && !capBac.isEmpty()) queryBuilder.append(" AND CAPBAC LIKE '%").append(capBac).append("%'");
        if (phongBan != null && !phongBan.isEmpty()) queryBuilder.append(" AND MAPB LIKE '%").append(phongBan).append("%'");

        queryBuilder.append(" ORDER BY MANV ASC");

        Cursor cursor = db.rawQuery(queryBuilder.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                NhanVien employee = new NhanVien(
                        cursor.getInt(cursor.getColumnIndexOrThrow("MANV")),
                        cursor.getString(cursor.getColumnIndexOrThrow("HOTEN")),
                        cursor.getString(cursor.getColumnIndexOrThrow("GIOITINH")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NGSINH")),
                        cursor.getString(cursor.getColumnIndexOrThrow("SDT")),
                        cursor.getString(cursor.getColumnIndexOrThrow("EMAIL")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DIACHI")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CCCD")),
                        cursor.getString(cursor.getColumnIndexOrThrow("CAPBAC")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("MAPB"))
                );
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return employeeList;
    }





//    update mk taikhoan
public boolean updatePassword(String email, String newPassword) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("MATKHAU", newPassword);

        // Câu lệnh SQL cập nhật MATKHAU trong bảng TAIKHOAN kết hợp với NHANVIEN
        String query = "UPDATE TAIKHOAN " +
                "SET MATKHAU = ? " +
                "WHERE MANV IN (SELECT MANV FROM NHANVIEN WHERE EMAIL = ?)";
        try {
            db.execSQL(query, new Object[]{newPassword, email});
            return true; // Trả về true nếu câu lệnh thực thi thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu xảy ra lỗi
        }
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM NHANVIEN WHERE email = ?";
            cursor = db.rawQuery(query, new String[]{email});

            // Check if the cursor contains any data
            if (cursor != null && cursor.moveToFirst()) {
                return true; // Email exists
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return false; // Email does not exist
    }
    public boolean addAccount(int employeeId, String username, String password, String accountType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MANV", employeeId);
        values.put("TENTK", username);
        values.put("MATKHAU", password);
        values.put("LOAITAIKHOAN", accountType);
        long result = db.insert("TAIKHOAN", null, values);
        return result != -1;
    }
    public List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM TAIKHOAN", null);
        if (cursor.moveToFirst()) {
            do {
                int accountId = cursor.getInt(0);
                int employeeId = cursor.getInt(1);
                String username = cursor.getString(2);
                String password = cursor.getString(3);
                String accountType = cursor.getString(4);

                accountList.add(new Account( employeeId, accountId, username, password, accountType));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return accountList;
    }

    public boolean updateAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MANV", account.getMaNV());
        values.put("TENTK", account.getTenTK());
        values.put("MATKHAU", account.getMatKhau());
        values.put("LOAITAIKHOAN", account.getLoaiTK());


        // Cập nhật thông tin dựa trên ID
        int rowsAffected = db.update("TAIKHOAN", values, "MATK" + " = ?", new String[]{String.valueOf(account.getMaTK())});
        db.close();
        return rowsAffected > 0; // Trả về true nếu có ít nhất 1 dòng được cập nhật
    }
    public boolean deleteAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("TAIKHOAN", "MATK" + " = ?", new String[]{String.valueOf(account.getMaTK())});
        db.close();
        return rowsDeleted > 0; // Trả về true nếu có ít nhất 1 dòng bị xóa
    }
    public Account getAccountById(int accountId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Câu lệnh SQL để truy vấn tài khoản theo ID
        String query = "SELECT * FROM TAIKHOAN WHERE MATK = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(accountId)});

        // Nếu tìm thấy tài khoản
        if (cursor != null && cursor.moveToFirst()) {
            // Khởi tạo đối tượng Account từ dữ liệu truy vấn
            Account account = new Account();
            account.setMaTK(cursor.getInt(0));  // Cột "id"
            account.setMaNV(cursor.getInt(1));  // Cột "maNV"
            account.setTenTK(cursor.getString(2));  // Cột "tenTK"
            account.setMatKhau(cursor.getString(3));
            account.setLoaiTK(cursor.getString(4));  // Cột "loaiTK"

            // Đóng con trỏ và trả về tài khoản
            cursor.close();
            db.close();
            return account;
        }
        // Nếu không tìm thấy tài khoản, trả về null
        cursor.close();
        db.close();
        return null;
    }
}
