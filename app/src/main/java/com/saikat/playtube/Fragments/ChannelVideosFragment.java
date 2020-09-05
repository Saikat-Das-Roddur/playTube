package com.saikat.playtube.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.saikat.playtube.Activity.MainActivity;
import com.saikat.playtube.Activity.PlayListActivity;
import com.saikat.playtube.Adapter.VideoAdapter;
import com.saikat.playtube.Model.Video;
import com.saikat.playtube.Others.ServerCalling;
import com.saikat.playtube.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class ChannelVideosFragment extends Fragment {
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    //Adapter
    VideoAdapter videoAdapter;
    ArrayList<Video> videoArrayList = new ArrayList<>();
    String channelId;
    Context context;

    String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.channel_video_layout,container,false);
        context = getContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        channelId = ((PlayListActivity) context).getIntent().getStringExtra("channelId");



        //Progress Dialog settings

        //((PlayListActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.second_frag,fragment).commit();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        initYoutubeVideoLoader(channelId);

        return view;
    }
        private void initYoutubeVideoLoader(String chanelId) {

        if (isOnline()){
            progressDialog.show();
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
                        progressDialog.dismiss();
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
            else
            showAlertDialog();
    }

    //Alert for internet connection
    private void showAlertDialog() {

        //This dialog will be shown if the device is not connected with the online
        new AlertDialog.Builder(context).setTitle("No Internet Connection")
                .setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {

                            //If the device is online then it can populate data from the github
                            initYoutubeVideoLoader(channelId);
                        } else {
                            ((Activity)context).finish();
                        }
                    }
                }).show();
    }


    public boolean isOnline() {

        //Get the connectivity service from the device
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        //Return true if there is no connectivity issues with the internet
        return info != null && info.isConnected();

    }

    private void setAdapter() {
        videoAdapter = new VideoAdapter(context,videoArrayList);
        recyclerView.setAdapter(videoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }
}
