package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class FoodDecisionActivity extends AppCompatActivity
{
    Button mFiltersButton;
    Button mSaveButton;
    Button mLoadButton;
    Button mMakeDecisionButton;
    SeekBar mSeekBar;
    TextView mDistanceText;

    void showSaveDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Current Settings As");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FoodDecisionActivity.this, "Current Settings Saved (NOT IN ALPHA)", Toast.LENGTH_SHORT).show();
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

        mFiltersButton = (Button)findViewById(R.id.filters_button);
        mSaveButton = (Button)findViewById(R.id.save_button);
        mLoadButton = (Button)findViewById(R.id.load_button);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mDistanceText = (TextView) findViewById(R.id.distance_text);
        mDistanceText.setText(getString(R.string.distance, mSeekBar.getProgress()));
        mMakeDecisionButton = (Button)findViewById(R.id.make_decision_button);

        mMakeDecisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(FoodDecisionActivity.this, "Would open up maps, drop a pin based on distance and filters", Toast.LENGTH_SHORT).show();
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
            public void onClick(View v)
            {
                Toast.makeText(FoodDecisionActivity.this, "Would open dialogue to load a saved distance/filter combo", Toast.LENGTH_SHORT).show();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                mDistanceText.setText(getString(R.string.distance, progress));
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
