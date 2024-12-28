package customlistview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;


import com.example.tenpm_hrm.CSVCManagement;
import com.example.tenpm_hrm.DatabaseHandler;
import com.example.tenpm_hrm.NewCSVC;
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

    public FacilityAdapter(Context context, List<Facility> facilityList) {
        this.context = context;
        this.facilityList = facilityList;
        this.inflater = LayoutInflater.from(context);
        dbHandler = new DatabaseHandler(context);
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
            tvCSVCStatus.setTextColor(R.color.green);
            tvCSVCStatus.setBackgroundResource(R.drawable.employee_type_shape);
        } else if (facilityStatus.equals("Hư hỏng")) {
            tvCSVCStatus.setBackgroundResource(R.drawable.manager_type_shape);
        }

        ivCSVCDetail = convertView.findViewById(R.id.ivCSVCDetail);
        ivCSVCDelete = convertView.findViewById(R.id.ivCSVCDelete);

        ivCSVCDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateFacilityIntent = new Intent(context, UpdateFacility.class);
                updateFacilityIntent.putExtra("facilityID", facility.getFacilityID());
                context.startActivity(updateFacilityIntent);
            }
        });

        ivCSVCDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteFacility(facility);
            }
        });

        return convertView;
    }

}