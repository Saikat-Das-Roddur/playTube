package com.saikat.playtube;

public class Config {
    public static String key = "AIzaSyBMETrltp2CUUA4yIkYKLDspu1u5ESsk3I";
    public static String youTubeChannelApi = "https://www.googleapis.com/youtube/v3/search";//?part=snippet&channelId=";
    public static String videoComment = "https://www.googleapis.com/youtube/v3/commentThreads";//?"+key+"&textFormat=plainText&part=snippet&videoId=";
    public static String videoRatingUrl = "https://www.googleapis.com/youtube/v3/videos/rate";
    public static String pageToken = "";
    public static String accessToken;
    public static String youTubeScopeUrl = "https://www.googleapis.com/auth/youtube.force-ssl";
}
