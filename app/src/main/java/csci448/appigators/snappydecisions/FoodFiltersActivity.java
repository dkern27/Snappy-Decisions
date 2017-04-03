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
    public enum Filter{AMERICAN, ASIAN, MEXICAN, $, $$, $$$};

    CheckBox mAmericanFilter;
    CheckBox mAsianFilter;
    CheckBox mMexicanFilter;
    CheckBox m$Filter;
    CheckBox m$$Filter;
    CheckBox m$$$Filter;

    private final static String FILTERS_KEY = "FILTERS_KEY";
    private ArrayList<Integer> mFiltersArray = new ArrayList<Integer>(Collections.nCopies(6, 0));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_filters);

        //for saving filter
        Intent savedDataIntent = getIntent();
        mFiltersArray = savedDataIntent.getIntegerArrayListExtra(FILTERS_KEY);

        mAmericanFilter = (CheckBox) findViewById(R.id.american_filter);


        mAsianFilter = (CheckBox) findViewById(R.id.asian_filter);


        mMexicanFilter = (CheckBox) findViewById(R.id.mexican_filter);
        boolean myBool = mFiltersArray.get(Filter.MEXICAN.ordinal()) > 0 ? true : false ;
        mMexicanFilter.setChecked(myBool);

        m$Filter = (CheckBox) findViewById(R.id.low_filter);


        m$$Filter = (CheckBox) findViewById(R.id.midde_filter);


        m$$$Filter = (CheckBox) findViewById(R.id.high_filter);

        final Intent returnIntent = new Intent();

        mMexicanFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int myInt = (mMexicanFilter.isChecked()) ? 1 : 0;
                mFiltersArray.add(Filter.MEXICAN.ordinal(), myInt);
                returnIntent.putIntegerArrayListExtra(FILTERS_KEY, mFiltersArray);
                setResult(Activity.RESULT_OK, returnIntent);
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
