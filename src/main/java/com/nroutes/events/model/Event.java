package com.nroutes.events.model;

import java.util.Arrays;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
public class Event {

	private String id;//For mongodb
	
	@Indexed(unique=true)
	private String name;
	private String sDesc;
	private String imgUrl;
	private String bgImgUrl;
	private String lDesc;	
	private String extras[];
	private EventStatus status;
	private String fcmName;
	
	private int priority;
	
	private String scheduleTime;
	public String getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getsDesc() {
		return sDesc;
	}
	public void setsDesc(String sDesc) {
		this.sDesc = sDesc;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getlDesc() {
		return lDesc;
	}
	public void setlDesc(String lDesc) {
		this.lDesc = lDesc;
	}
	
	public String[] getExtras() {
		return extras;
	}
	public void setExtras(String[] extras) {
		this.extras = extras;
	}
	
	public EventStatus getStatus() {
		return status;
	}
	public void setStatus(EventStatus status) {
		this.status = status;
	}
	public String getFcmName() {
		return fcmName;
	}
	public void setFcmName(String fcmName) {
		this.fcmName = fcmName;
	}
	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + ", sDesc=" + sDesc + ", imgUrl=" + imgUrl + ", lDesc=" + lDesc
				+ ", extras=" + Arrays.toString(extras) + ", status=" + status + ", fcmName=" + fcmName
				+ ", scheduleTime=" + scheduleTime + "]";
	}
	public String getBgImgUrl() {
		return bgImgUrl;
	}
	public void setBgImgUrl(String bgImgUrl) {
		this.bgImgUrl = bgImgUrl;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	
	
	
	
	
}
