package com.saikat.playtube.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comments implements Parcelable {

    String commentId;
    String userName;
    String userImage;
    String comment;

    public Comments(String commentId, String userName, String userImage, String comment) {
        this.commentId = commentId;
        this.userName = userName;
        this.userImage = userImage;
        this.comment = comment;
    }

    protected Comments(Parcel in) {
        commentId = in.readString();
        userName = in.readString();
        userImage = in.readString();
        comment = in.readString();
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static final Creator<Comments> CREATOR = new Creator<Comments>() {
        @Override
        public Comments createFromParcel(Parcel in) {
            return new Comments(in);
        }

        @Override
        public Comments[] newArray(int size) {
            return new Comments[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(commentId);
        dest.writeString(userName);
        dest.writeString(userImage);
        dest.writeString(comment);
    }
}
