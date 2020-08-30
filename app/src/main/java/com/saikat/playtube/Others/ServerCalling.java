package com.saikat.playtube.Others;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.saikat.playtube.Config;

public class ServerCalling {
    public static void postVideoRating(String videoId, String rating, OkHttpResponseListener listener){
        AndroidNetworking.post(Config.videoRatingUrl)
                .addHeaders("Authorization","Bearer "+Config.accessToken)
                .addBodyParameter("id",videoId)
                .addBodyParameter("rating",rating)
                .build()
                .getAsOkHttpResponse(listener);
    }
    public static void getChannelVideos(String channelId, int pageNumber, JSONObjectRequestListener listener){
        AndroidNetworking.get(Config.youTubeChannelApi)
               // .addHeaders("Authorization","Bearer "+Config.accessToken)
                .addQueryParameter("part","snippet")
                .addQueryParameter("channelId",channelId)
                .addQueryParameter("maxResults",String.valueOf(pageNumber))
                .addQueryParameter("key",Config.key)
                .build().getAsJSONObject(listener);

    }
    public static void getVideoComments(String videoId,JSONObjectRequestListener listener){
        AndroidNetworking.get(Config.videoComment)
                .addQueryParameter("key",Config.key)
                .addQueryParameter("textFormat","plainText")
                .addQueryParameter("part","snippet")
                .addQueryParameter("videoId",videoId)

                .build().getAsJSONObject(listener);

    }
}
