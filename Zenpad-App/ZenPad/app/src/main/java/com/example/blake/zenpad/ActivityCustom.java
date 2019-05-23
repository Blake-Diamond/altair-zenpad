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
    public FloatingActionButton button;
    private static final int[] BUTTON_IDS = {
            R.id.floatingActionButtonA1,
            R.id.floatingActionButtonA2,
            R.id.floatingActionButtonA3,
            R.id.floatingActionButtonA4,
            R.id.floatingActionButtonA5,
            R.id.floatingActionButtonA6,
            R.id.floatingActionButtonA7,
            R.id.floatingActionButtonB1,
            R.id.floatingActionButtonB2,
            R.id.floatingActionButtonB3,
            R.id.floatingActionButtonB4,
            R.id.floatingActionButtonB5,
            R.id.floatingActionButtonB6,
            R.id.floatingActionButtonB7,
            R.id.floatingActionButtonC1,
            R.id.floatingActionButtonC2,
            R.id.floatingActionButtonC3,
            R.id.floatingActionButtonC4,
            R.id.floatingActionButtonC5,
            R.id.floatingActionButtonC6,
            R.id.floatingActionButtonC7,
    };
/*    public boolean isToggled = true; //used for all Toggle Switches
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
    public boolean isToggledC7 = true;*/
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
        for(int i=0;i<isMotorOn.length;i++){ isMotorOn[i] = true; } //all motors to true
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
                //not really used
                progress_value = progress;
                motorIntensity = (byte) (motorScale*seek_bar.getProgress() + motorOffset);
                //Log.d(TAG,"Progress Change value: " + motorIntensity + " / " + seek_bar.getMax());
                //Log.d(TAG,"Progress Change value: " + progress_value + " / " + seek_bar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Do something?
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentIntensityValue = motorScale*seekBar.getProgress() + 50;
                maxIntensityValue = motorScale*seekBar.getMax()+50;
                motorIntensity = (byte) (currentIntensityValue);
                Log.d(TAG,"Progress Bar: " + currentIntensityValue + " / " + maxIntensityValue);
                updateIntensity(motorIntensity); //update motor intensity function

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
            button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent,getTheme())));
        }
        else { //if motor off, tell motor to turn off!
            iCMD[2] = motorOff; //assign OFF value to command array
            Log.d(TAG,"Motor Intensity OFF: " + iCMD[2]);
            writeCharacteristic(gatt,iCMD); //write command array to BLE module
            button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary,getTheme())));
        }
    }

    //TODO: Test this with real massage pad - debug log looks good
    public void stopButton(View view){
        //if motor off, tell motor to turn off!
        customCMD[1] = (byte) (0xF0);
        customCMD[2] = motorOff; //assign OFF value to command array
        writeCharacteristic(gatt, customCMD); //write characteristic to BLE module
        for(int i=0;i<BUTTON_IDS.length;i++) {
            button = findViewById(BUTTON_IDS[i]);
            button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary, getTheme())));
        }
        Log.d(TAG,"Motor Intensity OFF: " + customCMD[2]);
    }

    //TODO: Test this with real massage pad - debug log looks good
    public void startButton(View view){
        customCMD[2] = motorIntensity;
        for(int i=0;i<isMotorOn.length;i++) {
            if(isMotorOn[i]){
                //Motor was off, so leave it off
            }
            else {
                if(i <= 6){motor = (byte) (i + 161); }
                else if(i>=7 && i<=13){ motor = (byte) (i + 170); }
                else{ motor = (byte) (i + 179); }

                customCMD[1] = motor; //assign motor
                writeCharacteristic(gatt,customCMD);
                button = findViewById(BUTTON_IDS[i]);
                button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent,getTheme())));
                Log.d(TAG,"index:" + i + " Motor: " + motor + " Intensity: " + (int)motorIntensity);
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
                Log.d(TAG,"index:" + i + " Motor: " + motor + " Intensity: " +(int)motorIntensity);
            }
        }
    }
    public int findIndexFromMotor(int intMotor){
        if(intMotor >= 0xA1 && intMotor <= 0xA7){ return (intMotor - 161); }
        else if(intMotor >= 0xB1 && intMotor <= 0xB7){ return (intMotor - 170); }
        else if(intMotor >= 0xC1 && intMotor <= 0xC7){ return (intMotor- 179); }
        else{return -1;} //default value
    }

    //TODO: Scale onClick Methods
    //onClick Methods for each motor button
    public void clickA1(View view){
        int MOTOR = 0xA1;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }

    public void clickA2(View view){
        int MOTOR = 0xA2;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickA3(View view){
        int MOTOR = 0xA3;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickA4(View view){
        int MOTOR = 0xA4;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickA5(View view){
        int MOTOR = 0xA5;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickA6(View view){
        int MOTOR = 0xA6;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickA7(View view){
        int MOTOR = 0xA7;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickB1(View view){
        int MOTOR = 0xB1;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickB2(View view){
        int MOTOR = 0xB2;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickB3(View view){
        int MOTOR = 0xB3;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickB4(View view){
        int MOTOR = 0xB4;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickB5(View view){
        int MOTOR = 0xB5;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickB6(View view){
        int MOTOR = 0xB6;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickB7(View view){
        int MOTOR = 0xB7;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickC1(View view){
        int MOTOR = 0xC1;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickC2(View view){
        int MOTOR = 0xC2;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickC3(View view){
        int MOTOR = 0xC3;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickC4(View view){
        int MOTOR = 0xC4;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickC5(View view){
        int MOTOR = 0xC5;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickC6(View view){
        int MOTOR = 0xC6;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
    public void clickC7(View view){
        int MOTOR = 0xC7;
        motor = (byte) (MOTOR); //declare which motor to assign
        int buttonIndex = findIndexFromMotor(MOTOR);
        button = findViewById(BUTTON_IDS[buttonIndex]);
        Log.d(TAG,"button index: " + buttonIndex);
        customCMD[1] = motor; //assign motor to command array
        isMotorOn[buttonIndex] = toggleButton(button,isMotorOn[buttonIndex]); //change button color and get current state
        Log.d(TAG," Motor:" + motor + "  IsMotorOn: " + isMotorOn[buttonIndex]);
        sendMotorCMD(isMotorOn[buttonIndex],motorIntensity,customCMD);
    }
}
