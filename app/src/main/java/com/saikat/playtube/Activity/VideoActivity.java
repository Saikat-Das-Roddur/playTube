package com.saikat.playtube.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.saikat.playtube.Adapter.CommentAdapter;
import com.saikat.playtube.Config;
import com.saikat.playtube.Model.Comments;

import com.saikat.playtube.Others.GoogleEventListener;
import com.saikat.playtube.Others.GoogleLogIn;
import com.saikat.playtube.R;
import com.saikat.playtube.YouTube.YoutubeApiHelper;
import com.saikat.playtube.YouTube.YoutubeListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.opencensus.common.Scope;
import okhttp3.Response;

public class VideoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageViewLike, imageViewDisLike;

    GoogleLogIn logIn;
    YouTubePlayerView playerView;
    YouTubePlayer player;
    SharedPreferences preferences;

    CommentAdapter commentAdapter;
    ArrayList<Comments> commentsArrayList = new ArrayList<>();
    boolean tryToPlayVideo = false;
    String videoId = "";
    String token ="";
    private static boolean errorDialogShownOnce = false;

    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        AndroidNetworking.initialize(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerViewComments);
        imageViewLike = findViewById(R.id.likeIv);
        imageViewDisLike = findViewById(R.id.disLikeIv);
        playerView = findViewById(R.id.youtube_player_view);
        videoId = getIntent().getStringExtra("videoId");
        Log.d(TAG, "VideoId: " + videoId);
        preferences = getSharedPreferences("GOOGLE_ACCOUNT",MODE_PRIVATE);
        logIn = new GoogleLogIn(this);

        if (videoId.substring(0, 2).contains("PL")) {
            playerView.setVisibility(View.GONE);
            Toast.makeText(this, "It's a playlist: " + videoId, Toast.LENGTH_SHORT).show();
        } else {
            playerView.setVisibility(View.VISIBLE);
            initVideoLoader(videoId);
        }
        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawableCompat.setTint(imageViewLike.getDrawable(),
                        ContextCompat.getColor(VideoActivity.this, R.color.colorAccent));
                isLogedIn(videoId,"like");
            }
        });
        imageViewDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawableCompat.setTint(imageViewDisLike.getDrawable(),
                        ContextCompat.getColor(VideoActivity.this, R.color.colorPrimary));
                isLogedIn(videoId,"dislike");

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logIn.onSignInActivityResult(data);
    }

    public void rateVideo(final String videoId, final String rating) {
        AndroidNetworking.post(Config.videoRatingUrl)
                .addHeaders("Authorization","Bearer "+Config.accessToken)
                .addBodyParameter("id",videoId)
                .addBodyParameter("rating",rating)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        Log.d(TAG, "onResponse: "+response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError);
                    }
                });
    }

    public void isLogedIn(final String videoId, final String rating){

            logIn.signIn();
            logIn.setListener(new GoogleEventListener() {
                @Override
                public void onSignInSuccess(GoogleSignInAccount account) {
                    preferences.edit().putString("GOOGLE_ACCOUNT",Config.accessToken).apply();
                    Log.d(TAG, "onSignInSuccess: "+Config.accessToken );
                    rateVideo(videoId,rating);
                }

                @Override
                public void onFailureSignIn(String error) {
                    Log.d(TAG, "onFailureSignIn: "+error);
                }

                @Override
                public void onSignOutSuccess(Class activityClass) {

                }

                @Override
                public void onSignOutFailure(Class activityClass) {

                }
            });



    }


    private void initVideoLoader(final String videoId) {
        new YoutubeApiHelper(VideoActivity.this, Config.videoComment + videoId + "&maxResults=100&pageToken=" + Config.pageToken, new YoutubeListener() {
            @Override
            public void onJsonDataReceived(String updateModel) {
                try {
                    JSONObject jsonObject = new JSONObject(updateModel);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    Config.pageToken = jsonObject.getString("nextPageToken");
                    Log.d(TAG, "nextPageToken: " + Config.pageToken);
                    populateComments(jsonArray);
                    getLifecycle().addObserver(playerView);
                    playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(YouTubePlayer youTubePlayer) {

                            //if video id is available then the youtube player play the video
                            youTubePlayer.loadVideo(videoId, 0);
                            player = youTubePlayer;

                            //checking whether or not the user is in video mode or not
                            if (tryToPlayVideo) {

                                player.play();
                            } else
                                player.pause();

                        }

                        @Override
                        public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {

                            //Some country doesn't provide playing youtube video directly
                            //may be that's an issue of the library or the Internet service provider
                            //so here we checking first if the video is playing without a vpn
                            //if not then we show dialog for using vpn
                            if (errorDialogShownOnce) {
                                Toast.makeText(VideoActivity.this, "Error loading video. Please use a VPN", Toast.LENGTH_LONG).show();
                            } else {
                                new AlertDialog.Builder(VideoActivity.this)
                                        .setTitle("Error loading")
                                        .setMessage(String.format(
                                                "Error: %s\n\nThis may cause because of your ISP. Sometimes, using a VPN can fix the problem...", error.name()
                                        ))
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                            errorDialogShownOnce = !errorDialogShownOnce;
                            super.onError(youTubePlayer, error);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.d("What?", "onError: ");
            }
        }).execute();

    }

    private void populateComments(JSONArray jsonArray) {

        try {
            Log.d(TAG, "populateComments: " + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("snippet").getJSONObject("topLevelComment").getJSONObject("snippet");
                String userName = jsonObject.getString("authorDisplayName");
                String userComment = jsonObject.getString("textDisplay");
                String userImage = jsonObject.getString("authorProfileImageUrl");
                commentsArrayList.add(new Comments(userName, userImage, userComment));

            }
            setAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setAdapter() {
        commentAdapter = new CommentAdapter(VideoActivity.this, commentsArrayList);
        recyclerView.setAdapter(commentAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(VideoActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }


}
