package com.eowise.recyclerview.stickyheaders.samples.Paypal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;

@SuppressLint("ValidFragment")
public final class TextFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    int pos;


    public TextFragment(int textSource) {
        this.pos = textSource;

    }
    public TextFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            pos = savedInstanceState.getInt(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.paypal_layoutpage, null);
       TextView text = (TextView)root.findViewById(R.id.text);
        text.setText(PayPalAccountCreation.textarray[pos]);
        setRetainInstance(true);
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, pos);
    }
}
