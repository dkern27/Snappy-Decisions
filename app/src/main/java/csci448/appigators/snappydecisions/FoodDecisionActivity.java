package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

public class FoodDecisionActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener
{
    private String appId = "JYFlwDGsPiOAZBKgjm--_g";
    private String appSecret = "yYz53VHoK1p6wMa3lQ7B6jS7v7LNYx94oqAaMeyiXEmncxzi2p7Yxs9azWcDHb6K";
    YelpFusionApiFactory apiFactory;
    YelpFusionApi yelpFusionApi;
    int progressBarMinValue = 1;

    Button mFiltersButton;
    Button mSaveButton;
    Button mLoadButton;
    Button mMakeDecisionButton;
    SeekBar mSeekBar;
    TextView mDistanceText;

    void connectToYelp(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        apiFactory = new YelpFusionApiFactory();

        try {
            yelpFusionApi = apiFactory.createAPI(appId, appSecret);
        }catch(Exception e){
            Log.d("myTag", e.getMessage());
        }
    }

    void searchYelp(){
        Map<String, String> params = new HashMap<>();

        // general params
        // general params
        params.put("term", "restaurants");
        params.put("latitude", "39.7555");
        params.put("longitude", "-105.2226");
        params.put("categories", "burgers");
        int radius = (int) 1609.34 * (mSeekBar.getProgress() + progressBarMinValue);
        if (radius > 40000){
            radius = 40000;//max value is a little less than 25 miles and over 40k causes error for yelp API
        }
        //Toast.makeText(FoodDecisionActivity.this, Integer.toString(radius), Toast.LENGTH_SHORT).show();
        params.put("radius", Integer.toString(radius));
        params.put("limit", "50");
        //golden: 39.7555° N, 105.2211° W

        try{

            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
            SearchResponse searchResponse = call.execute().body();
            ArrayList<Business> businesses = searchResponse.getBusinesses();
            //String businessName = businesses.get(0).getName();
            //Toast.makeText(FoodDecisionActivity.this, "Number of businesses: " + Integer.toString(businesses.size()), Toast.LENGTH_SHORT).show();
            if (businesses.size() > 0){
                Random rand = new Random();
                int choice = rand.nextInt(businesses.size());
                Toast.makeText(FoodDecisionActivity.this, businesses.get(choice).getName() + Integer.toString(businesses.size()), Toast.LENGTH_SHORT).show();



                //this opens map
                //String url = "http://maps.google.com/maps?daddr="+businesses.get(choice).getName();
                String url = "http://maps.google.com/maps?daddr=" + businesses.get(choice).getLocation().getAddress1() + " " + businesses.get(choice).getName();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
                startActivity(intent);
            }

        }catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }

    void showSaveDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Current Distance and Filters As");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FoodDecisionActivity.this, "Would save current settings and add as an option in the load popup, but not in alpha", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_decision);

        connectToYelp();

        mFiltersButton = (Button)findViewById(R.id.filters_button);
        mSaveButton = (Button)findViewById(R.id.save_button);
        mLoadButton = (Button)findViewById(R.id.load_button);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mDistanceText = (TextView) findViewById(R.id.distance_text);
        mDistanceText.setText(getString(R.string.distance, mSeekBar.getProgress() + progressBarMinValue));
        mMakeDecisionButton = (Button)findViewById(R.id.make_decision_button);

        mMakeDecisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(FoodDecisionActivity.this, "Would open up maps, drop a pin based on distance and filters", Toast.LENGTH_SHORT).show();
                searchYelp();
            }
        });

        mFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = FoodFiltersActivity.newIntent(FoodDecisionActivity.this);
                startActivity(i);
            }
        });


        mSaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSaveDialogue();
                //Toast.makeText(FoodDecisionActivity.this, "Would open dialogue to save current settings", Toast.LENGTH_SHORT).show();
            }
        });

        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(FoodDecisionActivity.this, v);
                popupMenu.setOnMenuItemClickListener(FoodDecisionActivity.this);
                popupMenu.getMenu().add("Option1");
                popupMenu.getMenu().add("Option2");
                popupMenu.getMenu().add("Option3");
                popupMenu.getMenu().add("Option4");
                popupMenu.getMenu().add("Option5");
                popupMenu.getMenu().add("Option6");
                popupMenu.getMenu().add("Option7");
                popupMenu.getMenu().add("Option8");
                popupMenu.getMenu().add("Option9");
                popupMenu.getMenu().add("Option10");
                popupMenu.getMenu().add("Option11");
                popupMenu.getMenu().add("Option12");
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                int progressChanged = progress + progressBarMinValue;
                mDistanceText.setText(getString(R.string.distance, progressChanged));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }


    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(FoodDecisionActivity.this, "Would change to saved distance/weight combo, but not in alpha", Toast.LENGTH_SHORT).show();
        return true;
    }

    /**
     * Creates new intent
     * @param packageContext
     * @return intent containing activity to be started
     */
    public static Intent newIntent(Context packageContext)
    {
        Intent i = new Intent(packageContext, FoodDecisionActivity.class);
        return i;
    }
}
