package com.eowise.recyclerview.stickyheaders.samples.MoreInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.samples.R;

import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;
import android.widget.PopupWindow.OnDismissListener;

import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;


public class UserDescriptionFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.next)TextView next;
    @Bind(R.id.leftchar)TextView leftchar;



    @Bind(R.id.emojicon_edit_text)EditText emojiconEditText ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.description_fragment_layout, container,
                false);
        ButterKnife.bind(this, rootView);
        //setting up click listener
        next.setOnClickListener(this);
        emojiconEditText .addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                leftchar.setText(150 - charSequence.length() + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });










        return rootView;
    }

    @Override
    public void onClick(View view) {

        try {
            FillUserInfo.jobj.put("description",emojiconEditText.getText().toString());
        }
        catch(JSONException ex)
        {
            Log.e("error", ex.getMessage() + "");
        }

        FillUserInfo.mViewPager.setCurrentItem(2);
    }
    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }

}