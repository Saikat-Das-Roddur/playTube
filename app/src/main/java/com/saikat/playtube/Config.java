package com.saikat.playtube;

import com.google.android.gms.common.api.Api;

public class Config {
    public static String key = "AIzaSyAUTs4ufHTWzBge85cJ2goaWRO1e-YuyhY";
    public static String youTubeChannelApi = "https://www.googleapis.com/youtube/v3/search";//?part=snippet&channelId=";
    public static String videoComment = "https://www.googleapis.com/youtube/v3/commentThreads";//?"+key+"&textFormat=plainText&part=snippet&videoId=";
    public static String videoRatingUrl = "https://www.googleapis.com/youtube/v3/videos/rate";
    public static String pageToken = "";
    public static String accessToken;
    public static String refreshToken="1//0gtNUwJtsNslLCgYIARAAGBASNwF-L9IrxMM5sZgucwok6G33CsfEkYNX8RmGQw-ebe1ZiL4FCZGmbx2NA8TG-sK7L0c_4A4sCiE";
    public static String getRatingApi = "https://www.googleapis.com/youtube/v3/videos/getRating";
    public static String youTubeScopeUrl = "https://www.googleapis.com/auth/youtube.force-ssl";
    public static String commentApi = "https://www.googleapis.com/youtube/v3/commentThreads?part=snippet&key=";
    public static String playListApi = "https://www.googleapis.com/youtube/v3/playlistItems";
    public static String tokenUrl = "https://oauth2.googleapis.com/token";
}
