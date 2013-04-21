package com.grupo5.trabetapa1.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TimelineAdapter extends BaseAdapter {

    String items[] = new String[]{"Item - 1", "Item - 2", "Item - 3", "Item - 4"};

	private String[] itens;

    public TimelineAdapter(Context context, String [] itens) {

    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itens.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itens[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	

}
