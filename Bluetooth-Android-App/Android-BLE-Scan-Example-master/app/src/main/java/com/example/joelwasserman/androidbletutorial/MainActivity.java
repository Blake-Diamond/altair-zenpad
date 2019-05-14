package com.example.joelwasserman.androidbletutorial;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "BT-DEBUG";
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Button startScanningButton;
    Button stopScanningButton;
    TextView peripheralTextView;
    //int CLIENT_CHARACTERISTIC_CONFIG_UUID = convertFromInteger(0x2902);
    String address; //Bluetooth MAC Address of our device
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    BluetoothGatt gatt;
    //BluetoothGattCallback gattCallback = new BluetoothGattCallback();

    //Imported from the Bluefruit app
    // Constants
    private static final UUID kUartServiceUUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    private static final UUID kUartTxCharacteristicUUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    private static final UUID kUartRxCharacteristicUUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    private static final int kUartTxMaxBytes = 20;
    public static final int kUartReplyDefaultTimeout = 2000;       // in millis
    private BluetoothGattCharacteristic mUartTxCharacteristic;
    private BluetoothGattCharacteristic mUartRxCharacteristic;
    private int mUartTxCharacteristicWriteType;
    public byte[] xx = new byte[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        peripheralTextView = (TextView) findViewById(R.id.PeripheralTextView);
        peripheralTextView.setMovementMethod(new ScrollingMovementMethod());

        startScanningButton = (Button) findViewById(R.id.StartScanButton);
        startScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startScanning();
            }
        });

        stopScanningButton = (Button) findViewById(R.id.StopScanButton);
        stopScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopScanning();
            }
        });
        stopScanningButton.setVisibility(View.INVISIBLE);

        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();


        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }

        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }

    }


    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            address = gatt.getDevice().getAddress();

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "Attempting to start service discovery:" + gatt.discoverServices());


            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //onDisconnected(gatt);
                Log.i(TAG, "Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // success, we can communicate with the device
                Log.i(TAG, "OnServicesDiscovered = Success");
                boolean test = writeCharacteristic(gatt);

                if(test){Log.d(TAG,"test worked, check Teensy Serial Monitor");}
                else{Log.d(TAG,"test did not work");}
            } else {
                // failure
                Log.i(TAG, "OnServicesDiscovered = Failure");
            }
        }

    };


    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            peripheralTextView.append("Device Name: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "  Device:"  + result.getDevice()+ "\n");

            //display our device name
            Log.d(TAG, "Device Address: " + result.getDevice().getAddress());
            if(result.getDevice().getAddress().matches("D2:48:D2:3B:E0:34")) {
                address = result.getDevice().getAddress(); // this is what we will need to
                Log.d(TAG, "Device Name: " + address);
                Log.d(TAG,"We found our BLE Device");
                boolean connectionWorked = connect(address);
                if (connectionWorked ){
                    stopScanning(); //stop trying to find new devices
                    xx[0] = 12;
                    Log.d(TAG,"Connection worked");
                    //boolean sentWorked = uartSend(xx,mUartTxCharacteristic, address);
                    boolean sentWorked = false;
                    boolean writeCharWorked = writeCharacteristic(gatt);
                    if(sentWorked){Log.d(TAG,"UART SEND was actually successful");}
                    else{Log.d(TAG,"UART SEND was unfortunately NOT successful");}
                    if(writeCharWorked){Log.d(TAG,"writeCharacteristic was actually successful :)");}
                    else{Log.d(TAG,"WriteCharacteristic was unfortunately NOT successful :(");}

                }
                else{
                    Log.d(TAG,"Connection NOT Successful");
                }
                //result.getDevice().connectGatt(getApplicationContext(),true,gattCallback);
            }


            // auto scroll for text view
            final int scrollAmount = peripheralTextView.getLayout().getLineTop(peripheralTextView.getLineCount()) - peripheralTextView.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                peripheralTextView.scrollTo(0, scrollAmount);
        }
    };

    public boolean connect(String address) {
        Log.d(TAG, "Connecting to " + address);
        if (btAdapter == null || address == null) {
            Log.d(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        gatt = device.connectGatt(this, false, gattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    public void startScanning() {
        System.out.println("start scanning");
        peripheralTextView.setText("");
        startScanningButton.setVisibility(View.INVISIBLE);
        stopScanningButton.setVisibility(View.VISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.startScan(leScanCallback);
            }
        });
    }

    public void stopScanning() {
        System.out.println("stopping scanning");
        peripheralTextView.append("Stopped Scanning");
        startScanningButton.setVisibility(View.VISIBLE);
        stopScanningButton.setVisibility(View.INVISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
            }
        });
    }
    public void disconnectDevice(View view){
        disconnect(address,gatt);
    }

    public void disconnect(String address, BluetoothGatt gatt) {

        if (btAdapter == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        }
        if(gatt!=null) {
            gatt.disconnect();
            Log.d(TAG,"BLE Device Disconnected!");
        }
        else{Log.d(TAG,"gatt is null...");}

    }

    // region Send
    public boolean uartSend(@NonNull byte[] data, BluetoothGattCharacteristic characteristic, String address) {
        boolean result = false;
            characteristic.setValue(data); //set value

            if (characteristic == null) {
            Log.e(TAG, "Command Error: characteristic no longer valid");
            return result;
            }

            result = gatt.writeCharacteristic(characteristic); //write that value

            if(!result){
                Log.d(TAG,"gatt.writeCharacteristic Failed!");
            }
        return result;
    }

    public boolean writeCharacteristic(BluetoothGatt gatt){
        //check mBluetoothGatt is available
        if (gatt == null) {
            Log.e(TAG, "lost connection");
            return false;
        }

        //instantiating a service
        BluetoothGattService Service = gatt.getService(kUartServiceUUID); //what is your_services?
        if (Service == null) {
            Log.e(TAG, "service not found!");
            return false;
        }

        //instantiating a characteristic
        BluetoothGattCharacteristic charac = Service.getCharacteristic(kUartTxCharacteristicUUID); //what is my characteristic?
        if (charac == null) {
            Log.e(TAG, "char not found!");
            return false;
        }


        //set value and write characteristic
        byte[] value = new byte[1];
        value[0] = (byte) (0xFF);

        byte[] bytes = {(byte)(0xFF) ,(byte)(0xFD), (byte)(0xFE), (byte) (0x12), (byte) (0xAA)};
        byte[] newCMD = new byte[3];

        // untested below
        // setByteValue(newCMD,0xFF,0); //set mode
        // set motor
        // set

        charac.setValue(bytes);
        Log.d(TAG,"Value sent to serial monitor:");
        boolean status = gatt.writeCharacteristic(charac);
        return status;
    }
    public void onDestroy(){
        super.onDestroy();
        disconnect(address, gatt);
    }

    //Sets a byte value to a specific index of a byte array
    public void setByteValue(byte[] byteArray, int value, int index){
        byteArray[index] = (byte) (value);
    }

}

