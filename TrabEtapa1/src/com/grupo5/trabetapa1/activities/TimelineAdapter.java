package com.grupo5.trabetapa1.activities;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grupo5.trabetapa1.R;
import com.grupo5.trabetapa1.sql.StatusModel;

public class TimelineAdapter extends ArrayAdapter<StatusModel> {
	private static final int NUMOFCHAR = 10;
	
	public TimelineAdapter(Context context, int textViewResourceId, List<StatusModel> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View 	gridView;
		Context context = getContext();
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// get layout from timelinerow.xml
		gridView = inflater.inflate(R.layout.timeline_row, null);
		
		StatusModel status = getItem(position);
		String msg = status.getMessage();
		String date;
		
		long seg = (new Date().getTime() - status.getDate()) / 1000;
		if(seg < 60) {
			date = seg + " " + context.getString(R.string.timeline_Seconds);
		}
		else if(seg / 60 < 60) {
			date = (seg / 60) + " " + context.getString(R.string.timeline_Minutes);
		}
		else if(seg / 60 / 60 < 24) {
			date = (seg / 60 / 60) + " " + context.getString(R.string.timeline_Hours);
		}
		else {
			date = (seg / 60 / 60 / 24) + " " + context.getString(R.string.timeline_Days);
		}
		
		if(msg.length() > NUMOFCHAR){
			msg = msg.substring(0, NUMOFCHAR - 3) + "...";
		}
		((TextView) gridView.findViewById(R.id.messageColumnTextView)).setText(msg);
		((TextView) gridView.findViewById(R.id.authorColumnTextView)).setText(status.getAuthor());
		((TextView) gridView.findViewById(R.id.dateColumnTextView)).setText(date);

		return gridView;
	}
}
