package com.grupo5.trabetapa1.activities;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;


public class DetailData implements Parcelable {

	private long id;
    private String autor;
	private String msg;
    private long dt;

	
	public static final Parcelable.Creator<DetailData> CREATOR = new Parcelable.Creator<DetailData>()
	{
		@Override
    	public DetailData createFromParcel(Parcel in)
    	{
            return new DetailData(in);
    	}

        public DetailData[] newArray(int size) {
            throw new UnsupportedOperationException();
        }

    };
	
	public DetailData(Parcel in)
	{
		id = in.readLong();
		autor = in.readString();
		msg = in.readString();
		dt = in.readLong();
		
    }
	
	public DetailData(long i, String a, String m, long d)
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
		return new Date(dt).toString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeString(autor);
		out.writeString(msg);
		out.writeLong(dt);

		
	}
	

}
