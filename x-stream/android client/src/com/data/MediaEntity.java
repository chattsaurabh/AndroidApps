package com.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class MediaEntity implements Parcelable {

	private String media_title;
	private String media_thumbnailUri;
	private String media_desc;
	private String media_mediaUri;
	private String media_artist;
	private String media_channel;
	private String media_type;

	private int tempThumbnailImage;

	public MediaEntity() {
	}

	public MediaEntity(Parcel in) {
		readFromParcel(in);
	}

	public void setMedia_channel(String media_channel) {
		this.media_channel = null;
		this.media_channel = media_channel;
	}

	public String getMedia_channel() {
		return media_channel;
	}

	public void setMedia_artist(String media_artist) {
		this.media_artist = media_artist;
	}

	public String getMedia_artist() {
		return media_artist;
	}

	public void setMedia_mediaUri(String media_mediaUri) {
		this.media_mediaUri = media_mediaUri;
	}

	public String getMedia_mediaUri() {
		return media_mediaUri;
	}

	public void setMedia_desc(String media_desc) {
		this.media_desc = media_desc;
	}

	public String getMedia_desc() {
		return media_desc;
	}

	public void setMedia_thumbnailUri(String media_thumbnailUri) {
		this.media_thumbnailUri = media_thumbnailUri;
	}

	public String getMedia_thumbnailUri() {
		return media_thumbnailUri;
	}

	public void setMedia_title(String media_title) {
		this.media_title = media_title;
	}

	public String getMedia_title() {
		return media_title;
	}

	public void setTempThumbnailImage(int tempThumbnailImage) {
		this.tempThumbnailImage = tempThumbnailImage;
	}

	public int getTempThumbnailImage() {
		return tempThumbnailImage;
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public MediaEntity createFromParcel(Parcel in) {
			return new MediaEntity(in);
		}

		public MediaEntity[] newArray(int size) {
			return new MediaEntity[size];
		}
	};

	public void readFromParcel(Parcel in) {

		String[] data = new String[8];
		in.readStringArray(data);
		
		setMedia_title(data[0]);
		setMedia_desc(data[1]);
		setMedia_artist(data[2]);
		setMedia_channel(data[3]);
		setMedia_thumbnailUri(data[4]);
		setMedia_mediaUri(data[5]);
		setMedia_type(data[6]);
		setTempThumbnailImage(Integer.parseInt(data[7].trim()));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[]{media_title, 
				media_desc,
				media_artist,
				media_channel,
				media_thumbnailUri,
				media_mediaUri,
				media_type,
				"" + tempThumbnailImage
				});
	}

	public void setMedia_type(String media_type) {
		this.media_type = media_type;
	}

	public String getMedia_type() {
		return media_type;
	}
}
