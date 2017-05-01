package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity
{
    TextView mRandomDescription;

    public static Intent newIntent(Context packageContext)
    {
        Intent i = new Intent(packageContext, HelpActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mRandomDescription = (TextView)findViewById(R.id.help_random_description);
        Spannable span = (Spannable)mRandomDescription.getText();
        ForegroundColorSpan greenSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary));
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        span.setSpan(new StyleSpan(Typeface.BOLD), 58, 62, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        span.setSpan(greenSpan, 58, 62, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        span.setSpan(new StyleSpan(Typeface.BOLD), 112, 117, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        span.setSpan(redSpan, 112, 117, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

    }
}
