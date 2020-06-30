package com.huawei.probation.packagedeliveryapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.huawei.probation.packagedeliveryapp.R;
import com.huawei.probation.packagedeliveryapp.adapters.CartRecyclerViewAdapter;
import com.huawei.probation.packagedeliveryapp.data.Cart;
import com.huawei.probation.packagedeliveryapp.data.ProductData;
import com.huawei.probation.packagedeliveryapp.ınterfaces.OnCartChangedListener;

import java.util.HashMap;

public class CartFragment extends Fragment implements CartRecyclerViewAdapter.OnProductButtonPressed, OnCartChangedListener {

    private OnFragmentInteractionListener mListener;
    private Cart mCartRef;

    private OnItemsPurchased mOnItemsPurchased;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private Button confirmButton;

    public CartFragment() {

    }

    public static CartFragment newInstance(Cart cartref, OnItemsPurchased onItemsPurchased) {
        CartFragment fragment = new CartFragment();
        fragment.mCartRef = cartref;
        fragment.mOnItemsPurchased = onItemsPurchased;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        confirmButton = v.findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCartRef.getCart().size() > 0)
                    mOnItemsPurchased.onItemsPurchased();
                else
                {
                    Toast toast = Toast.makeText(getContext(), "There is nothing on your cart", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        recyclerView = v.findViewById(R.id.cart_list);
        Context context = v.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(mCartRef.toListPair(),this);
        recyclerView.setAdapter(cartRecyclerViewAdapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
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

        mCartRef.addOnCartChangedListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onRemoveButtonPressed(ProductData productData, int index) {
        mCartRef.removeItem(productData);
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

    @Override
    public void onItemChangedListener(ProductData productData, int count) {
        cartRecyclerViewAdapter.updateListProductCount(productData,count);
    }

    @Override
    public void onCartChangedListener(HashMap<ProductData, Integer> cart, float totalPrice) {

    }

    @Override
    public void onItemRemovedListener(ProductData productData) {
        mCartRef.removeItem(productData);
        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(mCartRef.toListPair(),this);
        recyclerView.setAdapter(cartRecyclerViewAdapter);
    }

    @Override
    public void onCartClearedListener() {
        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(mCartRef.toListPair(),this);
        recyclerView.setAdapter(cartRecyclerViewAdapter);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public interface OnItemsPurchased{
        void onItemsPurchased();
    }
}
