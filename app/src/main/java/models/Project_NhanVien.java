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

    public Project_NhanVien(int maNV, String vaiTro, String ngayTG) {
        this.maNV = maNV;
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

    public void setMaDA(int maDA) {
        this.maDA = maDA;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public void setNgayTG(String ngayTG) {
        this.ngayTG = ngayTG;
    }
}
