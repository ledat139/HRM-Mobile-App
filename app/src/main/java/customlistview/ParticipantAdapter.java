package customlistview;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenpm_hrm.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import models.NhanVien;
import models.Project_NhanVien;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {
    private Context context;
    private List<Project_NhanVien> participantList;

    public ParticipantAdapter(Context context, List<Project_NhanVien> participantList) {
        this.participantList = participantList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_participant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project_NhanVien participant = participantList.get(position);
        holder.bind(participantList, position, this);
    }

    private void showDatePickerDialog(final EditText editText, String date) {
        String[] day_month_year = date.split("/");
        int day = Integer.parseInt(day_month_year[0]);
        int month = Integer.parseInt(day_month_year[1]) - 1;
        int year = Integer.parseInt(day_month_year[2]);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                editText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showUpdateParticipantDialog(Project_NhanVien projectNhanVien) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.update_participant, null);
        builder.setView(dialogView);

        Spinner inputParticipantID = dialogView.findViewById(R.id.inputParticipantID);
        TextInputEditText inputParticipantRole = dialogView.findViewById(R.id.inputParticipantRole);
        TextInputEditText inputParticipantJoiningDate = dialogView.findViewById(R.id.inputParticipantJoiningDate);

        inputParticipantRole.setText(projectNhanVien.getVaiTro());
        inputParticipantJoiningDate.setText(projectNhanVien.getNgayTG());

        inputParticipantJoiningDate.setOnClickListener(view -> showDatePickerDialog(inputParticipantJoiningDate, projectNhanVien.getNgayTG()));

        ArrayList<NhanVien> nhanVienList = new ArrayList<>();
        nhanVienList.add(new NhanVien(1, "Trần Thị B", "Nữ", "1992-08-20", "0907654321", "tranthib@example.com", "Hà Nội", "123456789", "Nhân viên", 1));
        nhanVienList.add(new NhanVien(2, "Phạm Minh C", "Nam", "1988-11-10", "0912345678", "phamminhc@example.com", "Hồ Chí Minh", "987654321", "Quản lý", 1));
        nhanVienList.add(new NhanVien(3, "Lê Thị D", "Nữ", "1995-02-25", "0934567890", "lethid@example.com", "Đà Nẵng", "112233445", "Trưởng phòng", 1));

        ArrayList<String> tenNhanVienList = new ArrayList<>();
        for (NhanVien nhanVien : nhanVienList) {
            tenNhanVienList.add(nhanVien.getHoTen() + " (ID: " + nhanVien.getMaNV() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, tenNhanVienList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputParticipantID.setAdapter(adapter);

        int selectedPosition = 0;
        for (int i = 0; i < nhanVienList.size(); i++) {
            if (nhanVienList.get(i).getMaNV() == projectNhanVien.getMaNV()) {
                selectedPosition = i;
                break;
            }
        }
        inputParticipantID.setSelection(selectedPosition);

        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            int selectedPos = inputParticipantID.getSelectedItemPosition();
            NhanVien selectedNhanVien = nhanVienList.get(selectedPos);
            String role = inputParticipantRole.getText().toString().trim();
            String joiningDate = inputParticipantJoiningDate.getText().toString().trim();

            if (role.isEmpty() || joiningDate.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            Project_NhanVien participant = new Project_NhanVien(selectedNhanVien.getMaNV(), role, joiningDate);
            participantList.set(selectedPos, participant);
            notifyItemChanged(selectedPos);

            Toast.makeText(context, "Đã cập nhật người tham gia", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvParticipantId, tvRole, tvJoiningDate;
        private ParticipantAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvParticipantId = itemView.findViewById(R.id.tvParticipantId);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvJoiningDate = itemView.findViewById(R.id.tvJoiningDate);
        }

        public void bind(List<Project_NhanVien> participantList, int position, final ParticipantAdapter adapter) {
            tvParticipantId.setText(String.valueOf(participantList.get(position).getMaNV()));
            tvRole.setText(participantList.get(position).getVaiTro());
            tvJoiningDate.setText(participantList.get(position).getNgayTG());

            itemView.setOnClickListener(view -> {
                adapter.showUpdateParticipantDialog(participantList.get(position));
                adapter.notifyItemChanged(position);
            });

            itemView.setOnLongClickListener(view -> {
                new AlertDialog.Builder(adapter.context)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có muốn xóa nhân viên tham gia: " + participantList.get(position).getMaNV() + " không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            participantList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, participantList.size());
                            Toast.makeText(adapter.context, "Đã xóa người tham gia", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                        .show();
                return true;
            });
        }
    }
}

