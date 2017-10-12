package com.chicago.library.model;

public class LibraryVO {

	private String mTeacherInLibrary;
	private String mZip;
	private String mHoursOfOperation;
	private String mWebsite;
	private String mAddress;
	private String mCity;
	private String mPhone;
	private String mState;
	private String mCyberNavigator;
	private String mName;
	
	class Location {
		public Location(String lat, boolean needsRec, String longitude) {
			mLat = lat;
			needsRecording = needsRec;
			mLong = longitude;
		}
		public String mLat;
		public boolean needsRecording;
		public String mLong;
	}
	
	private Location mLocation;

	public String isTeacherInLibrary() {
		return mTeacherInLibrary;
	}

	public void setTeacherInLibrary(String teacherInLibrary) {
		mTeacherInLibrary = teacherInLibrary;
	}

	public String getZip() {
		return mZip;
	}

	public void setZip(String zip) {
		mZip = zip;
	}

	public String getHoursOfOperation() {
		return mHoursOfOperation;
	}

	public void setHoursOfOperation(String hourseOfOperation) {
		mHoursOfOperation = hourseOfOperation;
	}

	public String getWebsite() {
		return mWebsite;
	}

	public void setWebsite(String website) {
		mWebsite = website;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		mAddress = address;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		mCity = city;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	public String getState() {
		return mState;
	}

	public void setState(String state) {
		mState = state;
	}

	public String isCyberNavigator() {
		return mCyberNavigator;
	}

	public void setCyberNavigator(String cyberNavigator) {
		mCyberNavigator = cyberNavigator;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public Location getLocation() {
		return mLocation;
	}

	public void setLocation(String lat, boolean needsRec, String longitude) {
		mLocation = new Location(lat, needsRec, longitude);
	}
	
	
}
