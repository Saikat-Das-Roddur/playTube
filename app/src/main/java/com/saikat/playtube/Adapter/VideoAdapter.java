package com.saikat.playtube.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.saikat.playtube.Activity.MainActivity;
import com.saikat.playtube.Activity.PlayListActivity;
import com.saikat.playtube.Activity.VideoActivity;
import com.saikat.playtube.Fragments.PlayListVideoFragment;
import com.saikat.playtube.Model.Video;
import com.saikat.playtube.Others.ServerCalling;
import com.saikat.playtube.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.Holder>  {
    Context context;
    ArrayList<Video> videoArrayList = new ArrayList<>();

    public VideoAdapter(Context context, ArrayList<Video> videoArrayList) {
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public VideoAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.video_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.Holder holder, final int position) {
        holder.textViewTitle.setText(videoArrayList.get(position).getVideoName());
        holder.textViewDesc.setText(videoArrayList.get(position).getVideoDesc());
        Picasso.get().load(videoArrayList.get(position).getVideoImage()).into(holder.imageViewVideo);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoArrayList.get(position).getVideoId().substring(0, 2).contains("PL")){
                    Fragment fragment = new PlayListVideoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("playListId", videoArrayList.get(position).getVideoId());
                    fragment.setArguments(bundle);

                    //intent.putExtra("videoId",videoArrayList.get(position).getVideoId());
                    ((PlayListActivity) view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.play_list_video_layout,fragment).commit();
                    Toast.makeText(context, "This is an playlist", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(context, VideoActivity.class);
                    intent.putExtra("videoId",videoArrayList.get(position).getVideoId());
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public  void setVideos(ArrayList<Video> videos){
        this.videoArrayList = videos;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView textViewTitle,textViewDesc;
        ImageView imageViewVideo;
        CardView cardView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTv);
            textViewDesc = itemView.findViewById(R.id.descTv);
            imageViewVideo = itemView.findViewById(R.id.videoIV);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
