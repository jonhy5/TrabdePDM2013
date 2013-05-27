package com.grupo5.trabetapa1.services;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable{

	private String screenName;
	private int statusCount;
	private int friendsCount;
	private int followersCount;
	private String image;
	private Bitmap imageBitmap;
	
	public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>()
	{
		@Override
    	public UserInfo createFromParcel(Parcel in)
    	{
            return new UserInfo(in);
    	}

        public UserInfo[] newArray(int size) {
            throw new UnsupportedOperationException();
        }

    };
    
    public UserInfo(Parcel in)
	{
    	screenName = in.readString();
    	statusCount = in.readInt();
    	friendsCount = in.readInt();
    	followersCount = in.readInt();
    	image = in.readString();
    	imageBitmap = (Bitmap)in.readParcelable(Bitmap.class.getClassLoader());
    }
    
	public UserInfo(String name, int sc, int friends, int follower, String img, Bitmap bitmap) {
		screenName = name;
    	statusCount = sc;
    	friendsCount = friends;
    	followersCount = follower;
    	image = img;
    	imageBitmap = bitmap;
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
		out.writeString(image);
		out.writeParcelable(imageBitmap, flags);
	}
	
	public String getName() {
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
	
	public String getImageUri()
	{
		return image;
	}
	
	public Bitmap getImageBitmap()
	{
		return imageBitmap;
	}
}
