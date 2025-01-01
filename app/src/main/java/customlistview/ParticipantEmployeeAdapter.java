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

public class ParticipantEmployeeAdapter extends RecyclerView.Adapter<ParticipantEmployeeAdapter.ViewHolder> {
    private Context context;
    private List<Project_NhanVien> participantList;

    public ParticipantEmployeeAdapter(Context context, List<Project_NhanVien> participantList) {
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

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvParticipantId, tvRole, tvJoiningDate;
        private ParticipantEmployeeAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvParticipantId = itemView.findViewById(R.id.tvParticipantId);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvJoiningDate = itemView.findViewById(R.id.tvJoiningDate);
        }

        public void bind(List<Project_NhanVien> participantList, int position, final ParticipantEmployeeAdapter adapter) {
            tvParticipantId.setText(String.valueOf(participantList.get(position).getMaNV()));
            tvRole.setText(participantList.get(position).getVaiTro());
            tvJoiningDate.setText(participantList.get(position).getNgayTG());
        }
    }
}

