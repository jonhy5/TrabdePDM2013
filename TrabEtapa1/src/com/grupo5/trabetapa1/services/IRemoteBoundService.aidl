package com.grupo5.trabetapa1.services;

import com.grupo5.trabetapa1.parcelable.UserInfo;
import com.grupo5.trabetapa1.services.UserInfoReceiver;

interface IRemoteBoundService {
	void getStatus();
	void setCallback(UserInfoReceiver callback);
	void unsetCallback();
}