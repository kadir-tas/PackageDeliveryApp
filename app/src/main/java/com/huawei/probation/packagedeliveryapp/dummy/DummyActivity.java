package com.huawei.probation.packagedeliveryapp.dummy;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.probation.packagedeliveryapp.R;
import com.huawei.probation.packagedeliveryapp.data.Cart;
import com.huawei.probation.packagedeliveryapp.data.ProductData;
import com.huawei.probation.packagedeliveryapp.fragments.CartFragment;
import com.huawei.probation.packagedeliveryapp.fragments.LoginFragment;
import com.huawei.probation.packagedeliveryapp.fragments.ProductFragment;
import com.huawei.probation.packagedeliveryapp.fragments.ShopFragment;
import com.huawei.probation.packagedeliveryapp.ınterfaces.OnCartChangedListener;

import java.util.HashMap;

public class DummyActivity extends AppCompatActivity
        implements ShopFragment.OnFragmentInteractionListener,ProductFragment.OnListFragmentInteractionListener,
    OnCartChangedListener, CartFragment.OnFragmentInteractionListener , CartFragment.OnItemsPurchased, LoginFragment.OnLoginListener {

    private TextView totalPrice;
    private ImageButton cartButton;
    private ShopFragment shopFragment;
    private Cart shoppingCart;


    //TODO: RENAME EVERYTHINK

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        totalPrice = findViewById(R.id.cart_price);
        cartButton = findViewById(R.id.cart_button);

        totalPrice.setVisibility(View.INVISIBLE);
        cartButton.setVisibility(View.INVISIBLE);

        getSupportFragmentManager().beginTransaction().add(R.id.container, LoginFragment.newInstance(), "LOGIN_FRAGMENT").commit();
    }

    @Override
    public void onItemChangedListener(ProductData productData, int count) {

    }

    @Override
    public void onCartChangedListener(HashMap<ProductData, Integer> cart, float totalPrice) {
        printHashMap(cart);
        Log.d("FFF", "totalPrice = " + totalPrice);
        this.totalPrice.setText( String.valueOf(totalPrice) + " TL" );

    }

    @Override
    public void onItemRemovedListener(ProductData productData) {
        this.totalPrice.setText( String.valueOf(shoppingCart.getTotalCartPrice()) + " TL" );
    }

    @Override
    public void onCartClearedListener() {
        this.totalPrice.setText( "0 TL" );
    }

    private void printHashMap(HashMap<ProductData, Integer> cart){
        for (ProductData name: cart.keySet()){
            String key = name.getmProductName();
            String value = cart.get(name).toString();
            Log.d("FFF" , key + " " + value);
        }
    }

    @Override
    public void onItemsPurchased() {
        Toast toast = Toast.makeText(this, "Thanks for your purhcase", Toast.LENGTH_LONG);
        toast.show();
        shoppingCart.clear();
        CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
        getSupportFragmentManager().beginTransaction().remove(cartFragment).commit();
        getSupportFragmentManager().beginTransaction().show(shopFragment).commit();
    }

    @Override
    public void onListFragmentInteraction(ProductData productData) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
        if(cartFragment != null){
            getSupportFragmentManager().beginTransaction().remove(cartFragment).commit();
            getSupportFragmentManager().beginTransaction().show(shopFragment).commit();
        }else {
            finish();
        }
    }

    @Override
    public void onLoginCorrect() {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LOGIN_FRAGMENT");
        getSupportFragmentManager().beginTransaction().remove(loginFragment).commit();

        totalPrice.setVisibility(View.VISIBLE);
        cartButton.setVisibility(View.VISIBLE);

        shoppingCart = new Cart();
        shoppingCart.addOnCartChangedListener(this);
        shopFragment = ShopFragment.newInstance("1",shoppingCart);

        getSupportFragmentManager().beginTransaction().add(R.id.container, shopFragment,"SHOP_FRAGMENT").commit();

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
                if(cartFragment == null){
                    getSupportFragmentManager().beginTransaction().hide(shopFragment).commit();
                    getSupportFragmentManager().beginTransaction().add(R.id.container, CartFragment.newInstance(shoppingCart, DummyActivity.this), "CART_FRAGMENT").commit();
                }else{
                    getSupportFragmentManager().beginTransaction().remove(cartFragment).commit();
                    getSupportFragmentManager().beginTransaction().show(shopFragment).commit();
                }
            }
        });
    }

    @Override
    public void onLoginWrong() {

    }
}
