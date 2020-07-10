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

import com.google.android.material.bottomappbar.BottomAppBar;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.HwIdAuthProvider;
import com.huawei.agconnect.core.service.auth.TokenSnapshot;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.huawei.probation.packagedeliveryapp.activities.RegisterActivity;
import com.huawei.probation.packagedeliveryapp.data.Cart;
import com.huawei.probation.packagedeliveryapp.data.ProductData;
import com.huawei.probation.packagedeliveryapp.fragments.CartFragment;
import com.huawei.probation.packagedeliveryapp.fragments.CategoriesFragment;
import com.huawei.probation.packagedeliveryapp.fragments.ItemDetailFragment;
import com.huawei.probation.packagedeliveryapp.fragments.LoginFragment;
import com.huawei.probation.packagedeliveryapp.fragments.ProductFragment;
import com.huawei.probation.packagedeliveryapp.fragments.ShopFragment;
import com.huawei.probation.packagedeliveryapp.map.MapsActivity;
import com.huawei.probation.packagedeliveryapp.util.AppConstants;
import com.huawei.probation.packagedeliveryapp.ınterfaces.OnCartChangedListener;

import org.json.JSONException;

import java.util.HashMap;

import static com.huawei.probation.packagedeliveryapp.adapters.CategoriesRecyclerViewAdapter.recViewItemPressed;

public class MainActivity extends AppCompatActivity
        implements CategoriesFragment.OnFragmentInteractionListener,
        ShopFragment.OnFragmentInteractionListener, ProductFragment.OnListFragmentInteractionListener,
        OnCartChangedListener, CartFragment.OnFragmentInteractionListener, CartFragment.OnItemsPurchased,
        ItemDetailFragment.OnItemDetailInteraction,
        LoginFragment.LoginFragmentButtonListeners {

    private static final String TAG = "MainActivity";

    private TextView totalPrice;
    private ImageButton cartButton;
    private ImageView packgeDeliveryIcon;
    private ImageView orderCountIcon;

    private ShopFragment shopFragment;
    private Cart shoppingCart;

    private AGConnectAuth auth;
    private HuaweiIdAuthService service;

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

        auth = AGConnectAuth.getInstance();

        AGConnectAuth.getInstance().addTokenListener(tokenSnapshot -> {
            TokenSnapshot.State state = tokenSnapshot.getState();
            if (state == TokenSnapshot.State.TOKEN_INVALID) {
                transmitTokenIntoAppGalleryConnect(tokenSnapshot.getToken());
            }
        });

        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);

        bottomAppBar.setNavigationOnClickListener(v -> signOutClicked());

        if (auth.getCurrentUser() != null) {
            onLoginCorrect();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.container,
                    LoginFragment.newInstance(), "LOGIN_FRAGMENT").commit();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_SIGN_IN_LOGIN_CODE) {
            //login success
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                Log.i(TAG, "signIn success Auth code = " + huaweiAccount.getAccessToken());
                Log.i(TAG, "signIn success User Name = " + huaweiAccount.getDisplayName());
                try {
                    Log.d(TAG, "onActivityResult: " + huaweiAccount.toJson());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "signIn get code success.");
                transmitTokenIntoAppGalleryConnect(huaweiAccount.getAccessToken());

            } else {
                Toast.makeText(MainActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "signIn get code failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode());
            }
        }
    }

    private void transmitTokenIntoAppGalleryConnect(String accessToken) {
        AGConnectAuthCredential credential = HwIdAuthProvider.credentialWithToken(accessToken);
        Toast.makeText(MainActivity.this, accessToken, Toast.LENGTH_SHORT).show();
        AGConnectAuth.getInstance()/*.getCurrentUser()link(credential)*/.signIn(credential).addOnSuccessListener(signInResult -> {
            // onSuccess
            Toast.makeText(MainActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
            AGConnectUser user = signInResult.getUser();
            onLoginCorrect();
        }).addOnFailureListener(e -> {
            // onFail
            Toast.makeText(MainActivity.this, "onFailure" + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onBackPressed() {

        CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
        Fragment sop = getSupportFragmentManager().findFragmentByTag("SHOP_FRAG");
        Fragment cat = getSupportFragmentManager().findFragmentByTag("CAT_FRAG");
        Fragment loginFrag = getSupportFragmentManager().findFragmentByTag("LOGIN_FRAGMENT");
        Fragment itemDetailFragment = getSupportFragmentManager().findFragmentByTag("PROD_DETAIL_FRAG");

        if (itemDetailFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(itemDetailFragment).commit();
        } else if (loginFrag != null) {
            finish();
        } else if (cartFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(cartFragment).commit();
            getSupportFragmentManager().beginTransaction().show(shopFragment).commit();
        } else if (sop != null) {
            getSupportFragmentManager().beginTransaction().remove(sop).commit();
            getSupportFragmentManager().beginTransaction().show(cat).commit();
            recViewItemPressed = false;
        } else if (cat != null) {
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
        ShopFragment sf = ShopFragment.newInstance(categoryName, shoppingCart);
        getSupportFragmentManager().beginTransaction().hide(cat).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container, sf, "SHOP_FRAG").commit();
    }

    @Override
    public void onListFragmentInteraction(ProductData productData) {

    }

    private void printHashMap(HashMap<ProductData, Integer> cart) {
        for (ProductData name : cart.keySet()) {
            String key = name.getmProductName();
            String value = cart.get(name).toString();
            Log.d("FFF", key + " " + value);
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

    void signOutClicked() {
                AGConnectAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
    }

    @Override
    public void onLoginCorrect() {
        totalPrice.setVisibility(View.VISIBLE);
        cartButton.setVisibility(View.VISIBLE);
        packgeDeliveryIcon.setVisibility(View.VISIBLE);

        shoppingCart = new Cart();
        shoppingCart.addOnCartChangedListener(this);
        shopFragment = ShopFragment.newInstance("1", shoppingCart);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
                if (cartFragment == null) {
                    getSupportFragmentManager().beginTransaction().hide(shopFragment).commit();
                    getSupportFragmentManager().beginTransaction().add(R.id.container, CartFragment.newInstance(shoppingCart, MainActivity.this), "CART_FRAGMENT").commit();
                } else {
                    getSupportFragmentManager().beginTransaction().remove(cartFragment).commit();
                    getSupportFragmentManager().beginTransaction().show(shopFragment).commit();
                }
            }
        });

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LOGIN_FRAGMENT");
        if (loginFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(loginFragment).commit();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new CategoriesFragment(), "CAT_FRAG");
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
        this.totalPrice.setText(String.valueOf(totalPrice) + " TL");
    }

    @Override
    public void onItemRemovedListener(ProductData productData) {
        this.totalPrice.setText(String.valueOf(shoppingCart.getTotalCartPrice()) + " TL");
    }

    @Override
    public void onCartClearedListener() {

    }

    @Override
    public void onAddCartButtonPressed(ItemDetailFragment itemDetailFragment) {
        shoppingCart.putItem(itemDetailFragment.getProduct().first, itemDetailFragment.getProduct().second);
        Toast.makeText(this, "Added to cart", Toast.LENGTH_LONG).show();


        getSupportFragmentManager().beginTransaction().remove(itemDetailFragment).commit();
    }

    @Override
    public void onRemoveCartButtonPressed(ItemDetailFragment itemDetailFragment) {
        shoppingCart.removeItem(itemDetailFragment.getProduct().first);
        Toast.makeText(this, "Removed from cart", Toast.LENGTH_LONG).show();

        getSupportFragmentManager().beginTransaction().remove(itemDetailFragment).commit();
    }

    //Login with email and password (First user need to complete registration process)
    @Override
    public void onLoginButtonClick(String email, String password) {
        AGConnectAuthCredential credential = EmailAuthProvider.credentialWithPassword(email, password);
        AGConnectAuth.getInstance().signIn(credential)
                .addOnSuccessListener(signInResult -> onLoginCorrect())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Bad credentials", Toast.LENGTH_LONG).show());
    }

    //Register
    @Override
    public void onRegisterButtonClicked() {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        finish();
    }

    //Login with Huawei ID
    @Override
    public void onHuaweiLogoClick() {
//        HuaweiIdAuthParams authParams= new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAccessToken().createParams();
//        HuaweiIdAuthService service = HuaweiIdAuthManager.getService (MainActivity.this, authParams);
//        startActivityForResult(service.getSignInIntent(), AppConstants.REQUEST_SIGN_IN_LOGIN_CODE);

//        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
//            onLoginCorrect();
//        }

        HuaweiIdAuthParamsHelper huaweiIdAuthParamsHelper = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM);
        HuaweiIdAuthParams authParams = huaweiIdAuthParamsHelper.setAccessToken().createParams();
        service = HuaweiIdAuthManager.getService(MainActivity.this, authParams);
        startActivityForResult(service.getSignInIntent(), AppConstants.REQUEST_SIGN_IN_LOGIN_CODE);
    }

    //Login as anonymous user
    @Override
    public void onAnonymousLoginButtonClick() {
        AGConnectAuth.getInstance().signInAnonymously()
                .addOnSuccessListener(signInResult -> {
                    AGConnectUser user = signInResult.getUser();
                    Toast.makeText(MainActivity.this, user.getUid(), Toast.LENGTH_LONG).show();
                    onLoginCorrect();
                }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Anonymous SignIn Failed", Toast.LENGTH_LONG).show());
    }
}