package customlistview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tenpm_hrm.DatabaseHandler;
import com.example.tenpm_hrm.R;
import com.example.tenpm_hrm.UpdateFacility;

import java.util.List;

import models.Facility;

public class FacilityAdapter extends BaseAdapter {
    private List<Facility> facilityList;
    private LayoutInflater inflater;
    private Context context;
    private DatabaseHandler dbHandler;

    TextView tvCSVCName, tvCSVCID, tvCSVCQuantity, tvCSVCStatus;
    ImageView ivCSVCDetail, ivCSVCDelete;
    private RelativeLayout csvcContainerInformation;

    public FacilityAdapter(Context context, List<Facility> facilityList) {
        this.context = context;
        this.facilityList = facilityList;
        this.inflater = LayoutInflater.from(context);
        dbHandler = new DatabaseHandler(context);
    }

    public void updateFacilityList(List<Facility> newFacilityList) {
        this.facilityList = newFacilityList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return facilityList.size();
    }

    @Override
    public Object getItem(int position) {
        return facilityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_facility, parent, false);
        }

        Facility facility = facilityList.get(position);

        csvcContainerInformation = convertView.findViewById(R.id.csvcContainerInformation);
        TextView tvCSVCName = convertView.findViewById(R.id.tvCSVCName);
        TextView tvCSVCID = convertView.findViewById(R.id.tvCSVCID);
        TextView tvCSVCQuantity = convertView.findViewById(R.id.tvCSVCQuantity);
        TextView tvCSVCStatus = convertView.findViewById(R.id.tvCSVCStatus);

        tvCSVCName.setText(facility.getFacilityName());
        tvCSVCID.setText("Mã CSVC: " + String.valueOf(facility.getFacilityID()));
        tvCSVCQuantity.setText("Số lượng: " + String.valueOf(facility.getFacilityQuantity()));

        String facilityStatus = facility.getFacilityStatus();

        tvCSVCStatus.setText(facilityStatus);

        if (facilityStatus.equals("Sử dụng")) {
            tvCSVCStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
            tvCSVCStatus.setBackgroundResource(R.drawable.employee_type_shape);
        } else if (facilityStatus.equals("Hư hỏng")) {
            tvCSVCStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
            tvCSVCStatus.setBackgroundResource(R.drawable.manager_type_shape);
        } else if (facilityStatus.equals("Bảo trì")) {
            tvCSVCStatus.setTextColor(ContextCompat.getColor(context, R.color.blue));
            tvCSVCStatus.setBackgroundResource(R.drawable.blue_type_shape);
        }

        ivCSVCDetail = convertView.findViewById(R.id.ivCSVCDetail);
        ivCSVCDelete = convertView.findViewById(R.id.ivCSVCDelete);

        ivCSVCDetail.setOnClickListener(view -> {
            Intent updateFacilityIntent = new Intent(context, UpdateFacility.class);
            updateFacilityIntent.putExtra("facilityID", facility.getFacilityID());
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).startActivityForResult(updateFacilityIntent, 3);
            }
        });

        ivCSVCDelete.setOnClickListener(view -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có muốn xóa cơ sở vật chất: " + facilityList.get(position).getFacilityID() + " không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            try {
                                dbHandler.deleteFacility(facility);
                                facilityList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Đã xóa cơ sở vật chất", Toast.LENGTH_SHORT).show();
                            }catch (Exception e) {
                                Toast.makeText(context, "Xóa cơ sở vật chất không thành công", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                        .show();
        });
        return convertView;
    }

}