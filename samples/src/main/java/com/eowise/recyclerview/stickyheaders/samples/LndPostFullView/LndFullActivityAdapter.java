package com.eowise.recyclerview.stickyheaders.samples.LndPostFullView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eowise.recyclerview.stickyheaders.samples.ImageLoaderImage;
import com.eowise.recyclerview.stickyheaders.samples.Likers.LikersActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndMessage.SendSwapRequestActivity;
import com.eowise.recyclerview.stickyheaders.samples.LndNotificationMessage.TagSelectingTextview;
import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.NewMessage.SendMessageActivity;
import com.eowise.recyclerview.stickyheaders.samples.Purchase.PurchaseActivity;
import com.eowise.recyclerview.stickyheaders.samples.Purchase.ShippingAddressActivity;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.SQLDB.FavoriteData;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.HomeImageSliderLayout;
import com.eowise.recyclerview.stickyheaders.samples.StickyHeader.Home_List_Data;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.OtherUserProfileActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.Capitalize;
import com.eowise.recyclerview.stickyheaders.samples.adapters.SentToAdapter;
import com.eowise.recyclerview.stickyheaders.samples.data.FollowersFollowingData;
import com.eowise.recyclerview.stickyheaders.samples.interfaces.TagClick;
import com.init.superslim.GridSLM;
import com.init.superslim.LinearSLM;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class LndFullActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TagClick {

    private static final int VIEW_TYPE_HEADER = 0x01;

    private static final int VIEW_TYPE_CONTENT_PRIVATE = 0x00;
    private static final int VIEW_TYPE_CONTENT_SHOP = 0x02;

    private static final int LINEAR = 0;

    private final ArrayList<Home_List_Data> mItems;

    private int mHeaderDisplay;

    private boolean mMarginsFixed;

    private final Activity mContext;
    List<FollowersFollowingData> users = new ArrayList<>();
    public static EditText usermessage;
    public static TextView sendcancel;
    private SentToAdapter mAdapter;
    private ProgressBar prog;
    private TextView showtext;
    private ImageButton likebutton;
    private Home_List_Data headerclass;
    MyLike mylike;
    TagSelectingTextview mTagSelectingTextview;
    public static int hashTagHyperLinkEnabled = 1;
    public static int hashTagHyperLinkDisabled = 0;
    String hastTagColorBlue = "#be4d66";

    public LndFullActivityAdapter(Activity context, int headerMode, ArrayList<Home_List_Data> homeitems) {
        mContext = context;

        mHeaderDisplay = headerMode;

        mItems = homeitems;

        mTagSelectingTextview = new TagSelectingTextview();
    }

    public boolean isItemHeader(int position) {
        if (mItems.get(position).sectiontype.compareTo("header") == 0)
            return true;
        else
            return false;
    }

    public String itemToString(int position) {
        return mItems.get(position).text;
    }

    @Override
    public void clickedTag(CharSequence tag) {
        Toast.makeText(mContext, "Clicked on " + tag, 1).show();
    }

    class MyLike implements OnClickListener {
        TextView likescount;
        String postid;
        ImageButton likebutton;

        public MyLike() {

        }

        public MyLike(ImageButton likebtn, String postid) {
            this.postid = postid;
            this.likebutton = likebtn;
        }

        public void setupView(TextView txt) {
            likescount = txt;
        }

        @Override
        public void onClick(View v) {
            RequestQueue queue = Volley.newRequestQueue(mContext);
            StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/lndlikeunlike.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("response", response.toString());
                    try {

                        JSONObject jobj = new JSONObject(response.toString());
                        if (jobj.getBoolean("status")) {
                            int val = jobj.getInt("value");
                            if (val == 1) {
                                String str = likescount.getText().toString();
                                String numberOnly = str.replaceAll("[^0-9]", "");
                                int value = Integer.parseInt(numberOnly);
                                likescount.setText(value - 1 + " likes");
                                likebutton.setImageResource(R.drawable.like_icon);
                            } else {
                                String str = likescount.getText().toString();
                                String numberOnly = str.replaceAll("[^0-9]", "");

                                int value = Integer.parseInt(numberOnly);
                                likescount.setText(value + 1 + " likes");
                                likebutton.setImageResource(R.drawable.liked_icon);

                            }
                        }

                    } catch (Exception ex) {
                        Log.e("json parsing error", ex.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //showing snakebar here
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("image_url", "http://");
                    params.put("user_id", ImageLoaderImage.pref.getString("user_id", ""));
                    params.put("post_id", postid);


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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_item, parent, false);

            return new HeaderHolder(view);
        } else if (viewType == VIEW_TYPE_CONTENT_PRIVATE) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_private_item, parent, false);
            return new LndProductPrivateHolder(view, mContext);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_shop_item, parent, false);
            return new LndProductShopHolder(view, mContext);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Home_List_Data item = mItems.get(position);


        final View itemView = holder.itemView;
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                headerclass = item;
                HeaderHolder vh1 = (HeaderHolder) holder;
                likebutton = vh1.likebtn;
                mylike = new MyLike(vh1.likebtn, item.getPost_id());
                vh1.likebtn.setOnClickListener(mylike);

                vh1.uname.setText(Capitalize.capitalize(item.text));
                if (item.getLikedvalue().compareTo("1") == 0)
                    vh1.likebtn.setImageResource(R.drawable.liked_icon);
                else
                    vh1.likebtn.setImageResource(R.drawable.like_icon);

                //setting profile pic
                ImageLoaderImage.imageLoader.displayImage(item.getProfilepicurl(), vh1.profilepic, ImageLoaderImage.options);

                break;
            case VIEW_TYPE_CONTENT_PRIVATE:
                LndProductPrivateHolder vh2 = (LndProductPrivateHolder) holder;
                String uname = item.getUname();
                //end here
                vh2.description.setMovementMethod(LinkMovementMethod.getInstance());
                vh2.description.setText(mTagSelectingTextview.addClickablePart(uname + " " + item.getDescription(),
                                this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);
                vh2.price_was.setText("$" + item.getPricewas());
                vh2.price_now.setText("$" + item.getPricenow());
                vh2.len.setText(item.getSize());
                vh2.likescount.setText(item.getLikestotal() + " likes");
                mylike.setupView(vh2.likescount);
                try {
                    vh2.hfl.setFeatureItems(headerclass, item, vh2.forward, vh2.backward, vh2.likescount, likebutton);
                    int pos = Integer.parseInt(item.getConditon());
                    vh2.condition.setText(ImageLoaderImage.condition[pos]);
                } catch (Exception ex) {

                }
                //likers view
                vh2.likescount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent likers = new Intent(mContext, LikersActivity.class);
                        likers.putExtra("user_id", ImageLoaderImage.pref.getString("user_id", ""));
                        likers.putExtra("postid", item.getPost_id());
                        mContext.startActivity(likers);
                    }
                });
                break;
            case VIEW_TYPE_CONTENT_SHOP:
                final LndProductShopHolder vh3 = (LndProductShopHolder) holder;
                uname = item.getUname();
                //end here
                vh3.description.setMovementMethod(LinkMovementMethod.getInstance());
                vh3.description.setText(mTagSelectingTextview.addClickablePart(uname + " " + item.getDescription(),
                                this, hashTagHyperLinkDisabled, hastTagColorBlue, uname.length()),
                        TextView.BufferType.SPANNABLE);
                vh3.price_was.setText("$" + item.getPricewas());
                vh3.price_now.setText("$" + item.getPricenow());
                vh3.likescount.setText(item.getLikestotal() + " likes");
                mylike.setupView(vh3.likescount);
                vh3.size.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        popupWindow(item.getSize(), (TextView) view).showAsDropDown(view, -5, 0);
                    }
                });

               /* if (item.getCategory().compareTo("4") == 0)
                    vh3.color.setText("Metal");*/
                vh3.color.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow2(item.getColors(), (TextView) view, item.getCategory()).showAsDropDown(view, -5, 0);

                    }
                });

                try {
                    vh3.hfl.setFeatureItems(headerclass, item, vh3.forward, vh3.backward, vh3.likescount, likebutton);
                    int pos = Integer.parseInt(item.getConditon());
                    vh3.condition.setText(ImageLoaderImage.condition[pos]);
                } catch (Exception ex) {

                }
                //likers view
                vh3.likescount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent likers = new Intent(mContext, LikersActivity.class);
                        likers.putExtra("user_id", ImageLoaderImage.pref.getString("user_id", ""));
                        likers.putExtra("postid", item.getPost_id());
                        mContext.startActivity(likers);
                    }
                });
                break;

            default:
                break;
        }


        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        // Overrides xml attrs, could use different layouts too.
        if (item.sectiontype.compareTo("header") == 0) {
            lp.headerDisplay = mHeaderDisplay;
            if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            lp.headerEndMarginIsAuto = !mMarginsFixed;
            lp.headerStartMarginIsAuto = !mMarginsFixed;
        }
        lp.setSlm(item.sectionManager == LINEAR ? LinearSLM.ID : GridSLM.ID);
        //  lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.grid_column_width));
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);
    }

    public PopupWindow popupWindow(String sizes, TextView size) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(mContext);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(mContext);


        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter(popupAdapter(sizes.split(",")));

        // set the item click listener
        listViewDogs.setOnItemClickListener(new DropdownOnItemClickListener(popupWindow, size, sizes));

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(100);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    public PopupWindow popupWindow2(String sizes, TextView size, String category) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(mContext);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(mContext);

        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter(popupAdapter(sizes.split(",")));

        // set the item click listener
        listViewDogs.setOnItemClickListener(new DropdownOnItemClickListener(popupWindow, size, sizes));

        // some other visual settings
        popupWindow.setFocusable(true);
        if (category.compareTo("4") == 0)
            popupWindow.setWidth(250);
        else
            popupWindow.setWidth(170);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    public class DropdownOnItemClickListener implements AdapterView.OnItemClickListener {

        PopupWindow popupwindow;
        TextView sizecolor;
        String sizecolorvalues[];

        public DropdownOnItemClickListener(PopupWindow popupWindow, TextView sizecolor, String sizescolors) {
            this.popupwindow = popupWindow;
            this.sizecolor = sizecolor;
            this.sizecolorvalues = sizescolors.split(",");
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {

            // get the context and main activity to access variables
            Context mContext = v.getContext();

            // add some animation when a list item was clicked
            Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
            fadeInAnimation.setDuration(10);
            v.startAnimation(fadeInAnimation);
            popupwindow.dismiss();
            try {
                sizecolor.setText(Capitalize.capitalizeFirstLetter(ImageLoaderImage.metaltype[Integer.parseInt(sizecolorvalues[pos])]));

            } catch (Exception ex) {
                sizecolor.setText(Capitalize.capitalizeFirstLetter(sizecolorvalues[pos]));

            }
            notifyDataSetChanged();


        }

    }

    private ArrayAdapter<String> popupAdapter(String popArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, popArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                String text = "";
                // setting the ID and text for every items in the list
                try {
                    text = ImageLoaderImage.metaltype[Integer.parseInt(getItem(position))];
                } catch (Exception ex) {
                    text = getItem(position);

                }

                // visual settings for the list item
                TextView listItem = new TextView(mContext);

                listItem.setText(Capitalize.capitalizeFirstLetter(text));

                listItem.setTag(position);
                listItem.setTextSize(16);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };

        return adapter;
    }

    @Override
    public int getItemViewType(int position) {
        Home_List_Data lineItem = mItems.get(position);
        if (lineItem.sectiontype.compareTo("header") == 0)
            return VIEW_TYPE_HEADER;
        else {
            if (lineItem.isprivate)
                return VIEW_TYPE_CONTENT_PRIVATE;
            else
                return VIEW_TYPE_CONTENT_SHOP;

        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setHeaderDisplay(int headerDisplay) {
        mHeaderDisplay = headerDisplay;
        notifyHeaderChanges();
    }

    public void setMarginsFixed(boolean marginsFixed) {
        mMarginsFixed = marginsFixed;
        notifyHeaderChanges();
    }

    private void notifyHeaderChanges() {
        for (int i = 0; i < mItems.size(); i++) {
            Home_List_Data item = mItems.get(i);
            if (item.sectiontype.compareTo("header") == 0) {
                notifyItemChanged(i);
            }
        }
    }

    public class MySize implements PopupMenu.OnMenuItemClickListener {
        TextView size;
        TextView sizetext;

        public MySize(TextView txt, TextView sizetext) {
            size = txt;
            this.sizetext = sizetext;

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            size.setText(menuItem.getTitle());
            sizetext.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
            return false;
        }
    }

    public class MyColor implements PopupMenu.OnMenuItemClickListener {
        TextView color;
        TextView colortext;

        public MyColor(TextView txt, TextView colortext) {

            color = txt;
            this.colortext = colortext;

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            color.setText(menuItem.getTitle());
            colortext.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
            return false;
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public TextView uname;
        public TextView brandname;
        public ImageView profilepic;
        public ImageButton likebtn;


        HeaderHolder(View view) {
            super(view);
            uname = (TextView) view.findViewById(R.id.uname);
            brandname = (TextView) view.findViewById(R.id.brandname);
            profilepic = (ImageView) view.findViewById(R.id.profilepic);
            likebtn = (ImageButton) view.findViewById(R.id.likesclick);
            //setting fonts
            uname.setTypeface(ImageLoaderImage.robotomedium);
            brandname.setTypeface(ImageLoaderImage.robotoregular);
            uname.setOnClickListener(this);
        }


        @Override

        public void onClick(View view) {
            Intent userprofile = new Intent(mContext, OtherUserProfileActivity.class);
            userprofile.putExtra("uname", mItems.get(getAdapterPosition()).getUname() + "");
            mContext.startActivity(userprofile);
        }
    }

    class LndProductPrivateHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView color;
        public HomeImageSliderLayout hfl;
        public TextView description;
        public TextView len;
        public TextView price_was, price_now;
        public ImageButton msgtouser, favorates, sendto;
        private TextView buy, swap;
        public TextView likescount;
        public TextView condition;
        Context con;
        ImageButton forward, backward;

        LndProductPrivateHolder(View view, Context context) {
            super(view);
            con = context;
            //setting height
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

//setting margins around imageimageview
            params.height = (height * 70) / 100; //left, top, right, bottom
            params.width = width;
            description = (TextView) view.findViewById(R.id.desc);
            price_was = (TextView) view.findViewById(R.id.pricewas);
            price_now = (TextView) view.findViewById(R.id.pricenow);
            len = (TextView) view.findViewById(R.id.len);
            msgtouser = (ImageButton) view.findViewById(R.id.messagetouser);
            favorates = (ImageButton) view.findViewById(R.id.favorate);
            sendto = (ImageButton) view.findViewById(R.id.sendto);
            buy = (TextView) itemView.findViewById(R.id.buy);
            swap = (TextView) itemView.findViewById(R.id.swap);
            likescount = (TextView) view.findViewById(R.id.likescount);
            condition = (TextView) view.findViewById(R.id.condition);
            color = (TextView) view.findViewById(R.id.color);
            hfl = (HomeImageSliderLayout) view.findViewById(R.id.switcher);
            hfl.setLayoutParams(params);
            forward = (ImageButton) view.findViewById(R.id.forward);
            backward = (ImageButton) view.findViewById(R.id.backward);

            //bind with listeners
            this.buy.setOnClickListener(this);
            this.swap.setOnClickListener(this);
            this.sendto.setOnClickListener(this);
            this.msgtouser.setOnClickListener(this);
            this.favorates.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            show(v, getAdapterPosition());
        }

    }

    class LndProductShopHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView sizetext, colortext;
        public HomeImageSliderLayout hfl;
        public TextView description;
        public TextView price_was, price_now;
        public ImageButton msgtouser, favorates, sendto;
        public TextView buy;
        public TextView likescount;
        public TextView condition;
        Context con;
        ImageButton forward, backward;
        public TextView size, color;

        LndProductShopHolder(View view, Context context) {
            super(view);
            con = context;
            //setting height
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

//setting margins around imageimageview
            params.height = (height * 70) / 100; //left, top, right, bottom
            params.width = width;
            description = (TextView) view.findViewById(R.id.desc);
            price_was = (TextView) view.findViewById(R.id.pricewas);
            price_now = (TextView) view.findViewById(R.id.pricenow);
            msgtouser = (ImageButton) view.findViewById(R.id.messagetouser);
            favorates = (ImageButton) view.findViewById(R.id.favorate);
            sendto = (ImageButton) view.findViewById(R.id.sendto);
            buy = (TextView) itemView.findViewById(R.id.buy);
            //size text and color text

            likescount = (TextView) view.findViewById(R.id.likescount);
            condition = (TextView) view.findViewById(R.id.condition);
            // pattern = (TextView) view.findViewById(R.id.pattern);
            hfl = (HomeImageSliderLayout) view.findViewById(R.id.switcher);
            hfl.setLayoutParams(params);
            forward = (ImageButton) view.findViewById(R.id.forward);
            backward = (ImageButton) view.findViewById(R.id.backward);

            size = (TextView) view.findViewById(R.id.size);
            color = (TextView) view.findViewById(R.id.color);

            description = (TextView) view.findViewById(R.id.desc);
            //bind with listeners
            this.buy.setOnClickListener(this);
            this.sendto.setOnClickListener(this);
            this.msgtouser.setOnClickListener(this);
            this.favorates.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            show(view, getAdapterPosition());
        }
    }

    private void show(View v, final int pos) {
        if (v.getId() == R.id.buy) {
            Intent buy = new Intent(mContext, ShippingAddressActivity.class);
            mContext.startActivity(buy);
        } else if (v.getId() == R.id.swap) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            View view = LayoutInflater.from(mContext).inflate(R.layout.swap_dialog_layout, null);

            dialog.setView(view);
            final AlertDialog alert = dialog.create();
            alert.show();
            view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
            view.findViewById(R.id.swapcontinue).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    Intent sendswaprequest = new Intent(mContext, SendSwapRequestActivity.class);
                    sendswaprequest.putExtra("data", mItems.get(pos));
                    Main_TabHost.activity.startActivityForResult(sendswaprequest, 4);
                    Log.e("uname", mItems.get(pos).getUname());
                }
            });
        } else if (v.getId() == R.id.sendto) {

            users.clear();

            final Dialog dialog = new Dialog(mContext, R.style.DialogSlideAnim3);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle("Comments");

            // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
            dialog.setContentView(R.layout.sendtouser_dialog_layout);
            prog = (ProgressBar) dialog.findViewById(R.id.progressBar);
            usermessage = (EditText) dialog.findViewById(R.id.messagetext);
            sendcancel = (TextView) dialog.findViewById(R.id.cancel);
            showtext = (TextView) dialog.findViewById(R.id.showtext);
            showtext.setVisibility(View.GONE);

            RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

            // mLayoutManager.
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new SentToAdapter(mContext, users);
            recyclerView.setAdapter(mAdapter);
            String uname = ImageLoaderImage.pref.getString("uname", "uname");

            getFollowers(uname);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.show();
            //cancel event
            dialog.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else if (v.getId() == R.id.messagetouser) {
            Intent msgtofrnd = new Intent(mContext, SendMessageActivity.class);
            msgtofrnd.putExtra("fromhome", true);
            msgtofrnd.putExtra("uname", mItems.get(pos).getUname());
            mContext.startActivity(msgtofrnd);
        } else if (v.getId() == R.id.favorate) {
            FavoriteData fav = ImageLoaderImage.db.getContact(mItems.get(pos).getPost_id());
            if (fav == null) {
                FavoriteData favdata = new FavoriteData();
                Home_List_Data lndhome = mItems.get(pos);
                favdata.setPostid(lndhome.getPost_id());
                favdata.setCost(lndhome.getPricenow());
                favdata.setImageurl(lndhome.getImageurls().get(0));
                ImageLoaderImage.db.addContact(favdata);
                ((ImageButton) v).setImageResource(R.drawable.filled_favorate_icon);
            } else {
                Log.e("postid", fav.getPostid() + "");
                ImageLoaderImage.db.deleteContact(fav.getPostid());
                ((ImageButton) v).setImageResource(R.drawable.favorite_icon);

            }
        }
    }

    public void getFollowers(final String uname) {
        prog.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.76.68.122/lnd/androidiosphpfiles/followfollowing.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog.setVisibility(View.GONE);

                Log.e("follower", response.toString());
                try {
                    JSONObject jobj = new JSONObject(response.toString());
                    JSONArray jsonArray = jobj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FollowersFollowingData fd = new FollowersFollowingData();
                        fd.setUname(jsonObject.getString("uname"));
                        fd.setUserpic(jsonObject.getString("user_pic"));
                        fd.setStatus(jsonObject.getString("check"));
                        fd.setSelected(false);
                        users.add(fd);
                    }
                    if (jsonArray.length() == 0)
                        showtext.setVisibility(View.VISIBLE);
                } catch (Exception ex) {

                    Log.e("json parsing error", ex.getMessage());
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e("response",error.getMessage()+"");
                prog.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rqid", "1");
                params.put("uid", uname);


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



