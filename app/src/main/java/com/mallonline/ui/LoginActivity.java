package com.mallonline.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.View;

import com.mallonline.R;
import com.mallonline.api.ClientCallback;
import com.mallonline.api.VolleyHelper;
import com.mallonline.data.SharedPref;
import com.mallonline.databinding.ActivityLoginBinding;
import com.mallonline.models.ServerResponse;
import com.mallonline.models.User;
import com.mallonline.util.UtilityMethods;

import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements ClientCallback<JSONObject> {

    private ActivityLoginBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_login, null, false);
        inflateContent(binding.getRoot());
        setToolBarContent(getString(R.string.login_title), true, false);
        user = new User();
        binding.setUser(user);
        binding.register.setText(Html.fromHtml(getString(R.string.donot_have_account) + " <font color='#5BBEF7'>" + getString(R.string.register_now) + "</font>"));
    }

    public void onLoginClicked(View view) {
        resetValidatinErrors();
        user.setUserData(binding.email.getText().toString(), binding.password.getText().toString());
        if (isValidData()) {
            showProgressDialog(getString(R.string.please_Wait), false, true);
            apiClient.userLogin(user, this);
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
        if (!isValid)
            UtilityMethods.showSnackbar(getString(R.string.invalid_fields), binding.getRoot(), this);
        return isValid;
    }


    private void resetValidatinErrors() {
        setError(binding.emailTextInput, null);
        setError(binding.passwordTextInput, null);
    }

    @Override
    public void onServerResultSuccess(JSONObject jsonObject) {
        if (jsonObject != null) {
            User responseUser = (User) VolleyHelper.getGsonObj(jsonObject.toString(), User.class);
            if (responseUser != null) {
                if (!isFinishing() && responseUser.getEmail() == null || "".equals(responseUser.getEmail().trim()))
                    responseUser.setEmail(binding.email.getText().toString());

                String token = responseUser.getToken();
                SharedPref.SaveString(this, SharedPref.USER_TOKEN, token);
                responseUser.setLogin(true);
                responseUser.saveUser();

                if (!isFinishing()) {
                    ActivityCompat.finishAffinity(this);
                    startActivityWithTransition(this, new Intent(this, IntroActivity.class));
                }
            }
        }
    }

    @Override
    public void onServerResultFailure(ServerResponse serverResponse, int statusCode) {
        String errorMessage = getString(R.string.something_wrong);
        if (serverResponse != null && serverResponse.getErrorMessageStr() != null && !"".equals((serverResponse.getErrorMessageStr()).trim()))
            errorMessage = serverResponse.getErrorMessageStr();

        if (!isFinishing()) {
            UtilityMethods.showSnackbar(errorMessage, getWindow().getDecorView(), this);
            dismissProgressDialog();
        }
        SharedPref.deleteShared(this);
    }

    public void onRegisterButtonClicked(View view) {
        startActivityWithTransition(this, new Intent(this, SignupActivity.class));
    }

}
