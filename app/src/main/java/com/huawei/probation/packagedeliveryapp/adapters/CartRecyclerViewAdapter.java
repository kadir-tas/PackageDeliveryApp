package com.huawei.probation.packagedeliveryapp.adapters;

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

import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

    private final List<Pair<ProductData, Integer>> mValues;
   // private final ProductFragment.OnListFragmentInteractionListener mListener;
    private final OnProductButtonPressed mOnProductButtonPressed;

    public CartRecyclerViewAdapter(List<Pair<ProductData, Integer>> items,
                                   OnProductButtonPressed onProductCountChangedListenner) {
        mValues = items;
        mOnProductButtonPressed = onProductCountChangedListenner;
    }

    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);
        return new CartRecyclerViewAdapter.ViewHolder(view);
    }

    public void updateListProductCount(int index, int productCount){
        if(productCount < 0) {
            throw new RuntimeException("NO NEGATIVES PLZ");
        }

        mValues.get(index).second = productCount;
        notifyItemChanged(index);
    }

    public void clearProducts(){
        mValues.clear();
        notifyDataSetChanged();
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
    public void onBindViewHolder(final CartRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mCartProduct = mValues.get(position);
        ProductData product = holder.mCartProduct.first;

        holder.mProductName.setText(product.getmProductName());
        holder.mProductPrice.setText(Float.toString(product.getmProductPrice()));

        holder.mCartProductRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnProductButtonPressed.onRemoveButtonPressed(holder.mCartProduct.first, position);
            }
        });

        holder.mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnProductButtonPressed.onPlusButtonPressed(holder.mCartProduct.first, position);
            }
        });

        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnProductButtonPressed.onMinusButtonPressed(holder.mCartProduct.first, position);

            }
        });


        int buyCount = holder.mCartProduct.second;

        if(buyCount > 0){
            holder.mProductCount.setText(buyCount + "");
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

        public Pair<ProductData, Integer> mCartProduct;

        public final View mView;
        public final TextView mProductName;
        public final TextView mProductPrice;
        public final TextView mProductCount;
        public final Button mAddButton;
        public final Button mRemoveButton;
        public final Button mCartProductRemoveButton;
        public final ImageView mProductImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProductCount = view.findViewById(R.id.product_count);
            mProductPrice = view.findViewById(R.id.product_price);
            mProductName =  view.findViewById(R.id.product_name);
            mAddButton = view.findViewById(R.id.add_button);
            mRemoveButton = view.findViewById(R.id.remove_button);
            mProductImage = view.findViewById(R.id.product_image);
            mCartProductRemoveButton = view.findViewById(R.id.cart_product_remove);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProductName.getText() + "'";
        }


    }

    public interface OnProductButtonPressed{
        void onRemoveButtonPressed(ProductData productData, int index);
        void onPlusButtonPressed(ProductData product, int index);
        void onMinusButtonPressed(ProductData product, int index);
    }
}
