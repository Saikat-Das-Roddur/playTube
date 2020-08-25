package com.saikat.playtube.YouTube;

public interface YoutubeListener {
    void onJsonDataReceived(String updateModel);

    void onError(String error);
}