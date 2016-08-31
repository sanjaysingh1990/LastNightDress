package com.eowise.recyclerview.stickyheaders.samples.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.data.PeopleData;

import java.io.InputStream;
import java.util.List;

/**
 * Created by aurel on 22/09/14.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private static List<PeopleData> items;
    static Context mContext;
    static int count = 0;

    public ContactsAdapter(Context context, List<PeopleData> data) {
        this.mContext = context;
        this.items = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item_row, viewGroup, false);


        ViewHolder vh = new ViewHolder(itemView);
        return vh;

    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        PeopleData peopledata = items.get(position);
        SingleTon.imageLoader.displayImage(peopledata.getImageurl(), viewHolder.img, SingleTon.options3);

        viewHolder.uname.setText(capitalize(peopledata.getUname()));
        viewHolder.number.setText(peopledata.getNumber());
        if(peopledata.getContactImage()!=null)
        viewHolder.img.setImageURI(peopledata.getContactImage());
    }

    private String capitalize(final String line) {
        String[] split = line.split(" ");
        String output = "";
        for (String str : split) {

            output += Character.toUpperCase(str.charAt(0)) + str.substring(1) + " ";
        }
        return output;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView uname, number;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            uname = (TextView) itemView.findViewById(R.id.uname);
            number = (TextView) itemView.findViewById(R.id.number);
            img = (ImageView) itemView.findViewById(R.id.profilepic);
            uname.setTypeface(SingleTon.unamefont);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            Uri uri = Uri.parse("smsto:" + items.get(getAdapterPosition()).getNumber());
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", "https://play.google.com/store/apps/details?id=com.init.sikhdiary&hl=en");
            mContext.startActivity(it);


        }
    }

    private class LoadContactImage extends AsyncTask<Void, Void, Bitmap> {
        private ImageView imageView;
        private Long contactID;

        public LoadContactImage(ImageView imgView, Long contactID) {
            this.imageView = imgView;
            this.contactID = contactID;

        }

        @Override
        protected Bitmap doInBackground(Void... params) {
           return retrieveContactPhoto(contactID);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            imageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap retrieveContactPhoto(Long contactID) {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(mContext.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }

            assert inputStream != null;
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return photo;
    }
}
