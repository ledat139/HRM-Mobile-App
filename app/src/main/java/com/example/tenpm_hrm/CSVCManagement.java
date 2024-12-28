package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import customlistview.FacilityAdapter;
import models.Facility;

public class CSVCManagement extends AppCompatActivity {
    private Button btnAddCSVC;
    ListView FacilityContainer;
    private DatabaseHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.csvc_management_admin);

        btnAddCSVC = findViewById(R.id.btnAddCSVC);
        FacilityContainer = findViewById(R.id.FacilityContainer);

        List<Facility> facilityList = new ArrayList<>();

        dbHandler = new DatabaseHandler(this);
        facilityList = dbHandler.getAllFacility();

        FacilityAdapter adapter = new FacilityAdapter(this, facilityList);
        FacilityContainer.setAdapter(adapter);

        btnAddCSVC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRequestIntent = new Intent(CSVCManagement.this, NewCSVC.class);
                startActivity(newRequestIntent);
            }
        });

    }
}