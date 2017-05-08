package csci448.appigators.snappydecisions;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import csci448.appigators.snappydecisions.database.SnappyDecisionsBaseHelper;
import csci448.appigators.snappydecisions.database.SnappyDecisionsCursorWrapper;
import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema;

public class ProductDecisionFragment extends Fragment {

    private static final String NEW_PRODUCTS_KEY = "new_products";
    private static final int REQUEST_CODE = 0;

    private ImageButton mAddProductButton;
    private Button mSaveProductListButton;
    private EditText mAddProductText;
    private String mProductName;
    private LinearLayout mProductList;
    private String mProductListName;
    private ImageView mLogo;
    private List<String> mProducts = new ArrayList<>();
    private List<RandomDecisionOption> mOptions = new ArrayList<>();
    private SQLiteDatabase mDatabase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new SnappyDecisionsBaseHelper(getContext()).getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_product_decision, container, false);

        mAddProductText = (EditText)v.findViewById(R.id.new_product_text);
        mProductList = (LinearLayout)v.findViewById(R.id.product_list_linear_layout);
        mLogo = (ImageView)v.findViewById(R.id.logo);

        mAddProductButton = (ImageButton)v.findViewById(R.id.add_product_button);
        mAddProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only update if user has put text
                if (!mAddProductText.getText().toString().equals("")) {
                    mProductName = mAddProductText.getText().toString();
                    addNewProduct();
                    clearNewFields();
                } else {
                    Toast.makeText(getActivity(), "Enter a search term", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSaveProductListButton = (Button)v.findViewById(R.id.save_button);
        mSaveProductListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRandomDecisions();
                if (!mOptions.isEmpty()) {
                    saveProductList();
                } else {
                    Toast.makeText(getContext(), "Add some items to the list before saving!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void addNewProduct() {
        mLogo.setVisibility(View.GONE);
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

        final String productName = mProductName;
        mProducts.add(productName);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductList.removeView(productLayout);
                mProducts.remove(productName);
                if (mProducts.isEmpty()) {
                    mLogo.setVisibility(View.VISIBLE);
                }
            }
        });

        productLayout.addView(removeButton);

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
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        productLayout.addView(imageButton, params);

        mProductList.addView(productLayout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
            if (data == null) {
                return;
            }
            String[] newProducts = data.getStringArrayExtra(NEW_PRODUCTS_KEY);
            for (String s : newProducts) {
                mProductName = s;
                addNewProduct();
            }
        }
    }

    private void saveProductList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter List Name");

        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        mProductListName = input.getText().toString();
                        if (checkUniqueName(mProductListName)) {
                            saveToDatabase(mProductListName);
                            Toast.makeText(getActivity(), "Product list saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "List name must be unique", Toast.LENGTH_SHORT).show();
                        }
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

    private ContentValues getDecisionContentValues(String name) {
        ContentValues values = new ContentValues();
        values.put(SnappyDecisionsSchema.RandomDecisionTable.Cols.NAME, name);
        return values;
    }

    private ContentValues getOptionContentValues(String name, RandomDecisionOption opt) {
        ContentValues values = new ContentValues();
        values.put(SnappyDecisionsSchema.RandomDecisionOptionTable.Cols.DECISION, name);
        values.put(SnappyDecisionsSchema.RandomDecisionOptionTable.Cols.OPTION, opt.getOption());
        values.put(SnappyDecisionsSchema.RandomDecisionOptionTable.Cols.WEIGHT, opt.getWeight());
        return values;
    }

    private void saveToDatabase(String name) {
        ContentValues decisionValues = getDecisionContentValues(name);
        mDatabase.insert(SnappyDecisionsSchema.RandomDecisionTable.NAME, null, decisionValues);

        for(RandomDecisionOption opt : mOptions)
        {
            ContentValues optionValues = getOptionContentValues(name, opt);
            mDatabase.insert(SnappyDecisionsSchema.RandomDecisionOptionTable.NAME, null, optionValues);
        }
    }

    private boolean checkUniqueName(String name) {
        ArrayList<String> allNames = getDecisionNames();

        if(allNames.contains(name))
        {
            return false;
        }
        return true;
    }

    private SnappyDecisionsCursorWrapper queryTable(String dbName, String whereClause,
                                                    String[] whereArgs) {
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

    private ArrayList<String> getDecisionNames() {
        SnappyDecisionsCursorWrapper cursor = queryTable(SnappyDecisionsSchema.RandomDecisionTable.NAME, null, null);
        ArrayList<String> allNames = new ArrayList<>();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                allNames.add(cursor.getRandomDecisionName());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return allNames;
    }

    private void makeRandomDecisions() {
        mOptions.clear();
        for (String s : mProducts) {
            RandomDecisionOption option = new RandomDecisionOption(s, 1);
            mOptions.add(option);
        }
    }

    private void clearNewFields() {
        mAddProductText.setText("");
    }
}
