package com.example.minhaj.inapppurchase;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private IInAppBillingService iInAppBillingService;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d("tag","connected");
                iInAppBillingService = IInAppBillingService.Stub.asInterface(iBinder);
                requestItems();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d("tag","disconnected");
                iInAppBillingService = null;
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE);

    }

    private void requestItems() {
        Bundle bundleRequest = new Bundle();
        ArrayList<String> arrayListPId = new ArrayList<>();
        arrayListPId.add("gas");
        //arrayListPId.add("premiumUpgrade");
        bundleRequest.putStringArrayList("ITEM_ID_LIST",arrayListPId);
        new ItemsPurchaseTask(this,iInAppBillingService,bundleRequest).execute();
    }

    public void getItemDetails(Bundle bundle){
        Log.d("tag","getting details..");
        int responseCode = bundle.getInt("RESPONSE_CODE");
        Log.d("tag","response code = "+responseCode);
        if (responseCode == 0){
            ArrayList<String> arrayList = bundle.getStringArrayList("DETAILS_LIST");
            Log.d("tag","list = "+arrayList.size());
            for (String response : arrayList){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String pId = jsonObject.getString("productId");
                    String price = jsonObject.getString("price");
                    Log.d("tag","pId="+pId);
                    Log.d("tag","price="+price);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
