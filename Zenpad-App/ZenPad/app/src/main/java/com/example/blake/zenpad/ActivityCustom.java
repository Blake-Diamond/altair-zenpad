package com.example.blake.zenpad;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static com.example.blake.zenpad.Settings3.gatt;
import static com.example.blake.zenpad.Settings3.kUartServiceUUID;
import static com.example.blake.zenpad.Settings3.kUartTxCharacteristicUUID;

public class ActivityCustom extends AppCompatActivity {
    public String TAG = "massage";
    final Intent custom_Intent = getIntent();
    Button buttonA1 = (Button) findViewById(R.id.floatingActionButtonA1);
    Button buttonA2 = (Button) findViewById(R.id.floatingActionButtonA2);
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
    Button buttonC7 = (Button) findViewById(R.id.floatingActionButtonC7);
    public boolean isToggled = true; //used for all Toggle Switches
    public boolean isToggledA1 = true;
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
    byte[] customCMD = {(byte)(0x01) ,(byte)(0x00), (byte)(0x00)};
    byte motorIntensity = (byte) (0x00); //some intensity between 0-255
    byte motorOff = (byte) (0x00); //intensity for OFF
    byte motor = (byte) (0x00);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        //writeCharacteristic(gatt); //writing dummy bytes from new activity
    }

    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_custom);
    }


    /*Write a function to get seekbar value/*
      Write a function that
     */

    //OnClick Method for Home Button
    public void toHome(View view){
        Intent presetMassage = new Intent(ActivityCustom.this, MainActivity.class);
        startActivity(presetMassage);
    }

    public boolean writeCharacteristic(BluetoothGatt gatt, byte[] byteArray){
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
    public Boolean toggleButton(Button button, Boolean isToggled){
        if (isToggled) { //Turning motor on
            button.setBackgroundColor(Color.RED);
            isToggled = false;
        }
        else{ //Turning Motor off
            button.setBackgroundColor(Color.GREEN);
            isToggled = true;
        }
        return isToggled;
    }

    //TODO: Scale onClick Methods
    //onClick Methods for each motor button
    public void clickA1(View view){
        //Toggle color function
        motor = (byte) (0xA1); //declare which motor to assign
        customCMD[1] = motor; //assign motor to command array
        Button button = buttonA1;
        Boolean motorState = toggleButton(button,isToggledA1); //change button color and get current state
        if (!motorState) { //if motor on, get intensity and send command!
            //TODO: get current seekbar value

            customCMD[2] = (byte) (0xBB); //dummy intensity value
            writeCharacteristic(gatt,customCMD);
        }
        else { //if motor off, tell motor to turn off!
            customCMD[2] = motorOff;
            writeCharacteristic(gatt,customCMD);
        }
    }

}
