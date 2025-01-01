package com.example.tenpm_hrm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import models.Account;
import models.AccountItem;
import models.NhanVien;

public class AccountManagement extends AppCompatActivity {
    private DatabaseHandler dbHandler;
    private AccountAdapter adapter;
    private ImageView findAccountIcon;
    private EditText etSearchEmployeeName;
    ListView lvAccount;
    private List<Account> filteredAccounts;
    List<Account> accountList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.account_management_admin);
        etSearchEmployeeName = findViewById(R.id.etSearchEmployeeName);
        findAccountIcon = findViewById(R.id.findAccountIcon);

        dbHandler = new DatabaseHandler(this);

        lvAccount = findViewById(R.id.lvAccount);
        changeData();
        filteredAccounts = new ArrayList<>(accountList);
        Button addAccountBtn = findViewById(R.id.addAccountBtn);
        addAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountManagement.this, NewAccount.class);
                startActivityForResult(intent, 1);

            }
        });
        // Xử lý nút tìm kiếm
        findAccountIcon.setOnClickListener(v -> searchAccounts());

        // Xử lý sự kiện nhấn Enter trên bàn phím
        etSearchEmployeeName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                searchAccounts();
                return true;
            }
            return false;
        });
    }
    private void searchAccounts() {
        String query = etSearchEmployeeName.getText().toString().trim().toLowerCase();

        // Lọc danh sách tài khoản theo tên nhân viên
        filteredAccounts.clear();
        for (Account account : accountList) {
            NhanVien nv = dbHandler.getEmployeeById(account.getMaNV());

            if (nv.getHoTen().toLowerCase().contains(query)) {
                filteredAccounts.add(account);
            }
        }

        // Cập nhật lại ListView
        adapter = new AccountAdapter(this, R.layout.account_item, filteredAccounts);
        lvAccount.setAdapter(adapter);
        // Hiển thị thông báo nếu không tìm thấy kết quả
        if (filteredAccounts.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy tài khoản phù hợp!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            changeData();
        }
    }

    public void changeData(){
            accountList = dbHandler.getAllAccounts();
            adapter = new AccountAdapter(this, R.layout.account_item, accountList);
            lvAccount.setAdapter(adapter);

    }
}




//    private void addAccountItems(LinearLayout container) {
//        LayoutInflater inflater = LayoutInflater.from(this);
//        AccountItem[] accounts = {
//                new AccountItem("Nguyen Van A", "01", "accountA", "Manager"),
//                new AccountItem("Nguyen Van B", "02", "accountB", "Manager"),
//                new AccountItem("Nguyen Van C", "03", "accountC", "Manager")
//        };
//
//        for (AccountItem account : accounts) {
//            View accountItemView = inflater.inflate(R.layout.item_account, container, false);
//            TextView employeeNameTextView = accountItemView.findViewById(R.id.tv_employee_name);
//            TextView employeeIdTextView = accountItemView.findViewById(R.id.tv_employee_id);
//            TextView accountNameTextView = accountItemView.findViewById(R.id.tv_account_name);
//            TextView managerTypeTextView = accountItemView.findViewById(R.id.tv_manager_type);
//            ImageView deleteAccountImageView = accountItemView.findViewById(R.id.iv_delete_account);
//
//            employeeNameTextView.setText(account.getEmployeeName());
//            employeeIdTextView.setText("Mã nhân viên: " + account.getEmployeeId());
//            accountNameTextView.setText("Tên tài khoản: " + account.getAccountName());
//            managerTypeTextView.setText(account.getManagerType());
//
//            deleteAccountImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new AlertDialog.Builder(AccountManagement.this)
//                            .setTitle("Xóa tài khoản")
//                            .setMessage("Bạn có muốn xóa tài khoản này không?")
//                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    container.removeView(accountItemView);
//                                }
//                            })
//                            .setNegativeButton("Không", null)
//                            .show();
//                }
//            });
//
//            container.addView(accountItemView);
//        }
//    }
//}