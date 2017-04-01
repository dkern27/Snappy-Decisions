package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ProductDecisionActivity extends AppCompatActivity {

    private ImageButton mAddProductButton;
    private EditText mAddProductText;
    private LinearLayout mProductList;
    private ArrayList<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_decision);

        mAddProductText = (EditText)findViewById(R.id.new_product_text);
        mProductList = (LinearLayout) findViewById(R.id.product_list_linear_layout);

        mAddProductButton = (ImageButton) findViewById(R.id.add_product_button);
        mAddProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addNewProduct();
                clearNewFields();
            }
        });

    }

    /**
     * Creates new intent
     * @param packageContext
     * @return intent containing activity to be started
     */
    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, ProductDecisionActivity.class);
        return i;
    }

    private void addNewProduct() {
        final String productName = mAddProductText.getText().toString();
        EditText textView = new EditText(ProductDecisionActivity.this);
        textView.setText(productName);

        LinearLayout productLayout = new LinearLayout(ProductDecisionActivity.this);
        productLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 3);

        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
                r.getDisplayMetrics());
        params.setMargins(0, 0, px, px);
        productLayout.addView(textView, params);

        ImageButton imageButton = new ImageButton(ProductDecisionActivity.this);
        imageButton.setImageResource(R.drawable.arrow_right);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("https://www.amazon.com/s/" +
                        "ref=nb_sb_noss?" +
                        "url=search-alias%3Daps&" +
                        "field-keywords=" +
                        convertToUTF8(productName));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
//                Intent i = ProductInfoActivity.newIntent(ProductDecisionActivity.this);
//                startActivity(i);
            }
        });
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        productLayout.addView(imageButton, params);

        mProductList.addView(productLayout);
    }

    private void clearNewFields() {
        mAddProductText.setText("");
    }

    public String convertToUTF8(String s) {
        String utf8Encoded = null;
        try {
            utf8Encoded = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return utf8Encoded;
    }
}
