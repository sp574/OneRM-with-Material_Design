package sleeping_vityaz.onerm;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import sleeping_vityaz.onerm.util.IabHelper;
import sleeping_vityaz.onerm.util.IabResult;
import sleeping_vityaz.onerm.util.Purchase;

/**
 * Created by naja-ox on 1/14/15.
 */
public class DonateActivity extends ActionBarActivity {

    static final String SKU_SMALL = "s_donation";
    static final String SKU_MEDIUM = "m_donation";
    static final String SKU_LARGE = "l_donation";
    static final String SKU_XL = "xl_donation";
    static final String SKU_XXL = "xxl_donation";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // TAG for debugging
    final String TAG = "IAB-OneRM";

    IabHelper mHelper;

    TextView plz_donate;
    RadioGroup rg_donate;
    RadioButton small, medium, large, xlarge, xxlarge;
    ButtonFlat bt_donate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_layout);

        String base64EncodedPublicKey = getString(R.string.app_license);
        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_toolbar);
        
        findViewsById();

        // In-app payment setup
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    return;
                }
                // Hooray, IAB is fully set up!

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

            }
        });

        bt_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = rg_donate.indexOfChild(findViewById(rg_donate.getCheckedRadioButtonId()));
                if (selected == -1){

                }else{
                    Log.d(TAG, "Button selected: "+selected);
                    makeDonation(selected);
                }

            }
        });

    }

    private void findViewsById() {
        plz_donate = (TextView) findViewById(R.id.plz_donate);
        rg_donate = (RadioGroup) findViewById(R.id.rg_donate);
        small = (RadioButton) findViewById(R.id.small);
        medium = (RadioButton) findViewById(R.id.medium);
        large = (RadioButton) findViewById(R.id.large);
        xlarge = (RadioButton) findViewById(R.id.xlarge);
        xxlarge = (RadioButton) findViewById(R.id.xxlarge);
        bt_donate = (ButtonFlat) findViewById(R.id.bt_donate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.donate_me) {
            return true;
        }
        if (id == R.id.about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    // Used to launch in-app purchase
        /*

        String payload = "";

        mHelper.launchPurchaseFlow(this, SKU_GAS, RC_REQUEST,
                mPurchaseFinishedListener, payload);

        * */

    //the button clicks send an int value which would then call the specific SKU, depending on the
    //application
    public void makeDonation(int value) {
        //check your own payload string.
        String payload = "";

        switch (value) {
            case (0):
                mHelper.launchPurchaseFlow(this, SKU_SMALL, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
                Log.d(TAG, SKU_SMALL+" purchase");
                break;
            case (1):
                mHelper.launchPurchaseFlow(this, SKU_MEDIUM, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
                Log.d(TAG, SKU_MEDIUM + " purchase");
                break;
            case (2):
                mHelper.launchPurchaseFlow(this, SKU_LARGE, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
                Log.d(TAG, SKU_LARGE + " purchase");
                break;
            case (3):
                mHelper.launchPurchaseFlow(this, SKU_XL, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
                Log.d(TAG, SKU_XL + " purchase");
                break;
            case (4):
                mHelper.launchPurchaseFlow(this, SKU_XXL, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
                Log.d(TAG, SKU_XXL + " purchase");
                break;

            default:
                break;
        }

    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {

        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            if (!verifyDeveloperPayload(purchase)) {
                complain("Error verification");
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_SMALL)
                    || purchase.getSku().equals(SKU_MEDIUM)
                    || purchase.getSku().equals(SKU_LARGE)
                    || purchase.getSku().equals(SKU_XL)
                    || purchase.getSku().equals(SKU_XXL)) {
                //check if any item is consumed
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
        }
    };

    // Called when consumption is complete

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {

        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            //check which SKU is consumed here and then proceed.
            if (result.isSuccess()) {
                Log.d(TAG, "Consumption successful. Provisioning.");
                SnackbarManager.show(
                        Snackbar.with(getApplicationContext())
                                .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                                .text(getApplicationContext().getString(R.string.thank_you)));
            } else {
                complain("Error consuming SKU "+result);
            }
            Log.d(TAG, "End consumption flow.");

        }};



    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {

            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);

        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    void complain(String message) {
        Log.e(TAG, "**** Application Error: " + message);
        alert("Error: " + message);
    }
    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

}
