package com.example.shreehari.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.shreehari.R;

public class AnnouncementsVIewStatus extends Fragment {
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
		View view = inflater.inflate(R.layout.fragment_addannouncement_status, container, false);

		final LinearLayout parent = view.findViewById(R.id.parent);
		LinearLayout student = view.findViewById(R.id.student);
		LinearLayout both = view.findViewById(R.id.both);

		final TextView both_txt = view.findViewById(R.id.both_txt);
		final TextView student_txt = view.findViewById(R.id.student_txt);
		final TextView parent_txt = view.findViewById(R.id.parent_txt);


		parent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				parent_txt.setBackground(getResources().getDrawable(R.drawable.bg_black_border));
				student_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
				both_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
			}
		});

		student.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				parent_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
				student_txt.setBackground(getResources().getDrawable(R.drawable.bg_black_border));
				both_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));

			}
		});

		both.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				parent_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
				student_txt.setBackground(getResources().getDrawable(R.drawable.bg_yellow_border));
				both_txt.setBackground(getResources().getDrawable(R.drawable.bg_black_border));
			}
		});

		return view;
	}

}