package com.huawei.probation.packagedeliveryapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.huawei.probation.packagedeliveryapp.R;
import com.huawei.probation.packagedeliveryapp.util.Helper;


public class LoginFragment extends Fragment {


    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private Button mAnonymousLoginButton;
    private ImageView mHuaweiLogo;

    private LoginFragmentButtonListeners mLoginFragmentButtonListeners;

    private String mEmail;
    private String mPassword;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mEmailEditText = v.findViewById(R.id.username);
        mPasswordEditText = v.findViewById(R.id.password);

        mLoginButton = v.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(v1 -> {
            getEditTextValues();
            if(validateForm()) {
                mLoginFragmentButtonListeners.onLoginButtonClick(mEmail,mPassword);
            }else{
                Toast.makeText(getContext(), "Please fill each field!", Toast.LENGTH_LONG).show();
            }
        });

        mRegisterButton = v.findViewById(R.id.register);
        mRegisterButton.setOnClickListener(view -> mLoginFragmentButtonListeners.onRegisterButtonClicked());

        mHuaweiLogo = v.findViewById(R.id.loginHuaweiId);
        mHuaweiLogo.setOnClickListener(view-> mLoginFragmentButtonListeners.onHuaweiLogoClick());

        mAnonymousLoginButton = v.findViewById(R.id.loginAnonymous);
        mAnonymousLoginButton.setOnClickListener(view-> mLoginFragmentButtonListeners.onAnonymousLoginButtonClick());

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentButtonListeners) {
            mLoginFragmentButtonListeners = (LoginFragmentButtonListeners) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoginFragmentButtonListeners = null;
    }

    private void getEditTextValues() {
        mEmail = mEmailEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
    }

    private boolean validateForm() {
        boolean isValid = (!mEmail.isEmpty() && Helper.isEmailValid(mEmail));
        isValid = isValid & (!mPassword.isEmpty() && mPassword.length() >= 6);
        return isValid;
    }

    //TODO: NOT COMPLETE YET
    public interface LoginFragmentButtonListeners {
        void onLoginButtonClick(String username, String password);
        void onLoginCorrect();
        void onLoginWrong();
        void onRegisterButtonClicked();
        void onHuaweiLogoClick();
        void onAnonymousLoginButtonClick();
    }
}
