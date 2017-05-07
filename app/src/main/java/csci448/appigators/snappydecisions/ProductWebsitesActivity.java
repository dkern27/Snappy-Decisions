package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.UnsupportedEncodingException;

/**
 * Created by Dan on 5/7/2017.
 */

public class ProductWebsitesActivity extends AppCompatActivity {
    private static final String PRODUCT_NAME_KEY = "product_name";

    private enum Website {AMAZON, NEWEGG, EBAY}

    private LinearLayout mWebsiteList;
    private LinearLayout mWebsiteLayout;
    private String mProductName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_websites);

        mWebsiteList = (LinearLayout)findViewById(R.id.website_list_linear_layout);

        Intent i = getIntent();
        mProductName = i.getStringExtra(PRODUCT_NAME_KEY);

        setupWebsiteList();
    }

    private void setupWebsiteList() {
        addWebsite(Website.AMAZON);
        addWebsite(Website.NEWEGG);
        addWebsite(Website.EBAY);
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

    public static Intent newIntent(Context packageContext, String productName) {
        Intent i = new Intent(packageContext, ProductWebsitesActivity.class);
        i.putExtra(PRODUCT_NAME_KEY, productName);
        return i;
    }

    private void addWebsite(final Website website) {
        mWebsiteLayout = new LinearLayout(this.getApplicationContext());
        mWebsiteLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 3);

        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
                r.getDisplayMetrics());
        params.setMargins(0, 0, px, px);

        String websiteName = "";
        switch(website) {
            case AMAZON:
                websiteName = "Amazon";
                break;
            case NEWEGG:
                websiteName = "Newegg";
                break;
            case EBAY:
                websiteName = "Ebay";
                break;
        }

        EditText textView = new EditText(this.getApplicationContext());
        textView.setText(websiteName);
        textView.setKeyListener(null);
        mWebsiteLayout.addView(textView, params);

        ImageButton imageButton = new ImageButton(this.getApplicationContext());
        imageButton.setImageResource(R.drawable.arrow_right);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Uri uri = getUri(website);
                Intent intent = ProductInfoActivity.newIntent(getApplicationContext(), uri);
                startActivity(intent);
            }
        });
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        mWebsiteLayout.addView(imageButton, params);

        mWebsiteList.addView(mWebsiteLayout);
    }

    private Uri getUri(Website website) {
        Uri uri;
        switch(website) {
            case AMAZON:
                uri = Uri.parse("https://www.amazon.com/s/" +
                        "ref=nb_sb_noss?" +
                        "url=search-alias%3Daps&" +
                        "field-keywords=" +
                        convertToUTF8(mProductName));
                break;
            case NEWEGG:
                uri = Uri.parse("https://www.newegg.com/" +
                        "Product/ProductList.aspx?Submit=ENE" +
                        "&DEPA=0&Order=BESTMATCH" +
                        "&Description=" +
                        convertToUTF8(mProductName) +
                        "&N=-1&isNodeId=1");
                break;
            case EBAY:
                uri = Uri.parse("http://www.ebay.com/sch/i.html?_from=R40&" +
                        "_nkw=" +
                        convertToUTF8(mProductName) +
                        "&_sacat=0");
                break;
            default:
                uri = Uri.EMPTY;
        }

        return uri;
    }
}
