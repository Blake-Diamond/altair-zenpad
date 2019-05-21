package com.example.blake.zenpad;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import static com.example.blake.zenpad.Settings3.gatt;
import static com.example.blake.zenpad.Settings3.kUartServiceUUID;
import static com.example.blake.zenpad.Settings3.kUartTxCharacteristicUUID;

public class ActivityCustom extends AppCompatActivity {
    public String TAG = "massage";
    final Intent custom_Intent = getIntent();
    private SeekBar seek_bar;
    private TextView text_view;
    public FloatingActionButton buttonA1;
    public boolean isToggled = true; //used for all Toggle Switches
    public boolean isToggledA1 = true;
    public boolean motorState = true;
    public boolean isToggledA2 = true;
    public boolean isToggledA3 = true;
    public boolean isToggledA4 = true;
    public boolean isToggledA5 = true;
    public boolean isToggledA6 = true;
    public boolean isToggledA7 = true;
    public boolean isToggledB1 = true;
    public boolean isToggledB2 = true;
    public boolean isToggledB3 = true;
    public boolean isToggledB4 = true;
    public boolean isToggledB5 = true;
    public boolean isToggledB6 = true;
    public boolean isToggledB7 = true;
    public boolean isToggledC1 = true;
    public boolean isToggledC2 = true;
    public boolean isToggledC3 = true;
    public boolean isToggledC4 = true;
    public boolean isToggledC5 = true;
    public boolean isToggledC6 = true;
    public boolean isToggledC7 = true;
    public boolean[] isMotorOn = new boolean[21];

    private byte[] customCMD = {(byte)(0x01) ,(byte)(0x00), (byte)(0x00)};
    private byte motorIntensity = (byte) (0x00); //mapped from 50-250
    private int motorOffset = 50; //Intensity offset
    private int motorScale = 2; //Intensity scaling factor
    private byte motorOff = (byte) (0x00); //intensity for OFF
    byte motor = (byte) (0x00);
    private int test1;
    private int maxIntensityValue;
    private int currentIntensityValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        buttonA1 = findViewById(R.id.floatingActionButtonA1);
/*      buttonA2 = (Button) findViewById(R.id.floatingActionButtonA2);
        Button buttonA3 = (Button) findViewById(R.id.floatingActionButtonA3);
        Button buttonA4 = (Button) findViewById(R.id.floatingActionButtonA4);
        Button buttonA5 = (Button) findViewById(R.id.floatingActionButtonA5);
        Button buttonA6 = (Button) findViewById(R.id.floatingActionButtonA6);
        Button buttonA7 = (Button) findViewById(R.id.floatingActionButtonA7);
        Button buttonB1 = (Button) findViewById(R.id.floatingActionButtonB1);
        Button buttonB2 = (Button) findViewById(R.id.floatingActionButtonB2);
        Button buttonB3 = (Button) findViewById(R.id.floatingActionButtonB3);
        Button buttonB4 = (Button) findViewById(R.id.floatingActionButtonB4);
        Button buttonB5 = (Button) findViewById(R.id.floatingActionButtonB5);
        Button buttonB6 = (Button) findViewById(R.id.floatingActionButtonB6);
        Button buttonB7 = (Button) findViewById(R.id.floatingActionButtonB7);
        Button buttonC1 = (Button) findViewById(R.id.floatingActionButtonC1);
        Button buttonC2 = (Button) findViewById(R.id.floatingActionButtonC2);
        Button buttonC3 = (Button) findViewById(R.id.floatingActionButtonC3);
        Button buttonC4 = (Button) findViewById(R.id.floatingActionButtonC4);
        Button buttonC5 = (Button) findViewById(R.id.floatingActionButtonC5);
        Button buttonC6 = (Button) findViewById(R.id.floatingActionButtonC6);
        Button buttonC7 = (Button) findViewById(R.id.floatingActionButtonC7);*/
        for(int i=0;i<isMotorOn.length;i++){ isMotorOn[i] = true; } //intializing all motors to true
    }

    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_custom);
        seekbarUpdate();
    }

    //OnClick Method for Home Button
    public void toHome(View view){
        Intent presetMassage = new Intent(ActivityCustom.this, MainActivity.class);
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
                //TODO: Test this function
                updateIntensity(motorIntensity);

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
        byte[] value = new byte[1];
        value[0] = (byte) (0xFF);

        byte[] bytes = {(byte)(0xFF) ,(byte)(0xF1), (byte)(0xF2) ,  (byte)(0x12)};
        charac.setValue(byteArray);
        Log.d(TAG,"Value sent to serial monitor:");
        boolean status = gatt.writeCharacteristic(charac);
        return status;
    }

    //TODO: figure out how to change FAB color
    public Boolean toggleButton(FloatingActionButton button, Boolean isToggledFUNC){
        if (isToggledFUNC) { //Turning motor on
            button.setBackgroundColor(Color.RED);
            isToggledFUNC = false;
        }
        else{ //Turning Motor off
            button.setBackgroundColor(Color.GREEN);
            isToggledFUNC = true;
        }
        return isToggledFUNC;
    }

    public void sendMotorCMD(boolean iMotorOn,byte iIntensity,byte[] iCMD){
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

    //TODO: Test this with real massage pad - debug log looks good
    public void stopButton(View view){
        //if motor off, tell motor to turn off!
        customCMD[1] = (byte) (0xF0);
        customCMD[2] = motorOff; //assign OFF value to command array
        writeCharacteristic(gatt, customCMD); //write characteristic to BLE module
        Log.d(TAG,"Motor Intensity OFF: " + customCMD[2]);
    }

    //TODO: Test this with real massage pad - debug log looks good
    public void startButton(View view){
        customCMD[2] = motorIntensity;
        for(int i=0;i<isMotorOn.length;i++) {
            if(isMotorOn[i]){
                //Motor was off, so leave it off
                Log.d(TAG,"Motor: " + i + " is OFF");
            }
            else {
                if(i <= 6){
                    //Left Column
                    motor = (byte) (i + 161);
                }
                else if(i>=7 && i<=13){
                    //Center Column
                    motor = (byte) (i + 170);
                }
                else{
                    //Right Column
                    motor = (byte) (i + 179);
                }

                customCMD[1] = motor; //assign motor
                writeCharacteristic(gatt,customCMD);
                Log.d(TAG,"Motor: " + motor + " Intensity: " + motorIntensity);
                isMotorOn[i] = false;
            }
        }
    }

    //TODO: test with massage pad
    public void updateIntensity(byte Intensity){
        customCMD[2] = Intensity;
        for(int i=0;i<isMotorOn.length;i++){
            if(!isMotorOn[i]) {
                //Motor is ON
                if(i <= 6){
                    //Left Column
                    motor = (byte) (i + 161);
                }
                else if(i>=7 && i<=13){
                    //Center Column
                    motor = (byte) (i + 170);
                }
                else{
                    //Right Column
                    motor = (byte) (i + 179);
                }

                customCMD[1] = motor; //assign motor
                writeCharacteristic(gatt,customCMD);
                Log.d(TAG,"Motor: " + motor + " Intensity: " + motorIntensity);
            }
        }
    }


    //TODO: Scale onClick Methods
    //onClick Methods for each motor button
    public void clickA1(View view){
        motor = (byte) (0xA1); //declare which motor to assign
        customCMD[1] = motor; //assign motor to command array
        FloatingActionButton button = buttonA1;
        isMotorOn[1] = toggleButton(button,isMotorOn[1]); //change button color and get current state
       // buttonA1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent,getTheme()))); //This does not work
        Log.d(TAG,"IsMotorOn A1: " + isMotorOn[1]);
        sendMotorCMD(isMotorOn[1],motorIntensity,customCMD);
    }



}
