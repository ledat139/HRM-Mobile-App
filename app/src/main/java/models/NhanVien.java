package models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NhanVien implements Parcelable {

    private int maNV;
    private String hoTen;
    private String gioiTinh;
    private String ngSinh;
    private String sdt;
    private String email;
    private String diaChi;
    private String cccd;
    private String capBac;
    private int maPB;

    public NhanVien(int maNV, String hoTen, String gioiTinh, String ngSinh, String sdt, String email, String diaChi, String cccd, String capBac, int maPB) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngSinh = ngSinh;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
        this.cccd = cccd;
        this.capBac = capBac;
        this.maPB = maPB;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(maNV);
        dest.writeString(hoTen);
        dest.writeString(gioiTinh);
        dest.writeString(ngSinh);
        dest.writeString(sdt);
        dest.writeString(email);
        dest.writeString(diaChi);
        dest.writeString(cccd);
        dest.writeString(capBac);
        dest.writeInt(maPB);
    }

    protected NhanVien(Parcel in) {
        maNV = in.readInt();
        hoTen = in.readString();
        gioiTinh = in.readString();
        ngSinh = in.readString();
        sdt = in.readString();
        email = in.readString();
        diaChi = in.readString();
        cccd = in.readString();
        capBac = in.readString();
        maPB = in.readInt();
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getNgSinh() {
        return ngSinh;
    }

    public void setNgSinh(String ngSinh) {
        this.ngSinh = ngSinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getCapBac() {
        return capBac;
    }

    public void setCapBac(String capBac) {
        this.capBac = capBac;
    }

    public int getMaPB() {
        return maPB;
    }

    public void setMaPB(int maPB) {
        this.maPB = maPB;
    }

    public NhanVien(){

    }


    @Override
    public String toString() {
        return "NhanVien{" +
                "maNV=" + maNV +
                ", hoTen='" + hoTen + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", ngSinh='" + ngSinh + '\'' +
                ", sdt='" + sdt + '\'' +
                ", email='" + email + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", cccd='" + cccd + '\'' +
                ", capBac='" + capBac + '\'' +
                ", maPB=" + maPB +
                '}';
    }

    public static final Creator<NhanVien> CREATOR = new Creator<NhanVien>() {
        @Override
        public NhanVien createFromParcel(Parcel in) {
            return new NhanVien(in);
        }

        @Override
        public NhanVien[] newArray(int size) {
            return new NhanVien[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}