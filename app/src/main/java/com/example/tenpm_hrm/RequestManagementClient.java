package com.example.tenpm_hrm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import customlistview.RequestAdminAdapter;
import customlistview.RequestClientAdapter;
import models.NhanVien; // Đảm bảo bạn đã import lớp NhanVien
import models.Request;

public class RequestManagementClient extends AppCompatActivity {
    private Button btn_new_request;
    private Button btn_pending_requests;
    private ArrayList<Request> requests;
    private ArrayList<Request> pendingRequests;
    private RequestClientAdapter adapter;
    private DatabaseHandler dbHandler;
    private ListView lvRQ;
    private NhanVien nhanVien; // Khai báo biến để lưu đối tượng NhanVien

    private boolean pendingMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.request_management_client);

        btn_new_request = findViewById(R.id.btn_new_request);
        btn_pending_requests = findViewById(R.id.btn_pending_requests);
        lvRQ = findViewById(R.id.lvRQ);

        requests = new ArrayList<>();

        // Lấy Intent và extra
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nhanVien")) {
            nhanVien = intent.getParcelableExtra("nhanVien"); // Lấy đối tượng NhanVien từ Intent
            if (nhanVien != null) {
                // Sử dụng nhanVien ở đây nếu cần
                // Ví dụ: Log.d("RequestManagementClient", "Received NhanVien: " + nhanVien.toString());
            } else {
                Log.e("RequestManagementClient", "NhanVien is null");
            }
        } else {
            Log.e("RequestManagementClient", "Intent is null or does not have extra 'nhanVien'");
        }

        // Khởi tạo DatabaseHandler
        dbHandler = new DatabaseHandler(this);


        // Set up item click listener
        lvRQ.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request selectedRequest = requests.get(position);
                int maYC = selectedRequest.getId();
                showConfirmationDialog(position, maYC);
            }
        });

        btn_new_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRequestIntent = new Intent(RequestManagementClient.this, NewRequest.class);
                newRequestIntent.putExtra("nhanVien", nhanVien); // Gửi nhanVien tới NewRequest nếu cần
                startActivity(newRequestIntent);
            }
        });

        btn_pending_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pendingMode) {
                    pendingMode = true;  // Set to pending mode
                    // Get only the pending requests for the current employee (based on NVId)
                    requests = dbHandler.getRequestsByNVId(nhanVien.getMaNV());

                    // Filter the pending requests if needed
                    pendingRequests = new ArrayList<>();
                    for (Request request : requests) {
                        if (request.isApproved() == 0) { // Assuming 0 means pending
                            pendingRequests.add(request);
                        }
                    }

                    // If there are no pending requests, you can show a message or handle it accordingly
                    if (pendingRequests.isEmpty()) {
                        Toast.makeText(RequestManagementClient.this, "No pending requests.", Toast.LENGTH_SHORT).show();
                    }

                    btn_pending_requests.setText("Tất Cả Yêu Cầu");
                    // Set the adapter with pending requests
                    adapter = new RequestClientAdapter(RequestManagementClient.this, R.layout.request_item, pendingRequests);
                    lvRQ.setAdapter(adapter);
                } else {
                    // Switch back to normal mode (all requests)
                    pendingMode = false;
                    // Get all the requests again (if you want all requests for the employee)
                    requests = dbHandler.getRequestsByNVId(nhanVien.getMaNV());

                    // If there are no requests, you can show a message or handle it accordingly
                    if (requests.isEmpty()) {
                        Toast.makeText(RequestManagementClient.this, "No requests available.", Toast.LENGTH_SHORT).show();
                    }

                    btn_pending_requests.setText("Yêu Cầu Chờ Duỵệt");
                    // Set the adapter with all requests
                    adapter = new RequestClientAdapter(RequestManagementClient.this, R.layout.request_item, requests);
                    lvRQ.setAdapter(adapter);
                }
            }
        });
    }


        private void loadRequests() {
        requests.clear();
        requests.addAll(dbHandler.getRequestsByNVId(nhanVien.getMaNV()));
        adapter = new RequestClientAdapter(this, R.layout.request_item, requests);
        lvRQ.setAdapter(adapter);
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
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean isDeleted = dbHandler.deleteRequest(maYC);
                        if (isDeleted) {
                            Toast.makeText(RequestManagementClient.this, "Request deleted successfully.", Toast.LENGTH_SHORT).show();
                            loadRequests(); // Refresh the list of requests
                        } else {
                            Toast.makeText(RequestManagementClient.this, "Failed to delete request.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Chỉnh sửa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Get the current request to edit
                        Request requestToEdit = requests.get(position);

                        // Create a dialog for editing
                        final EditText inputTopic = new EditText(RequestManagementClient.this);
                        final EditText inputInformation = new EditText(RequestManagementClient.this);

                        inputTopic.setText(requestToEdit.getTopic()); // Pre-fill with current data
                        inputInformation.setText(requestToEdit.getInformation());

                        // Create a layout to hold the input fields
                        LinearLayout layout = new LinearLayout(RequestManagementClient.this);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.addView(inputTopic);
                        layout.addView(inputInformation);

                        // Show the dialog with input fields
                        AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(RequestManagementClient.this);
                        editDialogBuilder.setTitle("Chỉnh sửa yêu cầu")
                                .setView(layout)
                                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String newTopic = inputTopic.getText().toString();
                                        String newInformation = inputInformation.getText().toString();

                                        // Call the method to edit the request
                                        boolean isEdited = dbHandler.editRequest(maYC, newTopic, newInformation);

                                        if (isEdited) {
                                            Toast.makeText(RequestManagementClient.this, "Request edited successfully.", Toast.LENGTH_SHORT).show();
                                            loadRequests(); // Refresh the list of requests
                                        } else {
                                            Toast.makeText(RequestManagementClient.this, "Failed to edit request.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Hủy", null); // Cancel button
                        editDialogBuilder.create().show();
                        loadRequests(); // Refresh the list of requests
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
