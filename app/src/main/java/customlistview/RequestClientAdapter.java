package customlistview;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tenpm_hrm.R;

import java.util.ArrayList;

import models.Request;

public class RequestClientAdapter extends ArrayAdapter<Request> {
    private Activity context;
    public RequestClientAdapter(Activity context, int layoutID, ArrayList<Request> objects) {
        super(context, layoutID, objects);
        this.context = context;
    }
    @NonNull
    @Override

    public View getView(final int position, @Nullable  View convertView,@NonNull ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.request_item, parent,false);
        }
        // Get item
        Request request = getItem(position);
        // Get view
        TextView tvFullName = (TextView) convertView.findViewById(R.id.item_employee_tv_fullname);
        TextView tvInfor = (TextView) convertView.findViewById(R.id.item_employee_tv_infor);
        ImageView ivManager_pending = (ImageView) convertView.findViewById(R.id.item_employee_iv_manager_pending);
        ImageView ivManager_reject = (ImageView) convertView.findViewById(R.id.item_employee_iv_manager_reject);
        ImageView ivManager_approved = (ImageView) convertView.findViewById(R.id.item_employee_iv_manager_approved);
        LinearLayout llParent = (LinearLayout) convertView.findViewById(R.id.item_employee_ll_parent);
        // Set fullname
        if (request.getInformation()!=null) {
            tvFullName.setText(request.getTopic());
            tvInfor.setText(request.getInformation());
        }
        else tvFullName.setText("");
        // If this is a manager -> show icon manager. Otherwise, show Staff in tvPosition
//        if (request.isApproved() == 1)
//        {
//            ivManager_approved.setVisibility(View.VISIBLE);
//        }
//        else if(request.isApproved() == 0)
//        {
//            ivManager_pending.setVisibility(View.VISIBLE);
//        }
//        else{
//            ivManager_reject.setVisibility(View.VISIBLE);
//        }
        if (request.isApproved() == 1)
        {
            ivManager_approved.setVisibility(View.VISIBLE);
            convertView.setAlpha(0.5f);
            convertView.setEnabled(false);
        }
        else if(request.isApproved() == 0)
        {
            ivManager_pending.setVisibility(View.VISIBLE);
        }
        else if(request.isApproved() == -1){
            ivManager_reject.setVisibility(View.VISIBLE);
            convertView.setAlpha(0.5f);
            convertView.setEnabled(false);
        }



        return convertView;
    }
}

