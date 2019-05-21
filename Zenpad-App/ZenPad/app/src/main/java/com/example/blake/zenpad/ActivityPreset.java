package com.example.blake.zenpad;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import static com.example.blake.zenpad.Settings3.gatt;
import static com.example.blake.zenpad.Settings3.kUartServiceUUID;
import static com.example.blake.zenpad.Settings3.kUartTxCharacteristicUUID;

public class ActivityPreset extends AppCompatActivity {
    private String TAG = "massage";
    final Intent preset_Intent = getIntent();
    private SeekBar seek_bar;
    private byte[] presetCMD = {(byte) (0x02), (byte) (0x00), (byte) (0x00)};
    private byte motorIntensity = (byte) (0x00); //mapped from 50-250
    private int motorOffset = 50; //Intensity offset
    private int motorScale = 2; //Intensity scaling factor
    private byte motorOff = (byte) (0x00); //intensity for OFF
    int currentIntensityValue;
    int maxIntensityValue;
    int test1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset);
    }

    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_preset);
        seekbarUpdate();
    }

    public void toHome(View view) {
        Intent presetMassage = new Intent(ActivityPreset.this, MainActivity.class);
        startActivity(presetMassage);
    }

    public void toSettings1(View view) {
        Intent presetMassage = new Intent(ActivityPreset.this, ActivitySettings.class);
        startActivity(presetMassage);
    }

    //Seekbar Handler - Range is from 0 - 100 before adjustment
    public void seekbarUpdate() {
        seek_bar = (SeekBar) findViewById(R.id.seekBar2);
        motorIntensity = (byte) (motorScale*seek_bar.getProgress() + motorOffset);

        Log.d(TAG,"SeekbarUpdate value: " + motorIntensity + " / " + seek_bar.getMax());
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                int progress_value;

                                                @Override
                                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                    progress_value = progress;
                                                    motorIntensity = (byte) (motorScale*seek_bar.getProgress() + motorOffset);
                                                    Log.d(TAG,"Progress Change value: " + motorIntensity + " / " + seek_bar.getMax());
                                                    Log.d(TAG,"Progress Change value: " + progress_value + " / " + seek_bar.getMax());
                                                }

                                                @Override
                                                public void onStartTrackingTouch(SeekBar seekBar) {
                                                    // Toast.makeText(ActivityCustom.this, "SeekBar Started Tracking", Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onStopTrackingTouch(SeekBar seekBar) {
                                                    //motorIntensity = (byte) (seek_bar.getProgress() + motorOffset);
                                                    currentIntensityValue = motorScale*seekBar.getProgress() + 50;
                                                    maxIntensityValue = motorScale*seekBar.getMax()+50;
                                                    motorIntensity = (byte) (currentIntensityValue);
                                                    Log.d(TAG,"Stop value MI: " + motorIntensity + " / " + maxIntensityValue);
                                                    Log.d(TAG,"Progress value: " + progress_value + " / " + seek_bar.getMax());
                                                    Log.d(TAG,"Test1: " + test1 + " / " + maxIntensityValue);
                                                    //TODO: make function that updates intensity and restarts current massage pattern
                                                }
                                            }
        );
    }

    private void sendMotorCMD(boolean iMotorOn,byte iIntensity,byte[] iCMD){
        if (!iMotorOn) { //if motor on, get intensity and send command
            iCMD[2] = iIntensity; //assign seekbar value to command array
            Log.d(TAG,"Motor Intensity ON: " + iCMD[2]);
            writeCharacteristic(gatt,iCMD); //write command array to BLE module
        }
        else { //if motor off, tell motor to turn off!
            iCMD[2] = motorOff; //assign OFF value to command array
            Log.d(TAG,"Motor Intensity OFF: " + iCMD[2]);
            writeCharacteristic(gatt,iCMD); //write command array to BLE module
        }
    }

    //Funnction to write up to ~20 bytes to the BLE Module
    private boolean writeCharacteristic(BluetoothGatt gatt, byte[] byteArray){
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
        charac.setValue(byteArray);
        Log.d(TAG,"Value sent to serial monitor:");
        boolean status = gatt.writeCharacteristic(charac);
        return status;
    }

    public void sendHorizontalWave(View view){

    }

    public void sendVerticalWave(View view){

    }

    public void sendStarburst(View view){

    }

}
