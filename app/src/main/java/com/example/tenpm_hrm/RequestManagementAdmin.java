package com.example.tenpm_hrm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import customlistview.RequestAdminAdapter;
import models.NhanVien;
import models.Request;
public class RequestManagementAdmin extends AppCompatActivity {
    private ListView listView;
    private RequestAdminAdapter adapter;
    private ArrayList<Request> requests;
    private DatabaseHandler dbHandler;

    private Button approveAllBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_management_admin);

        listView = findViewById(R.id.listView);
        requests = new ArrayList<>();
        dbHandler = new DatabaseHandler(this);

        approveAllBtn = findViewById(R.id.btn_approve_all);


        // Load requests initially
        loadRequests();

        // Set up item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request selectedRequest = requests.get(position);
                int maYC = selectedRequest.getId();
                if(dbHandler.getRequestsByRqId(maYC).isApproved() == 0) {
                    showConfirmationDialog(position, maYC);
                }
                else{
                    Toast.makeText(RequestManagementAdmin.this, "Request handled.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        approveAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbHandler.approveAllRequests()){
                    Toast.makeText(RequestManagementAdmin.this, "Request approved successfully.", Toast.LENGTH_SHORT).show();
                    loadRequests(); // Refresh the list of requests
                }
                else{
                    Toast.makeText(RequestManagementAdmin.this, "Failed to approve request.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void loadRequests() {
        requests.clear();
        requests.addAll(dbHandler.getAllRequests());
        adapter = new RequestAdminAdapter(this, R.layout.request_item, requests);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRequests(); // Refresh requests when the activity resumes
    }

    private void showConfirmationDialog(int position, int maYC) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận hành động")
                .setMessage("Bạn có muốn xử lý yêu cầu: " + requests.get(position).getInformation() + "?")
                .setPositiveButton("Xét duyệt", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean isApproved = dbHandler.approveRequest(maYC);
                        if (isApproved) {
                            Toast.makeText(RequestManagementAdmin.this, "Request approved successfully.", Toast.LENGTH_SHORT).show();
                            loadRequests();
                        } else {
                            Toast.makeText(RequestManagementAdmin.this, "Failed to approve request.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean isRejected = dbHandler.rejectRequest(maYC);
                        if (isRejected) {
                            Toast.makeText(RequestManagementAdmin.this, "Request rejected successfully.", Toast.LENGTH_SHORT).show();
                            loadRequests();
                        } else {
                            Toast.makeText(RequestManagementAdmin.this, "Failed to reject request.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
