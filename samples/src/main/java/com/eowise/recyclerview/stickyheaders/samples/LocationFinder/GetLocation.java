package com.eowise.recyclerview.stickyheaders.samples.LocationFinder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sanjay on 12/14/2015.
 */
public class GetLocation
{
 private   GPSTracker gps;
private Context con;
private TextView location;
public GetLocation(Context con,TextView show)
{
    this.con=con;
    this.location=show;

}
    public void getLocation()
    {
        gps = new GPSTracker(con);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude, con, new GeocoderHandler());
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            location.setText(locationAddress);
        }
    }
}
