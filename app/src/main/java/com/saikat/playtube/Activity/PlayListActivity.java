package com.saikat.playtube.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.saikat.playtube.Adapter.VideoAdapter;
import com.saikat.playtube.Config;
import com.saikat.playtube.Model.Video;
import com.saikat.playtube.Others.ServerCalling;
import com.saikat.playtube.R;
import com.saikat.playtube.YouTube.YoutubeApiHelper;
import com.saikat.playtube.YouTube.YoutubeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

public class PlayListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    //Adapter
    VideoAdapter videoAdapter;

    ArrayList<Video> videoArrayList = new ArrayList<>();

    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        String channelId = getIntent().getStringExtra("channelId");
        recyclerView = findViewById(R.id.recyclerView);
        initYoutubeVideoLoader(channelId);
    }

    private void initYoutubeVideoLoader(String chanelId) {
        ServerCalling.getChannelVideos(chanelId, 50, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, "onResponse: "+jsonObject);
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                        for (int i = 0; i <jsonArray.length() ; i++) {
                        String videoId="";
                               if(jsonArray.getJSONObject(i).getJSONObject("id").getString("kind").equals("youtube#video")) {
                                   videoId = jsonArray.getJSONObject(i).getJSONObject("id").getString("videoId");
                                   }else {
                                   videoId = jsonArray.getJSONObject(i).getJSONObject("id").getString("playlistId");
                               }
                        String videoTitle = jsonArray.getJSONObject(i).getJSONObject("snippet").getString("title");
                        String videoDesc = jsonArray.getJSONObject(i).getJSONObject("snippet").getString("description");
                        String videoImage = jsonArray.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");

                        videoArrayList.add(new Video(videoId,videoTitle,videoImage,videoDesc));

                    }
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                Log.d(TAG, "onError: "+anError);
            }
        });
    }

    private void setAdapter() {
        videoAdapter = new VideoAdapter(PlayListActivity.this,videoArrayList);
        recyclerView.setAdapter(videoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PlayListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }
}
