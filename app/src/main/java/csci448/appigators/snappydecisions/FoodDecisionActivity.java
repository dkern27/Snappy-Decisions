package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class FoodDecisionActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener
{
    Button mFiltersButton;
    Button mSaveButton;
    Button mLoadButton;
    Button mMakeDecisionButton;
    SeekBar mSeekBar;
    TextView mDistanceText;

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
