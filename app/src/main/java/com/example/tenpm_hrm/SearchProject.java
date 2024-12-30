package com.example.tenpm_hrm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

import models.Department;

public class SearchProject extends AppCompatActivity {
    private Button buttonCancelSearchProject, buttonSearchProject;
    private EditText inputSearchProjectID, inputSearchProjectName, inputSearchStartingDate, inputSearchEndingDate;
    private Spinner inputSearchProjectDepartmentID, inputSearchProjectStatus;
    private DatabaseHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_project);
        findViews();

        dbHandler = new DatabaseHandler(this);

        String[] projectStatusOptions = getResources().getStringArray(R.array.project_status_options);

        ArrayList<String> statusList = new ArrayList<>();
        statusList.add("Chọn trạng thái");
        for (String status : projectStatusOptions) {
            statusList.add(status);
        }


        ArrayList<Department> phongBanList = new ArrayList<>();
        phongBanList.add(new Department(1, "Phòng Kỹ Thuật", "2020-01-01", 1, "Nguyễn Văn A", "path_to_it_avatar"));
        phongBanList.add(new Department(2, "Phòng Kinh Doanh", "2019-05-15", 2, "Trần Thị B", "path_to_sale_avatar"));
        phongBanList.add(new Department(3, "Phòng Nhân Sự", "2018-03-20", 3, "Lê Thị C", "path_to_hr_avatar"));
        phongBanList.add(new Department(4, "Phòng Marketing", "2021-06-10", 4, "Phạm Minh D", "path_to_marketing_avatar"));


        ArrayList<String> tenPhongBanList = new ArrayList<>();
        tenPhongBanList.add("Chọn phòng ban");
        for (Department phongBan : phongBanList) {
            tenPhongBanList.add(phongBan.getDepartmentName() + " (ID: " + phongBan.getDepartmentId() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenPhongBanList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSearchProjectDepartmentID.setAdapter(adapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSearchProjectStatus.setAdapter(statusAdapter);

        inputSearchStartingDate.setOnClickListener(view -> showDatePickerDialog(inputSearchStartingDate));
        inputSearchEndingDate.setOnClickListener(view -> showDatePickerDialog(inputSearchEndingDate));

        buttonCancelSearchProject.setOnClickListener(v -> finish());

        buttonSearchProject.setOnClickListener(v -> {
            String id = inputSearchProjectID.getText().toString().trim();
            String name = inputSearchProjectName.getText().toString().trim();
            String startingDate = inputSearchStartingDate.getText().toString().trim();
            String endingDate = inputSearchEndingDate.getText().toString().trim();
            String departmentId = "";
            String status = "";

            int selectedDepartmentPosition = inputSearchProjectDepartmentID.getSelectedItemPosition();
            if (selectedDepartmentPosition > 0) {
                Department department = phongBanList.get(selectedDepartmentPosition - 1);
                departmentId = String.valueOf(department.getDepartmentId());
            }

            int selectedStatusPosition = inputSearchProjectStatus.getSelectedItemPosition();
            if (selectedStatusPosition > 0) {
                status = projectStatusOptions[selectedStatusPosition - 1];
            }

            if (id.isEmpty() && name.isEmpty() && startingDate.isEmpty() && endingDate.isEmpty() && departmentId.isEmpty() && status.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập ít nhất một trường để tìm kiếm!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo intent và truyền thông tin tìm kiếm
            Intent resultIntent = new Intent();
            resultIntent.putExtra("id", id);
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("startingDate", startingDate);
            resultIntent.putExtra("endingDate", endingDate);
            resultIntent.putExtra("departmentId", departmentId);
            resultIntent.putExtra("status", status);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                editText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void findViews() {
        buttonCancelSearchProject = findViewById(R.id.buttonCancelSearchProject);
        buttonSearchProject = findViewById(R.id.buttonSearchProject);
        inputSearchProjectID = findViewById(R.id.inputSearchProjectID);
        inputSearchProjectName = findViewById(R.id.inputSearchProjectName);
        inputSearchStartingDate = findViewById(R.id.inputSearchStartingDate);
        inputSearchEndingDate = findViewById(R.id.inputSearchEndingDate);
        inputSearchProjectDepartmentID = findViewById(R.id.inputSearchProjectDepartmentID);
        inputSearchProjectStatus = findViewById(R.id.inputSearchProjectStatus);
    }

}
