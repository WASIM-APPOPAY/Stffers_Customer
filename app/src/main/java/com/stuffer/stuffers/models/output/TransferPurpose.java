package com.stuffer.stuffers.models.output;

import android.os.Parcel;
import android.os.Parcelable;

public class TransferPurpose implements Parcelable {
    String purposeOfTransfer;
    String purposeOfTransferId;

    public TransferPurpose(String purposeOfTransfer, String purposeOfTransferId) {
        this.purposeOfTransfer = purposeOfTransfer;
        this.purposeOfTransferId = purposeOfTransferId;
    }

    protected TransferPurpose(Parcel in) {
        purposeOfTransfer = in.readString();
        purposeOfTransferId = in.readString();
    }

    public static final Creator<TransferPurpose> CREATOR = new Creator<TransferPurpose>() {
        @Override
        public TransferPurpose createFromParcel(Parcel in) {
            return new TransferPurpose(in);
        }

        @Override
        public TransferPurpose[] newArray(int size) {
            return new TransferPurpose[size];
        }
    };

    public String getPurposeOfTransfer() {
        return purposeOfTransfer;
    }

    public String getPurposeOfTransferId() {
        return purposeOfTransferId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(purposeOfTransfer);
        parcel.writeString(purposeOfTransferId);
    }
}
