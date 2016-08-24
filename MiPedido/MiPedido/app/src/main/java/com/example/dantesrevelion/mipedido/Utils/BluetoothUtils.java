package com.example.dantesrevelion.mipedido.Utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.example.dantesrevelion.mipedido.CarritoCompra;
import com.example.dantesrevelion.mipedido.R;
import com.example.dantesrevelion.mipedido.SearchList;
import com.example.dantesrevelion.mipedido.VysorAdapterMenu;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Dantes Revelion on 20/08/2016.
 */
public class BluetoothUtils {
    BluetoothAdapter mBluetoothAdapter;
    List<BluetoothDevice> searchResult;
    TextView tv_test;

    public BluetoothUtils(Context context){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        searchResult=new ArrayList<>();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, filter);

    }
    int n=0;
    /** Se ejecuta cuando encuentra un dispositivo */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                searchResult.add(device);
                System.out.println("Found device " + device.getName());
                n++;
                tv_test.setText("N "+n);
//                SearchList search=new SearchList();
 //               search.setSearchList();

            }

            /*
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state        = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState    = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    System.out.println("PAIRED");
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    System.out.println("UNPAIRED");
                }

            }
            */


            System.out.println(" state receiver"+mBluetoothAdapter.getState());
        }
    };


    public boolean bluetoothIsOn(Activity activity){

        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetooth.putExtra("Proceso","1");
            activity.setResult(2,enableBluetooth);
            activity.startActivityForResult(enableBluetooth,101);


        }
        System.out.println(" state "+mBluetoothAdapter.getState());
        return mBluetoothAdapter.isEnabled();
    }

    public List<BluetoothDevice> getListDevice(){
        return searchResult;
    }
    public void setTextViewLEL(TextView tv){
        tv_test=tv;

    }
    public void searchDevices(){

        mBluetoothAdapter.startDiscovery();
        System.out.println(" state "+mBluetoothAdapter.getState());

    }


    /** enpareja un dispositivo */
    public void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** desenpareja dispositivo */
    public void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPaired() {
        Set<BluetoothDevice> pairedDevice = mBluetoothAdapter.getBondedDevices();
      //  ArrayList<String> arrayListpaired;
        if(pairedDevice.size()>0) {
            return true;
         //   for(BluetoothDevice device : pairedDevice){
        //        arrayListpaired.add(device.getName()+"\n"+device.getAddress());
        //        arrayListPairedBluetoothDevices.add(device);
            //}
        }else{
            return false;
        }

    }


}
