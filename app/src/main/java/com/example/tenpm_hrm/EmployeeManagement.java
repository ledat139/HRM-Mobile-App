package com.example.tenpm_hrm;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.List;

import models.NhanVien;

public class EmployeeManagement extends AppCompatActivity {
    private ConstraintLayout mainLayout;
    private View lastAddedItem; // Lưu tham chiếu đến item cuối cùng được thêm
    private Button btnAddEmployee;
    private DatabaseHandler dbHelper;
    private ImageView deleteImageView, editImageView, ivSearch;
    private List<ConstraintLayout> addedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_management);

        mainLayout = findViewById(R.id.main);
        btnAddEmployee = findViewById(R.id.button);
        dbHelper = new DatabaseHandler(this);
        ivSearch = findViewById(R.id.ivSearch);

        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chuyển sang form thêm nhân viên mới
                Intent intent = new Intent(EmployeeManagement.this, NewEmployee.class);
                startActivityForResult(intent, 2);
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeManagement.this, SearchEmployee.class);
                startActivityForResult(intent, 1); // Sử dụng request code 1
            }
        });
        // Lấy dữ liệu từ cơ sở dữ liệu và thêm các mục
        addItemsFromDatabase();
    }

    private void addItemsFromDatabase() {
        Cursor cursor = dbHelper.getAllEmployees();
        if (cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_NAME);
                int departmentIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_DEPARTMENT);
                int positionIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_POSITION);
                int employeeIdIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_EMPLOYEE_ID);

                if (nameIndex >= 0 && departmentIndex >= 0 && positionIndex >= 0 && employeeIdIndex >= 0) {
                    String name = cursor.getString(nameIndex);
                    String department = cursor.getString(departmentIndex);
                    String position = cursor.getString(positionIndex);
                    String employeeId = cursor.getString(employeeIdIndex);
                    addItemToManagement(name, department, position, employeeId);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void addItemToManagement(String name, String department, String position, String employeeId) {
        // Tạo một ConstraintLayout mới cho item
        ConstraintLayout newItem = (ConstraintLayout) getLayoutInflater().inflate(R.layout.item_employee, null);
        newItem.setId(View.generateViewId());

        // Gán dữ liệu động
        TextView nameTextView = newItem.findViewById(R.id.tvName);
        TextView departmentTextView = newItem.findViewById(R.id.tvDepartment);
        TextView positionTextView = newItem.findViewById(R.id.tvPosition);
        TextView employeeIdTextView = newItem.findViewById(R.id.tvID);
        deleteImageView = newItem.findViewById(R.id.ivDelete);
        editImageView = newItem.findViewById(R.id.imageView_Detail);

        nameTextView.setText(name);
        departmentTextView.setText("Phòng ban: " + department);
        positionTextView.setText(position);
        employeeIdTextView.setText("Mã nhân viên: " + employeeId);

        // Thêm sự kiện xóa
        deleteImageView.setOnClickListener(v -> {
            // Hiển thị dialog xác nhận
            new AlertDialog.Builder(EmployeeManagement.this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa nhân viên này không?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Xóa nhân viên khỏi database
                        boolean isDeleted = dbHelper.deleteEmployee(employeeId);
                        if (isDeleted) {
                            // Xóa view khỏi giao diện
                            mainLayout.removeView(newItem);
                            addedItems.remove(newItem); // Loại bỏ item khỏi danh sách đã thêm
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });

        editImageView.setOnClickListener(v -> {
            // Chuyển sang intent sửa thông tin, truyền ID của nhân viên
            Intent intent = new Intent(EmployeeManagement.this, ChangeEmployeeInformation.class);
            intent.putExtra("employeeId", employeeId);
            startActivity(intent);
        });

        // Thêm item mới vào layout chính
        mainLayout.addView(newItem);
        addedItems.add(newItem); // Lưu trữ item đã được thêm

        // Thiết lập ràng buộc cho item mới
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);

        if (lastAddedItem == null) {
            // Nếu là item đầu tiên, căn nó với đỉnh của layout chính
            constraintSet.connect(newItem.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 800);
        } else {
            // Nếu không, căn nó với cạnh dưới của item trước đó
            constraintSet.connect(newItem.getId(), ConstraintSet.TOP, lastAddedItem.getId(), ConstraintSet.BOTTOM, 32); // Khoảng cách 32dp
        }

        // Căn chỉnh item theo chiều ngang
        constraintSet.connect(newItem.getId(), ConstraintSet.START, mainLayout.getId(), ConstraintSet.START, 16);
        constraintSet.connect(newItem.getId(), ConstraintSet.END, mainLayout.getId(), ConstraintSet.END, 16);

        // Áp dụng ràng buộc
        constraintSet.applyTo(mainLayout);

        // Cập nhật tham chiếu đến item cuối cùng
        lastAddedItem = newItem;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Only proceed if the result is OK
        if (resultCode == RESULT_OK) {
            String maNV = data.getStringExtra("maNV");
            String hoTen = data.getStringExtra("name");
            String gioiTinh = data.getStringExtra("gender");
            String ngSinh = data.getStringExtra("birthday");
            String sdt = data.getStringExtra("phone");
            String email = data.getStringExtra("email");
            String diaChi = data.getStringExtra("address");
            String cccd = data.getStringExtra("cccd");
            String capBac = data.getStringExtra("position");
            String phongBan = data.getStringExtra("iddepartment");

            // Trả về từ form search
            if (requestCode == 1) {
                // Lấy danh sách nhân viên có thông tin tương ứng
                List<NhanVien> employeeList = dbHelper.searchEmployees(maNV, hoTen, gioiTinh, ngSinh, sdt, email, diaChi, capBac, phongBan);

                removeAllItems();

                if (employeeList.isEmpty()) {
                    // Thông báo không thấy nhân viên
                    Toast.makeText(this, "Không có nhân viên có thông tin đang tìm kiếm.", Toast.LENGTH_SHORT).show();
                } else {
                    for (NhanVien nhanVien : employeeList) {
                        addItemToManagement(
                                nhanVien.getHoTen(),
                                String.valueOf(nhanVien.getMaPB()),
                                nhanVien.getCapBac(),
                                String.valueOf(nhanVien.getMaNV())
                        );
                    }
                }
            } else if (requestCode == 2) {
                if (hoTen != null && !hoTen.isEmpty()) {
                    // Thêm nhân viên
                    NhanVien nhanVien = new NhanVien(Integer.parseInt(maNV), hoTen, gioiTinh, ngSinh, sdt, email, diaChi, cccd, capBac, Integer.parseInt(phongBan));
                    dbHelper.addNhanVien(nhanVien);
                    addItemToManagement(hoTen, phongBan, capBac, maNV);
                }
            }
        }
    }


    private void removeAllItems() {
        // Xóa các item đã thêm trên giao diện
        for (ConstraintLayout item : addedItems) {
            mainLayout.removeView(item);
        }
        lastAddedItem = null; // Reset
        addedItems.clear(); // Xóa danh sách các item đã thêm
    }



}