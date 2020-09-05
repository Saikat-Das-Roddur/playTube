package com.saikat.playtube.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
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
import com.saikat.playtube.Others.ServerCalling;
import com.saikat.playtube.R;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

public class VideoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageViewLike, imageViewDisLike;
    EditText editTextComment;
    Button buttonSave,buttonCancel;
    TextView textViewName;
    CircleImageView imageViewProfile;
    ProgressDialog progressDialog;


    GoogleLogIn logIn;
    YouTubePlayerView playerView;
    YouTubePlayer player;
    SharedPreferences preferences;


    CommentAdapter commentAdapter;
    ArrayList<Comments> commentsArrayList = new ArrayList<>();
    boolean tryToPlayVideo = false;
    JSONArray jsonArray;
    String videoId = "";
    String rate="",name="",image="";



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
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        playerView = findViewById(R.id.youtube_player_view);
        videoId = getIntent().getStringExtra("videoId");
        preferences = getSharedPreferences("GOOGLE_ACCOUNT", MODE_PRIVATE);
        logIn = new GoogleLogIn(this);
        logIn();
        initVideoLoader(videoId);
        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+ rate);
                if (rate.equals("like")){
                    rateVideo(videoId,"none");
                    DrawableCompat.setTint(imageViewLike.getDrawable(),
                            ContextCompat.getColor(VideoActivity.this, R.color.colorGrey));
                showRating("none");
                //rate="";
                }else if(rate.equals("none")|| rate.equals("dislike")){
                    rateVideo(videoId,"like");
                    DrawableCompat.setTint(imageViewDisLike.getDrawable(),
                            ContextCompat.getColor(VideoActivity.this, R.color.colorGrey));
                    DrawableCompat.setTint(imageViewLike.getDrawable(),
                            ContextCompat.getColor(VideoActivity.this, R.color.colorBlue));
                    showRating("like");
                    //rate="";
                }
            }
        });
        imageViewDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rate.equals("dislike")){
                    rateVideo(videoId,"none");
                    DrawableCompat.setTint(imageViewDisLike.getDrawable(),
                            ContextCompat.getColor(VideoActivity.this, R.color.colorGrey));
                    showRating("none");
                   // rate="";

                }else if(rate.equals("none")|| rate.equals("like")){
                    rateVideo(videoId,"dislike");
                    showRating("dislike");
                    DrawableCompat.setTint(imageViewLike.getDrawable(),
                            ContextCompat.getColor(VideoActivity.this, R.color.colorGrey));
                    DrawableCompat.setTint(imageViewDisLike.getDrawable(),
                            ContextCompat.getColor(VideoActivity.this, R.color.colorBlue));
                   // rate="";
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logIn.onSignInActivityResult(data);
    }

    private void initVideoLoader(final String videoId) {
        progressDialog.show();
        ServerCalling.getVideoComments(videoId, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    jsonArray = jsonObject.getJSONArray("items");
                    Config.pageToken = jsonObject.getString("nextPageToken");
                    Log.d(TAG, "nextPageToken: " + Config.pageToken);
                    populateComments(jsonArray);
                    playVideo(videoId);
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void populateComments(JSONArray jsonArray) {
        progressDialog.show();
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
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void logIn() {

        logIn.signIn();
        logIn.setListener(new GoogleEventListener() {
            @Override
            public void onSignInSuccess(GoogleSignInAccount account) {
                preferences.edit().putString("myToken", Config.accessToken).commit();
                Log.d(TAG, "Name: "+account.getDisplayName());
                if(account.getPhotoUrl()!=null) {
                    preferences.edit().putString("name",account.getDisplayName()).commit();
                    preferences.edit().putString("image", account.getPhotoUrl().toString()).commit();
                }
                previousRating(Config.accessToken);
            }

            @Override
            public void onFailureSignIn(String error) {
                Log.d(TAG, "onFailureSignIn: " + error);
            }

            @Override
            public void onSignOutSuccess(Class activityClass) {

            }

            @Override
            public void onSignOutFailure(Class activityClass) {

            }
        });

    }

    public void rateVideo( String videoId, String rating) {

        ServerCalling.postVideoRating(videoId, rating, new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {
                if (response.code() == 204)
                    Toast.makeText(VideoActivity.this, "Successfully " + rating.toUpperCase(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(ANError anError) {
                Log.d(TAG, "onError: " + anError);
            }
        });
    }

    private void playVideo(final String videoId) {
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


    }

    private void setAdapter() {
        commentAdapter = new CommentAdapter(VideoActivity.this, commentsArrayList);
        recyclerView.setAdapter(commentAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(VideoActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void previousRating(String token){

        Log.d(TAG, "previousRatingToken: "+token);
        ServerCalling.getVideoRating(videoId, token, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "RatingResponse: "+response);
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    showRating(jsonArray.getJSONObject(0).getString("rating"));
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

    private void showRating(String rating) {
        Log.d(TAG, "showRating: "+rating);
        if (rating.equals("none")){
            rate=rating;
            DrawableCompat.setTint(imageViewLike.getDrawable(),
                    ContextCompat.getColor(VideoActivity.this, R.color.colorGrey));
            DrawableCompat.setTint(imageViewDisLike.getDrawable(),
                    ContextCompat.getColor(VideoActivity.this, R.color.colorGrey));

        }else if (rating.equals("like")){
            rate=rating;
            DrawableCompat.setTint(imageViewDisLike.getDrawable(),
                    ContextCompat.getColor(VideoActivity.this, R.color.colorGrey));
            DrawableCompat.setTint(imageViewLike.getDrawable(),
                    ContextCompat.getColor(VideoActivity.this, R.color.colorBlue));
        }else if (rating.equals("dislike")) {
            rate=rating;
            DrawableCompat.setTint(imageViewDisLike.getDrawable(),
                    ContextCompat.getColor(VideoActivity.this, R.color.colorBlue));
            DrawableCompat.setTint(imageViewLike.getDrawable(),
                    ContextCompat.getColor(VideoActivity.this, R.color.colorGrey));

        }
    }

    public void postComment(View view) {
        Log.d(TAG, "image: "+preferences.getString("image",image));
        Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.comment_dialog);
        textViewName = dialog.findViewById(R.id.nameTv);
        imageViewProfile = dialog.findViewById(R.id.profileIv);
        editTextComment = dialog.findViewById(R.id.comentEt);
        buttonSave = dialog.findViewById(R.id.saveBtn);
        buttonCancel = dialog.findViewById(R.id.cancelBtn);

        textViewName.setText(preferences.getString("name",name));
        Picasso.get().load(preferences.getString("image",image)).into(imageViewProfile);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonBody = new JSONObject().put("snippet",new JSONObject().put("topLevelComment",new JSONObject()
                            .put("snippet", new JSONObject()
                                    .put("videoId",videoId)
                                    .put("textOriginal",editTextComment.getText().toString()))));
                    Log.d(TAG, "onClick: "+jsonBody);
                    ServerCalling.postComment(jsonBody,editTextComment.getText().toString(), new OkHttpResponseListener() {
                        @Override
                        public void onResponse(Response response) {
                            Log.d(TAG, "onResponse: "+response);
                            //recreate();
                            populateComments(jsonArray);
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d(TAG, "onError: "+anError);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }





    public boolean isOnline() {

        //Get the connectivity service from the device
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        //Return true if there is no connectivity issues with the internet
        return info != null && info.isConnected();

    }

}
