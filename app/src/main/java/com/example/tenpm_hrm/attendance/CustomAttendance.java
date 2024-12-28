package com.example.tenpm_hrm.attendance;

public class CustomAttendance {
    private int employeeId;
    private String name;
    private String phone;
    private String department;
    private String status;

    public CustomAttendance(int employeeId, String name, String phone, String department, String status) {
        this.employeeId = employeeId;
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.status = status;
    }

    public CustomAttendance() {
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public String getStatus() {
        return status;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
