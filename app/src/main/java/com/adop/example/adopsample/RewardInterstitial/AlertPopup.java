package com.adop.example.adopsample.RewardInterstitial;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adop.example.adopsample.R;

public class AlertPopup extends Dialog {
    private CountDownTimer cdt;
    private long timeRemaining;
    private int time = 5;
    private OnClickListener mListener;

    private TextView textView;
    private TextView negativeButton;
    private TextView positiveButton;

    public AlertPopup(@NonNull Context context, OnClickListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_popup);
        createTimer();

        textView = findViewById(R.id.popupMessage);
        negativeButton = findViewById(R.id.popupNegative);
        positiveButton = findViewById(R.id.popupPositive);

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnNegativeButton();
                dismiss();
            }
        });

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnPositiveButton();
                dismiss();
            }
        });

    }

    private void createTimer() {
        if (cdt != null) {
            cdt.cancel();
        }
        cdt = new CountDownTimer(time * 1000, 50) {
                    @Override
                    public void onTick(long millisUnitFinished) {
                        timeRemaining = ((millisUnitFinished / 1000) + 1);
                        textView.setText("seconds remaining: " + timeRemaining);
                    }

                    @Override
                    public void onFinish() {
                        mListener.OnPositiveButton();
                        dismiss();
                    }
                };
        cdt.start();
    }

    public interface OnClickListener {
        void OnNegativeButton();
        void OnPositiveButton();
    }

    @Override
    public void dismiss() {
        super.dismiss();

        if(cdt != null)
            cdt.cancel();
    }
}
