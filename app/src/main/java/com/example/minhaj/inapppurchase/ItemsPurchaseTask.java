package com.example.minhaj.inapppurchase;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;

/**
 * Created by minhaj on 30/08/2017.
 */

public class ItemsPurchaseTask extends AsyncTask<Void,Void,Bundle> {

    private Context context;
    private MainActivity mainActivity;
    private IInAppBillingService iInAppBillingService;
    private Bundle requestBundle;

    public ItemsPurchaseTask(Context context, IInAppBillingService iInAppBillingService,Bundle bundle){
        this.context = context;
        mainActivity = (MainActivity) context;
        this.iInAppBillingService = iInAppBillingService;
        requestBundle = bundle;

    }
    @Override
    protected Bundle doInBackground(Void... voids) {
        Bundle bundleDetails = null;
        try {
            bundleDetails = iInAppBillingService.getSkuDetails(3,context.getPackageName(),"inapp",requestBundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return bundleDetails;
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        super.onPostExecute(bundle);
        mainActivity.getItemDetails(bundle);
    }
}
