package com.example.lijinguo.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by lijinguo on 11/29/16.
 */

public class ListeningThread extends Thread{
    private final BluetoothServerSocket bluetoothServerSocket;
    private final static UUID uuid = UUID.fromString("fc5ffc49-00e3-4c8b-9cf1-6b72aad1001a");
    Activity listenerActivity;

    public ListeningThread(Activity listenerActivity, BluetoothAdapter bluetoothAdapter) {

            this.listenerActivity = listenerActivity;
            BluetoothServerSocket temp = null;
            try {

                temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(GlobalVariable.getAppContext().getString(R.string.app_name), uuid);

            } catch (IOException e) {
                e.printStackTrace();
            }
            bluetoothServerSocket = temp;

    }
    public void run() {
        BluetoothSocket bluetoothSocket;
        while (true) {
            try {
                bluetoothSocket = bluetoothServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection is accepted
            if (bluetoothSocket != null) {

                listenerActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(GlobalVariable.getAppContext(), "A connection has been accepted.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                // Manage the connection in a separate thread

//                try {
//                    bluetoothServerSocket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                break;
            }
        }
    }

    // Cancel the listening socket and terminate the thread
    public void cancel() {
        try {
            bluetoothServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
