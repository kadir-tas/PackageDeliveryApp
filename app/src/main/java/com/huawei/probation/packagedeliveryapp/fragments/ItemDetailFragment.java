package com.huawei.probation.packagedeliveryapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.huawei.probation.packagedeliveryapp.R;
import com.huawei.probation.packagedeliveryapp.data.Cart;
import com.huawei.probation.packagedeliveryapp.data.Pair;
import com.huawei.probation.packagedeliveryapp.data.ProductData;

public class ItemDetailFragment extends Fragment {

    private Pair<ProductData, Integer> mProductData;

    private TextView mProductName;
    private TextView mProductPrice;
    private TextView mProductDetail;
    private TextView mProductCount;

    private Button mProductCountIncrement;
    private Button mProductCountDecrement;
    private Button mProductAddCart;
    private Button mProductRemoveCart;

    private Spinner mProductType;

    //TODO: TEMPORARY
    private Cart mShopCart;

    private OnItemDetailInteraction mOnItemDetailInteraction;

    public ItemDetailFragment() {

    }

    public static ItemDetailFragment newInstance(Pair<ProductData, Integer> productData, Cart cartRef) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.mProductData = productData;
        fragment.mShopCart = cartRef;
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
        View v = inflater.inflate(R.layout.fragment_item_detail, container, false);

        mProductName = v.findViewById(R.id.bread_product_name);
        mProductDetail = v.findViewById(R.id.bread_product_detail);
        mProductPrice = v.findViewById(R.id.bread_product_price);
        mProductCount = v.findViewById(R.id.bread_product_count);

        mProductCountIncrement = v.findViewById(R.id.bread_product_count_increment);
        mProductCountDecrement = v.findViewById(R.id.bread_product_count_decrement);
        mProductAddCart = v.findViewById(R.id.bread_product_add_cart);
        mProductRemoveCart = v.findViewById(R.id.bread_product_remove_product);

        //TODO: DO ITEM RECOOMENDS
        getFragmentManager().beginTransaction().add(R.id.bread_product_recom, ProductFragment.newInstance(2,mProductData.first.getmCategory(),mShopCart),"PRODUCT_RECOM").commit();


        mProductAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDetailInteraction.onAddCartButtonPressed(ItemDetailFragment.this);
            }
        });

        mProductRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDetailInteraction.onRemoveCartButtonPressed(ItemDetailFragment.this);
            }
        });

        mProductCountIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mOnItemDetailInteraction.onIncrementButtonPressed(ItemDetailFragment.this);
                mProductData.second++;
                updateView();
            }
        });

        mProductCountDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mOnItemDetailInteraction.onDecrementButtonPressed(ItemDetailFragment.this);
                if(mProductData.second > 0)
                    mProductData.second--;
                updateView();
            }
        });

        updateView();
        return v;
    }


    public void updateView(){
        ProductData productData = mProductData.first;
        mProductName.setText( productData.getmProductName() );
        mProductDetail.setText( productData.getmDetail() );
        mProductCount.setText( mProductData.second + "");
        mProductPrice.setText( productData.getmProductPrice() + "");

        mProductDetail.setText((getResources().getString(R.string.lorem)));

        updateViewFunction();
    }

    private void updateViewFunction(){
        if(mProductData.second > 0){
            mProductRemoveCart.setVisibility(View.VISIBLE);
        }else {
            mProductRemoveCart.setVisibility(View.INVISIBLE);
        }

        //TODO: UPDATE VIEW FUNCTION OF SPINNER
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  OnItemDetailInteraction){
            mOnItemDetailInteraction = (OnItemDetailInteraction) context;
        }else{
            throw new RuntimeException(context.toString() + " MUST IMPLEMENT OnItemDetailInteraction plz");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public Pair<ProductData, Integer> getProduct(){
        return mProductData;
    }

    public interface OnItemDetailInteraction{
        void onAddCartButtonPressed(ItemDetailFragment itemDetailFragment);
        void onRemoveCartButtonPressed(ItemDetailFragment itemDetailFragment);
    }
}
