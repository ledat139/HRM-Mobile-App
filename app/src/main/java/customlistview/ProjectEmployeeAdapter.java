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
import com.example.tenpm_hrm.ProjectDetail;
import com.example.tenpm_hrm.R;
import com.example.tenpm_hrm.UpdateProject;

import java.util.ArrayList;
import java.util.List;

import models.Department;
import models.Facility;
import models.Project;

public class ProjectEmployeeAdapter extends BaseAdapter {
    private List<Project> projectList;
    private LayoutInflater inflater;
    private Context context;
    private DatabaseHandler dbHandler;

    private TextView tvProjectName;
    private TextView tvProjectDepartment;
    private TextView tvProjectStatus;
    private ImageView ivProjectDetail;


    public ProjectEmployeeAdapter(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
        this.inflater = LayoutInflater.from(context);
        dbHandler = new DatabaseHandler(context);
    }

    @Override
    public int getCount() {
        return projectList.size();
    }

    @Override
    public Object getItem(int position) {
        return projectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateProjectList(List<Project> newProjectList) {
        this.projectList = newProjectList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_project_employee, parent, false);
        }

        Project project = projectList.get(position);

        tvProjectName = convertView.findViewById(R.id.tvProjectName);
        tvProjectDepartment = convertView.findViewById(R.id.tvProjectDepartment);
        tvProjectStatus = convertView.findViewById(R.id.tvProjectStatus);
        ivProjectDetail = convertView.findViewById(R.id.ivProjectDetail);

        tvProjectName.setText(project.getTenDA());

        Department department = dbHandler.getDepartment(project.getMaPB());
        tvProjectDepartment.setText("Phòng ban: " + department.getDepartmentName());
        tvProjectStatus.setText(project.getTrangThai());

        if (project.getTrangThai().equals("Hoàn thành")) {
            tvProjectStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
            tvProjectStatus.setBackgroundResource(R.drawable.employee_type_shape);
        } else if (project.getTrangThai().equals("Bị hủy")) {
            tvProjectStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
            tvProjectStatus.setBackgroundResource(R.drawable.manager_type_shape);
        } else if (project.getTrangThai().equals("Đang thực hiện")) {
            tvProjectStatus.setTextColor(ContextCompat.getColor(context, R.color.blue));
            tvProjectStatus.setBackgroundResource(R.drawable.blue_type_shape);
        }

        ivProjectDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProjectDetail.class);
                intent.putExtra("ProjectID", project.getMaDA());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

}
