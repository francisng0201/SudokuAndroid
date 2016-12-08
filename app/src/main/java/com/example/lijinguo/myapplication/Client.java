package com.example.lijinguo.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by lijinguo on 11/29/16.
 */

public class Client extends Thread{
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private final static UUID uuid = UUID.fromString("fc5ffc49-00e3-4c8b-9cf1-6b72aad1001a");
    private BluetoothAdapter bluetoothAdapter;

    public Client(BluetoothDevice device, BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
        BluetoothSocket temp = null;
        bluetoothDevice = device;

        try {
            temp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothSocket = temp;
    }

    public void run() {
        // Cancel any discovery as it will slow down the connection
        bluetoothAdapter.cancelDiscovery();

        try {
            bluetoothSocket.connect();
        } catch (IOException connectException) {
            connectException.printStackTrace();
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
        }

        // Code to manage the connection in a separate thread
        /*
            manageBluetoothConnection(bluetoothSocket);
        */
    }

    // Cancel an open connection and terminate the thread
    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
