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
import java.util.Collections;
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
    private String addressPlusName;
    private String websiteUrl;

    Button mFiltersButton;
    Button mSaveButton;
    Button mLoadButton;
    Button mMakeDecisionButton;
    Button mOpenInMapsButton;
    Button mOpenWebsiteButton;
    SeekBar mSeekBar;
    TextView mDistanceText;
    TextView mDecisionText;

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
        params.put("limit", "50");

        //radius param isnt perfect, need to also filter results for distance
        //but including the param helps keep the list as large as possible
        int radius = (int) 1609.34 * (mSeekBar.getProgress() + progressBarMinValue);
        if (radius > 40000){
            radius = 40000;//max value is a little less than 25 miles and over 40k causes error for yelp API
        }
        params.put("radius", Integer.toString(radius));

        try{

            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
            SearchResponse searchResponse = call.execute().body();
            ArrayList<Business> businesses = searchResponse.getBusinesses();
            //String businessName = businesses.get(0).getName();
            //Toast.makeText(FoodDecisionActivity.this, "Number of businesses: " + Integer.toString(businesses.size()), Toast.LENGTH_SHORT).show();

            //removing things too far away
            ArrayList<Integer> indicesToRemove = new ArrayList<>();
            for (int i = 0; i < businesses.size(); i++){
                if (businesses.get(i).getDistance() > 1609.34 * (mSeekBar.getProgress() + progressBarMinValue)){
                    indicesToRemove.add(i);
                }
            }

            //work from right to left
            Collections.reverse(indicesToRemove);

            for (int i = 0; i < indicesToRemove.size(); i++){
                int index = indicesToRemove.get(i);
                businesses.remove(index);
            }
            //

            if (businesses.size() > 0){
                Random rand = new Random();
                int choice = rand.nextInt(businesses.size());
                //Toast.makeText(FoodDecisionActivity.this, businesses.get(choice).getName() + Integer.toString(businesses.size()), Toast.LENGTH_SHORT).show();
                mDecisionText.setText(businesses.get(choice).getName() + " " + Integer.toString((int) businesses.get(choice).getDistance()) + " " + Integer.toString(businesses.size()));
                addressPlusName = businesses.get(choice).getLocation().getAddress1() + " " + businesses.get(choice).getName();
                websiteUrl = businesses.get(choice).getUrl();//yelp website
            }else{
                mDecisionText.setText("");
                addressPlusName = "";
                websiteUrl = "";
            }

        }catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }

    void openInMaps(){
        if (!addressPlusName.equals("")) {
            String url = "http://maps.google.com/maps?daddr=" + addressPlusName;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }else{
            Toast.makeText(FoodDecisionActivity.this, "No decision made yet!", Toast.LENGTH_SHORT).show();
        }
    }

    void openWebsite(){
        if (!websiteUrl.equals("")) {
            String url = websiteUrl;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }else{
            Toast.makeText(FoodDecisionActivity.this, "No decision made yet!", Toast.LENGTH_SHORT).show();
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

        addressPlusName = "";
        websiteUrl = "";
        connectToYelp();

        mFiltersButton = (Button)findViewById(R.id.filters_button);
        mSaveButton = (Button)findViewById(R.id.save_button);
        mLoadButton = (Button)findViewById(R.id.load_button);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mDistanceText = (TextView) findViewById(R.id.distance_text);
        mDistanceText.setText(getString(R.string.distance, mSeekBar.getProgress() + progressBarMinValue));
        mMakeDecisionButton = (Button)findViewById(R.id.make_decision_button);
        mOpenInMapsButton = (Button)findViewById(R.id.open_in_maps_button);
        mOpenWebsiteButton = (Button)findViewById(R.id.website_button);
        mDecisionText = (TextView)findViewById(R.id.decision_chosen_text);

        mMakeDecisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(FoodDecisionActivity.this, "Would open up maps, drop a pin based on distance and filters", Toast.LENGTH_SHORT).show();
                searchYelp();
            }
        });

        mOpenInMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(FoodDecisionActivity.this, "Would open up maps, drop a pin based on distance and filters", Toast.LENGTH_SHORT).show();
                openInMaps();
            }
        });

        mOpenWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(FoodDecisionActivity.this, "Would open up maps, drop a pin based on distance and filters", Toast.LENGTH_SHORT).show();
                openWebsite();
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
