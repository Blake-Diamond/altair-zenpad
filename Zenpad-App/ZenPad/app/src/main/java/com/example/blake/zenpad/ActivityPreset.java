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
    int currentMassage; //0 OFF, 1 Horizontal, 2 Vertical, 3 Starburst, 4 4th Pattern

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
        Intent presetMassage = new Intent(ActivityPreset.this, Settings3.class);
        startActivity(presetMassage);
    }

    //Seekbar Handler - Range is from 0 - 100 before adjustment
    public void seekbarUpdate() {
        seek_bar = (SeekBar) findViewById(R.id.seekBar3);
        motorIntensity = (byte) (motorScale*seek_bar.getProgress() + motorOffset);
        Log.d(TAG,"SeekbarUpdate value: " + motorIntensity + " / " + seek_bar.getMax());
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                int progress_value;

                                                @Override
                                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                    //not really used
                                                    progress_value = progress;
                                                    motorIntensity = (byte) (motorScale*seek_bar.getProgress() + motorOffset);
                                                    //Log.d(TAG,"Progress Change value: " + motorIntensity + " / " + seek_bar.getMax());
                                                    //Log.d(TAG,"Progress Change value: " + progress_value + " / " + seek_bar.getMax());
                                                }

                                                @Override
                                                public void onStartTrackingTouch(SeekBar seekBar) {
                                                    // Toast.makeText(ActivityCustom.this, "SeekBar Started Tracking", Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onStopTrackingTouch(SeekBar seekBar) {
                                                    currentIntensityValue = motorScale*seekBar.getProgress() + 50;
                                                    maxIntensityValue = motorScale*seekBar.getMax()+50;
                                                    motorIntensity = (byte) (currentIntensityValue);
                                                    Log.d(TAG,"Stop value MI: " + currentIntensityValue + " / " + maxIntensityValue);
                                                    updateIntensityValue(motorIntensity); //updates intensity and restarts pattern
                                                }
                                            }
        );
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
        currentMassage = 1;
        presetCMD[1] = (byte) (currentMassage);
        presetCMD[2] = motorIntensity;
        writeCharacteristic(gatt,presetCMD);
        Log.d(TAG,"Massage Pattern: " + currentMassage + " Intensity: " + currentIntensityValue + " / " + maxIntensityValue);
    }

    public void sendVerticalWave(View view){
        currentMassage = 2;
        presetCMD[1] = (byte) (currentMassage);
        presetCMD[2] = motorIntensity;
        writeCharacteristic(gatt,presetCMD);
        Log.d(TAG,"Massage Pattern: " + currentMassage + " Intensity: " + currentIntensityValue + " / " + maxIntensityValue);
    }

    public void sendStarburst(View view){
        currentMassage = 3;
        presetCMD[1] = (byte) (currentMassage);
        presetCMD[2] = motorIntensity;
        writeCharacteristic(gatt,presetCMD);
        Log.d(TAG,"Massage Pattern: " + currentMassage + " Intensity: " + currentIntensityValue + " / " + maxIntensityValue);
    }

    public void presetStopButton(View view){
        presetCMD[1] = (byte) (0xF0);
        presetCMD[2] = (byte) (0x00);
        writeCharacteristic(gatt,presetCMD);
        Log.d(TAG,"Stop Button Pressed");
    }

    public void presetStartButton(View view){
        Log.d(TAG,"Resuming Massage Activity");
        presetCMD[1] = (byte) (currentMassage);
        presetCMD[2] = motorIntensity;
        writeCharacteristic(gatt,presetCMD);
        Log.d(TAG,"Massage Pattern: " + currentMassage + " Intensity: " + currentIntensityValue + " / " + maxIntensityValue);
    }

    public void updateIntensityValue(byte Intensity){
        presetCMD[1] = (byte) (currentMassage);
        presetCMD[2] = Intensity;
        writeCharacteristic(gatt,presetCMD);
        Log.d(TAG,"Updating Intensity Value from Seekbar");
        Log.d(TAG,"Massage Pattern: " + currentMassage + " Intensity: " + currentIntensityValue + " / " + maxIntensityValue);
    }

}
