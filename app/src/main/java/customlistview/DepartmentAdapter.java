package customlistview;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tenpm_hrm.DatabaseHandler;
import com.example.tenpm_hrm.R;

import java.util.List;

import models.Department;

public class DepartmentAdapter extends BaseAdapter {
    private List<Department> departmentList;
    private LayoutInflater inflater;
    DatabaseHandler dbHandler;

    public DepartmentAdapter(Context context, List<Department> departmentList) {
        dbHandler = new DatabaseHandler(context);
        this.departmentList = dbHandler.getAllDepartment();
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return departmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return departmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_department, parent, false);
        }

        Department department = departmentList.get(position);

        ImageView departmentImageView = convertView.findViewById(R.id.departmentImageView);
        TextView departmentNameTextView = convertView.findViewById(R.id.departmentNameTextView);

        String avatarPath = department.getAvatarPath();
        Log.d("DepartmentAdapter", "Avatar path: " + avatarPath);

        if (avatarPath != null && !avatarPath.isEmpty()) {
            try {
                Uri avatarUri = Uri.parse(avatarPath);
                departmentImageView.setImageURI(avatarUri);
            } catch (Exception e) {
                Log.e("DepartmentAdapter", "Failed to set image URI: " + e.getMessage());
                // Set a default placeholder image if URI loading fails
                departmentImageView.setImageResource(R.drawable.arrow);
            }
        } else {
            // Set a default placeholder image when `avatarPath` is null or empty
            departmentImageView.setImageResource(R.drawable.arrow);
            Log.e("DepartmentAdapter", "Avatar path is null or empty for department: " + department.getDepartmentName());
        }

        departmentNameTextView.setText(department.getDepartmentName());

        return convertView;
    }

}