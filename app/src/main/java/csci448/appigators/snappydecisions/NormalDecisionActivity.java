package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NormalDecisionActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener
{
    ImageButton mAddButton;
    CheckBox mWeightsCheckbox;
    EditText mNewDecisionText;
    EditText mNewDecisionWeightText;
    LinearLayout mDecisionListLinearLayout;
    Button mSaveButton;
    Button mLoadButton;
    Button mMakeDecisionButton;
    TextView mDecisionText;
    ArrayList<EditText> mWeightFields = new ArrayList<>();

    ArrayList<NormalDecisionOption> mOptions = new ArrayList<>();

    //region Static Methods
    /**
     * Creates new intent
     * @param packageContext
     * @return intent containing activity to be started
     */
    public static Intent newIntent(Context packageContext)
    {
        Intent i = new Intent(packageContext, NormalDecisionActivity.class);
        return i;
    }

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_decision);

        mWeightsCheckbox = (CheckBox)findViewById(R.id.weights_checkbox);
        mAddButton = (ImageButton)findViewById(R.id.add_button);
        mNewDecisionText = (EditText)findViewById(R.id.new_decision_text);
        mNewDecisionWeightText = (EditText)findViewById(R.id.weight_text);
        mDecisionListLinearLayout = (LinearLayout) findViewById(R.id.decision_list_linear_layout);
        mSaveButton = (Button)findViewById(R.id.save_button);
        mLoadButton = (Button)findViewById(R.id.load_button);
        mDecisionText = (TextView)findViewById(R.id.decision_chosen_text);
        mMakeDecisionButton = (Button)findViewById(R.id.make_decision_button);

        mWeightsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                changeWeightVisibility(isChecked);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!mNewDecisionText.getText().toString().equals(""))
                {
                    String option = mNewDecisionText.getText().toString();
                    String weight = mNewDecisionWeightText.getText().toString();
                    addNewDecision(option, weight);
                    clearNewFields();
                }
                else
                {
                    showEmptyTextToast();
                }
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showSaveDialogue();
            }
        });

        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PopupMenu popupMenu = new PopupMenu(NormalDecisionActivity.this, v);
                popupMenu.setOnMenuItemClickListener(NormalDecisionActivity.this);
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

        mMakeDecisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                makeDecision();
            }
        });
    }

    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(NormalDecisionActivity.this, "Would change to saved decisions/weights combo, but not in alpha", Toast.LENGTH_SHORT).show();
        return true;
    }


    void showSaveDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Current Decisions and Weights As");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(NormalDecisionActivity.this, "Would save current settings and add as an option in the load popup, but not in alpha", Toast.LENGTH_SHORT).show();
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

    //region Private methods

    /**
     * Adds a new decision to the scroll view by taking the data from the new fields
     * Stores new decision in arraylist of options
     */
    private void addNewDecision(String option, String weight)
    {
        final NormalDecisionOption opt = new NormalDecisionOption();

        if(weight.equals(""))
        {
            weight = "1";
        }

        final LinearLayout ll = new LinearLayout(NormalDecisionActivity.this);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton removeButton = new ImageButton(NormalDecisionActivity.this);
        removeButton.setImageResource(android.R.drawable.ic_delete);
        removeButton.setBackgroundColor(Color.TRANSPARENT);
        ll.addView(removeButton);

        EditText decision_et = new EditText(NormalDecisionActivity.this);
        decision_et.setText(option);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
        ll.addView(decision_et, lp2);

        EditText weight_et = new EditText(NormalDecisionActivity.this);
        weight_et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        weight_et.setText(weight);
        weight_et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        if(!mWeightsCheckbox.isChecked())
        {
            weight_et.setVisibility(GONE);
        }
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        ll.addView(weight_et, lp3);

        mDecisionListLinearLayout.addView(ll);

        opt.setOption(option);
        opt.setWeight(Integer.parseInt(weight));
        mOptions.add(opt);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDecisionListLinearLayout.removeView(ll);
                mOptions.remove(opt);
            }
        });

        //TextWatcher updates the values in the option variable
        decision_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s != "")
                {
                    mOptions.get(mOptions.indexOf(opt)).setOption(s.toString());
                }
                else
                {
                    showEmptyTextToast();
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        weight_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.toString().equals(""))
                {
                    s= "1";
                }
                mOptions.get(mOptions.indexOf(opt)).setWeight(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mWeightFields.add(weight_et);

    }

    private void clearNewFields()
    {
        mNewDecisionText.setText("");
        mNewDecisionWeightText.setText("");
    }

    private void makeDecision()
    {
        Random rand = new Random();
        if (mOptions.size() > 0)
        {
            int choice;
            NormalDecisionOption optionChosen = null;
            if(mWeightsCheckbox.isChecked())
            {
                int sumWeights = 0;
                for (NormalDecisionOption opt : mOptions)
                {
                    sumWeights += opt.getWeight();
                }
                choice = rand.nextInt(sumWeights);
                for (NormalDecisionOption opt : mOptions)
                {
                    int weight = opt.getWeight();
                    if(choice < weight)
                    {
                        optionChosen = opt;
                        break;
                    }
                    choice -= weight;
                }
            }
            else
            {
                choice = rand.nextInt(mOptions.size());
                optionChosen = mOptions.get(choice);
            }
            mDecisionText.setText(optionChosen.getOption());
        }
        else
        {
            Toast.makeText(NormalDecisionActivity.this, "No decisions to choose from!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEmptyTextToast()
    {
        Toast.makeText(NormalDecisionActivity.this, "Option cannot be blank", Toast.LENGTH_SHORT).show();

    }

    private void changeWeightVisibility(boolean isChecked)
    {
        mNewDecisionWeightText.setEnabled(isChecked);
        int visiblity;
        if (isChecked)
        {
            visiblity = VISIBLE;
        }
        else
        {
            visiblity = GONE;
        }
        for (EditText et : mWeightFields)
        {
            et.setVisibility(visiblity);
        }
    }

    //endregion
}
