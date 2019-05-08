package com.example.myapplication;
import android.app.Activity;
import android.bluetooth.le.BluetoothLeScanner;
import java.util.*;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.bluetooth.*;
import android.bluetooth.le.ScanSettings;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.*;
import android.util.Log;

public class BLEDriver
{
    private BluetoothAdapter adaptor = BluetoothAdapter.getDefaultAdapter();
    private BluetoothLeScanner scanner = adaptor.getBluetoothLeScanner();

    private final String serviceUUID = "0001A7D3-D8A4-4FEA-8174-1736E808C066";
    private final UUID powerUUID = UUID.fromString("0004A7D3-D8A4-4FEA-8174-1736E808C066");
    private final UUID hsvUUID = UUID.fromString("0002A7D3-D8A4-4FEA-8174-1736E808C066");
    private final UUID brightnessUUID = UUID.fromString("0003A7D3-D8A4-4FEA-8174-1736E808C066");

    private BluetoothGattCharacteristic power;
    private BluetoothGattCharacteristic hsv;
    private BluetoothGattCharacteristic brightness;

    private BluetoothDevice currentDevice = null;
    private BluetoothGatt mygatt = null;

    private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

    public static BLEDriver instance;

    public static void makeInstance(Activity activity)
    {
        if(instance == null)
        {
            instance = new BLEDriver(activity);
        }
    }

    private BLEDriver(Activity activity)
    {
        //Everything breaks if you don't have this.
        int permissionCheck = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    public void startBrowsing(LampDiscoveryDelegate delegate)
    {
        disconnect();
        ParcelUuid serviceId = new ParcelUuid(UUID.fromString(serviceUUID));
        ScanFilter serviceFilter = new ScanFilter.Builder().setServiceUuid(serviceId).build();
        List<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(serviceFilter);
        //scanner.startScan(new BrowserStartCallBack(delegate));
        scanner.startScan(filters, new ScanSettings.Builder().build(), new BrowserStartCallBack(delegate));
    }

    public void stopBrowsing()
    {
        scanner.stopScan(new BrowserStopCallBack());
    }

    public void connect(String mac, Context context, LampiNotifyDelegate delegate)
    {
        stopBrowsing();
        BluetoothDevice device = matchDeviceMac(mac);
        currentDevice = device;
        device.connectGatt(context, true, new LampiCallBack(delegate));
    }

    public void disconnect()
    {
        if(mygatt != null && currentDevice != null)
        {
            mygatt.close();
            mygatt = null;
            currentDevice = null;
            power = null;
            hsv = null;
            brightness = null;
        }
    }

    private BluetoothDevice matchDeviceMac(String mac)
    {
        for(BluetoothDevice device : devices)
        {
            if(device.getAddress().equals(mac))
            {
                return device;
            }
        }
        return null;
    }

    private boolean isDeviceNew(String mac)
    {
        for(BluetoothDevice device : devices)
        {
            if(device.getAddress().equals(mac))
            {
                return false;
            }
        }
        return true;
    }

    public void writePower(boolean isOn)
    {
        if(mygatt != null && power != null)
        {
            if(isOn)
            {
                power.setValue(new byte[]{0x01});
            }
            else
            {
                power.setValue(new byte[]{0x00});
            }
            mygatt.writeCharacteristic(power);
        }

    }

    public void writeHSV(byte h, byte s)
    {
        if(mygatt != null && hsv != null)
        {
            hsv.setValue(new byte[]{h, s, (byte) 0xFF});
            mygatt.writeCharacteristic(power);
        }
    }

    public void writeBrightness(byte brightnessVal)
    {
        if(mygatt != null && brightness != null)
        {
            brightness.setValue(new byte[]{brightnessVal});
            mygatt.writeCharacteristic(power);
        }
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
            Log.d("BLEDriver","Found Device");
            //result.
            if (discovered != null && isDeviceNew(discovered.getAddress()))
            {
                devices.add(discovered);
                //discovered.
                if (discovered.getName() != null)
                {
                    delegate.discoveredLamp(discovered.getName() + " (" + discovered.getAddress() + ")");
                }
            }
        }
    }

    class BrowserStopCallBack extends ScanCallback
    {
    }

    class LampiCallBack extends BluetoothGattCallback
    {
        private LampiNotifyDelegate delegate;

        public LampiCallBack(LampiNotifyDelegate delegate)
        {
            this.delegate = delegate;
        }

        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {
            if(status == BluetoothProfile.STATE_CONNECTED)
            {
                gatt.discoverServices();
                mygatt = gatt;
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            BluetoothGattService service = gatt.getService(UUID.fromString(serviceUUID));
            if(service != null)
            {
                power = service.getCharacteristic(powerUUID);
                hsv = service.getCharacteristic(hsvUUID);
                brightness = service.getCharacteristic(brightnessUUID);

                gatt.setCharacteristicNotification(power, true);
                gatt.setCharacteristicNotification(hsv, true);
                gatt.setCharacteristicNotification(brightness, true);

                //Set delegate values
                readPower();
                readhsv();
                readbrightness();
            }
        }

        public void onCharacteristicChanged (BluetoothGatt gatt,
                                             BluetoothGattCharacteristic characteristic)
        {
            characteristic.getValue();
            UUID current = characteristic.getUuid();

            if(current.equals(powerUUID))
            {
                readPower();
            }

            if(current.equals(hsvUUID))
            {
                readhsv();
            }

            if(current.equals(brightnessUUID))
            {
                readbrightness();
            }
        }

        public void readPower()
        {
            byte val = power.getValue()[0];
            if(val == 0x00)
            {
                delegate.setPower(false);
            }
            else
            {
                delegate.setPower(true);
            }
        }

        public void readhsv()
        {
            byte[] val = hsv.getValue();
            delegate.setHS(val[0], val[1]);
        }

        public void readbrightness()
        {
            byte[] val = brightness.getValue();
            delegate.setB(val[0]);
        }
    }

}


