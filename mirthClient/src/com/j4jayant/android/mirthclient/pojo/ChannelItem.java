package com.j4jayant.android.mirthclient.pojo;

public class ChannelItem {

	private String ChannelId;
	private String ChannelName;
	private String ChannelStatus;
	private int Revision;
	private String LastDeployed;
	private int Received;
	private int Filtered;
	private int Queued;
	private int Sent;
	private int Errored;
	private int Alerted;
	private String Connection;
	
	public String getChannelId() {
		return ChannelId;
	}
	public void setChannelId(String channelId) {
		ChannelId = channelId;
	}
	public String getChannelName() {
		return ChannelName;
	}
	public void setChannelName(String channelName) {
		ChannelName = channelName;
	}
	
	public String getChannelStatus() {
		return ChannelStatus;
	}
	public void setChannelStatus(String channelStatus) {
		ChannelStatus = channelStatus;
	}
	public int getRevision() {
		return Revision;
	}
	public void setRevision(int revision) {
		Revision = revision;
	}
	public String getLastDeployed() {
		return LastDeployed;
	}
	public void setLastDeployed(String lastDeployed) {
		LastDeployed = lastDeployed;
	}
	public int getReceived() {
		return Received;
	}
	public void setReceived(int received) {
		Received = received;
	}
	public int getFiltered() {
		return Filtered;
	}
	public void setFiltered(int filtered) {
		Filtered = filtered;
	}
	public int getQueued() {
		return Queued;
	}
	public void setQueued(int queued) {
		Queued = queued;
	}
	public int getSent() {
		return Sent;
	}
	public void setSent(int sent) {
		Sent = sent;
	}
	public int getErrored() {
		return Errored;
	}
	public void setErrored(int errored) {
		Errored = errored;
	}
	public int getAlerted() {
		return Alerted;
	}
	public void setAltered(int alerted) {
		Alerted = alerted;
	}
	public String getConnection() {
		return Connection;
	}
	public void setConnection(String connection) {
		Connection = connection;
	}
	
	
}
