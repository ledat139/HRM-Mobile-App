package com.example.tenpm_hrm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import customlistview.FacilityAdapter;
import models.Facility;
import models.NhanVien;

public class CSVCManagement extends AppCompatActivity {
    private Button btnAddCSVC;
    private ImageView ivSearchCSVC;
    ListView FacilityContainer;
    private DatabaseHandler dbHandler;
    private FacilityAdapter facilityAdapter;
    private List<Facility> facilityList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.csvc_management_admin);

        btnAddCSVC = findViewById(R.id.btnAddCSVC);
        ivSearchCSVC = findViewById(R.id.ivSearchCSVC);
        FacilityContainer = findViewById(R.id.FacilityContainer);

        dbHandler = new DatabaseHandler(this);
        facilityList = dbHandler.getAllFacility();

        facilityAdapter = new FacilityAdapter(this, facilityList);
        FacilityContainer.setAdapter(facilityAdapter);

        btnAddCSVC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CSVCManagement.this, NewCSVC.class);
                startActivityForResult(intent, 2);
            }
        });

        ivSearchCSVC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CSVCManagement.this, SearchFacilities.class);
                startActivityForResult(intent, 1);
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        dbHandler = new DatabaseHandler(this);
//        facilityList = dbHandler.getAllFacility();
//
//        facilityAdapter = new FacilityAdapter(this, facilityList);
//        FacilityContainer.setAdapter(facilityAdapter);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");
            String quantity = data.getStringExtra("quantity");
            String buyingDate = data.getStringExtra("buyingDate");
            String departmentId = data.getStringExtra("departmentId");
            String status = data.getStringExtra("status");

            // Trả về từ form search
            if (requestCode == 1) {
                // Lấy danh sách nhân viên có thông tin tương ứng
                List<Facility> newFacilityList = dbHandler.searchFacility(id, name, quantity, buyingDate, departmentId, status);
                for (Facility facility : newFacilityList) {
                    Log.d("FacilityList", "MACSVC: " + facility.getFacilityID() +
                            ", TENCSVC: " + facility.getFacilityName() +
                            ", SOLUONG: " + facility.getFacilityQuantity() +
                            ", NGAYMUA: " + facility.getFacilityBuyingDate() +
                            ", TRANGTHAI: " + facility.getFacilityStatus() +
                            ", MAPB: " + facility.getDepartmentID());
                }


                if (newFacilityList.isEmpty()) {
                    Toast.makeText(this, "Không có cơ sở vật chất có thông tin đang tìm kiếm.", Toast.LENGTH_SHORT).show();
                }
                else {
                    facilityAdapter.updateFacilityList(newFacilityList);
                }
            }
        }

        if (requestCode == 2 || requestCode == 3) {
            dbHandler = new DatabaseHandler(this);
            facilityList = dbHandler.getAllFacility();
            facilityAdapter.updateFacilityList(facilityList);
        }
    }
}