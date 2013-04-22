package com.grupo5.trabetapa1.activities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.grupo5.trabetapa1.R;

import winterwell.jtwitter.Twitter.Status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimelineAdapter extends BaseAdapter {
	private static final int NUMOFCHAR = 10;
	private Context context;
	private final List<Status> list;

    public TimelineAdapter(Context context, List<Status> list) {
    	this.context = context;
    	this.list = list;
    }
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View gridView;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			gridView = new View(context);
			
			// get layout from timelinerow.xml
			gridView = inflater.inflate(R.layout.timeline_row, null);
			
			Status status = list.get(position);
			String msg = status.getText();
			Date dt = status.getCreatedAt();
			Date dtAct =  Calendar.getInstance().getTime();
			if(status.getText().length() > NUMOFCHAR){
				msg = msg.substring(0, NUMOFCHAR-3)+"...";
			}
			((TextView) gridView.findViewById(R.id.messageColumnTextView)).setText(msg);
			((TextView) gridView.findViewById(R.id.authorColumnTextView)).setText(status.getUser().getName());
			((TextView) gridView.findViewById(R.id.dateColumnTextView)).setText(dt.toString());
			
		} else {
			gridView = (View) convertView;
		}
		return gridView;
	}
}
