package com.grupo5.trabetapa1.services;

import com.grupo5.trabetapa1.services.UserInfo;
import com.grupo5.trabetapa1.services.UserInfoReceiverCallback;

interface IRemoteBoundService {
	void getStatus();
	void setCallback(UserInfoReceiverCallback callback);
	void unsetCallback();
}