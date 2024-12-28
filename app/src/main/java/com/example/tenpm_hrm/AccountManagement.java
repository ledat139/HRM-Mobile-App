package com.example.tenpm_hrm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import models.AccountItem;

public class AccountManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.account_management_admin);

        Button addAccountBtn = findViewById(R.id.addAccountBtn);
        addAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountManagement.this, NewAccount.class);
                startActivity(intent);
            }
        });

        LinearLayout accountContainer = findViewById(R.id.linearLayout);
        addAccountItems(accountContainer);
    }

    private void addAccountItems(LinearLayout container) {
        LayoutInflater inflater = LayoutInflater.from(this);
        AccountItem[] accounts = {
                new AccountItem("Nguyen Van A", "01", "accountA", "Manager"),
                new AccountItem("Nguyen Van B", "02", "accountB", "Manager"),
                new AccountItem("Nguyen Van C", "03", "accountC", "Manager")
        };

        for (AccountItem account : accounts) {
            View accountItemView = inflater.inflate(R.layout.item_account, container, false);
            TextView employeeNameTextView = accountItemView.findViewById(R.id.tv_employee_name);
            TextView employeeIdTextView = accountItemView.findViewById(R.id.tv_employee_id);
            TextView accountNameTextView = accountItemView.findViewById(R.id.tv_account_name);
            TextView managerTypeTextView = accountItemView.findViewById(R.id.tv_manager_type);
            ImageView deleteAccountImageView = accountItemView.findViewById(R.id.iv_delete_account);

            employeeNameTextView.setText(account.getEmployeeName());
            employeeIdTextView.setText("Mã nhân viên: " + account.getEmployeeId());
            accountNameTextView.setText("Tên tài khoản: " + account.getAccountName());
            managerTypeTextView.setText(account.getManagerType());

            deleteAccountImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(AccountManagement.this)
                            .setTitle("Xóa tài khoản")
                            .setMessage("Bạn có muốn xóa tài khoản này không?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    container.removeView(accountItemView);
                                }
                            })
                            .setNegativeButton("Không", null)
                            .show();
                }
            });

            container.addView(accountItemView);
        }
    }
}