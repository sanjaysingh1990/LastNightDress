package com.eowise.recyclerview.stickyheaders.samples.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import com.eowise.recyclerview.stickyheaders.samples.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeAgo {

	public static final List<Long> times = Arrays.asList(
			TimeUnit.DAYS.toMillis(365),
			TimeUnit.DAYS.toMillis(30),
			TimeUnit.DAYS.toMillis(1),
			TimeUnit.HOURS.toMillis(1),
			TimeUnit.MINUTES.toMillis(1),
			TimeUnit.SECONDS.toMillis(1));
	public static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");

	protected Context context;

	public TimeAgo(Context context) {
		this.context = context;
	}

	public static String toDuration(long duration) {

		StringBuffer res = new StringBuffer();
		for (int i = 0; i < times.size(); i++) {
			Long current = times.get(i);
			long temp = duration / current;
			if (temp > 0) {
				res.append(temp).append(" ").append(timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
				break;
			}
		}
		if ("".equals(res.toString()))
			return "0 second ago";
		else
			return res.toString();
	}

	public String timeAgo(Date date) {
		return toDuration(date.getTime());
	}

	@SuppressLint("StringFormatInvalid")
	public String timeAgo(long millis) {
		long diff = new Date().getTime() - millis;

		Resources r = context.getResources();

		String prefix = r.getString(R.string.time_ago_prefix);
		String suffix = r.getString(R.string.time_ago_suffix);

		double seconds = Math.abs(diff) / 1000;
		double minutes = seconds / 60;
		double hours = minutes / 60;
		double days = hours / 24;
		double weeks = days / 7;

		String words;
		words = (int) hours + "|" + (int) minutes + "|" + (int) seconds;

		if (seconds < 60) {
			words = r.getString(R.string.time_ago_seconds, (int) Math.round(seconds));
		} else if (seconds < 90) {
			words = r.getString(R.string.time_ago_minute, 1);
		} else if (minutes < 60) {
			words = r.getQuantityString(R.plurals.time_ago_minutes,
					(int) (minutes), (int)(minutes));
		} else if (minutes < 90) {
			words = r.getString(R.string.time_ago_hour, 1);
		} else if (hours < 24) {
			words = r.getQuantityString(R.plurals.time_ago_hours,
					(int) (hours), (int) (hours));
		} else if (hours < 42) {
			words = r.getString(R.string.time_ago_day, 1);
		} else if (days < 7) {
			words = r.getQuantityString(R.plurals.time_ago_days,
					(int) (days), (int) (days));
		} else if (days < 14) {
			words = r.getQuantityString(R.plurals.time_ago_weeks,
					(int) (days / 7), (int) (days / 7));
		} else {
			words = r.getQuantityString(R.plurals.time_ago_weeks,
					(int) (weeks), (int)   (weeks));
		}

		StringBuilder sb = new StringBuilder();

		if (prefix != null && prefix.length() > 0) {
			sb.append(prefix).append(" ");
		}

		sb.append(words);

		if (suffix != null && suffix.length() > 0) {
			sb.append(" ").append(suffix);
		}

		//return sb.toString().trim();
		return words;
	}

	public static long getMilliseconds(String datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

		try {

			Date date = formatter.parse(datetime);
			// Log.e("date",date.toString());
			// Log.e("date2",formatter.format(date));

			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		Date currdate = new Date();
		String formattedDate = df.format(currdate.getTime());
		return  formattedDate;
	}
	public static String getCurrentDate(long milliseconds) {
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		Date currdate = new Date(milliseconds);
		String formattedDate = df.format(currdate.getTime());
		return  formattedDate;
	}
}
