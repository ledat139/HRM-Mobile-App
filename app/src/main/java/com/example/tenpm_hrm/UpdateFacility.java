package com.example.tenpm_hrm;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import models.Facility;

public class UpdateFacility extends AppCompatActivity {

    private TextView tvLabelUpdateFacility;
    private EditText inputUpdateFacilityName, inputUpdateFacilityQuantity, inputUpdateBuyingDate, inputUpdateDepartmentID;
    private Button buttonUpdateFacility;
    private DatabaseHandler dbHandler;
    private int facilityID;
    private Facility facility;
    private Spinner spinnerFacilityStatus;
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
        inputUpdateDepartmentID = findViewById(R.id.inputUpdateDepartmentID);
        tvLabelUpdateFacility = findViewById(R.id.tvLabelUpdateFacility);
        buttonUpdateFacility = findViewById(R.id.buttonUpdateFacility);
        spinnerFacilityStatus = findViewById(R.id.spinnerFacilityStatus);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.facility_status_options, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFacilityStatus.setAdapter(spinnerAdapter);

        facilityID = getIntent().getIntExtra("facilityID", -1);

        facility = dbHandler.getFacility(facilityID);
        updatedStatus = facility.getFacilityStatus();

        inputUpdateFacilityName.setText(facility.getFacilityName());
        inputUpdateFacilityQuantity.setText(String.valueOf(facility.getFacilityQuantity()));
        inputUpdateBuyingDate.setText(facility.getFacilityBuyingDate());
        inputUpdateDepartmentID.setText(String.valueOf(facility.getDepartmentID()));


        tvLabelUpdateFacility.setText("Cập nhật CSVC: " + String.valueOf(facilityID));

        spinnerFacilityStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updatedStatus = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                updatedStatus = updatedStatus;
            }
        });

        buttonUpdateFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = inputUpdateFacilityName.getText().toString();
                int updatedQuantity = Integer.parseInt(inputUpdateFacilityQuantity.getText().toString());
                String updatedBuyingDate = inputUpdateBuyingDate.getText().toString();
                int updatedDepartmentID = Integer.parseInt(inputUpdateDepartmentID.getText().toString());

                Facility updatedFacility = new Facility(facilityID, updatedName, updatedQuantity, updatedBuyingDate, updatedStatus, updatedDepartmentID);
                boolean isUpdated = dbHandler.updateFacility(updatedFacility);
                if (isUpdated) {
                    Toast.makeText(UpdateFacility.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateFacility.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
