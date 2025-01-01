package com.example.tenpm_hrm;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

import models.Account;
import models.NhanVien;

public class AccountAdapter extends ArrayAdapter<Account> {
    private Context context;
    private int resource;
    private List<Account> accounts;
    private DatabaseHandler dbHandler;
    public AccountAdapter(Context context, int resource, List<Account> accounts) {
        super(context, resource, accounts);
        this.context = context;
        this.resource = resource;
        this.accounts = accounts;
        this.dbHandler = new DatabaseHandler(context);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.account_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvUserId = convertView.findViewById(R.id.tvUserId);
        TextView tvUserName = convertView.findViewById(R.id.tvUserName);
        TextView tvEmployeeType = convertView.findViewById(R.id.tvEmployeeType);

        Account account = accounts.get(position);
        NhanVien nv = dbHandler.getEmployeeById(account.getMaNV());

        tvName.setText(nv.getHoTen());
        tvUserId.setText("Mã nhân viên: " + account.getMaNV());
        tvUserName.setText("Tên tài khoản: " + account.getTenTK());
        if(account.getLoaiTK().equals("quản lý")){
            tvEmployeeType.setBackground(ContextCompat.getDrawable(context, R.drawable.manager_type_shape));
        }
        tvEmployeeType.setText(account.getLoaiTK());
        ImageView btnDelete = convertView.findViewById(R.id.deleteAccountIcon);
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa tài khoản")
                    .setMessage("Bạn có chắc chắn muốn xóa tài khoản này không?")
                    .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xóa tài khoản khỏi cơ sở dữ liệu
                            dbHandler.deleteAccount(account);  // Giả sử Account có phương thức getId()

                            // Cập nhật lại danh sách tài khoản
                            accounts.remove(position);  // Xóa tài khoản khỏi danh sách
                            notifyDataSetChanged();  // Cập nhật lại ListView
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();  // Hiển thị dialog
        });

        ImageView btnDetail = convertView.findViewById(R.id.accountDetailIcon);
        btnDetail.setOnClickListener(v -> {
            // Mở màn hình chi tiết tài khoản
            Intent intent = new Intent(context, AccountDetail.class);  // Giả sử bạn có một activity chi tiết tài khoản
            intent.putExtra("accountId", account.getMaTK());  // Truyền ID của tài khoản
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, 1); // Sử dụng requestCode là 1
            } // Mở màn hình chi tiết
        });
        return convertView;
    }
}
