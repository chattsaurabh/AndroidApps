package com.fanclub.model;

import java.io.File;

import com.fanclub.data.PhotoEntityVO;

import android.content.Context;

public class PhotosPagerModel extends PhotosElementModel {

	public PhotosPagerModel(Context a_cont, int a_dataRange) {
		super(a_cont, a_dataRange);
		// TODO Auto-generated constructor stub
	}
	
	public void setMetaDataFile(File a_metaDataFile) {
		m_metadataFile = a_metaDataFile;			
		calculateCurrentDataSet();		
	}
	public PhotoEntityVO getCurrentElement() {
		return m_photosDataList.get(0);
	}

}
