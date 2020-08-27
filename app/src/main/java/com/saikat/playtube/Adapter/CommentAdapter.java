package com.saikat.playtube.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.saikat.playtube.Model.Comments;
import com.saikat.playtube.Model.Video;
import com.saikat.playtube.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {

    Context context;
    ArrayList<Comments> commentsArrayList = new ArrayList<>();

    public CommentAdapter(Context context, ArrayList<Comments> commentsArrayList) {
        this.context = context;
        this.commentsArrayList = commentsArrayList;
    }

    @NonNull
    @Override
    public CommentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.comments_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.Holder holder, final int position) {
        holder.textViewUserName.setText(commentsArrayList.get(position).getUserName());
        holder.textViewComment.setText(commentsArrayList.get(position).getComment());
        Picasso.get().load(commentsArrayList.get(position).getUserImage()).into(holder.imageViewUser);
    }

    @Override
    public int getItemCount() {
        return commentsArrayList.size();
    }

    public  void setVideos(ArrayList<Comments> comments){
        this.commentsArrayList = comments;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView textViewUserName, textViewComment;
        ImageView imageViewUser;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.nameTv);
            textViewComment = itemView.findViewById(R.id.commentTv);
            imageViewUser = itemView.findViewById(R.id.userIV);
        }
    }
}
