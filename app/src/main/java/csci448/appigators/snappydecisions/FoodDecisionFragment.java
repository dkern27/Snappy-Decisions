package csci448.appigators.snappydecisions;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class FoodDecisionFragment extends Fragment
{
    private final String APPID = "JYFlwDGsPiOAZBKgjm--_g";
    private final String APPSECRET = "yYz53VHoK1p6wMa3lQ7B6jS7v7LNYx94oqAaMeyiXEmncxzi2p7Yxs9azWcDHb6K";
    private final static String FILTERS_KEY = "FILTERS_KEY";
    private final int PROGRESS_BAR_MIN_VALUE = 1;

    YelpFusionApiFactory apiFactory;
    YelpFusionApi yelpFusionApi;
    private String addressPlusName;
    private String websiteUrl;
    private Location mCurrentLocation;
    private GoogleApiClient mClient;

    Button mFiltersButton;
    Button mSaveButton;
    Button mLoadButton;
    Button mMakeDecisionButton;
    Button mOpenInMapsButton;
    Button mOpenWebsiteButton;
    SeekBar mSeekBar;
    TextView mDistanceText;
    TextView mDecisionText;
    TextView mChoicesText;
    ImageView mLogo;

    private int NUM_FILTERS = FoodFiltersActivity.Filter.values().length;
    private ArrayList<Integer> mFiltersArray = new ArrayList<>(Collections.nCopies(NUM_FILTERS, 0));

    //region Overridden methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();

        addressPlusName = "";
        websiteUrl = "";

        mClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle)
                    {
                        getCurrentLocation();
                    }

                    @Override
                    public void onConnectionSuspended(int i)
                    {

                    }
                })
                .build();

        new ConnectTask().execute(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_food_decision, container, false);
        mFiltersButton = (Button)v.findViewById(R.id.filters_button);
        mSaveButton = (Button)v.findViewById(R.id.save_button);
        mLoadButton = (Button)v.findViewById(R.id.load_button);
        mSeekBar = (SeekBar)v.findViewById(R.id.seekBar);
        mDistanceText = (TextView)v.findViewById(R.id.distance_text);
        mDistanceText.setText(getString(R.string.distance, mSeekBar.getProgress() + PROGRESS_BAR_MIN_VALUE));
        mMakeDecisionButton = (Button)v.findViewById(R.id.make_decision_button);
        mOpenInMapsButton = (Button)v.findViewById(R.id.open_in_maps_button);
        mOpenWebsiteButton = (Button)v.findViewById(R.id.website_button);
        mDecisionText = (TextView)v.findViewById(R.id.decision_chosen_text);
        mChoicesText = (TextView)v.findViewById(R.id.num_choices_text);
        mLogo = (ImageView)v.findViewById(R.id.logo);

        mMakeDecisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getCurrentLocation();
                initiateSearch();
            }
        });

        mOpenInMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openInMaps();
            }
        });

        mOpenWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openWebsite();
            }
        });

        mFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = FoodFiltersActivity.newIntent(getActivity());
                i.putIntegerArrayListExtra(FILTERS_KEY, mFiltersArray);
                startActivityForResult(i,0);
            }
        });


        mSaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSaveDialogue();
            }
        });

        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadDialogue(v);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                int progressChanged = progress + PROGRESS_BAR_MIN_VALUE;
                mDistanceText.setText(getString(R.string.distance, progressChanged));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return v;
    }

    /**
     * starts the googleApiClient
     */
    @Override
    public void onStart()
    {
        mClient.connect();
        super.onStart();
    }

    /**
     * Stops googleApiClient
     */
    @Override
    public void onStop() {
        mClient.disconnect();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 0 && resultCode == RESULT_OK)
        {
            mFiltersArray = data.getIntegerArrayListExtra(FILTERS_KEY);
        }
    }

    //endregion

    public void checkPermission(){
        Log.d(TAG, "checkPermission()");
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "Requesting Permission");
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }
    }

    //region Save/Load

    private void showSaveDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Save Current Distance and Filters As");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Would save current settings and add as an option in the load popup, but not in alpha", Toast.LENGTH_SHORT).show();
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

    private void showLoadDialogue(View v)
    {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                return false;
            }
        });
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

    //endregion

    //region Yelp API

    void connectToYelp(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        apiFactory = new YelpFusionApiFactory();

        try
        {
            yelpFusionApi = apiFactory.createAPI(APPID, APPSECRET);
        }
        catch(Exception e)
        {
            Log.d("myTag", e.getMessage());
        }
    }

    private void initiateSearch() {
        mLogo.setVisibility(View.GONE);
        mDecisionText.setText(R.string.Loading);
        mChoicesText.setText("");
        addressPlusName = "";
        websiteUrl = "";
        new SearchTask().execute(0);
    }

    private ArrayList<Business> searchYelp(){

        Map<String, String> params = new HashMap<>();

        ArrayList<Business> businesses = new ArrayList<>();

        params.put("term", "restaurants");

        params.put("latitude", Double.toString(mCurrentLocation.getLatitude()));
        params.put("longitude", Double.toString(mCurrentLocation.getLongitude()));

        //adding food type filters
        ArrayList<String> stringsToAdd = new ArrayList<>();
        String categoriesString = "";
        if (mFiltersArray.get(FoodFiltersActivity.Filter.MEXICAN.ordinal()) == 1){
            stringsToAdd.add("mexican");
            stringsToAdd.add("newmexican");
        }
        if (mFiltersArray.get(FoodFiltersActivity.Filter.AMERICAN.ordinal()) == 1){
            stringsToAdd.add("newamerican");
            stringsToAdd.add("tradamerican");
        }
        if (mFiltersArray.get(FoodFiltersActivity.Filter.ASIAN.ordinal()) == 1){
            stringsToAdd.add("panasian");
            stringsToAdd.add("asianfusion");
        }

        for(int i = 0; i < stringsToAdd.size(); i++){
            if(i == 0){
                categoriesString += stringsToAdd.get(i);
            }else{
                categoriesString = categoriesString + "," + stringsToAdd.get(i);
            }
        }

        if(!categoriesString.equals("")){
            params.put("categories", categoriesString);
        }

        //adding pricing filters
        ArrayList<String> pricingFilters = new ArrayList<>();
        String pricingFilterString = "";
        if (mFiltersArray.get(FoodFiltersActivity.Filter.$.ordinal()) == 1){
            pricingFilters.add("1");
        }
        if (mFiltersArray.get(FoodFiltersActivity.Filter.$$.ordinal()) == 1){
            pricingFilters.add("2");
        }
        if (mFiltersArray.get(FoodFiltersActivity.Filter.$$$.ordinal()) == 1){
            pricingFilters.add("3");
        }
        if (mFiltersArray.get(FoodFiltersActivity.Filter.$$$$.ordinal()) == 1){
            pricingFilters.add("4");
        }

        for(int i = 0; i < pricingFilters.size(); i++){
            if(i == 0){
                pricingFilterString += pricingFilters.get(i);
            }else{
                pricingFilterString = pricingFilterString + "," + pricingFilters.get(i);
            }
        }

        if(!pricingFilterString.equals("")){
            params.put("price", pricingFilterString);
        }

        params.put("limit", "50");
        params.put("open_now","true");

        //radius param isnt perfect, need to also filter results for distance
        //but including the param helps keep the list as large as possible
        int radius = (int) 1609.34 * (mSeekBar.getProgress() + PROGRESS_BAR_MIN_VALUE);
        if (radius > 40000){
            radius = 40000;//max value is a little less than 25 miles and over 40k causes error for yelp API
        }
        params.put("radius", Integer.toString(radius));

        try{

            Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
            SearchResponse searchResponse = call.execute().body();
            businesses = searchResponse.getBusinesses();
            //String businessName = businesses.get(0).getName();
            //Toast.makeText(FoodDecisionFragment.this, "Number of businesses: " + Integer.toString(businesses.size()), Toast.LENGTH_SHORT).show();

            //removing things too far away
            ArrayList<Integer> indicesToRemove = new ArrayList<>();
            for (int i = 0; i < businesses.size(); i++){
                if (businesses.get(i).getDistance() > 1609.34 * (mSeekBar.getProgress() + PROGRESS_BAR_MIN_VALUE)){
                    indicesToRemove.add(i);
                }
            }

            //work from right to left
            Collections.reverse(indicesToRemove);

            for (int i = 0; i < indicesToRemove.size(); i++)
            {
                int index = indicesToRemove.get(i);
                businesses.remove(index);
            }
            return businesses;

        }catch(java.io.IOException e) {
            e.printStackTrace();
        }

        return businesses;
    }

    private void pickBusiness( ArrayList<Business> businesses ) {
        if (businesses.size() > 0)
        {
            Random rand = new Random();
            int choice = rand.nextInt(businesses.size());
            mDecisionText.setText(businesses.get(choice).getName());
            mChoicesText.setText("Chosen From " + Integer.toString(businesses.size()) + " businesses.");
            addressPlusName = businesses.get(choice).getLocation().getAddress1() + " " + businesses.get(choice).getName();
            websiteUrl = businesses.get(choice).getUrl(); //yelp website
        }
        else
            {
            mDecisionText.setText("");
            mChoicesText.setText("");
            addressPlusName = "";
            websiteUrl = "";
        }
    }

    //endregion

    //region Maps/Location
    private void openInMaps(){
        if (!addressPlusName.equals(""))
        {
            String url = "http://maps.google.com/maps?daddr=" + addressPlusName;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getActivity(), "No decision made yet!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation()
    {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(10000);
        request.setFastestInterval(5000);
        try
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, new LocationListener()
            {
                @Override
                public void onLocationChanged(Location location)
                {
                    mCurrentLocation = location;
                }
            });
        }
        catch (SecurityException se)
        {
            Toast.makeText(getContext(), "The food decision requires Location permissions", Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    private void openWebsite(){
        if (!websiteUrl.equals("")) {
            String url = websiteUrl;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }else{
            Toast.makeText(getActivity(), "No decision made yet!", Toast.LENGTH_SHORT).show();
        }
    }

    //region AsyncTasks

    private class SearchTask extends AsyncTask<Integer,Void,Void> {

        ArrayList<Business> businesses;

        @Override
        protected Void doInBackground(Integer... params) {
            businesses = searchYelp();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pickBusiness(businesses);
        }
    }

    private class ConnectTask extends AsyncTask<Integer,Void,Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            connectToYelp();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    //endregion
}
