package csci448.appigators.snappydecisions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Streebit on 3/3/17.
 */

public class FoodFiltersActivity extends AppCompatActivity
{
    public enum Filter{AMERICAN, ASIAN, MEXICAN, $, $$, $$$, $$$$};

    CheckBox mAmericanFilter;
    CheckBox mAsianFilter;
    CheckBox mMexicanFilter;
    CheckBox m$Filter;
    CheckBox m$$Filter;
    CheckBox m$$$Filter;
    CheckBox m$$$$Filter;
    private int NUM_FILTERS = Filter.values().length;

    private final static String FILTERS_KEY = "FILTERS_KEY";
    private ArrayList<Integer> mFiltersArray = new ArrayList<Integer>(Collections.nCopies(NUM_FILTERS, 0));

    private boolean integerToBoolean(int x){
        return x > 0 ? true: false;
    }

    private int booleanToInteger(boolean b){
        return b ? 1 : 0;
    }

    private void setReturningIntent(){
        final Intent returnIntent = new Intent();
        returnIntent.putIntegerArrayListExtra(FILTERS_KEY, mFiltersArray);
        setResult(Activity.RESULT_OK, returnIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_filters);

        //for saving filters
        Intent savedDataIntent = getIntent();
        mFiltersArray = savedDataIntent.getIntegerArrayListExtra(FILTERS_KEY);

        mAmericanFilter = (CheckBox) findViewById(R.id.american_filter);
        mAmericanFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.AMERICAN.ordinal())));

        mAsianFilter = (CheckBox) findViewById(R.id.asian_filter);
        mAsianFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.ASIAN.ordinal())));

        mMexicanFilter = (CheckBox) findViewById(R.id.mexican_filter);
        mMexicanFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.MEXICAN.ordinal())));

        m$Filter = (CheckBox) findViewById(R.id.low_filter);
        m$Filter.setChecked(integerToBoolean(mFiltersArray.get(Filter.$.ordinal())));

        m$$Filter = (CheckBox) findViewById(R.id.midde_filter);
        m$$Filter.setChecked(integerToBoolean(mFiltersArray.get(Filter.$$.ordinal())));

        m$$$Filter = (CheckBox) findViewById(R.id.high_filter);
        m$$$Filter.setChecked(integerToBoolean(mFiltersArray.get(Filter.$$$.ordinal())));

        m$$$$Filter = (CheckBox) findViewById(R.id.highest_filter);
        m$$$$Filter.setChecked(integerToBoolean(mFiltersArray.get(Filter.$$$$.ordinal())));

        //for sending result back


        mAmericanFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.AMERICAN.ordinal(), booleanToInteger(mAmericanFilter.isChecked()));
                setReturningIntent();
            }
        });

        mMexicanFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.MEXICAN.ordinal(), booleanToInteger(mMexicanFilter.isChecked()));
                setReturningIntent();
            }
        });

        mAsianFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.ASIAN.ordinal(), booleanToInteger(mAsianFilter.isChecked()));
                setReturningIntent();
            }
        });

        m$Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.$.ordinal(), booleanToInteger(m$Filter.isChecked()));
                setReturningIntent();
            }
        });

        m$$Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.$$.ordinal(), booleanToInteger(m$$Filter.isChecked()));
                setReturningIntent();
            }
        });

        m$$$Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.$$$.ordinal(), booleanToInteger(m$$$Filter.isChecked()));
                setReturningIntent();
            }
        });

        m$$$$Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.$$$$.ordinal(), booleanToInteger(m$$$$Filter.isChecked()));
                setReturningIntent();
            }
        });
    }

    /**
     * Creates new intent
     * @param packageContext
     * @return intent containing activity to be started
     */
    public static Intent newIntent(Context packageContext)
    {
        Intent i = new Intent(packageContext, FoodFiltersActivity.class);
        return i;
    }
}
