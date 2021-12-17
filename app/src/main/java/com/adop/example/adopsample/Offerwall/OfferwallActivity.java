package com.adop.example.adopsample.Offerwall;

import ad.helper.openbidding.offerwall.BidmadOfferwallAd;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.adop.example.adopsample.R;
import com.adop.sdk.offerwall.OfferwallAdListener;
import com.adop.sdk.offerwall.OfferwallCurrencyListener;
import com.adop.sdk.offerwall.OfferwallInitListener;

public class OfferwallActivity extends AppCompatActivity {

    BidmadOfferwallAd mOfferwall;
    TextView callbackStatus;
    TextView balanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerwall);

        callbackStatus = findViewById(R.id.offerwallCallbackStatus);
        balanceText = findViewById(R.id.currencyBalance);

        //Require
        mOfferwall = new BidmadOfferwallAd(this, "YOUR ZONE ID", new OfferwallInitListener() {
            @Override
            public void onInitSuccess() {
                callbackLog("onInitSuccess() Called");
            }

            @Override
            public void onInitFail(String error) {
                callbackLog("onInitFail() Called ["+error+"]");
            }
        });

        mOfferwall.setOfferwallAdListener(new OfferwallAdListener() {
            @Override
            public void onLoadAd() {
                callbackLog("onLoadAd() Called");
            }

            @Override
            public void onShowAd() {
                callbackLog("onShowAd() Called");
            }

            @Override
            public void onFailedAd() {
                callbackLog("onFailedAd() Called");
            }

            @Override
            public void onCloseAd() {
                callbackLog("onCloseAd() Called");
            }
        });

        mOfferwall.setOfferwallCurrencyListener(new OfferwallCurrencyListener() {
            @Override
            public void onGetCurrencyBalanceSuccess(String currencyName, int balance) {
                callbackLog("onGetCurrencyBalanceSuccess() Called [" + balance + "]");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        balanceText.setText(""+balance);
                    }
                });
            }

            @Override
            public void onGetCurrencyBalanceFail(String error) {
                callbackLog("onGetCurrencyBalanceFail() Called [" + error + "]");
            }

            @Override
            public void onSpendCurrencySuccess(String currencyName, int balance) {
                callbackLog("onSpendCurrencySuccess() Called [" + balance + "]");
            }

            @Override
            public void onSpendCurrencyFail(String error) {
                callbackLog("onSpendCurrencyFail() Called [" + error + "]");
            }
        });

        //Option(Use when needed)
//        mOfferwall.setChildDirected(true); //COPPA
//        mOfferwall.setCUID("YOUR ENCRYPTED CUID"); //Encrypt the identifier and send it to Bidmad.


        findViewById(R.id.loadOfferwall).setOnClickListener(v -> {
            if(mOfferwall.isSDKInit()){
                mOfferwall.load();
            }
        });

        findViewById(R.id.showOfferwall).setOnClickListener(v -> {
            if(mOfferwall.isLoaded()){
                mOfferwall.show();
            }else{
                Log.d("", "Ad Not Load");
            }
        });

        findViewById(R.id.getCurrencyBalance).setOnClickListener(v -> {
            mOfferwall.getCurrencyBalance();
        });

        findViewById(R.id.spendCurrency).setOnClickListener(v -> {
            EditText amount_txt = findViewById(R.id.spandAmount);
            String amount_str = amount_txt.getText().toString().isEmpty() ? "0" : amount_txt.getText().toString();
            int amount = Integer.parseInt(amount_str);

            if(amount > 0){
                mOfferwall.spendCurrency(amount);
            }else{
                callbackLog("Please enter an amount greater than 0.");
            }
        });
    }

    public void callbackLog(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callbackStatus.append(msg+"\n");
            }
        });
    }
}
