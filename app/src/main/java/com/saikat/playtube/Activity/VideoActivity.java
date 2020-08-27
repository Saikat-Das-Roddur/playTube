package com.saikat.playtube.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.saikat.playtube.Config;
import com.saikat.playtube.Model.Video;
import com.saikat.playtube.R;
import com.saikat.playtube.YouTube.YoutubeApiHelper;
import com.saikat.playtube.YouTube.YoutubeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoActivity extends AppCompatActivity {

    YouTubePlayerView playerView;
    YouTubePlayer player;

    boolean tryToPlayVideo = false;
    private static boolean errorDialogShownOnce = false;

    String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        playerView = findViewById(R.id.youtube_player_view);
        String videoId = getIntent().getStringExtra("videoId");
        Log.d(TAG, "VideoId: "+videoId);
        if (videoId.substring(0,2).contains("PL")){
            playerView.setVisibility(View.GONE);
            Toast.makeText(this, "It's a playlist: "+videoId, Toast.LENGTH_SHORT).show();
        }else {
            playerView.setVisibility(View.VISIBLE);
            initVideoloder(videoId);
        }

    }

    private void initVideoloder(final String videoId) {
        new YoutubeApiHelper(VideoActivity.this, Config.videoComment+videoId+"&maxResults=100&pageToken="+Config.pageToken, new YoutubeListener() {
            @Override
            public void onJsonDataReceived(String updateModel) {
                try {
                    JSONObject jsonObject = new JSONObject(updateModel);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    Log.d(TAG, "onJsonDataReceived: "+jsonArray);
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
}
