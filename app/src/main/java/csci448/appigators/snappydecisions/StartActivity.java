package csci448.appigators.snappydecisions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class StartActivity extends AppCompatActivity
{
    Button mRandomDecisionButton;
    Button mFoodNearMeButton;
    Button mProductDecisionButton;


    /**
     * Creates the activity, sets the view, hooks up buttons
     * @param savedInstanceState Holds data from saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_start);

        mRandomDecisionButton = (Button)findViewById(R.id.normal_decision_button);
        mFoodNearMeButton = (Button)findViewById(R.id.food_decision_button);
        mProductDecisionButton = (Button)findViewById(R.id.product_decision_button);

        mRandomDecisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = RandomDecisionActivity.newIntent(StartActivity.this);
                startActivity(i);
            }
        });

        mFoodNearMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = FoodDecisionActivity.newIntent(StartActivity.this);
                startActivity(i);
            }
        });

        mProductDecisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = ProductDecisionActivity.newIntent(StartActivity.this);
                startActivity(i);
            }
        });
    }
}
