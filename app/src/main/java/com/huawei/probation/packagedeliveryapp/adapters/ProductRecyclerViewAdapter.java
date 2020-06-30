package com.huawei.probation.packagedeliveryapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.huawei.probation.packagedeliveryapp.R;
import com.huawei.probation.packagedeliveryapp.data.Pair;
import com.huawei.probation.packagedeliveryapp.data.ProductData;
import com.huawei.probation.packagedeliveryapp.fragments.ProductFragment.OnListFragmentInteractionListener;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    private final List<Pair<ProductData, Integer>> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final OnProductButtonPressed mOnProductButtonPressed;

    public ProductRecyclerViewAdapter(List<Pair<ProductData, Integer>> items,
                                      OnListFragmentInteractionListener listener,
                                      OnProductButtonPressed onProductButtonPressed) {
        mValues = items;
        mListener = listener;
        mOnProductButtonPressed = onProductButtonPressed;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product, parent, false);
        return new ViewHolder(view);
    }

    public void updateListProductCount(int index, int productCount){
        if(productCount < 0) {
            throw new RuntimeException("NO NEGATIVES PLZ");
        }

        mValues.get(index).second = productCount;
        notifyItemChanged(index);
    }

    public void clearBuyCounts(){
        for(int i = 0 ;i < mValues.size() ; i++){
            if(mValues.get(i).second != 0){
                mValues.get(i).second = 0;
                notifyItemChanged(i);
            }
        }
    }

    public void updateListProductCount(List<Pair<ProductData, Integer>> products){
        if(products.size() > 0){
            for(int i = 0 ;i <products.size() ; i++){
                for(int j = 0; j < mValues.size() ; j++){
                    if(mValues.get(j).first.equals(products.get(i).first)){
                        mValues.get(j).second = products.get(i).second;
                        notifyItemChanged(j);
                    }
                }
            }
        }else {
            clearBuyCounts();
        }
    }

    public void updateListProductCount(ProductData productData, int productCount){
        if(productCount < 0) {
            throw new RuntimeException("NO NEGATIVES PLZ");
        }

        for(int i = 0 ;i < mValues.size() ; i++){
            if(mValues.get(i).first.equals(productData)){
                mValues.get(i).second = productCount;
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

            Log.d("BBB",  " index is  " + position);
            holder.mProductData = mValues.get(position);
            final ProductData product = holder.mProductData.first;

            holder.mProductName.setText(product.getmProductName());
            holder.mProductPrice.setText(Float.toString(product.getmProductPrice()));

            holder.mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnProductButtonPressed.onPlusButtonPressed(holder.mProductData.first, position);
                }
            });

            holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnProductButtonPressed.onMinusButtonPressed(holder.mProductData.first, position);
                }
            });

            holder.mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnProductButtonPressed.onContentClicked(mValues.get(position));
                    Log.d("JJJ", "BUTT CLICK");
                }
            });



        int buyCount = holder.mProductData.second;

        if(buyCount > 0){
            holder.mProductCount.setText("Adet " + buyCount);
            holder.mRemoveButton.setVisibility(View.VISIBLE);
            holder.mRemoveButton.setEnabled(true);

            holder.mProductCount.setVisibility(View.VISIBLE);
            holder.mProductCount.setEnabled(true);
        }else{

            holder.mRemoveButton.setVisibility(View.INVISIBLE);
            holder.mRemoveButton.setEnabled(false);

            holder.mProductCount.setVisibility(View.INVISIBLE);
            holder.mProductCount.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Pair<ProductData, Integer> mProductData;

        public final View mView;
        public final TextView mProductName;
        public final TextView mProductPrice;
        public final TextView mProductCount;
        public final Button mAddButton;
        public final Button mRemoveButton;
        public final ImageView mProductImage;
        public final View mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProductCount = view.findViewById(R.id.product_count);
            mProductPrice = view.findViewById(R.id.product_price);
            mProductName =  view.findViewById(R.id.product_name);
            mAddButton = view.findViewById(R.id.add_button);
            mRemoveButton = view.findViewById(R.id.remove_button);
            mProductImage = view.findViewById(R.id.product_image);
            mContentView = view.findViewById(R.id.product_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProductName.getText() + "'";
        }
    }

    public interface OnProductButtonPressed{
        void onPlusButtonPressed(ProductData product, int index);
        void onMinusButtonPressed(ProductData product, int index);
        void onContentClicked(Pair<ProductData, Integer> productDataInteger);
    }
}
