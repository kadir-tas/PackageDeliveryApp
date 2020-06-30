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

        ProductFragment frag1 = ProductFragment.newInstance(2,"Ekmek Çeşitleri",mCartRef);
        ProductFragment frag2 = ProductFragment.newInstance(2,"240 Derece Atölye",mCartRef);
        ProductFragment frag3 = ProductFragment.newInstance(2,"Pastane",mCartRef);
      /*  ProductFragment frag4 = ProductFragment.newInstance(1,"Dondurma",mCartRef);
        ProductFragment frag5 = ProductFragment.newInstance(2,"Yiyecek",mCartRef);
        ProductFragment frag6 = ProductFragment.newInstance(1,"İçeçek",mCartRef);
        ProductFragment frag7 = ProductFragment.newInstance(2,"Fit & Form",mCartRef);
        ProductFragment frag8 = ProductFragment.newInstance(2,"Süt & Kahvaltı",mCartRef);
        ProductFragment frag9 = ProductFragment.newInstance(2,"Temel Gıda",mCartRef);
        ProductFragment frag10 = ProductFragment.newInstance(2,"Kişisel Bakım",mCartRef);
        ProductFragment frag11 = ProductFragment.newInstance(2,"Ev & Temizlik",mCartRef);
        ProductFragment frag12 = ProductFragment.newInstance(1,"Ev & Yaşam",mCartRef);
        ProductFragment frag13 = ProductFragment.newInstance(2,"Teknoloji",mCartRef);
        ProductFragment frag14 = ProductFragment.newInstance(1,"Evcil Hayvan",mCartRef);
        ProductFragment frag15 = ProductFragment.newInstance(2,"Bebek",mCartRef);
        ProductFragment frag16 = ProductFragment.newInstance(2,"Cinsel Sağlık",mCartRef);
        ProductFragment frag17 = ProductFragment.newInstance(2,"Giyim",mCartRef);
*/
        fragmentPagerAdapter.addPage(frag1);
        fragmentPagerAdapter.addPage(frag2);
        fragmentPagerAdapter.addPage(frag3);

    /*    fragmentPagerAdapter.addPage(frag4);
        fragmentPagerAdapter.addPage(frag5);
        fragmentPagerAdapter.addPage(frag6);
        fragmentPagerAdapter.addPage(frag7);
        fragmentPagerAdapter.addPage(frag8);
        fragmentPagerAdapter.addPage(frag9);
        fragmentPagerAdapter.addPage(frag10);
        fragmentPagerAdapter.addPage(frag11);
        fragmentPagerAdapter.addPage(frag12);
        fragmentPagerAdapter.addPage(frag13);
        fragmentPagerAdapter.addPage(frag14);
        fragmentPagerAdapter.addPage(frag15);
        fragmentPagerAdapter.addPage(frag16);
        fragmentPagerAdapter.addPage(frag17);
*/
        vpPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(vpPager);
        fragmentPagerAdapter.notifyDataSetChanged();

        switch(mArgCategory) {
            case "Ekmek Çeşitleri":
                tabLayout.getTabAt(0).select();
                break;
            case "240 Derece Atölye":
                tabLayout.getTabAt(1).select();
                break;
            case "Pastane":
                tabLayout.getTabAt(2).select();
                break;
            case "Dondurma":
                tabLayout.getTabAt(3).select();
                break;
            case "Yiyecek":
                tabLayout.getTabAt(4).select();
                break;
            case "İçeçek":
                tabLayout.getTabAt(5).select();
                break;
            case "Fit & Form":
                tabLayout.getTabAt(6).select();
                break;
            case "Süt & Kahvaltı":
                tabLayout.getTabAt(7).select();
                break;
            case "Temel Gıda":
                tabLayout.getTabAt(8).select();
                break;
            case "Kişisel Bakım":
                tabLayout.getTabAt(9).select();
                break;
            case "Ev & Temizlik":
                tabLayout.getTabAt(10).select();
                break;
            case "Ev & Yaşam":
                tabLayout.getTabAt(11).select();
                break;
            case "Teknoloji":
                tabLayout.getTabAt(12).select();
                break;
            case "Evcil Hayvan":
                tabLayout.getTabAt(13).select();
                break;
            case "Bebek":
                tabLayout.getTabAt(14).select();
                break;
            case "Cinsel Sağlık":
                tabLayout.getTabAt(15).select();
                break;
            case "Giyim":
                tabLayout.getTabAt(16).select();
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
