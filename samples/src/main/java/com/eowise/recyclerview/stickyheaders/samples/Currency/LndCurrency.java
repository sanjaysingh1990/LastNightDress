package com.eowise.recyclerview.stickyheaders.samples.Currency;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LndCurrency extends Activity {

    @Bind(R.id.list)
    ListView myListView;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.selected)
    ImageButton selected;

    private ArrayList<String> Currency = new ArrayList<String>();

    private void initCurrencyList() {
        Currency.add("United States dollar");
        Currency.add("Euro");
        Currency.add("Japanese yen");
        Currency.add("Pound sterling");
        Currency.add("Australian dollar");
        Currency.add("Swiss franc");
        Currency.add("Canadian dollar");
        Currency.add("Mexican peso");
        Currency.add("Chinese yuan");
        Currency.add("New Zealand dollar");
        Currency.add("Swedish krona");
        Currency.add("Russian ruble");
        Currency.add("Hong Kong dollar");
        Currency.add("Norwegian krone");
        Currency.add("Singapore dollar");
        Currency.add("Turkish lira");
        Currency.add("South Korean won");
        Currency.add("South African rand");
        Currency.add("Brazilian real");
        Currency.add("Indian rupee");


    }

    MyArrayAdapter myArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCurrencyList();
        setContentView(R.layout.lnd_currency_layout);
        ButterKnife.bind(this);
        //set header font
        heading.setTypeface(SingleTon.robotobold);
        myArrayAdapter = new MyArrayAdapter(
                this,
                R.layout.lnd_currency_row,
                android.R.id.text1,
                Currency
        );

        myListView.setAdapter(myArrayAdapter);
        myListView.setOnItemClickListener(myOnItemClickListener);

        int pos = SingleTon.pref.getInt("currency", -1);
        //check currency selected
        if (pos >= 0)
            myArrayAdapter.toggleChecked(pos);
        else
            myArrayAdapter.toggleChecked(0);
        //to save selected currency
        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saving on shared preference
                SharedPreferences.Editor edit = SingleTon.pref.edit();
                edit.putInt("currency", myArrayAdapter.getCheckedItemPosition());
                edit.commit();
                finish();
            }
        });
    }

    OnItemClickListener myOnItemClickListener
            = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            myArrayAdapter.toggleChecked(position);
            selected.setVisibility(View.VISIBLE);

        }
    };

    //save

    private class MyArrayAdapter extends ArrayAdapter<String> {

        private HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();

        public MyArrayAdapter(Context context, int resource,
                              int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);

            for (int i = 0; i < objects.size(); i++) {
                myChecked.put(i, false);
            }
        }

        public void toggleChecked(int position) {
            for (int i = 0; i < myChecked.size(); i++) {
                myChecked.put(i, false);
            }
            myChecked.put(position, true);


            notifyDataSetChanged();
        }

        public int getCheckedItemPosition() {
            int pos = -1;
            for (int i = 0; i < myChecked.size(); i++) {
                if (myChecked.get(i)) {
                    pos = i;
                }
            }

            return pos;
        }

        public List<String> getCheckedItems() {
            List<String> checkedItems = new ArrayList<String>();

            for (int i = 0; i < myChecked.size(); i++) {
                if (myChecked.get(i)) {
                    (checkedItems).add(Currency.get(i));
                }
            }

            return checkedItems;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.lnd_currency_row, parent, false);
            }

            CheckedTextView checkedTextView = (CheckedTextView) row.findViewById(R.id.text1);
            checkedTextView.setCheckMarkDrawable(R.drawable.lnd_currency_checkbox_selector);
            checkedTextView.setText(Currency.get(position));
            checkedTextView.setTypeface(SingleTon.settingfont);
            Boolean checked = myChecked.get(position);
            if (checked != null) {
                checkedTextView.setChecked(checked);
            }

            return row;
        }

    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}