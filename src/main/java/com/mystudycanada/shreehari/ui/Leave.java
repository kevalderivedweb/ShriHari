package com.mystudycanada.shreehari.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.mystudycanada.shreehari.Adapter.Pager;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.google.android.material.tabs.TabLayout;

public class Leave extends Fragment implements TabLayout.OnTabSelectedListener {


	//This is our tablayout
	private TabLayout tabLayout;

	//This is our viewPager
	private ViewPager viewPager;

	public static Context Leavecontext;
	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_leave, container, false);

		Leavecontext = getContext();
		view.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				replaceFragment(R.id.nav_host_fragment,new AddLeave(),"Fragment",null);

			}
		});

		UserSession userSession = new UserSession(getContext());
		if(userSession.getUserType().equals("admin")){
			view.findViewById(R.id.add_btn).setVisibility(View.GONE);
		}else if(userSession.getUserType().equals("parent")){
			view.findViewById(R.id.add_btn).setVisibility(View.GONE);
		}else {
			view.findViewById(R.id.add_btn).setVisibility(View.VISIBLE);
		}

		//Initializing the tablayout
		tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

		//Adding the tabs using addTab() method
		tabLayout.addTab(tabLayout.newTab().setText("Pending"));
		tabLayout.addTab(tabLayout.newTab().setText("Approve"));
		tabLayout.addTab(tabLayout.newTab().setText("Rejected"));
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

		//Initializing viewPager
		viewPager = (ViewPager) view.findViewById(R.id.pager);

		viewPager.setOffscreenPageLimit(3);
		//Creating our pager adapter
		Pager adapter = new Pager(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

		//Adding adapter to pager
		viewPager.setAdapter(adapter);
		//tabLayout.setupWithViewPager(viewPager);
		//Adding onTabSelectedListener to swipe views

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				tabLayout.selectTab(tabLayout.getTabAt(position));

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		tabLayout.setupWithViewPager(viewPager);



		return view;
	}


	@Override
	public void onTabSelected(TabLayout.Tab tab) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(TabLayout.Tab tab) {

	}

	@Override
	public void onTabReselected(TabLayout.Tab tab) {

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