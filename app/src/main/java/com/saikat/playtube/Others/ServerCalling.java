package com.saikat.playtube.Others;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.saikat.playtube.Config;
import com.saikat.playtube.R;

import org.json.JSONObject;

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
                .addQueryParameter("key",Config.key)
                .addQueryParameter("channelId",channelId)
                .addQueryParameter("part","snippet")
                //.addQueryParameter("maxResults",String.valueOf(pageNumber))
                .build().getAsJSONObject(listener);

    }
    public static void getPlayListVideos(String playListId, int pageNumber, JSONObjectRequestListener listener){
        AndroidNetworking.get(Config.playListApi)
               // .addHeaders("Authorization","Bearer "+Config.accessToken)
                .addQueryParameter("key",Config.key)
                .addQueryParameter("playlistId",playListId)
                .addQueryParameter("part","snippet")
                //.addQueryParameter("maxResults",String.valueOf(pageNumber))
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
    public static void getVideoRating(String videoId,String accessToken, JSONObjectRequestListener listener){
        Log.d("OMG", "getVideoRating: "+accessToken);
        AndroidNetworking.get(Config.getRatingApi)
                .addHeaders("Authorization","Bearer "+accessToken)
                .addQueryParameter("id",videoId)
                .build().getAsJSONObject(listener);
    }
    public static void postComment(JSONObject jsonBody,String comment,OkHttpResponseListener listener){
        Log.d("OMG", "postComment: "+comment);
        AndroidNetworking.post(Config.commentApi+Config.key)
                .addHeaders("Authorization","Bearer "+Config.accessToken)
                .addJSONObjectBody(jsonBody)
                .build().getAsOkHttpResponse(listener);
    }
    public static void getRefreshToken(String token,OkHttpResponseListener listener){
        Log.d("OMG", "token: "+token);
        AndroidNetworking.post("https://accounts.google.com/o/oauth2/token")
                .addHeaders("Authorization","Bearer "+token)
//                .addBodyParameter("scope",Config.youTubeScopeUrl)
//                .addBodyParameter("response_type","code")
//                .addBodyParameter("access_type","offline")
                .addBodyParameter("grant_type","authorization_code")
                .addBodyParameter("code","4/3gFV0XTf1j6IcMHa6FvTd-yOmu8YrP5dhXeESpM9SOHADV_H3NWDex8fgktOQ87xPJvdC-LOvyhk0osV5oIJIXs")
                .addBodyParameter("client_id",String.valueOf((R.string.server_client_id)))
                .addBodyParameter("client_secret",String.valueOf(R.string.client_secret))
                .addBodyParameter("redirect_uri","")
                .build()
                .getAsOkHttpResponse(listener);
    }
}
