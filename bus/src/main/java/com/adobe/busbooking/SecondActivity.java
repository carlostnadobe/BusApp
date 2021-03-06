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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.adobe.marketing.mobile.MobileCore;

import java.util.HashMap;


//import com.adobe.marketing.mobile.MobileCore;

/**
 * This is the second screen for testing of multiple screens scenario
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setUpToolBar();
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setTitle("Journey Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                HashMap cData = new HashMap<String, String>();
                cData.put("section", "journey details");
                MobileCore.trackAction("go back", cData);
                System.out.println("AA-LOG:MobileCore.trackAction-go back");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        HashMap cData = new HashMap<String, String>();
        cData.put("section", "bus booking");
        MobileCore.trackState("journey details",cData);
        System.out.println("AA-LOG:MobileCore.trackState-screen-journey details");


    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

