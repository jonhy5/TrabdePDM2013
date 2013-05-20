package com.grupo5.trabetapa1.services;

import android.os.Parcel;
import android.os.Parcelable;

public class MyParcelable implements Parcelable{

	private String screenName;
	private int statusCount;
	private int friendsCount;
	private int followersCount;
	private String  imageUrl;
	
	
	public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>()
	{
		@Override
    	public MyParcelable createFromParcel(Parcel in)
    	{
            return new MyParcelable(in);
    	}

        public MyParcelable[] newArray(int size) {
            throw new UnsupportedOperationException();
        }

    };
    
    public MyParcelable(Parcel in)
	{
    	screenName = in.readString();
    	statusCount = in.readInt();
    	friendsCount = in.readInt();
    	followersCount = in.readInt();
    	imageUrl = in.readString();
  	
    }
    
	public MyParcelable(String name, int sc, int friends, int follower, String image)
	{
		screenName = name;
    	statusCount = sc;
    	friendsCount = friends;
    	followersCount = follower;
    	imageUrl = image;
	}
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(screenName);
		out.writeInt(statusCount);
		out.writeInt(friendsCount);
		out.writeInt(followersCount);
		out.writeString(imageUrl);
		
	}
	
	public String getName()
	{
		return screenName;
	}
	
	public int getStatusCount()
	{
		return statusCount;
	}
	
	public int getFriendsCount()
	{
		return friendsCount;
	}
	
	public int getFollowersCount()
	{
		return followersCount;
	}
	
	
}
