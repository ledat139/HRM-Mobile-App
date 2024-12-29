package customlistview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;

import com.example.tenpm_hrm.ProjectManagement;
import com.example.tenpm_hrm.R;
import com.example.tenpm_hrm.SearchProject;
import com.example.tenpm_hrm.UpdateDepartment;

import java.util.List;

import models.Department;

public class DepartmentAdapter extends BaseAdapter {
    private List<Department> departmentList;
    private LinearLayout departmentContainerItem;
    private LayoutInflater inflater;
    private Context context;

    public DepartmentAdapter(Context context, List<Department> departmentList) {
        this.departmentList = departmentList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
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
            convertView = inflater.inflate(com.example.tenpm_hrm.R.layout.item_department, parent, false);
        }

        Department department = departmentList.get(position);
        Log.d("DepartmentAdapter", "Position: " + position + ", Department: " + department);

        departmentContainerItem = convertView.findViewById(R.id.departmentContainerItem);
        ImageView avatarImageView = convertView.findViewById(R.id.departmentImageView);
        TextView nameTextView = convertView.findViewById(R.id.departmentNameTextView);

        String avatarPath = department.getAvatarPath();
        int avatarResId = convertView.getContext().getResources().getIdentifier(
                avatarPath,
                "drawable",
                convertView.getContext().getPackageName()
        );

        avatarImageView.setImageResource(avatarResId);
        nameTextView.setText(department.getDepartmentName());

        departmentContainerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateDepartment.class);
                intent.putExtra("departmentID", department.getDepartmentId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}