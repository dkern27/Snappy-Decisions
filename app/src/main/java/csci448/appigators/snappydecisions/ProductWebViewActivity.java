package csci448.appigators.snappydecisions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.ecommerce.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 3/3/2017.
 */

public class ProductWebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private Uri mUri;
    private List<String> mAdditionalProducts = new ArrayList<>();
    private String mProductName;
    private static final String NEW_PRODUCTS_KEY = "new_products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        mWebView = (WebView)findViewById(R.id.product_web_view);

        mUri = getIntent().getData();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mWebView.loadUrl(mUri.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.web_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.web_view_add_product:
                addProduct();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Creates new intent
     * @param packageContext
     * @return intent containing activity to be started
     */
    public static Intent newIntent(Context packageContext, Uri uri) {
        Intent i = new Intent(packageContext, ProductWebViewActivity.class);
        i.setData(uri);
        return i;
    }

    private void addProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Product Name");

        // Set `EditText` to `dialog`. You can add `EditText` from `xml` too.
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        mProductName = input.getText().toString();
                        mAdditionalProducts.add(mProductName);
                        setResultData();
                        Toast.makeText(ProductWebViewActivity.this, "Product added!", Toast.LENGTH_SHORT).show();
                    }
                });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();

        // Initially disable the button
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                // only enable button if text has been entered
                if (TextUtils.isEmpty(s)) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    private void setResultData() {
        Intent data = new Intent();
        String[] newProducts = new String[mAdditionalProducts.size()];
        for (int i = 0; i < mAdditionalProducts.size(); i++) {
            newProducts[i] = mAdditionalProducts.get(i);
        }
        data.putExtra(NEW_PRODUCTS_KEY, newProducts);
        setResult(RESULT_OK, data);
    }
}
