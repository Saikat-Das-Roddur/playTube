package org.richit.animal.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.richit.animal.AdUtils.AdHelper;
import org.richit.animal.AdUtils.MyAdsListener;
import org.richit.animal.AdUtils.MyAllAdsUtil;
import org.richit.animal.Adapters.AnimalAdapter;
import org.richit.animal.Database.FavouriteDB;
import org.richit.animal.Models.AnimalModel;
import org.richit.animal.Models.OnlineData;
import org.richit.animal.Config;
import org.richit.animal.Others.GlobaMethods;
import org.richit.animal.Others.GlobalVariables;
import org.richit.animal.R;
import org.richit.materialofficeaboutlib.Others.LoadListener;
import org.richit.materialofficeaboutlib.Others.OfficeAboutHelper;

import java.util.ArrayList;
import java.util.Collections;

import p32929.myhouseads2lib.FayazSP;
import p32929.myhouseads2lib.HouseAds;
import p32929.myhouseads2lib.InterListener;
import p32929.updaterlib.AppUpdater;
import p32929.updaterlib.UpdateListener;
import p32929.updaterlib.UpdateModel;

public class MainActivity extends AppCompatActiv