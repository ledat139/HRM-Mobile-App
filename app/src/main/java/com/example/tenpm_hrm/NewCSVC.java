package com.example.tenpm_hrm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import models.Department;
import models.Facility;

public class NewCSVC extends AppCompatActivity {

    Button buttonAddFacility;
    TextInputEditText inputAddFacilityName, inputAddFacilityQuantity, inputAddBuyingDate, inputAddDepartmentID;

    private DatabaseHandler dbHandler;

    private void findViewsByIds() {
        buttonAddFacility = (Button) findViewById(R.id.buttonAddFacility);
        inputAddFacilityName = (TextInputEditText) findViewById(R.id.inputAddFacilityName);
        inputAddFacilityQuantity = (TextInputEditText) findViewById(R.id.inputAddFacilityQuantity);
        inputAddBuyingDate = (TextInputEditText) findViewById(R.id.inputAddBuyingDate);
        inputAddDepartmentID = (TextInputEditText) findViewById(R.id.inputAddDepartmentID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_csvc_admin);

        findViewsByIds();
        dbHandler = new DatabaseHandler(this);

        buttonAddFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String facilityName = inputAddFacilityName.getText().toString();
                int facilityQuantity = Integer.parseInt(inputAddFacilityQuantity.getText().toString());
                String buyingDate = inputAddBuyingDate.getText().toString();
                int departmentID = Integer.parseInt(inputAddDepartmentID.getText().toString());

                Facility facility = new Facility(facilityName, facilityQuantity, buyingDate, departmentID);
                dbHandler.addFacility(facility);
            }
        });
    }
}