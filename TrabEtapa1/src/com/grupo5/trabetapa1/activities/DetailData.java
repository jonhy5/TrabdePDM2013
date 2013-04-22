package com.grupo5.trabetapa1.activities;

import android.os.Parcel;
import android.os.Parcelable;


public class DetailData implements Parcelable {

	private String msg;
	
	protected static final Parcelable.Creator<DetailData> CREATOR = new Parcelable.Creator<DetailData>()
	{
        	public DetailData createFromParcel()
        	{
	            final DetailData d = new DetailData();
	           
	            return d;
        	}

        public DetailData[] newArray(int size) {
            throw new UnsupportedOperationException();
        }

		@Override
		public DetailData createFromParcel(Parcel arg0) {
			// TODO Auto-generated method stub
			return null;
		}

    };
	
	private DetailData()
	{
		
	}
	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(msg);

		
	}
	

}
