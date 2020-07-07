package com.cheda.skysevents.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SkyEvent implements Parcelable{

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("venue")
    @Expose
    public String venue;

    @SerializedName("date")
    @Expose
    public String date;

    @SerializedName("latlng")
    @Expose
    public String latlng;

    @SerializedName("admission")
    @Expose
    public String admission;

    @SerializedName("poster")
    @Expose
    public String poster;

    public SkyEvent(String name, String venue, String date, String latlng, String admission, String poster){

        this.name = name;
        this.venue = venue;
        this.venue = date;
        this.venue = latlng;
        this.venue = admission;
        this.poster = poster;

    }

    protected SkyEvent(Parcel in) {
        name = in.readString();
        venue = in.readString();
        date = in.readString();
        latlng = in.readString();
        admission = in.readString();
        poster = in.readString();
    }

    public static final Creator<SkyEvent> CREATOR = new Creator<SkyEvent>() {
        @Override
        public SkyEvent createFromParcel(Parcel in) {
            return new SkyEvent(in);
        }

        @Override
        public SkyEvent[] newArray(int size) {
            return new SkyEvent[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(venue);
        dest.writeString(date);
        dest.writeString(latlng);
        dest.writeString(admission);
        dest.writeString(poster);
    }
}
