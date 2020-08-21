package com.example.shreehari.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shreehari.R;

public class ResultDetails extends Fragment {
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
		View view = inflater.inflate(R.layout.fragment_result_details, container, false);

		pd1 = view.findViewById(R.id.pd1);
		pd1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new ResultDetailsScore(),"Fragment",null);

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