package models;

public class Facility {
    int facilityID;
    String facilityName;
    int facilityQuantity;
    String facilityStatus;
    String facilityBuyingDate;
    int departmentID;

    public Facility(int facilityID, String facilityName, int facilityQuantity, String facilityBuyingDate, String facilityStatus, int departmentID) {
        this.facilityID = facilityID;
        this.facilityName = facilityName;
        this.facilityQuantity = facilityQuantity;
        this.facilityBuyingDate = facilityBuyingDate;
        this.facilityStatus = facilityStatus;
        this.departmentID = departmentID;
    }

    public Facility(String facilityName, int facilityQuantity, String facilityBuyingDate, int departmentID) {
        this.facilityName = facilityName;
        this.facilityQuantity = facilityQuantity;
        this.facilityStatus = "Sử dụng";
        this.facilityBuyingDate = facilityBuyingDate;
        this.departmentID = departmentID;
    }

    public int getFacilityID() {
        return facilityID;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public int getFacilityQuantity() {
        return facilityQuantity;
    }

    public String getFacilityStatus() {
        return facilityStatus;
    }

    public String getFacilityBuyingDate() {
        return facilityBuyingDate;
    }

    public int getDepartmentID() {
        return departmentID;
    }
}
