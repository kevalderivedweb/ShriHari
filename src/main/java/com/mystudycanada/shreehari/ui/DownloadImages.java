package com.mystudycanada.shreehari.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mystudycanada.shreehari.API.ServerUtils;
import com.mystudycanada.shreehari.Adapter.GalleryAdapter2;
import com.mystudycanada.shreehari.PhotoDetailsActivity;
import com.mystudycanada.shreehari.R;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DownloadImages extends Fragment {
	private ArrayList mPhotoFiles;
	private GridView gridPhoto;
	// Store instance variables
	public static boolean FILE_CHANGED = false;

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_images, container, false);

		gridPhoto = view.findViewById(R.id.grid_gallery_photo);
		reloadPhoto();
		gridPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				gotoPhotoDetail(((File) parent.getAdapter().getItem(position)).getAbsolutePath());

			}
		});

		return view;
	}

	private void reloadPhoto() {
		try {
			showListPhoto();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
		}
	}

	private void showListPhoto() {
		new Thread(new Runnable() {
			public void run() {
				final File[] files = getListImageFromSDCard();
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if (files == null || files.length <= 0) {
							Toast.makeText(getActivity(),"No Photo",Toast.LENGTH_SHORT).show();
							return;
						}
						mPhotoFiles = new ArrayList(Arrays.asList(files));
						gridPhoto.setAdapter(new GalleryAdapter2(getActivity(), mPhotoFiles));
					}
				});
			}
		}).start();
	}
	private File[] getListImageFromSDCard() {
		String[] IMAGE_ACCEPT_EXTENSIONS = new String[]{"jpg", "png", "gif", "jpeg"};

		File f = new File(getContext().getExternalFilesDir(null) + "/" + "Shree Hari Images");

		if (!f.exists()) {
			return null;
		}
		File[] files = f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				for (String extension : IMAGE_ACCEPT_EXTENSIONS) {
					if (filename.endsWith(extension)) {
						return true;
					}
				}
				return false;
			}
		});
		Collections.reverse(Arrays.asList(files));
		return files;
	}

	private void gotoPhotoDetail(String imagePathSelected) {
		Bundle bundle = new Bundle();
		bundle.putString(ServerUtils.BUNDLE_KEY_IMAGE_SELECTED, imagePathSelected);
		bundle.putSerializable(ServerUtils.BUNDLE_KEY_LIST_PHOTO, (Serializable) this.mPhotoFiles);
		Intent intent = new Intent(getActivity(), PhotoDetailsActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (FILE_CHANGED) {
			reloadPhoto();
			FILE_CHANGED = false;
		}
	}
}