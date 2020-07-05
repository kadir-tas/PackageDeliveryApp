package com.huawei.probation.packagedeliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;
import com.huawei.probation.packagedeliveryapp.MainActivity;
import com.huawei.probation.packagedeliveryapp.R;
import com.huawei.probation.packagedeliveryapp.data.User;
import com.huawei.probation.packagedeliveryapp.util.Helper;

import java.util.Locale;

import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_REGISTER_LOGIN;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private Button verifyCodeConfirmButton;
    private EditText clientNameEditText;
    private EditText clientLastnameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText clientCityEditText;
    private EditText clientPhoneEditText;
    private EditText verifyCodeEditText;

    private String clientName;
    private String clientLastname;
    private String password;
    private String email;
    private String clientCity;
    private String clientPhone;

    private BottomSheetDialog verifyCodeDialog;
    private String verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
                .action(ACTION_REGISTER_LOGIN)   //ACTION_REGISTER_LOGIN/ACTION_RESET_PASSWORD
                .sendInterval(30) // Minimum sending interval, ranging from 30s to 120s.
                .locale(Locale.ENGLISH) // Language in which a verification code is sent, which is optional. The default value is Locale.getDefault.
                .build();


        clientNameEditText = findViewById(R.id.clientName);
        clientLastnameEditText = findViewById(R.id.clientLastname);
        passwordEditText = findViewById(R.id.password);
        emailEditText = findViewById(R.id.email);
        clientCityEditText = findViewById(R.id.clientCity);
        clientPhoneEditText = findViewById(R.id.clientPhone);

        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_verify_code, null);

        verifyCodeDialog = new BottomSheetDialog(RegisterActivity.this);
        verifyCodeDialog.setContentView(dialogView);

        verifyCodeEditText = dialogView.findViewById(R.id.verifyCodeEditText);
        verifyCodeConfirmButton = dialogView.findViewById(R.id.verifyCodeConfirmButton);

        registerButton = findViewById(R.id.registerConfirm);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setEnabled(false);
                getEditTextValues();
                User user = new User(clientName, clientLastname, password, email, clientCity, clientPhone);
                if (validateForm()) {
                    Task<VerifyCodeResult> task = EmailAuthProvider.requestVerifyCode(email, settings);
                    task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                        @Override
                        public void onSuccess(VerifyCodeResult verifyCodeResult) {
                            verifyCodeDialog.show();
                            verifyCodeConfirmButton.setOnClickListener(v -> {

                                verifyCode = verifyCodeEditText.getText().toString();
                                EmailUser emailUser = new EmailUser.Builder().setEmail(email).setVerifyCode(verifyCode).setPassword(password).build();
                                AGConnectAuth.getInstance().createUser(emailUser)
                                        .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                                            @Override
                                            public void onSuccess(SignInResult signInResult) {
                                                Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                Toast.makeText(RegisterActivity.this, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                registerButton.setEnabled(true);
                                            }
                                        });
                            });
                        }
                    }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                        }
                    });

                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill each field", Toast.LENGTH_LONG).show();
                    registerButton.setEnabled(true);
                }
            }
        });

    }

    //Too easy with databinding replace all with databinding
    private void getEditTextValues() {
        clientName = clientNameEditText.getText().toString();
        clientLastname = clientLastnameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        email = emailEditText.getText().toString();
        clientCity = clientCityEditText.getText().toString();
        clientPhone = clientPhoneEditText.getText().toString();
    }

    private boolean validateForm() {
        boolean isValid = !clientName.isEmpty();
        isValid = isValid & !clientLastname.isEmpty();
        isValid = isValid & (!password.isEmpty() && password.length() >= 6);
        isValid = isValid & (!email.isEmpty() && Helper.isEmailValid(email));
        isValid = isValid & !clientCity.isEmpty();
        isValid = isValid & !clientPhone.isEmpty();
        return isValid;
    }

}
