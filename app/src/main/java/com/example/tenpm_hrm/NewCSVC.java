package com.example.tenpm_hrm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

import models.Department;
import models.Facility;

public class NewCSVC extends AppCompatActivity {

    Button buttonAddFacility;
    TextInputEditText inputAddFacilityName, inputAddFacilityQuantity, inputAddBuyingDate;
    Spinner inputAddDepartmentID;

    private DatabaseHandler dbHandler;

    private void findViewsByIds() {
        buttonAddFacility = (Button) findViewById(R.id.buttonAddFacility);
        inputAddFacilityName = (TextInputEditText) findViewById(R.id.inputAddFacilityName);
        inputAddFacilityQuantity = (TextInputEditText) findViewById(R.id.inputAddFacilityQuantity);
        inputAddBuyingDate = (TextInputEditText) findViewById(R.id.inputAddBuyingDate);
        inputAddDepartmentID = (Spinner) findViewById(R.id.inputAddDepartmentID);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_csvc_admin);

        findViewsByIds();
        dbHandler = new DatabaseHandler(this);

        inputAddBuyingDate.setOnClickListener(view -> showDatePickerDialog(inputAddBuyingDate));

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
        inputAddDepartmentID.setAdapter(adapter);

        buttonAddFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String facilityName = inputAddFacilityName.getText().toString().trim();
                String facilityQuantityStr = inputAddFacilityQuantity.getText().toString().trim();
                String buyingDate = inputAddBuyingDate.getText().toString().trim();

                int selectedDepartmentPosition = inputAddDepartmentID.getSelectedItemPosition();

                if (facilityName.isEmpty() || facilityQuantityStr.isEmpty() || !facilityQuantityStr.matches("\\d+") || Integer.parseInt(facilityQuantityStr) <= 0 || buyingDate.isEmpty() || selectedDepartmentPosition == 0) {
                    Toast.makeText(NewCSVC.this, "Thông tin cơ sở vật chất không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                Department selectedPhongBan = phongBanList.get(selectedDepartmentPosition - 1);

                try {
                    int facilityQuantity = Integer.parseInt(facilityQuantityStr);
                    Facility facility = new Facility(facilityName, facilityQuantity, buyingDate, selectedPhongBan.getDepartmentId());
                    dbHandler.addFacility(facility);
                }catch (Exception e) {
                    Toast.makeText(NewCSVC.this, "Thêm cơ sở vật chất không thành công", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(NewCSVC.this, "Thêm cơ sở vật chất thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}