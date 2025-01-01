package models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Account implements Parcelable {
    private int maNV;
    private int maTK;
    private String tenTK;
    private String matKhau;
    private String loaiTK;


    public Account(int maNV, int maTK, String tenTK, String matKhau, String loaiTK) {
        this.maNV = maNV;
        this.maTK = maTK;
        this.tenTK = tenTK;
        this.matKhau = matKhau;
        this.loaiTK = loaiTK;
    }
    public Account(){}
    public Account(int maNV,  String tenTK, String matKhau, String loaiTK) {
        this.maNV = maNV;
        this.tenTK = tenTK;
        this.matKhau = matKhau;
        this.loaiTK = loaiTK;
    }

    protected Account(Parcel in) {
        maNV = in.readInt();
        maTK = in.readInt();
        tenTK = in.readString();
        matKhau = in.readString();
        loaiTK = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public int getMaTK() {
        return maTK;
    }

    public void setMaTK(int maTK) {
        this.maTK = maTK;
    }

    public String getTenTK() {
        return tenTK;
    }

    public void setTenTK(String tenTK) {
        this.tenTK = tenTK;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getLoaiTK() {
        return loaiTK;
    }

    public void setLoaiTK(String loaiTK) {
        this.loaiTK = loaiTK;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maNV=" + maNV +
                ", maTK=" + maTK +
                ", tenTK='" + tenTK + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", loaiTK='" + loaiTK + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(maNV);
        dest.writeInt(maTK);
        dest.writeString(tenTK);
        dest.writeString(matKhau);
        dest.writeString(loaiTK);
    }
}
