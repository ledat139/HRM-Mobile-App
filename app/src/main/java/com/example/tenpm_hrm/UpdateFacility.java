package com.example.tenpm_hrm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.Department;
import models.Facility;

public class UpdateFacility extends AppCompatActivity {

    private TextView tvLabelUpdateFacility;
    private EditText inputUpdateFacilityName, inputUpdateFacilityQuantity, inputUpdateBuyingDate;
    private Button buttonUpdateFacility;
    private DatabaseHandler dbHandler;
    private int facilityID;
    private Facility facility;
    private Spinner spinnerFacilityStatus, inputUpdateDepartmentID;
    private String updatedStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_facility);

        dbHandler = new DatabaseHandler(this);

        inputUpdateFacilityName = findViewById(R.id.inputUpdateFacilityName);
        inputUpdateFacilityQuantity = findViewById(R.id.inputUpdateFacilityQuantity);
        inputUpdateBuyingDate = findViewById(R.id.inputUpdateBuyingDate);
//        inputUpdateDepartmentID = findViewById(R.id.inputUpdateDepartmentID);
        inputUpdateDepartmentID = findViewById(R.id.inputUpdateDepartmentID);
        tvLabelUpdateFacility = findViewById(R.id.tvLabelUpdateFacility);
        buttonUpdateFacility = findViewById(R.id.buttonUpdateFacility);
        spinnerFacilityStatus = findViewById(R.id.spinnerFacilityStatus);

        facilityID = getIntent().getIntExtra("facilityID", -1);
        facility = dbHandler.getFacility(facilityID);

        List<Department> phongBanList = dbHandler.getAllDepartment();

        ArrayList<String> tenPhongBanList = new ArrayList<>();
        for (Department phongBan : phongBanList) {
            tenPhongBanList.add(phongBan.getDepartmentName() + " (ID: " + phongBan.getDepartmentId() + ")");
        }

        int selectedStatusPosition = 0;
        int selectedDepartmentPosition = facility.getDepartmentID() - 1;

        for (int i = 0; i < getResources().getStringArray(R.array.facility_status_options).length; i++) {
            if (getResources().getStringArray(R.array.facility_status_options)[i].equals(facility.getFacilityStatus())) {
                selectedStatusPosition = i;
                break;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenPhongBanList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputUpdateDepartmentID.setAdapter(adapter);
        inputUpdateDepartmentID.setSelection(selectedDepartmentPosition);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.facility_status_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFacilityStatus.setAdapter(spinnerAdapter);
        spinnerFacilityStatus.setSelection(selectedStatusPosition);


        inputUpdateFacilityName.setText(facility.getFacilityName());
        inputUpdateFacilityQuantity.setText(String.valueOf(facility.getFacilityQuantity()));
        inputUpdateBuyingDate.setText(facility.getFacilityBuyingDate());

        inputUpdateBuyingDate.setOnClickListener(view -> showDatePickerDialog(inputUpdateBuyingDate, facility.getFacilityBuyingDate()));


        tvLabelUpdateFacility.setText("Cập nhật CSVC: " + String.valueOf(facilityID));


        buttonUpdateFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = inputUpdateFacilityName.getText().toString();
                String facilityQuantityStr = inputUpdateFacilityQuantity.getText().toString();
                String updatedBuyingDate = inputUpdateBuyingDate.getText().toString();

                int selectedDepartmentPosition = inputUpdateDepartmentID.getSelectedItemPosition();
                Department selectedPhongBan = phongBanList.get(selectedDepartmentPosition);

                int selectedStatusPosition = spinnerFacilityStatus.getSelectedItemPosition();
                String facilityStatus = getResources().getStringArray(R.array.facility_status_options)[selectedStatusPosition];

                if (updatedName.isEmpty() || facilityQuantityStr.isEmpty() || !facilityQuantityStr.matches("\\d+") || Integer.parseInt(facilityQuantityStr) <= 0 || updatedBuyingDate.isEmpty() || selectedPhongBan == null || facilityStatus.isEmpty()) {
                    Toast.makeText(UpdateFacility.this, "Thông tin cơ sở vật chất không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int facilityQuantity = Integer.parseInt(facilityQuantityStr);
                    Facility updatedFacility = new Facility(facilityID, updatedName, facilityQuantity, updatedBuyingDate, facilityStatus, selectedPhongBan.getDepartmentId());
                    dbHandler.updateFacility(updatedFacility);
                }catch (Exception e) {
                    Toast.makeText(UpdateFacility.this, "Cập nhật cơ sở vật chất không thành công", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(UpdateFacility.this, "Cập nhật cơ sở vật chất thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showDatePickerDialog(final EditText editText, String date) {
        String[] day_month_year = date.split("/");
        int day = Integer.parseInt(day_month_year[0]);
        int month = Integer.parseInt(day_month_year[1]) - 1;
        int year = Integer.parseInt(day_month_year[2]);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                editText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

}
