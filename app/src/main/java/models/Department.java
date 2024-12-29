package models;

public class Department {
    private int departmentId;
    private String departmentName;
    private String establishmentDate;
    private int managerId;
    private String managerAppointmentDate;
    private String avatarPath;
    private int employeeCount;


    public Department(int departmentId, String departmentName, String establishmentDate, int managerId, String managerAppointmentDate, String avatarPath) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.establishmentDate = establishmentDate;
        this.managerId = managerId;
        this.managerAppointmentDate = managerAppointmentDate;
        this.avatarPath = avatarPath;
    }

    public Department(String departmentName, String establishmentDate, int managerId, String managerAppointmentDate, String avatarPath) {
        this.departmentName = departmentName;
        this.establishmentDate = establishmentDate;
        this.managerId = managerId;
        this.managerAppointmentDate = managerAppointmentDate;
        this.avatarPath = avatarPath;
    }



    public int getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getEstablishmentDate() {
        return establishmentDate;
    }

    public int getManagerId() {
        return managerId;
    }

    public String getManagerAppointmentDate() {
        return managerAppointmentDate;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }
}