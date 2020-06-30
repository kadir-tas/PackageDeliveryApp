package com.huawei.probation.packagedeliveryapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.huawei.probation.packagedeliveryapp.R;
import com.huawei.probation.packagedeliveryapp.adapters.ProductRecyclerViewAdapter;
import com.huawei.probation.packagedeliveryapp.data.Cart;
import com.huawei.probation.packagedeliveryapp.data.Pair;
import com.huawei.probation.packagedeliveryapp.data.ProductData;
import com.huawei.probation.packagedeliveryapp.dummy.DummyContent;
import com.huawei.probation.packagedeliveryapp.ınterfaces.OnCartChangedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProductFragment extends Fragment implements ProductRecyclerViewAdapter.OnProductButtonPressed , OnCartChangedListener  {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PRODUCT_CATEGORY = "product-category";

    private int mColumnCount = 3;

    private Cart mCartRef;

    private String mProductCatagory;
    private OnListFragmentInteractionListener mListener;
    private ProductRecyclerViewAdapter productRecyclerViewAdapter;

    public ProductFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mProductCatagory = getArguments().getString(ARG_PRODUCT_CATEGORY);

        }
    }

    public static ProductFragment newInstance(int columnCount,
                                              String productCategory,
                                              Cart cartRef) {
        ProductFragment fragment = new ProductFragment();
        fragment.mCartRef = cartRef;
        fragment.mProductCatagory = productCategory;
        fragment.mColumnCount = columnCount;
      //  Bundle args = new Bundle();
      //  args.putInt(ARG_COLUMN_COUNT, columnCount);
      ////  args.putString(ARG_PRODUCT_CATEGORY, productCategory);
       // fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);


        if (recyclerView != null) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, mColumnCount);

                recyclerView.setLayoutManager(gridLayoutManager);
            }

            Log.d("FFF", "EMEK INITTED");
            switch(mProductCatagory) {
                case "Ekmek Çeşitleri":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.EKMEK_CESITLERI, mListener, this);
                    break;
                case "240 Derece Atölye":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.ATOLYE, mListener, this);
                    break;
                case "Pastane":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.PASTANE, mListener, this);
                    break;
                case "Meyve & Sebze":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.EKMEK_CESITLERI, mListener, this);
                    break;
                case "Unlu Mamüller":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.ATOLYE, mListener, this);
                    break;
                case "Atıştırmalık":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.PASTANE, mListener, this);
                    break;
                case "Dondurma":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.DONDURMA, mListener, this);
                    break;
                case "Yiyecek":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.YIYECEK, mListener, this);
                    Log.d("MMM" , "HEREE");
                    break;
                case "İçeçek":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.ICECEK, mListener, this);

                    break;
                case "Fit & Form":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.FIT_FORM, mListener, this);

                    break;
                case "Süt & Kahvaltı":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.SUT_KAHVALTI, mListener, this);

                    break;
                case "Temel Gıda":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.TEMEL_GIDA, mListener, this);

                    break;
                case "Kişisel Bakım":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.KISISEL_BAKIM, mListener, this);

                    break;
                case "Ev & Temizlik":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.EV_TEMIZLIK, mListener, this);

                    break;
                case "Ev & Yaşam":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.EV_YASAM, mListener, this);

                    break;
                case "Teknoloji":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.TEKNOLOJI, mListener, this);

                    break;
                case "Evcil Hayvan":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.EVCIL_HAYVAN, mListener, this);

                    break;
                case "Bebek":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.BEBEK, mListener, this);

                    break;
                case "Cinsel Sağlık":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.CINSEL_SAGLIK, mListener, this);

                    break;
                case "Giyim":
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(DummyContent.GIYIM, mListener, this);
                    break;

            }
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            recyclerView.setAdapter(productRecyclerViewAdapter);

            if(productRecyclerViewAdapter == null){
                Log.d("KKK" , mProductCatagory + " is inited its adaptor with null");
            }
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("FFF", toString() + " Resume");
        List<Pair<ProductData, Integer>> products = new ArrayList<>();
        HashMap<ProductData, Integer> cart = mCartRef.getCart();

        for(ProductData key : cart.keySet()){
            if(key.getmCategory().equals(mProductCatagory)){
                products.add(new Pair<ProductData, Integer>(key, cart.get(key)));
            }
        }
        if(productRecyclerViewAdapter == null) Log.d("MMM",mProductCatagory + " adaptor null");
        productRecyclerViewAdapter.updateListProductCount(products);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d("FFF", toString() + " Pause");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }

        mCartRef.addOnCartChangedListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mCartRef.removeOnCartChangedListener(this);
    }

    @Override
    public void onPlusButtonPressed(ProductData product, int index) {
        int itemPurchsed = mCartRef.getItemCount(product) + 1;
        mCartRef.putItem(product, itemPurchsed);
    }

    @Override
    public void onMinusButtonPressed(ProductData product, int index) {
        int itemPurchsed = mCartRef.getItemCount(product) - 1;
        if(itemPurchsed <= 0) {
            mCartRef.removeItem(product);
        }else{
            mCartRef.putItem(product, itemPurchsed);
        }
    }

    boolean doubleOpen = false;
    @Override
    public void onContentClicked(Pair<ProductData, Integer> productData) {
        //TODO: FIX DOUBLE CLICK
        Fragment itemDetailFragment = getFragmentManager().findFragmentByTag("PROD_DETAIL_FRAG");
        if(itemDetailFragment == null){
            getFragmentManager().beginTransaction().add(R.id.container, ItemDetailFragment.newInstance(productData,mCartRef), "PROD_DETAIL_FRAG").commit();
        }else {
            getFragmentManager().beginTransaction().remove(itemDetailFragment).commit();
            getFragmentManager().beginTransaction().add(R.id.container, ItemDetailFragment.newInstance(productData,mCartRef), "PROD_DETAIL_FRAG").commit();
        }
    }

    @Override
    public void onItemChangedListener(ProductData productData, int count) {
        if(productData.getmCategory().equals(mProductCatagory)){
            productRecyclerViewAdapter.updateListProductCount(productData, count);
        }
    }

    @Override
    public void onCartChangedListener(HashMap<ProductData, Integer> cart, float totalPrice) {

    }

    @Override
    public void onItemRemovedListener(ProductData productData) {
        if(productData.getmCategory().equals(mProductCatagory)){
            productRecyclerViewAdapter.updateListProductCount(productData, 0);
        }
    }

    @Override
    public void onCartClearedListener() {
        productRecyclerViewAdapter.clearBuyCounts();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ProductData productData);
    }

    @Override
    public String toString() {
        return mProductCatagory;
    }

}
