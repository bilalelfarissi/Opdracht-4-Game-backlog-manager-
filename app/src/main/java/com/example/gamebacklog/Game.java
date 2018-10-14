package com.example.gamebacklog;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "game")
public class Game implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "titel")
    private String titel;
    @ColumnInfo(name = "platform")
    private String platform;
    @ColumnInfo(name = "notes")
    private String notes;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "datum")
    private String datum;

    public Game(String titel, String platform, String notes, String status) {
        this.titel = titel;
        this.platform = platform;
        this.notes = notes;
        this.status = status;
        this.datum = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.titel);
        dest.writeString(this.platform);
        dest.writeString(this.notes);
        dest.writeString(this.status);
        dest.writeString(this.datum);
    }

    protected Game(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.titel = in.readString();
        this.platform = in.readString();
        this.notes = in.readString();
        this.status = in.readString();
        this.datum = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
