package models;

public class Department {
    private String departmentId;
    private String departmentName;
    private String managerId;
    private String managerName;
    private int employeeCount;
    private int avatarResId;

    public Department(String departmentId, String departmentName, String managerId, String managerName, int employeeCount, int avatarResId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.managerId = managerId;
        this.managerName = managerName;
        this.employeeCount = employeeCount;
        this.avatarResId = avatarResId;
    }

    // Getter methods
    public String getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}