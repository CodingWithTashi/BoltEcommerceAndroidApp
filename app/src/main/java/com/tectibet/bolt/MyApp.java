package com.tectibet.bolt;

import android.app.Application;
import android.util.Log;

import com.stripe.android.PaymentConfiguration;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;

/**
 * Created by kharag on 05-05-2020.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_QepzQuDrjs6aE8xnKrpFaVs200mW5WR4Pn"
        );
        Log.i("TAG", "onCreate API");

        PaystackSdk.initialize(getApplicationContext());
    }
}