package com.eowise.recyclerview.stickyheaders.samples.Utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


/**
 * Created by sanju on 13/3/16.
 */
public class LndTextWatcher implements TextWatcher {
    MultiAutoCompleteTextView desc;
    Context con;
    ArrayList<String> data=new ArrayList<>();
    public static Hashtable<String,Integer> users=new Hashtable();

    char start=0;
    public LndTextWatcher(MultiAutoCompleteTextView desc,Context con)
    {
        this.desc=desc;
        this.con=con;
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try {
            char pos = charSequence.charAt(charSequence.toString().length() - 1);
            if (start == 0 && pos == '@')
                start = '@';
            if (start == '@' && pos != ' ') {
                Log.e("data", charSequence.toString().substring(charSequence.toString().lastIndexOf('@') + 1));
                getUsers(charSequence.toString().substring(charSequence.toString().lastIndexOf('@') + 1));
            }
            if (pos == ' ') {
                start = 0;

                desc.removeTextChangedListener(this);
                desc.setText(new HashTagandMention().addClickablePart(desc.getText().toString(), "#be4d66"));
                desc.setSelection(desc.getText().length());
                desc.addTextChangedListener(this);


            }
        }
        catch (Exception ex)
        {

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    public void getUsers(final String keyword) {


        RequestQueue queue = Volley.newRequestQueue(con);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/readname.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.e("data", response.toString());
                data.clear();


                try {

                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jarray = jobj.getJSONArray("data");
                    // Log.e("len",jarray.length()+"");
                    for (int i = 0; i < jarray.length(); i++)
                    {
                        JSONObject jo = jarray.getJSONObject(i);
                        //PeopleData pd = new PeopleData();
                        // pd.setUname(jo.getString("uname"));
                        // pd.setImageurl(jo.getString("imgurl"));
                        //pd.setUserid(jo.getString("user_id"));
                        users.put(jo.getString("uname"),jo.getInt("user_id"));
                        data.add(jo.getString("uname"));
                    }

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(con, R.layout.custom_textview, data);
                    desc.setAdapter(adapter);
                }
                catch (Exception ex)
                {
                    Log.e("response", ex.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("response", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "1");
                params.put("keyword", keyword);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
}
