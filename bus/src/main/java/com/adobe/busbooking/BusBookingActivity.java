/*************************************************************************
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 *  Copyright 2018 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by all applicable intellectual property
 * laws, including trade secret and copyright laws.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
package com.adobe.busbooking;
import java.util.HashMap;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.Analytics;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.MobilePrivacyStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * This activity class is responsible to show booking engine page and offer card.
 */

public class BusBookingActivity extends AppCompatActivity {
    private TextView mTextGoingTo, mTextLeavingFrom, mTextSource, mTextDestination;
    private ImageButton mBtnFlip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
        System.out.println("AA-LOG:preferenceManager - clear (sharedPreferences)");

        MobileCore.setPrivacyStatus(MobilePrivacyStatus.OPT_IN);
        System.out.println("AA-LOG:MobilePrivacyStatus.OPT_IN onCreate");

        setContentView(R.layout.activity_bus_booking);
        setUpToolBar();
        mTextGoingTo =  findViewById(R.id.text_going_to);
        mTextLeavingFrom =  findViewById(R.id.text_leaving_from);
        mTextDestination =  findViewById(R.id.text_destination);
        mTextSource =  findViewById(R.id.text_source);
        mBtnFlip =  findViewById(R.id.btn_flip);

        mBtnFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipSourceDesti();
            }
        });


        findViewById(R.id.btn_find_buses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
                HashMap cData = new HashMap<String, String>();
                cData.put("section", "bus booking");
                MobileCore.trackAction("get buses", cData);
                System.out.println("AA-LOG:MobileCore.trackAction-get buses");

            }
        });


        findViewById(R.id.rel_offer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusBookingActivity.this, OfferDetailsActivity.class));
            }
        });

        findViewById(R.id.fragOffer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusBookingActivity.this, SampleFragmentActivity.class));
            }
        });

        setSource("San Francisco");
        setDest("Las Vegas");
    }


    private void setUpToolBar() {

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

        toolbar.setTitle("Bus Booking");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    public void flipSourceDesti() {

        Animation animClockWise = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anin_rotating_50_clockwise);

        final Animation aniAntiClockWise = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_rotate_50_anti_clockwise);

        aniAntiClockWise.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBtnFlip.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        animClockWise.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mBtnFlip.setVisibility(View.INVISIBLE);
                mBtnFlip.startAnimation(aniAntiClockWise);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBtnFlip.startAnimation(animClockWise);
        startAnimation();

    }

    /**
     * Switch animation
     */
    private void startAnimation() {

        Animation forLeavingFromIn = AnimationUtils.loadAnimation(this, R.anim.left_to_right_in);
        Animation forGoingToIn = AnimationUtils.loadAnimation(this, R.anim.right_to_left_in);

        final Animation forLeavingFromOut = AnimationUtils.loadAnimation(this, R.anim.left_to_right_out);
        final Animation forGoingToOut = AnimationUtils.loadAnimation(this, R.anim.right_to_left_out);

        forLeavingFromIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateCities();
                mTextSource.startAnimation(forLeavingFromOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        forGoingToIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTextDestination.startAnimation(forGoingToOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        mTextSource.startAnimation(forLeavingFromIn);
        mTextDestination.startAnimation(forGoingToIn);
    }


    /**
     * Interchange
     */
    private void updateCities() {
        String strTemp = mTextSource.getText().toString().trim();
        mTextSource.setText(mTextDestination.getText().toString().trim());
        mTextDestination.setText(strTemp);
    }


    private void setDest(String city) {
        mTextGoingTo.setVisibility(View.VISIBLE);
        mTextDestination.setText(city);
        mTextDestination.setTextColor(ContextCompat.getColor(this, R.color.black_opac));

    }


    private void setSource(String city) {

        mTextLeavingFrom.setVisibility(View.VISIBLE);
        mTextSource.setText(city);
        mTextSource.setTextColor(ContextCompat.getColor(this, R.color.black_opac));
    }


    @Override
    protected void onResume() {
        super.onResume();

        MobileCore.setApplication(getApplication());
        MobileCore.lifecycleStart(null);


        HashMap cData = new HashMap<String, String>();
        cData.put("section", "bus booking");
        MobileCore.trackState("home",cData);
        System.out.println("AA-LOG:MobileCore.trackState-screen-home");

        Analytics.getTrackingIdentifier(new AdobeCallback<String>() {
            @Override
            public void call(final String trackingIdentifier) {
                // check the trackingIdentifier value --> ECID (if null, there is no ECID service)
                System.out.println("AA-LOG: trackingIdentifier: "+trackingIdentifier);
            }
        });
     }

    @Override
    protected void onPause() {
        super.onPause();
        MobileCore.lifecyclePause();
        System.out.println("AA-LOG:lifecyclePause - onPause");
    }

    private void showConfirmationDialog() {
        DialogFragment sampleDialogFragment = SampleDialogFragment.getInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag("dialogView");
        if (prevFragment != null) {
            fragmentTransaction.remove(prevFragment);
        }
        fragmentTransaction.addToBackStack(null);
        sampleDialogFragment.show(fragmentTransaction, "dialogView");
    }

}
