package models;

public class Attendance {
    private int id;
    private int employeeId;
    private String workDate;
    private String checkinTime;
    private String checkoutTime;
    private String status;

    public Attendance(){
        this.id = 0;
        this.employeeId = 0;
        this.workDate = "";
        this.checkinTime = "";
        this.checkoutTime = "";
        this.status = "";
    }
    public Attendance(int id, int employeeId, String workDate, String checkinTime, String checkoutTime, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.workDate = workDate;
        this.checkinTime = checkinTime;
        this.checkoutTime = checkoutTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
