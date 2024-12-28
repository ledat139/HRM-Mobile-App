package models;

public class Project_NhanVien {
    private int maNV;
    private int maDA;
    private String vaiTro;
    private String ngayTG;

    public Project_NhanVien(int maNV, int maDA, String vaiTro, String ngayTG) {
        this.maNV = maNV;
        this.maDA = maDA;
        this.vaiTro = vaiTro;
        this.ngayTG = ngayTG;
    }

    public int getMaNV() {
        return maNV;
    }

    public int getMaDA() {
        return maDA;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public String getNgayTG() {
        return ngayTG;
    }
}
