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
    public enum Filter{
        AMERICAN,
        ASIAN,
        GREEK,
        INDIAN,
        ITALIAN,
        MEXICAN,
        BBQ,
        BREAKFAST,
        BURGERS,
        CHICKEN_WINGS,
        SANDWICHES,
        STEAK,
        SUSHI,
        $,
        $$,
        $$$,
        $$$$
    }

    //nationality check boxes
    CheckBox mAmericanFilter;
    CheckBox mAsianFilter;
    CheckBox mGreekFilter;
    CheckBox mIndianFilter;
    CheckBox mItalianFilter;
    CheckBox mMexicanFilter;

    //food type check boxes
    CheckBox mSandwichesFilter;
    CheckBox mBbqFilter;
    CheckBox mBurgersFilter;
    CheckBox mBreakfastFilter;
    CheckBox mChickenWingsFilter;
    CheckBox mSushiFilter;
    CheckBox mSteakFilter;

    //pricing check boxes
    CheckBox m$Filter;
    CheckBox m$$Filter;
    CheckBox m$$$Filter;
    CheckBox m$$$$Filter;
    private int NUM_FILTERS = Filter.values().length;

    private final static String FILTERS_KEY = "FILTERS_KEY";
    private ArrayList<Integer> mFiltersArray = new ArrayList<Integer>(Collections.nCopies(NUM_FILTERS, 0));

    /**
     * Converts integer to boolean
     * @param x integert oconvert
     * @return true or false
     */
    private boolean integerToBoolean(int x){
        return x > 0 ? true: false;
    }

    /**
     * Converts boolean to integer
     * @param b boolean to convert
     * @return 1 or 0
     */
    private int booleanToInteger(boolean b){
        return b ? 1 : 0;
    }

    /**
     * Makes return intent with filter results
     */
    private void setReturningIntent(){
        final Intent returnIntent = new Intent();
        returnIntent.putIntegerArrayListExtra(FILTERS_KEY, mFiltersArray);
        setResult(Activity.RESULT_OK, returnIntent);
    }

    /**
     * Creates view, hooks up listeners
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_filters);

        //for saving filters
        Intent savedDataIntent = getIntent();
        mFiltersArray = savedDataIntent.getIntegerArrayListExtra(FILTERS_KEY);

        //nationality checkboxes
        mAmericanFilter = (CheckBox) findViewById(R.id.american_filter);
        mAmericanFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.AMERICAN.ordinal())));

        mAsianFilter = (CheckBox) findViewById(R.id.asian_filter);
        mAsianFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.ASIAN.ordinal())));

        mGreekFilter = (CheckBox) findViewById(R.id.greek_filter);
        mGreekFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.GREEK.ordinal())));

        mIndianFilter = (CheckBox) findViewById(R.id.indian_filter);
        mIndianFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.INDIAN.ordinal())));

        mItalianFilter = (CheckBox) findViewById(R.id.italian_filter);
        mItalianFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.ITALIAN.ordinal())));

        mMexicanFilter = (CheckBox) findViewById(R.id.mexican_filter);
        mMexicanFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.MEXICAN.ordinal())));

        //type of food checkboxes
        mBbqFilter = (CheckBox) findViewById(R.id.bbq_filter);
        mBbqFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.BBQ.ordinal())));

        mBreakfastFilter = (CheckBox) findViewById(R.id.breakfast_filter);
        mBreakfastFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.BREAKFAST.ordinal())));

        mBurgersFilter = (CheckBox) findViewById(R.id.burger_filter);
        mBurgersFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.BURGERS.ordinal())));

        mChickenWingsFilter = (CheckBox) findViewById(R.id.chicken_wings_filter);
        mChickenWingsFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.CHICKEN_WINGS.ordinal())));

        mSandwichesFilter = (CheckBox) findViewById(R.id.sandwiches_filter);
        mSandwichesFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.SANDWICHES.ordinal())));

        mSteakFilter = (CheckBox) findViewById(R.id.steak_filter);
        mSteakFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.STEAK.ordinal())));

        mSushiFilter = (CheckBox) findViewById(R.id.sushi_filter);
        mSushiFilter.setChecked(integerToBoolean(mFiltersArray.get(Filter.SUSHI.ordinal())));

        //pricing checkboxes
        m$Filter = (CheckBox) findViewById(R.id.low_filter);
        m$Filter.setChecked(integerToBoolean(mFiltersArray.get(Filter.$.ordinal())));

        m$$Filter = (CheckBox) findViewById(R.id.midde_filter);
        m$$Filter.setChecked(integerToBoolean(mFiltersArray.get(Filter.$$.ordinal())));

        m$$$Filter = (CheckBox) findViewById(R.id.high_filter);
        m$$$Filter.setChecked(integerToBoolean(mFiltersArray.get(Filter.$$$.ordinal())));

        m$$$$Filter = (CheckBox) findViewById(R.id.highest_filter);
        m$$$$Filter.setChecked(integerToBoolean(mFiltersArray.get(Filter.$$$$.ordinal())));

        //for sending result back

        //nationality listeners
        mAmericanFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.AMERICAN.ordinal(), booleanToInteger(mAmericanFilter.isChecked()));
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

        mGreekFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.GREEK.ordinal(), booleanToInteger(mGreekFilter.isChecked()));
                setReturningIntent();
            }
        });

        mIndianFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.INDIAN.ordinal(), booleanToInteger(mIndianFilter.isChecked()));
                setReturningIntent();
            }
        });

        mItalianFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.ITALIAN.ordinal(), booleanToInteger(mItalianFilter.isChecked()));
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

        //type of food listeners
        mBbqFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.BBQ.ordinal(), booleanToInteger(mBbqFilter.isChecked()));
                setReturningIntent();
            }
        });

        mBreakfastFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.BREAKFAST.ordinal(), booleanToInteger(mBreakfastFilter.isChecked()));
                setReturningIntent();
            }
        });

        mBurgersFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.BURGERS.ordinal(), booleanToInteger(mBurgersFilter.isChecked()));
                setReturningIntent();
            }
        });

        mChickenWingsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.CHICKEN_WINGS.ordinal(), booleanToInteger(mChickenWingsFilter.isChecked()));
                setReturningIntent();
            }
        });

        mSandwichesFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.SANDWICHES.ordinal(), booleanToInteger(mSandwichesFilter.isChecked()));
                setReturningIntent();
            }
        });

        mSteakFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.STEAK.ordinal(), booleanToInteger(mSteakFilter.isChecked()));
                setReturningIntent();
            }
        });

        mSushiFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFiltersArray.set(Filter.SUSHI.ordinal(), booleanToInteger(mSushiFilter.isChecked()));
                setReturningIntent();
            }
        });

        //pricing listeners
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
