package com.example.myapplication;
import android.bluetooth.le.BluetoothLeScanner;
import java.util.*;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.bluetooth.*;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;

import android.util.Log;

public class BLEDriver
{
    public static BLEDriver instance = new BLEDriver();
    private BluetoothAdapter adaptor = BluetoothAdapter.getDefaultAdapter();
    private BluetoothLeScanner scanner = adaptor.getBluetoothLeScanner();

    private final String serviceUUID = "0001A7D3-D8A4-4FEA-8174-1736E808C066";
    private final String powerUUID = "0004A7D3-D8A4-4FEA-8174-1736E808C066";
    private final String hsvUUID = "0002A7D3-D8A4-4FEA-8174-1736E808C066";
    private final String brightnessUUID = "0003A7D3-D8A4-4FEA-8174-1736E808C066";

    private BluetoothDevice currentDevice = null;

    private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
    public List<String> deviceNames()
    {
        List<String> names = new ArrayList<String>();
        for(BluetoothDevice device : devices)
        {
            names.add(device.getName());
        }
        return names;
    }

    private BLEDriver()
    {
    }

    public void startBrowsing(LampDiscoveryDelegate delegate)
    {
        devices = new ArrayList<BluetoothDevice>();
        ParcelUuid serviceId = new ParcelUuid(UUID.fromString(serviceUUID));
        ScanFilter serviceFilter = new ScanFilter.Builder().setServiceUuid(serviceId).build();
        List<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(serviceFilter);
        scanner.startScan(filters, new ScanSettings.Builder().build(), new BrowserStartCallBack(delegate));
    }

    public void stopBrowsing()
    {
        scanner.stopScan(new BrowserStopCallBack());
    }

    public void connect(String deviceName, Context context)
    {
        stopBrowsing();
        BluetoothDevice device = matchDeviceStringName(deviceName);
        BluetoothGatt gatt = device.connectGatt(context, true, new LampiCallBack());
        //gatt.
    }

    private BluetoothDevice matchDeviceStringName(String name)
    {
        for(BluetoothDevice device : devices)
        {
            if(device.getName().equals(name))
            {
                return device;
            }
        }
        return null;
    }

    class BrowserStartCallBack extends ScanCallback
    {
        LampDiscoveryDelegate delegate;
        public BrowserStartCallBack(LampDiscoveryDelegate delegate)
        {
            this.delegate = delegate;
        }
        public void onScanResult (int callbackType,
                                  ScanResult result)
        {
            BluetoothDevice discovered = result.getDevice();
            if (discovered != null)
            {
                devices.add(discovered);
                delegate.discoveredLamp(discovered.getName());
            }
        }
    }
    class BrowserStopCallBack extends ScanCallback
    {
    }

    class LampiCallBack extends BluetoothGattCallback
    {

    }

}


