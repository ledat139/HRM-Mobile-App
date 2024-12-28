package models;

public class AccountItem {
    private String employeeName;
    private String employeeId;
    private String accountName;
    private String managerType;

    public AccountItem(String employeeName, String employeeId, String accountName, String managerType) {
        this.employeeName = employeeName;
        this.employeeId = employeeId;
        this.accountName = accountName;
        this.managerType = managerType;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getManagerType() {
        return managerType;
    }

    public void setManagerType(String managerType) {
        this.managerType = managerType;
    }
}