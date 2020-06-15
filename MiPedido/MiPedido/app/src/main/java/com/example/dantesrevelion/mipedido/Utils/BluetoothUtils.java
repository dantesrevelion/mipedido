package com.example.dantesrevelion.mipedido.Utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.example.dantesrevelion.mipedido.CarritoCompra;
import com.example.dantesrevelion.mipedido.R;
import com.example.dantesrevelion.mipedido.SearchList;
import com.example.dantesrevelion.mipedido.VysorAdapterMenu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Dantes Revelion on 20/08/2016.
 */
public class BluetoothUtils {
    BluetoothAdapter mBluetoothAdapter;
    public static List<BluetoothDevice> searchResult;
    private static BluetoothDevice mmDevice;
    private static BluetoothSocket mmSocket;
    public static boolean alreadyConected=false;



    public static boolean printerConected=false;
    TextView tv_test;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;



    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    public static List<BluetoothDevice> devices;

    public BluetoothUtils(Context context){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        searchResult=new ArrayList<>();



    }



    public static BluetoothDevice getMmDevice() {
        return mmDevice;
    }

    public static void setMmDevice(BluetoothDevice mmDevice,Context context) {
        BluetoothUtils.mmDevice = mmDevice;
        SharedPreferencesUtils.writeSharedPreference(context,"nameDevice",mmDevice.getName());
    }


    // tries to open a connection to the bluetooth printer device
    public void openBT(Context context,BluetoothDevice deviceLocal) throws IOException {



            if(deviceLocal==null){
               // mmDevice=getPrinterDevice(context);
                Answers.getInstance().logCustom(new CustomEvent("openBT deviceLocal es null"));
                return;
            }

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = deviceLocal.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();
            alreadyConected=true;
            //myLabel.setText("Bluetooth Opened");


    }

    public static boolean isOpen(){
        boolean response=false;
        if(mmSocket!=null){
            response = mmSocket.isConnected() ;
        }
        return response ;
    }
    /*
 * after opening a connection to bluetooth printer device,
 * we have to listen and check if a data were sent to be printed.
 */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 50;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        Log.d("SEND DATA", data);
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //Toast toast=new Toast()
                                                System.out.println("result");
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void testText(List<String> data,String usuario){
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String format = s.format(new Date());

        Log.d("ticket","            MI PEDIDO           ");
        Log.d("ticket","                                ");
        Log.d("ticket","ID:                             ");
        Log.d("ticket","Nombre:"+CarritoCompra.padRight(usuario,25));
        Log.d("ticket",format+"             ");
        Log.d("ticket","                                ");
        Log.d("ticket","PROD         CTD   PRECIO  TOTAL");

        for(String reg:data){
            Log.d("ticket",reg);
        }

        Log.d("ticket","                                ");
        Log.d("ticket","                                ");
    }

    // this will send text data to be printed by the bluetooth printer
    public void sendData(List<String> data,String usuario) throws IOException {
        try {
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            String format = s.format(new Date());
            testText(data,usuario);
            // 32 caracteres
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            //String[] msg = new String[15];
            List<String> msg = new ArrayList<>();

            msg.add("                                ");
            msg.add("                                ");
            msg.add("                                ");
            msg.add("            MI PEDIDO           ");
            msg.add("                                ");
            msg.add("                                ");
            msg.add("ID:                             ");
            msg.add("Nombre:"+CarritoCompra.padRight(usuario,25));
            msg.add(format+"             ");
            msg.add("                                ");

            msg.add("PROD         CTD   PRECIO  TOTAL");
            for(String reg:data){
                msg.add(reg);
            }
            msg.add("                                ");
            msg.add("                                ");
            msg.add("      GRACIAS POR SU COMPRA     ");
            msg.add("                                ");
            msg.add("                                ");
            msg.add("                                ");
            msg.add("                                ");
            msg.add("                                ");
           // msg += "\n";
            readBufferPosition=0;

            for(int i=0;i<msg.size();i++){
                mmOutputStream.flush();
                mmOutputStream.write(msg.get(i).getBytes("windows-1252"));


            }




            // tell the user data were sent
            //myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
            Answers.getInstance().logCustom(new CustomEvent("Error al enviar los datos ").putCustomAttribute("Error",e.getMessage()));
        }
    }

    public boolean bluetoothIsOn(Activity activity){
        System.out.println("--------->Revisa bluetooth on");
        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetooth.putExtra("Proceso","1");
            activity.setResult(2,enableBluetooth);
            activity.startActivityForResult(enableBluetooth,101);


        }
       
        return mBluetoothAdapter.isEnabled();
    }


    public List<BluetoothDevice> getListDevice(){
        return searchResult;
    }
    public void setTextViewLEL(TextView tv){
        tv_test=tv;

    }

    final Handler handlerBT = new Handler();

    public void searchDevices(){
        Log.d("Printing Progres", ">>>>>>>>>>>>>>Inicia Busqueda");
        devices=new ArrayList<>();
        mBluetoothAdapter.startDiscovery();
        handlerBT.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Printing Progres", ">>>>>>>>>>>>>>Time out 15");
                stopSearch();

            }
        }, 15000);

    }
    public void stopSearch(){
        mBluetoothAdapter.cancelDiscovery();
        Log.d("Printing Progres", ">>>>>>>>>>>>>>Stop serch");
    }




    /** enpareja un dispositivo */
    public static void pairDevice(BluetoothDevice device) {
        try {

            Method method = device.getClass().getMethod("createBond", (Class[]) null);

            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** desenpareja dispositivo */
    public static void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // close the connection to bluetooth printer.
    public void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPaired(Context context) {

        System.out.println("--------->Revisa si esta emparejado");
        /*
        Set<BluetoothDevice> pairedDevice = mBluetoothAdapter.getBondedDevices();

        boolean flag=false;
        for(BluetoothDevice deviceLocal:pairedDevice){


            String paired=SharedPreferencesUtils.readStringSharedPreference(context,"nameDevice");
            Log.d("BLUETOOTH DEVICE: ",deviceLocal.getName()+" , "+deviceLocal.getType()+" , "+deviceLocal.getBluetoothClass()+" , "+deviceLocal.getAddress());
            if( "SPP-R200III".equals(deviceLocal.getName()) || "Bluedio".equals(deviceLocal.getName()) ){
                flag= true;
                break;
            }
        }
        if(pairedDevice.size()>0 && flag) {
            return true;
        }else{
            return false;
        }
*/
        return  printerConected;
    }

    /*
    public  BluetoothDevice getPrinterDevice(Context context){
        BluetoothDevice response=null;
        Set<BluetoothDevice> pairedDevice = mBluetoothAdapter.getBondedDevices();

        for(BluetoothDevice deviceLocal:pairedDevice){
            String paired=SharedPreferencesUtils.readStringSharedPreference(context,"nameDevice");
            Log.d("BLUETOOTH DEVICE: ",deviceLocal.getName()+" , "+deviceLocal.getType()+" , "+deviceLocal.getBluetoothClass()+" , "+deviceLocal.getUuids());
            if("SPP-R200III".equals(deviceLocal.getName()) || "Bluedio".equals(deviceLocal.getName()) || "v3".equals(deviceLocal.getName())){
                Toast.makeText(context, "> "+deviceLocal.getName(), Toast.LENGTH_SHORT).show();
                response= deviceLocal;
            }
        }

        return response;

    }
    */


}
