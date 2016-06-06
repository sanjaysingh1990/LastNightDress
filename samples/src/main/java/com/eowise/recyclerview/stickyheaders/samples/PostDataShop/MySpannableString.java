package com.eowise.recyclerview.stickyheaders.samples.PostDataShop;

import android.text.SpannableString;

import java.io.Serializable;

/**
 * Created by INIT on 6/6/2016.
 */
public class MySpannableString extends SpannableString implements Serializable {
    private static final long serialVersionUID = 2L;
    public MySpannableString(CharSequence source) {
        super(source);
    }
}
