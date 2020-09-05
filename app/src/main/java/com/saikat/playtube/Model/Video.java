package com.saikat.playtube.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

    String videoId;
    String channelId;
    String videoName;
    String videoImage;
    String videoDesc;

    public Video() {
    }

    public Video(String videoId, String channelId, String videoName, String videoImage, String videoDesc) {
        this.videoId = videoId;
        this.channelId = channelId;
        this.videoName = videoName;
        this.videoImage = videoImage;
        this.videoDesc = videoDesc;
    }

    public Video(String videoId, String videoName, String videoImage, String videoDesc) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoImage = videoImage;
        this.videoDesc = videoDesc;
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoName='" + videoName + '\'' +
                ", videoImage='" + videoImage + '\'' +
                ", videoDesc='" + videoDesc + '\'' +
                '}';
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
 public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    protected Video(Parcel in) {
        videoId = in.readString();
        channelId = in.readString();
        videoName = in.readString();
        videoDesc = in.readString();
        videoImage = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(videoId);
        dest.writeString(channelId);
        dest.writeString(videoName);
        dest.writeString(videoDesc);
        dest.writeString(videoImage);
    }

}
