package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NormalDecisionActivity extends AppCompatActivity
{
    ImageButton mAddButton;
    EditText mNewDecisionText;
    EditText mNewDecisionWeightText;
    LinearLayout mDecisionListLinearLayout;
    Button mSaveButton;
    Button mLoadButton;
    Button mMakeDecisionButton;
    TextView mDecisionText;

    ArrayList<NormalDecisionOption> mOptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_decision);

        mAddButton = (ImageButton)findViewById(R.id.add_button);
        mNewDecisionText = (EditText)findViewById(R.id.new_decision_text);
        mNewDecisionWeightText = (EditText)findViewById(R.id.weight_text);
        mDecisionListLinearLayout = (LinearLayout) findViewById(R.id.decision_list_linear_layout);
        mSaveButton = (Button)findViewById(R.id.save_button);
        mLoadButton = (Button)findViewById(R.id.load_button);
        mDecisionText = (TextView)findViewById(R.id.decision_chosen_text);
        mMakeDecisionButton = (Button)findViewById(R.id.make_decision_button);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addNewDecision();
                clearNewFields();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(NormalDecisionActivity.this, "Decision saved (not)", Toast.LENGTH_SHORT).show();
            }
        });

        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(NormalDecisionActivity.this, "Decision was NOT loaded", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(packageContext, NormalDecisionActivity.class);
        return i;
    }

    /**
     * Adds a new decision to the scroll view by taking the data from the new fields
     * Stores new decision in arraylist of options
     */
    private void addNewDecision()
    {
        String option = mNewDecisionText.getText().toString();
        String weight = mNewDecisionWeightText.getText().toString();
        if(weight.equals(""))
        {
            weight = "1";
        }

        LinearLayout ll = new LinearLayout(NormalDecisionActivity.this);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        EditText decision_et = new EditText(NormalDecisionActivity.this);
        decision_et.setText(option);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
        ll.addView(decision_et, lp1);

        EditText weight_et = new EditText(NormalDecisionActivity.this);
        weight_et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        weight_et.setText(weight);
        weight_et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        ll.addView(weight_et, lp2);

        mDecisionListLinearLayout.addView(ll);

        mOptions.add(new NormalDecisionOption(option, Double.parseDouble(weight)));
    }

    private void clearNewFields()
    {
        mNewDecisionText.setText("");
        mNewDecisionWeightText.setText("");
    }
}
