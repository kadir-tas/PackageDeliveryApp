package com.huawei.probation.packagedeliveryapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.huawei.probation.packagedeliveryapp.R;
import com.huawei.probation.packagedeliveryapp.adapters.FragPageAdapter;
import com.huawei.probation.packagedeliveryapp.data.Cart;
import com.google.android.material.tabs.TabLayout;

public class ShopFragment extends Fragment implements
        com.huawei.probation.packagedeliveryapp.ınterfaces.OnBackPressed {

    private static final String ARG_CATEGORY = "category";

    private OnFragmentInteractionListener mListener;
    private FragPageAdapter fragmentPagerAdapter;
    private String mArgCategory;

    private Cart mCartRef;

    public ShopFragment() {
        // Required empty public constructor
    }

    public static ShopFragment newInstance(String category, Cart cartref) {
        ShopFragment fragment = new ShopFragment();
        fragment.mCartRef = cartref;
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArgCategory = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        fragmentPagerAdapter = new FragPageAdapter(getFragmentManager());
        ViewPager vpPager = (ViewPager) v.findViewById(R.id.vpPager);
        TabLayout tabLayout = v.findViewById(R.id.tab_layout);

        ProductFragment frag1 = ProductFragment.newInstance(2,"Bread Types",mCartRef);
        ProductFragment frag2 = ProductFragment.newInstance(2,"Workshop",mCartRef);
        ProductFragment frag3 = ProductFragment.newInstance(2,"Patisserie",mCartRef);

        fragmentPagerAdapter.addPage(frag1);
        fragmentPagerAdapter.addPage(frag2);
        fragmentPagerAdapter.addPage(frag3);

        vpPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(vpPager);
        fragmentPagerAdapter.notifyDataSetChanged();

        switch(mArgCategory) {
            case "Bread Types":
                tabLayout.getTabAt(0).select();
                break;
            case "Workshop":
                tabLayout.getTabAt(1).select();
                break;
            case "Patisserie":
                tabLayout.getTabAt(2).select();
                break;
        }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

  @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
