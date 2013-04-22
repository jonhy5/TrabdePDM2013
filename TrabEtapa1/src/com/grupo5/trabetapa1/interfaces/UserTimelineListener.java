package com.grupo5.trabetapa1.interfaces;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;

public interface UserTimelineListener {
	void completeReport(List<Status> list);
}