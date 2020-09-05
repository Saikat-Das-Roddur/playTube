package com.saikat.playtube.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.saikat.playtube.Adapter.VideoAdapter;
import com.saikat.playtube.Model.Video;
import com.saikat.playtube.Others.ServerCalling;
import com.saikat.playtube.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlayListVideoFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    //Adapter
    VideoAdapter videoAdapter;
    ArrayList<Video> videoArrayList = new ArrayList<>();
    Context context;

    String TAG = getClass().getSimpleName();
    String playlistId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.play_list_video_layout,container,false);
        context = getContext();
        recyclerView = view.findViewById(R.id.recyclerView);

        Bundle bundle = getArguments();
        if(bundle!= null)
        {
            playlistId = getArguments().getString("playListId");
            Log.d(TAG, "onCreateView: "+playlistId);
            initvideoloader(playlistId);
        }
        return view;
    }

    private void initvideoloader(String playlistId) {
        ServerCalling.getPlayListVideos(playlistId, 50, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, "onResponse: "+jsonObject);
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    String nextPageToken = jsonObject.getString("nextPageToken");
                    Log.d(TAG, "nextPageToken: "+nextPageToken);

                    for (int i = 0; i <jsonArray.length() ; i++) {
                        String videoId= jsonArray.getJSONObject(i).getJSONObject("snippet").getJSONObject("resourceId").getString("videoId");
                            //videoId = jsonArray.getJSONObject(i).getJSONObject("id").getString("videoId");
                        Log.d(TAG, "videoId: "+videoId);
                        String videoTitle = jsonArray.getJSONObject(i).getJSONObject("snippet").getString("title");
                        String videoDesc = jsonArray.getJSONObject(i).getJSONObject("snippet").getString("description");
                        String videoImage = jsonArray.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");

                       videoArrayList.add(new Video(videoId,videoTitle,videoImage,videoDesc));

                    }
                    setAdapter();
                   // progressDialog.dismiss();
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
        videoAdapter = new VideoAdapter(context,videoArrayList);
        recyclerView.setAdapter(videoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }

}
