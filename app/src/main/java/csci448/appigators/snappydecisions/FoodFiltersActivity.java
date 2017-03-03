package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

/**
 * Created by Streebit on 3/3/17.
 */

public class FoodFiltersActivity extends AppCompatActivity
{
    CheckBox mAsianFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_filters);

        mAsianFilter = (CheckBox) findViewById(R.id.asian_filter);
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
