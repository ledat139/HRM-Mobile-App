package models;

public class Project {
    private int maDA;
    private String tenDA;
    private String ngayBD;
    private String ngayKT;
    private String trangThai;
    private int maPB;

    public Project(int maDA, String tenDA, String ngayBD, String ngayKT, String trangThai, int maPB) {
        this.maDA = maDA;
        this.tenDA = tenDA;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai = trangThai;
        this.maPB = maPB;
    }

    public Project(String tenDA, String ngayBD, String ngayKT, int maPB) {
        this.tenDA = tenDA;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai = "Đang thực hiện";
        this.maPB = maPB;
    }



    public int getMaDA() {
        return maDA;
    }

    public String getTenDA() {
        return tenDA;
    }

    public String getNgayBD() {
        return ngayBD;
    }

    public String getNgayKT() {
        return ngayKT;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public int getMaPB() {
        return maPB;
    }
}