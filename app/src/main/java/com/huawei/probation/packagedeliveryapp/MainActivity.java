package com.huawei.probation.packagedeliveryapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.HwIdAuthProvider;
import com.huawei.agconnect.core.service.auth.TokenSnapshot;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
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
import com.huawei.probation.packagedeliveryapp.util.Permissions;
import com.huawei.probation.packagedeliveryapp.ınterfaces.OnCartChangedListener;

import org.json.JSONException;

import java.util.HashMap;

import static com.huawei.hms.analytics.type.HAEventType.VIEWPRODUCT;
import static com.huawei.hms.analytics.type.HAParamType.CATEGORY;
import static com.huawei.hms.analytics.type.HAParamType.PRODUCTID;
import static com.huawei.hms.analytics.type.HAParamType.PRODUCTNAME;
import static com.huawei.probation.packagedeliveryapp.adapters.CategoriesRecyclerViewAdapter.recViewItemPressed;
import static com.huawei.probation.packagedeliveryapp.map.MapsActivity.isDrone;
import static com.huawei.probation.packagedeliveryapp.map.MapsActivity.isTrackOrder;

public class MainActivity extends AppCompatActivity
        implements CategoriesFragment.OnFragmentInteractionListener,
        ShopFragment.OnFragmentInteractionListener, ProductFragment.OnListFragmentInteractionListener,
        OnCartChangedListener, CartFragment.OnFragmentInteractionListener, CartFragment.OnItemsPurchased,
        ItemDetailFragment.OnItemDetailInteraction,
        LoginFragment.LoginFragmentButtonListeners {

    private static final String TAG = "MainActivity";
    private static String adID ="testw6vs28auh3";
    private static String reward_ad_id ="testx9dtjwj8hp";

    private RewardAd rewardAd;

    private ConstraintLayout constLytBanner;

    private TextView totalPrice;
    private ImageButton cartButton;
    private ImageView packgeDeliveryIcon;
    private ImageView orderCountIcon;

    private ShopFragment shopFragment;
    private Cart shoppingCart;

    private AGConnectAuth auth;
    private HuaweiIdAuthService service;
    private BottomAppBar bottomAppBar;
    private HiAnalyticsInstance analyticsInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingCart = new Cart();
        setContentView(R.layout.activity_main);

        // Generate the Analytics Instance
        analyticsInstance = HiAnalytics.getInstance(this);
        createPresetEvent();

        HwAds.init(this);

        constLytBanner = findViewById(R.id.consLytBanner);
        totalPrice = findViewById(R.id.cart_price);
        cartButton = findViewById(R.id.cart_button);
        packgeDeliveryIcon = findViewById(R.id.track_order_button);
        orderCountIcon = findViewById(R.id.order_count_icon);

        packgeDeliveryIcon.setVisibility(View.INVISIBLE);
        orderCountIcon.setVisibility(View.INVISIBLE);

        totalPrice.setVisibility(View.INVISIBLE);
        cartButton.setVisibility(View.INVISIBLE);

        bottomAppBar = findViewById(R.id.bottom_app_bar);
        bottomAppBar.setVisibility(View.INVISIBLE);

        setBannerAd();

        loadRewardAd();

        packgeDeliveryIcon.setOnClickListener(v -> {
            if(Permissions.getLocationPermission(this)){
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Permissions.LOCATION_PERMISSION_KEY == requestCode) {
            if ((grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("İzinler").setMessage("Konum izni vermeden map sayfasını açamazsınız. Lütfen uygulamaya konum izni veriniz.")
                        .setPositiveButton("Tamam", (dialogInterface, i) -> dialogInterface.dismiss());
                builder.show();
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

    private void createPresetEvent() {
        Bundle bundle = new Bundle();
        bundle.putLong(PRODUCTID, 1234);
        bundle.putString(PRODUCTNAME, getDeviceName());
        bundle.putString(CATEGORY, "Phone");
        // Report a predefined Event
        analyticsInstance.onEvent(VIEWPRODUCT, bundle);
    }

    private void createCustomEvent() {
        Bundle bundle = new Bundle();
        bundle.putString("customParam1", "value1");
        bundle.putString("customParam2", "value2");
        bundle.putString("customParam3", "value3");
        // Report a predefined event.
        //Tanımlanan custom event ismi Preset isimlendirmelerinden farklı olmalı.
        analyticsInstance.onEvent("customEventV1", bundle);
    }


    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


    private void setBannerAd(){
        BannerView bottomBanner = new BannerView(this);
        AdParam adParam = new AdParam.Builder().build();
        bottomBanner.setAdId(adID);
        bottomBanner.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        bottomBanner.loadAd(adParam);
        constLytBanner.addView(bottomBanner);
    }

    private void loadRewardAd(){
        if(rewardAd == null){
            rewardAd = new RewardAd(MainActivity.this,reward_ad_id);//set ad slot id
        }

        RewardAdLoadListener rewardAdLoadListener = new RewardAdLoadListener(){
            @Override
            public void onRewardAdFailedToLoad(int errorCode) {
                //i returns error code
                Log.e("ERROR",""+errorCode);
            }

            @Override
            public void onRewardedLoaded() {
                Log.i("INFO","Your reward was added successfully");
            }
        };

        rewardAd.loadAd(new AdParam.Builder().build(),rewardAdLoadListener);
    }

    private void rewardAdShow(){
        if(rewardAd.isLoaded()){
            rewardAd.show(MainActivity.this, new RewardAdStatusListener() {

                @Override
                public void onRewardAdClosed() {
                    loadRewardAd();
                }

                @Override
                public void onRewardAdFailedToShow(int i) {
                    Log.e("ERROR",String.valueOf(i));
                }

                @Override
                public void onRewardAdOpened() {
                    Toast.makeText(MainActivity.this,"Item Purchased",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewarded(Reward reward) {
                    //If you want to give reward to player , you can declare reward
                    reward = new Reward() {
                        @Override
                        public String getName() {
                            return "Tracking order chance";//My reword name
                        }

                        @Override
                        public int getAmount() { //specify reward amount
                            return 1;
                        }
                    };

                    Toast.makeText(getApplicationContext(),reward.getName()+"+"+reward.getAmount(),Toast.LENGTH_SHORT).show();

                    loadRewardAd();
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
        Fragment sop = getSupportFragmentManager().findFragmentByTag("SHOP_FRAG");
        Fragment cat = getSupportFragmentManager().findFragmentByTag("CAT_FRAG");
        Fragment loginFrag = getSupportFragmentManager().findFragmentByTag("LOGIN_FRAGMENT");
        Fragment itemDetailFragment = getSupportFragmentManager().findFragmentByTag("PROD_DETAIL_FRAG");

        if (itemDetailFragment != null) {
            bottomAppBar.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().remove(itemDetailFragment).commit();
        } else if (loginFrag != null) {
            finish();
        } else if (cartFragment != null) {
            bottomAppBar.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().remove(cartFragment).commit();
            getSupportFragmentManager().beginTransaction().show(shopFragment).commit();
        } else if (sop != null) {
            bottomAppBar.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().remove(sop).commit();
            getSupportFragmentManager().beginTransaction().show(cat).commit();
            recViewItemPressed = false;
        } else if (cat != null) {
            // clear for anonymous account when user want to exit from the app
            if (AGConnectAuth.getInstance().getCurrentUser().isAnonymous()) {
                AGConnectAuth.getInstance().deleteUser();
            }
            isTrackOrder = false;
            isDrone = false;
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
        bottomAppBar.setVisibility(View.INVISIBLE);
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
        orderCountIcon.setVisibility(View.VISIBLE);
        rewardAdShow();//triggered reward listeners
//        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
//        startActivity(intent);
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
        bottomAppBar.setVisibility(View.VISIBLE);

        shoppingCart = new Cart();
        shoppingCart.addOnCartChangedListener(this);
        shopFragment = ShopFragment.newInstance("1", shoppingCart);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
                if (cartFragment == null) {
                    bottomAppBar.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().hide(shopFragment).commit();
                    getSupportFragmentManager().beginTransaction().add(R.id.container, CartFragment.newInstance(shoppingCart, MainActivity.this), "CART_FRAGMENT").commit();
                } else {
                    bottomAppBar.setVisibility(View.INVISIBLE);
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

        //Test for analytics kit to create a custom event
        createCustomEvent();

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
                }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Anonymous SignIn Failed " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}