package com.eowise.recyclerview.stickyheaders.samples.Utils;

import com.eowise.recyclerview.stickyheaders.samples.R;

public interface ApplicationConstants {

	// Php Application URL to store Reg ID created
	public static final String APP_SERVER_URL_LND_USER="http://52.76.68.122/lnd/androidiosphpfiles/lndusers.php";
	public static final String APP_SERVER_URL_LND_NOTIFICATION_SETTING="http://52.76.68.122/lnd/androidiosphpfiles/notification.php";
	public static final String APP_SERVER_URL_LND_POST_DATA="http://52.76.68.122/lnd/androidiosphpfiles/postdata.php";
	public static final String APP_SERVER_URL_LND_NOTIFICATION="http://52.76.68.122/lnd/androidiosphpfiles/lndnotification.php";
	public static final String APP_SERVER_URL_LND_FOLLOWING="http://52.76.68.122/lnd/androidiosphpfiles/followfollowing.php";
	public static final String APP_SERVER_URL_LND_INBOXOPERATION="http://52.76.68.122/lnd/androidiosphpfiles/inboxope.php";
	public static final String APP_SERVER_URL_LND_COMMENTS="http://52.76.68.122/lnd/androidiosphpfiles/lndcomments.php";
	public static final String APP_SERVER_URL_LND_SHIPPINGINFO="http://52.76.68.122/lnd/androidiosphpfiles/lndshippinginfodata.php";
	public static final String APP_SERVER_URL_LND_LNDPOST="http://52.76.68.122/lnd/androidiosphpfiles/lndpost.php";
	public static final String APP_SERVER_URL_LND_FADEX_PURCHASE_SHIPPING_LABLE="http://sikhdiary.com/lnd/track/fedex/createLabel";

	// Google Project Number
	static final String GOOGLE_PROJ_ID = "834036612270";
	// Message Key
	static final String MSG_KEY = "m";
    public static final String[] user_position={"","Basic User","Agent","Agency","Area Manager","Regional Director"};
	int[] icons = {0, 0, R.drawable.agent_level, R.drawable.agency_level, R.drawable.area_manager_level, R.drawable.regional_manager_level, R.drawable.sales_director_level};

}
