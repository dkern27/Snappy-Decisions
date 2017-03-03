package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class NormalDecisionActivity extends AppCompatActivity
{
    ImageButton addButton;
    EditText newDecisionText;
    EditText newDecisionWeightText;
    ScrollView decisionListView;
    LinearLayout decisionListLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_decision);

        addButton = (ImageButton)findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LinearLayout ll = new LinearLayout(NormalDecisionActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);

                EditText decision_et = new EditText(NormalDecisionActivity.this);
                decision_et.setText(newDecisionText.getText());
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
                ll.addView(decision_et, lp1);

                EditText weight_et = new EditText(NormalDecisionActivity.this);
                weight_et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                weight_et.setText(newDecisionWeightText.getText());
                weight_et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                ll.addView(weight_et, lp2);

                decisionListLinearLayout.addView(ll);

                newDecisionText.setText("");
                newDecisionWeightText.setText("");
            }
        });
        newDecisionText = (EditText)findViewById(R.id.new_decision_text);
        newDecisionWeightText = (EditText)findViewById(R.id.weight_text);
        decisionListView = (ScrollView)findViewById(R.id.decision_list_view);
        decisionListLinearLayout = (LinearLayout) findViewById(R.id.decision_list_linear_layout);
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
}
