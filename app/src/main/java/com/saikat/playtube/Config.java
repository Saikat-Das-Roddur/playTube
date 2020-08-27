package com.saikat.playtube;

public class Config {
    public static String key = "&key=AIzaSyBMETrltp2CUUA4yIkYKLDspu1u5ESsk3I";
    public static String youTubeChannelApi = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=";
    public static String videoComment = "https://www.googleapis.com/youtube/v3/commentThreads?"+key+"&textFormat=plainText&part=snippet&videoId=";
    public static String videoRatingUrl = "https://www.googleapis.com/youtube/v3/videos/rate?id=YOUR_VIDEO_ID&rating=like"+key;
    public static String pageToken = "QURTSl9pMmlaVXRZWFduS1NySkloWnZRcFR4VVN2SzBPeG5ZQURmbFBubmlKWFpZYWlQM1RvYzIzZTRGM3VBVGU3TlZ3Njh6QWFiRnlHTm1UNEtZNzh1M2FZTnpjdWdRSU1QSjlMRXB5SXVFcWdvdGhoa1J6VElvT1JnMndHLTVIMWs=";
    }
