package models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Request implements Parcelable {
    private  int id; // ID của yêu cầu
    private  int nvId; // ID nhân viên
    private  String topic; // Chủ đề
    private  String information; // Thông tin chi tiết
    private  int isApproved; // Trạng thái phê duyệt

    public Request(int id, int nvId, String topic, String information, int isApproved) {
        this.id = id;
        this.nvId = nvId; // Khởi tạo nvId
        this.topic = topic;
        this.information = information;
        this.isApproved = isApproved;
    }

    protected Request(Parcel in) {
        id = in.readInt(); // Đọc id là int
        nvId = in.readInt(); // Đọc nvId là int
        topic = in.readString();
        information = in.readString();
        isApproved = in.readInt();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public int getId() {
        return id; // Trả về id
    }

    public int getNvId() {
        return nvId; // Thêm phương thức getter cho nvId
    }

    public String getTopic() {
        return topic;
    }

    public String getInformation() {
        return information;
    }

    public int isApproved() {
        return isApproved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id); // Ghi id là int
        dest.writeInt(nvId); // Ghi nvId là int
        dest.writeString(topic);
        dest.writeString(information);
        dest.writeInt(isApproved);
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", nvId=" + nvId + // Thêm nvId vào toString
                ", topic='" + topic + '\'' +
                ", information='" + information + '\'' +
                ", isApproved=" + isApproved +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return isApproved == request.isApproved &&
                id == request.id &&
                nvId == request.nvId && // So sánh nvId
                Objects.equals(topic, request.topic) &&
                Objects.equals(information, request.information);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nvId, topic, information, isApproved); // Thêm nvId vào hashCode
    }

    public Request() {
    }
}
