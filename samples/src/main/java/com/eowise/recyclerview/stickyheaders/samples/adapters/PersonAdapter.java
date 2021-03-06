package com.eowise.recyclerview.stickyheaders.samples.adapters;

/**
 * Created by aurel on 22/09/14.
 *//*
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> implements OnRemoveListener, OnEditListener {
   /* private static Map<Integer, Parcelable> scrollStatePositionsMap = new HashMap<>();
    private static List<LndHomeData> items;
    private PersonDataProvider personDataProvider;
    private  SentToAdapter mAdapter;
    private ProgressBar prog;
    private TextView showtext;
    List<FollowersData> users=new ArrayList<>() ;
    static Activity mContext;
     int count=0;
    CommentsAdapter adapter;
     public static EditText usermessage;
     public static TextView sendcancel;

    public PersonAdapter(Activity context, List<LndHomeData> data) {
        this.mContext = context;
        this.personDataProvider = personDataProvider;
        this.items = data;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);

        //Perhaps the first most crucial part. The ViewPager loses its width information when it is put
        //inside a RecyclerView. It needs to be explicitly resized, in this case to the width of the
        //screen. The height must be provided as a fixed value.
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        v.getLayoutParams().width = displayMetrics.widthPixels;
        v.requestLayout();

        ViewHolder vh = new ViewHolder(itemView);
        return vh;

    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        double price_was=0.0,price_now=0.0;
        String country=ImageLoaderImage.pref.getString("country","united states");
        Log.e("country",country);
        if(country.length()==0)
            country="united states";
        LndHomeData lndh=items.get(position);
        viewHolder.desc.setText(lndh.getDesc());
          //end here
        try
        {
            price_was=Double.parseDouble(lndh.getPricewas());
            price_now=Double.parseDouble(lndh.getPricenow());
        }
        catch(Exception ex)
        {

        }
       ImageLoaderImage.showValue(country,viewHolder.pricewas,viewHolder.pricenow,price_was,price_now);
        viewHolder.len.setText(lndh.getLen());
        if(lndh.isFavorate()) {
            viewHolder.favorate.setImageResource(R.drawable.filled_favorate_icon);
        }
        else
            viewHolder.favorate.setImageResource(R.drawable.favorite_icon);

        ImageAdapter adapter = new ImageAdapter(mContext,items.get(position).getImages());
    viewHolder.viewPager.setAdapter(adapter);


    }
    private String capitalize(final String line) {
        String[] split=line.split(" ");
        String output="";
        for(String str:split)
        {

            output+=Character.toUpperCase(str.charAt(0)) + str.substring(1)+" ";
        }
        return output;
    }

    private class SpanClick extends ClickableSpan {

        String uname;
        public SpanClick(String uname)
        {
            this.uname=uname;
        }
        @Override
        public void onClick(View widget) {
            Intent otherlnduser=new Intent(mContext, OtherUserProfileActivity.class);
            otherlnduser.putExtra("uname",uname);
            mContext.startActivity(otherlnduser);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onRemove(int position) {
        personDataProvider.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onEdit(final int position) {
        final EditText edit = new EditText(mContext);
        edit.setTextColor(Color.BLACK);
        edit.setText(personDataProvider.getItems().get(position));
        new AlertDialog.Builder(mContext).setTitle(R.string.edit).setView(edit).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edit.getText().toString();
                personDataProvider.update(position, name);
                notifyItemChanged(position);
            }
        }).create().show();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public void setPosition(int position) {
            this.position = position;
        }

        public  int position;
        TextView pricewas,pricenow,len,desc;
        TextView buy,swap;
        ImageButton sendto,msgtofriend,favorate;
        TextView condtion,size,color;
        ViewPager viewPager;
        protected RecyclerView horizontalRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
           //comments and comments all
            this.sendto= (ImageButton) itemView.findViewById(R.id.sendto);
            this.msgtofriend= (ImageButton) itemView.findViewById(R.id.messagetouser);
            this.favorate= (ImageButton) itemView.findViewById(R.id.favorate);
            this.pricenow= (TextView) itemView.findViewById(R.id.pricenow);
            this.pricewas= (TextView) itemView.findViewById(R.id.pricewas);
            this.len= (TextView) itemView.findViewById(R.id.len);
            this.buy= (TextView) itemView.findViewById(R.id.buy);
            this.swap= (TextView) itemView.findViewById(R.id.swap);
            this.desc= (TextView) itemView.findViewById(R.id.desc);
            this.condtion= (TextView) itemView.findViewById(R.id.condition);
            this.size= (TextView) itemView.findViewById(R.id.size);
            this.color= (TextView) itemView.findViewById(R.id.color);
//setting custom fonts
            this.condtion.setTypeface(ImageLoaderImage.normalfont);
            this.size.setTypeface(ImageLoaderImage.normalfont);
            this.color.setTypeface(ImageLoaderImage.normalfont);

            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();

            int height = displayMetrics.heightPixels;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

//setting margins around imageimageview
            params.height=(height*60)/100; //left, top, right, bottom

//adding attributes to the imageview


            this.buy.setOnClickListener(this);
            this.swap.setOnClickListener(this);
            this.sendto.setOnClickListener(this);
            this.msgtofriend.setOnClickListener(this);
            this.favorate.setOnClickListener(this);

            //  Log.e("position",count++ +"" );


            viewPager=   ((ViewPager) itemView.findViewById(R.id.viewPager));
            viewPager.setLayoutParams(params);

            viewPager.addOnPageChangeListener(new MypageListener(itemView));
            viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View view, float position) {

                    if (position <= -1.0F || position >= 1.0F) {
                        view.setTranslationX(view.getWidth() * position);
                        view.setAlpha(0.0F);
                    } else if (position == 0.0F) {
                        view.setTranslationX(view.getWidth() * position);
                        view.setAlpha(1.0F);
                    } else {
                        // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                        view.setTranslationX(view.getWidth() * -position);
                        view.setAlpha(1.0F - Math.abs(position));
                    }
                }
            });



            count++;
        }
private class MypageListener implements ViewPager.OnPageChangeListener
{
    private ImageView forward,backward;
public MypageListener(View convertView)
{
    forward= (ImageView) convertView.findViewById(R.id.forward);
    backward= (ImageView) convertView.findViewById(R.id.backward);
  }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e("position",position+"");
        if(position==0)
        {
            forward.setVisibility(View.VISIBLE);
            backward.setVisibility(View.INVISIBLE);
        }
        else if(position==3)
        {
            forward.setVisibility(View.INVISIBLE);
            backward.setVisibility(View.VISIBLE);

        }
        else
        {
            forward.setVisibility(View.VISIBLE);
            backward.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.buy) {
              Intent buy=new Intent(mContext, PurchaseActivity.class);
                mContext.startActivity(buy);
            }
            else if(v.getId()==R.id.swap)
            {
                AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
                View view = LayoutInflater.from(mContext).inflate(R.layout.swap_dialog_layout, null);

                dialog.setView(view);
            final    AlertDialog alert=dialog.create();
                alert.show();
              view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      alert.dismiss();
                  }
              });
                view.findViewById(R.id.swapcontinue).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        Intent sendswaprequest=new Intent(mContext, SendSwapRequestActivity.class);
                        sendswaprequest.putExtra("data", items.get(getAdapterPosition()));
                        Main_TabHost.activity.startActivityForResult(sendswaprequest, 4);
                        Log.e("uname", items.get(getAdapterPosition()).getUname());
                    }
                });
            }
            else if(v.getId()==R.id.sendto)
            {

                users.clear();

                final  Dialog dialog = new Dialog(mContext,R.style.DialogSlideAnim3);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setTitle("Comments");

                // dialog.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c6290")));
                dialog.setContentView(R.layout.sendtouser_dialog_layout);
                prog= (ProgressBar) dialog.findViewById(R.id.progressBar);
                usermessage= (EditText) dialog.findViewById(R.id.messagetext);
                sendcancel= (TextView) dialog.findViewById(R.id.cancel);
                showtext= (TextView) dialog.findViewById(R.id.showtext);
                showtext.setVisibility(View.GONE);

                RecyclerView  recyclerView= (RecyclerView)dialog. findViewById(R.id.recyclerView);
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

                // mLayoutManager.
                recyclerView.setLayoutManager(layoutManager);
                mAdapter = new SentToAdapter(mContext,users);
                recyclerView.setAdapter(mAdapter);
                String uname= ImageLoaderImage.pref.getString("uname","uname");

                getFollowers(uname);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
               // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();
            //cancel event
                dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
            else if(v.getId()==R.id.messagetouser)
            {
                Intent msgtofrnd=new Intent(mContext, SendMessageActivity.class);
                msgtofrnd.putExtra("fromhome",true);
                msgtofrnd.putExtra("uname", items.get(getAdapterPosition()).getUname());
                mContext.startActivity(msgtofrnd);
            }
            else if(v.getId()==R.id.favorate)
            {
              FavoriteData fav= ImageLoaderImage.db.getContact(items.get(getAdapterPosition()).getPostid());
              if(fav==null)
              {
                  FavoriteData favdata=new FavoriteData();
                  LndHomeData lndhome=items.get(getAdapterPosition());
                  favdata.setPostid(lndhome.getPostid());
                  favdata.setCost(lndhome.getPricenow());
                  favdata.setImageurl(lndhome.getImages().get(0));
                  ImageLoaderImage.db.addContact(favdata);
                  ((ImageButton)v).setImageResource(R.drawable.filled_favorate_icon);
              }
                else
              {
                  Log.e("postid",fav.getPostid()+"");
                  ImageLoaderImage.db.deleteContact(fav.getPostid());
                  ((ImageButton)v).setImageResource(R.drawable.favorite_icon);

              }
            }
            }
    }

    public  void getFollowers(final String uname){
        prog.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://52.76.68.122/lnd/followfollowing.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               prog.setVisibility(View.GONE);

                Log.e("follower", response.toString());
                try {
                    JSONObject jobj=new JSONObject(response.toString());
                    JSONArray jsonArray=jobj.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        FollowersData fd = new FollowersData();
                        fd.setUname(jsonObject.getString("uname"));
                        fd.setUserpic(jsonObject.getString("user_pic"));
                        fd.setStatus(jsonObject.getString("check"));
                        fd.setSelected(false);
                        users.add(fd);
                    }
                    if(jsonArray.length()==0)
                        showtext.setVisibility(View.VISIBLE);
                }
                catch(Exception ex)
                {

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
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rqid","1");
                params.put("uid",uname);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
}*/
