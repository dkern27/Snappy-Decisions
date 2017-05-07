package csci448.appigators.snappydecisions;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ProductDecisionFragment extends Fragment {

    private ImageButton mAddProductButton;
    private EditText mAddProductText;
    private LinearLayout mProductList;
    private Uri mUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_product_decision, container, false);

        mAddProductText = (EditText)v.findViewById(R.id.new_product_text);
        mProductList = (LinearLayout)v.findViewById(R.id.product_list_linear_layout);

        mAddProductButton = (ImageButton)v.findViewById(R.id.add_product_button);
        mAddProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // only update if user has put text
                if (!mAddProductText.getText().toString().equals("")) {
                    addNewProduct();
                    clearNewFields();
                } else {
                    Toast.makeText(getActivity(), "Enter a search term", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void addNewProduct() {
        final LinearLayout productLayout = new LinearLayout(getContext());
        productLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 3);

        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
                r.getDisplayMetrics());
        params.setMargins(0, 0, px, px);

        ImageButton removeButton = new ImageButton(getContext());
        removeButton.setImageResource(R.drawable.remove);
        removeButton.setBackgroundColor(Color.TRANSPARENT);
        removeButton.setPadding(35, 15, 35, 0);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductList.removeView(productLayout);
            }
        });

        productLayout.addView(removeButton);

        final String productName = mAddProductText.getText().toString();
        EditText textView = new EditText(getContext());
        textView.setText(productName);
        textView.setKeyListener(null);
        productLayout.addView(textView, params);

        ImageButton imageButton = new ImageButton(getContext());
        imageButton.setImageResource(R.drawable.arrow_right);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = ProductWebsitesActivity.newIntent(getContext(), productName);
                startActivity(intent);
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
}
