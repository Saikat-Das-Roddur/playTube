package org.richit.animal.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.richit.animal.AdUtils.MyAdsListener;
import org.richit.animal.AdUtils.MyAllAdsUtil;
import org.richit.animal.Config;
import org.richit.animal.Database.FavouriteDB;
import org.richit.animal.Models.AnimalModel;
import org.richit.animal.R;
import org.richit.animal.Youtube.YoutubeApiHelper;
import org.richit.animal.Youtube.YoutubeListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AnimalDetailsActivity extends AppCompatActivity {

    //Data Model
    AnimalModel animalModel;

    //Views
    TextView textViewTitle, textViewName,
            textViewDescription, textViewOtherDesc;
    ImageView imageViewAnimal,
            imageViewMap, imageViewBack,
            imageViewFavourite, imageViewMedia,
            imageViewShare;
    LinearLayout linearLayoutExtraData;
    ScrollView scrollView;

    //Library Classes
    YouTubePlayerView playerView;
    YouTubePlayer player;

    //Data variables
    String videoID;
    boolean tryToPlayVideo = false;
    boolean inVideoMode = true;
    private static boolean errorDialogShownOnce = false;

    //Bitmap Uri
    Uri uriBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_2);

        //Initializing Animal Model data
        animalModel = getIntent().getParcelableExtra("animalTypes");

        //Initialize FavouriteDB for adding data to favourite list
        FavouriteDB.init(this);

        //Initializing views
         initViews();

        //Animal image show at the beginning that's why video playing button is invisible
        playerView.setVisibility(View.GONE);

        //Sending extra available data via this method as an array so that
        //we can use only one textView for showing different data one by one
        addExtraDataIfAvailable(
                new ExtraData("Kingdom", animalModel.getKingdom()),
                new ExtraData("Phylum", animalModel.getPhylum()),
                new ExtraData("Class", animalModel.getAnimalClass()),
                new ExtraData("Order", animalModel.getOrder()),
                new ExtraData("Family", animalModel.getFamily()),
                new ExtraData("Genus", animalModel.getGenus()),
                new ExtraData("Scientific name", animalModel.getScientific()),
                new ExtraData("Common Name", animalModel.getCommonname()),
                new ExtraData("Number of Species", animalModel.getNumberofspecies()),
                new ExtraData("Location", animalModel.getLocation()),
                new ExtraData("Type", animalModel.getType()),
                new ExtraData("Origin", animalModel.getOrigin()),
                new ExtraData("Habitat", animalModel.getHabitat()),
                new ExtraData("Color", animalModel.getColor()),
                new ExtraData("Skin", animalModel.getSkin()),
                new ExtraData("Top Speed", animalModel.getSpeed()),
                new ExtraData("Diet", animalModel.getDiet()),
                new ExtraData("Wing Span", animalModel.getWingspan()),
                new ExtraData("Prey", animalModel.getPrey()),
                new ExtraData("Main Prey", animalModel.getMainprey()),
                new ExtraData("Predators", animalModel.getPredators()),
                new ExtraData("Life Style", animalModel.getLifestyle()),
                new ExtraData("Water Type", animalModel.getWater()),
                new ExtraData("ph Level", animalModel.getPhlevel()),
                new ExtraData("Conservation Status", animalModel.getConservation()),
                new ExtraData("Lifespan", animalModel.getLifespan()),
                new ExtraData("Average Weight", animalModel.getWeight()),
                new ExtraData("Temperament", animalModel.getTemperament()),
                new ExtraData("Training", animalModel.getTraining()),
                new ExtraData("Features", animalModel.getFeatures()),
                new ExtraData("Special Feature", animalModel.getSpecialfeature()),
                new ExtraData("Distinctive Feature", an