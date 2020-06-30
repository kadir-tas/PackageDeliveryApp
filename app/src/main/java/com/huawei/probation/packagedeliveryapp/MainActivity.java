package com.huawei.probation.packagedeliveryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.probation.packagedeliveryapp.data.Cart;
import com.huawei.probation.packagedeliveryapp.data.ProductData;
import com.huawei.probation.packagedeliveryapp.fragments.CartFragment;
import com.huawei.probation.packagedeliveryapp.fragments.CategoriesFragment;
import com.huawei.probation.packagedeliveryapp.fragments.ItemDetailFragment;
import com.huawei.probation.packagedeliveryapp.fragments.LoginFragment;
import com.huawei.probation.packagedeliveryapp.fragments.ProductFragment;
import com.huawei.probation.packagedeliveryapp.fragments.ShopFragment;
import com.huawei.probation.packagedeliveryapp.map.MapsActivity;
import com.huawei.probation.packagedeliveryapp.ınterfaces.OnCartChangedListener;

import java.util.HashMap;

import static com.huawei.probation.packagedeliveryapp.adapters.CategoriesRecyclerViewAdapter.recViewItemPressed;

public class MainActivity extends AppCompatActivity
        implements CategoriesFragment.OnFragmentInteractionListener,
        ShopFragment.OnFragmentInteractionListener,ProductFragment.OnListFragmentInteractionListener,
        OnCartChangedListener, CartFragment.OnFragmentInteractionListener , CartFragment.OnItemsPurchased,
        ItemDetailFragment.OnItemDetailInteraction,
        LoginFragment.OnLoginListener {

    private TextView totalPrice;
    private ImageButton cartButton;
    private ShopFragment shopFragment;
    private Cart shoppingCart;

    private ImageView packgeDeliveryIcon;
    private ImageView orderCountIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingCart = new Cart();
        setContentView(R.layout.activity_main);

        totalPrice = findViewById(R.id.cart_price);
        cartButton = findViewById(R.id.cart_button);
        packgeDeliveryIcon = findViewById(R.id.track_order_button);
        orderCountIcon = findViewById(R.id.order_count_icon);

        packgeDeliveryIcon.setVisibility(View.INVISIBLE);
        orderCountIcon.setVisibility(View.INVISIBLE);

        totalPrice.setVisibility(View.INVISIBLE);
        cartButton.setVisibility(View.INVISIBLE);

        getSupportFragmentManager().beginTransaction().add(R.id.container,
                LoginFragment.newInstance(), "LOGIN_FRAGMENT").commit();

    }

    @Override
    public void onBackPressed() {

        CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
        Fragment sop = getSupportFragmentManager().findFragmentByTag("SHOP_FRAG");
        Fragment cat = getSupportFragmentManager().findFragmentByTag("CAT_FRAG");
        Fragment loginFrag = getSupportFragmentManager().findFragmentByTag("LOGIN_FRAGMENT");
        Fragment itemDetailFragment = getSupportFragmentManager().findFragmentByTag("PROD_DETAIL_FRAG");

        if(itemDetailFragment != null){
            getSupportFragmentManager().beginTransaction().remove(itemDetailFragment).commit();
        }
        else if(loginFrag != null){
            finish();
        }else if(cartFragment != null){
            getSupportFragmentManager().beginTransaction().remove(cartFragment).commit();
            getSupportFragmentManager().beginTransaction().show(shopFragment).commit();
        }else if(sop != null){
            getSupportFragmentManager().beginTransaction().remove(sop).commit();
            getSupportFragmentManager().beginTransaction().show(cat).commit();
            recViewItemPressed = false;
        }else if(cat != null){
            finish();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    public void switchContent(String categoryName) {
        Fragment cat = getSupportFragmentManager().findFragmentByTag("CAT_FRAG");
        ShopFragment sf = ShopFragment.newInstance(categoryName,shoppingCart);
        getSupportFragmentManager().beginTransaction().hide(cat).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container, sf, "SHOP_FRAG").commit();
    }

    @Override
    public void onListFragmentInteraction(ProductData productData) {

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
        /*Toast toast = Toast.makeText(this, "Thanks for your purhcase", Toast.LENGTH_LONG);
        toast.show();
        shoppingCart.clear();
        CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
        getSupportFragmentManager().beginTransaction().remove(cartFragment).commit();
        getSupportFragmentManager().beginTransaction().show(shopFragment).commit();
        */
        orderCountIcon.setVisibility(View.VISIBLE);
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onLoginCorrect() {
        totalPrice.setVisibility(View.VISIBLE);
        cartButton.setVisibility(View.VISIBLE);
        packgeDeliveryIcon.setVisibility(View.VISIBLE);

        shoppingCart = new Cart();
        shoppingCart.addOnCartChangedListener(this);
        shopFragment = ShopFragment.newInstance("1",shoppingCart);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
                if(cartFragment == null){
                    getSupportFragmentManager().beginTransaction().hide(shopFragment).commit();
                    getSupportFragmentManager().beginTransaction().add(R.id.container, CartFragment.newInstance(shoppingCart, MainActivity.this), "CART_FRAGMENT").commit();
                }else{
                    getSupportFragmentManager().beginTransaction().remove(cartFragment).commit();
                    getSupportFragmentManager().beginTransaction().show(shopFragment).commit();
                }
            }
        });

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LOGIN_FRAGMENT");
        getSupportFragmentManager().beginTransaction().remove(loginFragment).commit();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new CategoriesFragment(),"CAT_FRAG");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();


    }

    @Override
    public void onLoginWrong() {
        ///bread push
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

    }

    @Override
    public void onAddCartButtonPressed(ItemDetailFragment itemDetailFragment) {
        shoppingCart.putItem(itemDetailFragment.getProduct().first , itemDetailFragment.getProduct().second);
        Toast.makeText(this, "Ürünler sepetinize eklendi", Toast.LENGTH_LONG).show();


        getSupportFragmentManager().beginTransaction().remove(itemDetailFragment).commit();
    }

    @Override
    public void onRemoveCartButtonPressed(ItemDetailFragment itemDetailFragment) {
        shoppingCart.removeItem(itemDetailFragment.getProduct().first);
        Toast.makeText(this, "Ürünler sepetinizden çıkarıldı", Toast.LENGTH_LONG).show();

        getSupportFragmentManager().beginTransaction().remove(itemDetailFragment).commit();
    }
}