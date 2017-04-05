package csci448.appigators.snappydecisions;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import csci448.appigators.snappydecisions.database.SnappyDecisionsBaseHelper;
import csci448.appigators.snappydecisions.database.SnappyDecisionsCursorWrapper;
import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema.RandomDecisionOptionTable;
import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema.RandomDecisionTable;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class RandomDecisionFragment extends Fragment
{
    RelativeLayout mParentLayout;
    ImageButton mAddButton;
    CheckBox mWeightsCheckbox;
    EditText mNewDecisionText;
    EditText mNewDecisionWeightText;
    LinearLayout mDecisionListLinearLayout;
    Button mSaveButton;
    Button mLoadButton;
    Button mMakeDecisionButton;
    TextView mDecisionText;
    ImageView mLogo;

    ArrayList<EditText> mWeightFields = new ArrayList<>();

    ArrayList<RandomDecisionOption> mOptions = new ArrayList<>();

    private SQLiteDatabase mDatabase;

    /**
     * Sets up listeners on various buttons
     * @param savedInstanceState holds saved variables
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Context context = getContext();
        mDatabase = new SnappyDecisionsBaseHelper(context).getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_random_decision, container, false);

        mParentLayout = (RelativeLayout)v.findViewById(R.id.activity_normal_decision);
        mWeightsCheckbox = (CheckBox)v.findViewById(R.id.weights_checkbox);
        mAddButton = (ImageButton)v.findViewById(R.id.add_button);
        mNewDecisionText = (EditText)v.findViewById(R.id.new_decision_text);
        mNewDecisionWeightText = (EditText)v.findViewById(R.id.weight_text);
        mDecisionListLinearLayout = (LinearLayout)v.findViewById(R.id.decision_list_linear_layout);
        mSaveButton = (Button)v.findViewById(R.id.save_button);
        mLoadButton = (Button)v.findViewById(R.id.load_button);
        mDecisionText = (TextView)v.findViewById(R.id.decision_chosen_text);
        mMakeDecisionButton = (Button)v.findViewById(R.id.make_decision_button);
        mLogo = (ImageView)v.findViewById(R.id.logo);

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
                mParentLayout.requestFocus();
                showSaveDialogue();
            }
        });

        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mParentLayout.requestFocus();
                showLoadDialogue(v);
            }
        });

        mMakeDecisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mParentLayout.requestFocus();
                makeDecision();
            }
        });

        return v;
    }

    //region Save and Load

    /**
     * Handles save options
     * Opens window to take a name to save decision by
     * Checks if name is valid and saves to database
     */
    private void showSaveDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Save Decision as");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String name = input.getText().toString();
                if (name.equals(""))
                {
                    Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkUniqueName(name))
                {
                    saveToDatabase(name);
                }
                else
                {
                    //Could also ask if want to overwrite but that's harder
                    Toast.makeText(getActivity(), "Name must be unique", Toast.LENGTH_SHORT).show();
                }

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

    /**
     * Opens load dialogue that has names of decisions
     * Choosing one will load it and its options
     * @param v
     */
    private void showLoadDialogue(View v)
    {
        PopupMenu loadMenu = new PopupMenu(getContext(), v);

        ArrayList<String> names = getDecisionNames();

        if(names.isEmpty())
        {
            Toast.makeText(getActivity(), "No decisions to load", Toast.LENGTH_SHORT).show();
            return;
        }

        for (String s : names)
        {
            loadMenu.getMenu().add(s);
        }
        loadMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                String name = item.getTitle().toString();
                loadOptions(name);
                return true;
            }
        });

        loadMenu.inflate(R.menu.popup_menu);
        loadMenu.show();
    }

    //endregion

    //region Adding New Decision Function

    /**
     * Adds a new decision to the scroll view by taking the data from the new fields
     * Stores new decision in arraylist of options
     * Sets focus listeners on new text fields
     */
    private void addNewDecision(String option, String weight)
    {
        final RandomDecisionOption opt = new RandomDecisionOption();

        if(weight.equals(""))
        {
            weight = "1";
        }

        mLogo.setVisibility(GONE);

        final LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton removeButton = new ImageButton(getContext());
        removeButton.setImageResource(R.drawable.remove);
        removeButton.setBackgroundColor(Color.TRANSPARENT);
        removeButton.setPadding(35,0,35,0);
        ll.addView(removeButton);

        final EditText decision_et = new EditText(getContext());
        decision_et.setText(option);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
        ll.addView(decision_et, lp2);

        final EditText weight_et = new EditText(getContext());
        weight_et.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(1);
        weight_et.setFilters(filters);
        weight_et.setText(weight);
        //weight_et.setEms(3);
        weight_et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        if(!mWeightsCheckbox.isChecked())
        {
            weight_et.setVisibility(GONE);
        }
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(187, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
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
                if(mOptions.isEmpty())
                {
                    mLogo.setVisibility(VISIBLE);
                }
            }
        });

        decision_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                EditText et = (EditText)v;
                if(!hasFocus)
                {
                    if(et.getText().equals(""))
                    {
                        String oldOption = mOptions.get(mOptions.indexOf(opt)).getOption();
                        et.setText(oldOption);
                    }
                    else
                    {
                        mOptions.get(mOptions.indexOf(opt)).setOption(et.getText().toString());
                    }
                }
            }
        });

        weight_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                EditText et = (EditText)v;
                if(!hasFocus)
                {
                    if(et.getText().equals(""))
                    {
                        et.setText("0");
                    }
                }
                mOptions.get(mOptions.indexOf(opt)).setWeight(Integer.parseInt(et.getText().toString()));
            }
        });

        mWeightFields.add(weight_et);

    }

    /**
     * Resets the fields for adding a new decision
     */
    private void clearNewFields()
    {
        mNewDecisionText.setText("");
        mNewDecisionWeightText.setText("");
    }

    //endregion

    /**
     * If weights are used: Takes into account the weights through a basic weight algorithm
     * Otherwise chooses randomly
     */
    private void makeDecision()
    {
        Random rand = new Random();
        if (mOptions.size() > 0)
        {
            int choice;
            RandomDecisionOption optionChosen = null;
            if(mWeightsCheckbox.isChecked())
            {
                Collections.shuffle(mOptions); //I don't know if this is needed
                int sumWeights = 0;
                for (RandomDecisionOption opt : mOptions)
                {
                    sumWeights += opt.getWeight();
                }
                choice = rand.nextInt(sumWeights);
                for (RandomDecisionOption opt : mOptions)
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
            Toast.makeText(getActivity(), "No decisions to choose from!", Toast.LENGTH_SHORT).show();
        }
    }

    //region Helper functions

    private void showEmptyTextToast()
    {
        Toast.makeText(getActivity(), "Option cannot be blank", Toast.LENGTH_SHORT).show();

    }

    private void changeWeightVisibility(boolean isChecked)
    {
        mNewDecisionWeightText.setEnabled(isChecked);
        int visibility = VISIBLE;
        if (!isChecked)
        {
            visibility = GONE;
        }
        for (EditText et : mWeightFields)
        {
            et.setVisibility(visibility);
        }

        mNewDecisionWeightText.setVisibility(visibility);
    }

    /**
     * Checks if the name is already in the database
     * @param name name trying to be saved
     * @return true if name is not in database already
     */
    private boolean checkUniqueName(String name)
    {
        ArrayList<String> allNames = getDecisionNames();

        if(allNames.contains(name))
        {
            return false;
        }
        return true;
    }

    //endregion

    //region Database Stuff

    /**
     * Generic query builder
     * @param dbName name of table
     * @param whereClause where clause string
     * @param whereArgs args for whereClause
     * @return retur cursor in database
     */
    private SnappyDecisionsCursorWrapper queryTable(String dbName, String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(dbName,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new SnappyDecisionsCursorWrapper(cursor);
    }

    /**
     * Content values for DecisionTable
     * @param name name to associate with decision
     * @return ContentValues with name in it
     */
    private ContentValues getDecisionContentValues(String name)
    {
        ContentValues values = new ContentValues();
        values.put(RandomDecisionTable.Cols.NAME, name);
        return values;
    }

    /**
     * Content values for Options
     * @param name name for decision
     * @param opt option to get weight and text from
     * @return ContentValues with text, weight, and name of decision
     */
    private ContentValues getOptionContentValues(String name, RandomDecisionOption opt)
    {
        ContentValues values = new ContentValues();
        values.put(RandomDecisionOptionTable.Cols.DECISION, name);
        values.put(RandomDecisionOptionTable.Cols.OPTION, opt.getOption());
        values.put(RandomDecisionOptionTable.Cols.WEIGHT, opt.getWeight());
        return values;
    }

    /**
     * Saves decision and options to database
     * @param name primary key of RandomDecisionTable
     */
    private void saveToDatabase(String name)
    {
        ContentValues decisionValues = getDecisionContentValues(name);
        mDatabase.insert(RandomDecisionTable.NAME, null, decisionValues);

        for(RandomDecisionOption opt : mOptions)
        {
            ContentValues optionValues = getOptionContentValues(name, opt);
            mDatabase.insert(RandomDecisionOptionTable.NAME, null, optionValues);
        }
    }

    /**
     * Runs through cursor to get names of all decisions
     * @return list of strings
     */
    private ArrayList<String> getDecisionNames()
    {
        SnappyDecisionsCursorWrapper cursor = queryTable(RandomDecisionTable.NAME, null, null);
        ArrayList<String> allNames = new ArrayList<>();
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                allNames.add(cursor.getRandomDecisionName());
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return allNames;
    }

    /**
     * Loads options for a given decision
     * @param name Name of decision for the options
     */
    private void loadOptions(String name)
    {
        SnappyDecisionsCursorWrapper cursor = queryTable(RandomDecisionOptionTable.NAME, RandomDecisionOptionTable.Cols.DECISION + " = ?", new String[]{name});
        clearNewFields();
        mOptions.clear();
        mWeightFields.clear();
        mDecisionListLinearLayout.removeAllViews();
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                String option = cursor.getRandomDecisionOptionText();
                String weight = cursor. getRandomDecisionOptionWeight();
                addNewDecision(option, weight);
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
    }

    //endregion
}
