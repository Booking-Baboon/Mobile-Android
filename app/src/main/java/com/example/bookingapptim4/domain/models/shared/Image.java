package com.example.bookingapptim4.domain.models.shared;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Image {
    Long id;
    String path;
    String fileName;
    byte[] content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
