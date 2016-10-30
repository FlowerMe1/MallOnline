package com.mallonline.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mallonline.R;
import com.mallonline.api.ClientCallback;
import com.mallonline.async.RemoveOrdersAsyncTask;
import com.mallonline.databinding.ActivityOrderUserDetailsBinding;
import com.mallonline.listeners.OnCompletedListener;
import com.mallonline.manager.OrdersManager;
import com.mallonline.manager.UsersManager;
import com.mallonline.models.CartOrder;
import com.mallonline.models.OrderUserDetails;
import com.mallonline.models.ServerResponse;
import com.mallonline.util.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderUserDetailsActivity extends BaseActivity implements ClientCallback<JSONObject>, OnCompletedListener {

    private ActivityOrderUserDetailsBinding binding;
    private OrderUserDetails orderUserDetails;
    private OrdersManager ordersManager;
    private UsersManager usersManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_order_user_details, null, false);
        inflateContent(binding.getRoot());
        setToolBarContent(getString(R.string.order_user_details), true, true);
        this.orderUserDetails = new OrderUserDetails();
        this.ordersManager = OrdersManager.getInstance();
        this.usersManager = UsersManager.getInstance();
        binding.setOrder(orderUserDetails);
        initSpinner();
    }

    private void initSpinner() {
        ArrayList<String> countriesList = UtilityMethods.getCountriesList();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.country_spinner_item, countriesList.toArray(new String[countriesList.size()]));
        adapter.setDropDownViewResource(R.layout.country_spinner_item);
        binding.spinnerCountry.setAdapter(adapter);
        binding.spinnerCountry.setSelection(((ArrayAdapter<String>)binding.spinnerCountry.getAdapter()).getPosition("Egypt"));

//        binding.spinnerCountry.setSelection(0, false);
    }

    public void onOrderNowButtonClicked(View view) {
        resetValidatinErrors();
        String uuid = UUID.randomUUID().toString();
        orderUserDetails.setUserData(binding.editTextFullName.getText().toString(),uuid, binding.editTextPhone.getText().toString(), binding.editTextAlternativePhone.getText().toString(), binding.spinnerCountry.getSelectedItem().toString(), binding.editTextAddress.getText().toString());
        if (isValidData()) {
            List<CartOrder> ordersList = ordersManager.getOrdersList(usersManager.getCurrentUser().getServerId());
            apiClient.addCartOrders(ordersList, orderUserDetails, this);
            showProgressDialog(getString(R.string.please_Wait), false, true);
        }
    }

    @Override
    public void onServerResultSuccess(JSONObject jsonObject) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CartOrder>>() {
        }.getType();
        List<CartOrder> cartOrderList = null;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("orders");
            cartOrderList = gson.fromJson(String.valueOf(jsonArray), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (cartOrderList != null && cartOrderList.size() > 0)
            new RemoveOrdersAsyncTask(cartOrderList, this).execute();
        else
            onCompleted();
    }

    @Override
    public void onServerResultFailure(ServerResponse serverResponse, int statusCode) {
        showFailureMessage(serverResponse, statusCode);
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
        navigateActivity(new Intent(OrderUserDetailsActivity.this, HomeActivity.class));
        Toast.makeText(this, getString(R.string.orders_submited_successfully), Toast.LENGTH_SHORT).show();
        finishAffinity();
    }

    private void resetValidatinErrors() {
        setError(binding.textInputFullName, null);
        setError(binding.textInputPhone, null);
        setError(binding.textInputAlternativePhone, null);
        setError(binding.textInputAddress, null);
    }

    private boolean isValidData() {
        boolean isValid = true;
        if (!orderUserDetails.isValidFullName()) {
            setError(binding.textInputFullName, getString(R.string.invalid_full_name));
            isValid = false;
        }
        if (!orderUserDetails.isValidPhone()) {
            setError(binding.textInputPhone, getString(R.string.invalid_phone));
            isValid = false;
        }
        if (!orderUserDetails.isValidAlternativePhone()) {
            setError(binding.textInputAlternativePhone, getString(R.string.invalid_alternative_phone));
            isValid = false;
        }

        if (orderUserDetails.isValidPhone() && orderUserDetails.isValidAlternativePhone() && orderUserDetails.getPhone().trim().equals(orderUserDetails.getAlternativePhone().trim())) {
            setError(binding.textInputAlternativePhone, "Alternative Phone must not be the same as Phone");
            isValid = false;
        }

        if (!orderUserDetails.isValidFullAddress()) {
            setError(binding.textInputAddress, getString(R.string.invalid_full_address));
            isValid = false;
        }
        if (!isValid)
            UtilityMethods.showSnackbar(getString(R.string.invalid_fields), binding.getRoot(), this);
        return isValid;
    }
}
