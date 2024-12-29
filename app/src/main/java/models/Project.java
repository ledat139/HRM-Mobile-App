package models;

public class Project {
    private int maDA;
    private String tenDA;
    private String ngayBD;
    private String ngayKT;
    private String trangThai;
    private String moTa;
    private int maPB;

    public Project(int maDA, String tenDA, String ngayBD, String ngayKT, String trangThai, String moTa, int maPB) {
        this.maDA = maDA;
        this.tenDA = tenDA;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai = trangThai;
        this.moTa = moTa;
        this.maPB = maPB;
    }

    public Project(String tenDA, String ngayBD, String ngayKT, String moTa, int maPB) {
        this.tenDA = tenDA;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai = "Đang thực hiện";
        this.moTa = moTa;
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

    public String getMoTa() { return moTa;}
}