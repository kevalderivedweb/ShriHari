package com.mystudycanada.shreehari.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mystudycanada.shreehari.R;

public class EXTRASCREEN extends Fragment {
	// Store instance variables
	private String title;
	private int page;
	private View pd1;

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_extra_btn, container, false);

		TextView ID1  = view.findViewById(R.id.id1);
		TextView ID2  = view.findViewById(R.id.id2);
		TextView ID3  = view.findViewById(R.id.id3);
		TextView ID4  = view.findViewById(R.id.id4);
		TextView ID5  = view.findViewById(R.id.id5);
		TextView ID6  = view.findViewById(R.id.id6);
		TextView ID7  = view.findViewById(R.id.id7);
		TextView ID8  = view.findViewById(R.id.id8);
		TextView ID9  = view.findViewById(R.id.id9);

		ID1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AddAnnouncements(),"Fragment",null);
			}
		});
		ID2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AddAnnouncementsForStudentDetails(),"Fragment",null);
			}
		});
		ID3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AnnouncementsFilter(),"Fragment",null);
			}
		});
		ID4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AnnouncementsVIewStatus(),"Fragment",null);
			}
		});

		ID5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AssignmentFilter(),"Fragment",null);
			}
		});

		ID6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new Assignment_Single(),"Fragment",null);
			}
		});
		ID7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new SendToGroup(),"Fragment",null);
			}
		});

		ID8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AddAnnouncements(),"Fragment",null);
			}
		});

		ID9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AddAnnouncementsNew(),"Fragment",null);
			}
		});
		return view;
	}

	protected void replaceFragment(@IdRes int containerViewId,
								   @NonNull Fragment fragment,
								   @NonNull String fragmentTag,
								   @Nullable String backStackStateName) {
		getActivity().getSupportFragmentManager()
				.beginTransaction()
				.replace(containerViewId, fragment, fragmentTag)
				.addToBackStack(backStackStateName)
				.commit();
	}


}