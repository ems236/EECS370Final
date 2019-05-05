package com.example.myapplication;
import android.content.Context;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;

public class BLEDriver
{
    public static BLEDriver instance = new BLEDriver();
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private BLEDriver()
    {
    }

    public void startBrowsing()
    {

    }
}
