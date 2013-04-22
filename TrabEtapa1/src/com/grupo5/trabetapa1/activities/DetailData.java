package com.grupo5.trabetapa1.activities;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;


public class DetailData implements Parcelable {

	private long id;
    private String autor;
	private String msg;
    private Date dt;

	
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
	
	public DetailData(long i, String a, String m,Date d)
	{
			id = i;
			autor = a;
			msg = m;
			dt = d;
	}
	
	public String getId(){
		return String.valueOf(id);
	}
	
	public String getAutor()
	{
		return autor;
	}
	
	public String getMsg()
	{
		return msg; 
	}
	
	public String getDate()
	{
		return dt.toString();
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
