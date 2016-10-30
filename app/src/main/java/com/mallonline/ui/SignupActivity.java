package com.mallonline.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.View;

import com.mallonline.R;
import com.mallonline.api.ApiClient;
import com.mallonline.api.ClientCallback;
import com.mallonline.api.VolleyHelper;
import com.mallonline.data.SharedPref;
import com.mallonline.databinding.ActivitySingupBinding;
import com.mallonline.models.ServerResponse;
import com.mallonline.models.User;
import com.mallonline.util.UtilityMethods;

import org.json.JSONObject;

public class SignupActivity extends BaseActivity implements ClientCallback<JSONObject> {

    private ActivitySingupBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_singup, null, false);
        inflateContent(binding.getRoot());
        setToolBarContent(getString(R.string.signup_title), true, true);
        user = new User();
        binding.login.setText(Html.fromHtml(getString(R.string.have_account) + " <font color='#5BBEF7'>" + getString(R.string.sign_in) + "</font>"));
    }

    public void onSignupClicked(View view) {
        user.setUserData(binding.emailEditText.getText().toString(), binding.passwordEditText.getText().toString(), binding.userNameEditText.getText().toString(), binding.phoneEditText.getText().toString());
        clearValidationErrors();
        if (isValidData()) {
            showProgressDialog(getString(R.string.please_Wait), false, true);
            apiClient.userSignup(user, this);
        }
    }

    private boolean isValidData() {
        boolean isValid = true;

        if (!user.isValidEmail()) {
            setError(binding.emailTextInput, getString(R.string.invalid_email));
            isValid = false;
        }
        if (!user.isValidPassword()) {
            setError(binding.passwordTextInput, getString(R.string.invalid_password));
            isValid = false;
        }
        if (!user.isValidUsername()) {
            setError(binding.usernameTextInput, getString(R.string.invalid_username));
            isValid = false;
        }
        if (user.getPassword() != null && !user.getPassword().equals(binding.passWordTextConfirmation.getText().toString())) {
            setError(binding.confirmPasswordTextInput, getString(R.string.invalid_password_confirmation));
            isValid = false;
        }
        if (!user.isValidPhone()) {
            setError(binding.phoneTextInput, getString(R.string.invalid_phone));
            isValid = false;
        }
        if (!isValid)
            UtilityMethods.showSnackbar(getString(R.string.invalid_fields), binding.getRoot(), this);
        return isValid;
    }


    private void clearValidationErrors() {
        setError(binding.emailTextInput, null);
        setError(binding.passwordTextInput, null);
        setError(binding.usernameTextInput, null);
        setError(binding.confirmPasswordTextInput, null);
        setError(binding.phoneTextInput, null);
    }


    public void onLoginClicked(View view) {
        startActivityWithTransition(this, new Intent(this, LoginActivity.class));
        finishActivity(this);
    }

    @Override
    public void onServerResultSuccess(JSONObject jsonObject) {

        if (jsonObject != null) {
            User responseUser = (User) VolleyHelper.getGsonObj(jsonObject.toString(), User.class);
            if (responseUser != null) {
                if (!isFinishing() && responseUser.getEmail() == null || "".equals(responseUser.getEmail().trim()))
                    responseUser.setEmail(binding.emailEditText.getText().toString());

                responseUser.setLogin(true);
                responseUser.saveUser();
                SharedPref.SaveString(this, SharedPref.USER_TOKEN, responseUser.getToken());

                if (!isFinishing()) {
                    ActivityCompat.finishAffinity(this);
                    startActivityWithTransition(this, new Intent(this, IntroActivity.class));
                }
            }
        }
        if (!isFinishing())
            dismissProgressDialog();
    }

    @Override
    public void onServerResultFailure(ServerResponse serverResponse, int statusCode) {
        String errorMessage = getString(R.string.something_wrong);
        if (serverResponse != null && serverResponse.getErrorMessageStr() != null && !"".equals((serverResponse.getErrorMessageStr()).trim()))
            errorMessage = serverResponse.getErrorMessageStr();

        if (!isFinishing()) {
            UtilityMethods.showSnackbar(errorMessage, binding.getRoot(), this);

            if (serverResponse != null && serverResponse.getErrors() != null && serverResponse.getErrors().length > 0) {
                for (int i = 0; i < serverResponse.getErrors().length; i++) {
                        if ((serverResponse.getErrors()[i]).getId().equals(ApiClient.USER_EMAIL))
                            setError(binding.emailTextInput, serverResponse.getErrors()[i].getTitle());
                        if ((serverResponse.getErrors()[i]).getId().equals(ApiClient.USER_PASSWORD))
                            setError(binding.passwordTextInput, serverResponse.getErrors()[i].getTitle());
                        if ((serverResponse.getErrors()[i]).getId().equals(ApiClient.USER_NAME))
                            setError(binding.usernameTextInput, serverResponse.getErrors()[i].getTitle());
                        if ((serverResponse.getErrors()[i]).getId().equals(ApiClient.PHONE))
                            setError(binding.phoneTextInput, serverResponse.getErrors()[i].getTitle());
                }
            }
        }
        SharedPref.deleteShared(this);

        if (!isFinishing())
            dismissProgressDialog();
    }
}
