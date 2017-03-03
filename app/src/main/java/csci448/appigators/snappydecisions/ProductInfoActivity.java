package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Dan on 3/3/2017.
 */

public class ProductInfoActivity extends AppCompatActivity {

    private TextView mProductNameView;
    private TextView mRatingTextView;
    private TextView mDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        mProductNameView = (TextView)findViewById(R.id.product_info_view_product_name);
        mRatingTextView = (TextView)findViewById(R.id.product_rating_view);
        mDescriptionTextView = (TextView)findViewById(R.id.product_description_view);

        mProductNameView.setText("Product name");
        mRatingTextView.setText("5");
        mDescriptionTextView.setText("Product desription displayed here");
    }

    /**
     * Creates new intent
     * @param packageContext
     * @return intent containing activity to be started
     */
    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, ProductInfoActivity.class);
        return i;
    }
}
