#include <Wire.h>
#include <Arduino.h>
#include <SPI.h>

#include "Adafruit_BLE.h"
#include "Adafruit_BluefruitLE_SPI.h"
#include "Adafruit_BluefruitLE_UART.h"

#include "PMIC.h"
#include "BluefruitConfig.h"
#include "MassagePatterns.h"

#if SOFTWARE_SERIAL_AVAILABLE
  #include <SoftwareSerial.h>
#endif

int motor; //which motor we are controlling
int massageRoutine;
int intensity; //vibration intensity at a particular motor

 // Create the bluefruit object, either software serial...uncomment these lines
  SoftwareSerial bluefruitSS = SoftwareSerial(BLUEFRUIT_SWUART_TXD_PIN, BLUEFRUIT_SWUART_RXD_PIN);

  Adafruit_BluefruitLE_UART ble(bluefruitSS, BLUEFRUIT_UART_MODE_PIN,
                      BLUEFRUIT_UART_CTS_PIN, BLUEFRUIT_UART_RTS_PIN);
                      
void setup() {
  Setup_i2c();              //i2c configuration
  delay(1000);              //wait 1 second
  Set_PMICReg();            //set PMIC Registers to Default Values

  ReadReg(PMIC_addr,1,1);   //garbage reading 
  Print_allReg();           //print all PMIC Registers to Serial Monitor - need to clean this up
  
  //Need to come up with function that checks the major errors in the PMIC using bitmasking

  Setup_bluetooth();         //Configure bluetooth module

  Set_pinsOutput();          //Set PWM pins as output on Teensy 3.6
  
  
}


void loop(void)
{
  // Check for user input
  char inputs[BUFSIZE+1];

  if ( getUserInput(inputs, BUFSIZE) )
  {
    // Send characters to Bluefruit
    Serial.print("[Send] ");
    Serial.println(inputs);

    ble.print("AT+BLEUARTTX=");
    ble.println(inputs);

    // check response stastus
    if (! ble.waitForOK() ) {
      Serial.println(F("Failed to send?"));
    }
  }

  // Check for incoming characters from Bluefruit
  ble.println("AT+BLEUARTRX");
  ble.readline();

  //template for receiving a particular command
  if (strcmp(ble.buffer, "OK") == 0) {
    // no data
    return;
  }

  // Some data was found, its in the buffer
  Serial.print(("[Recv] ")); 

  //Custom Massage
  if(ble.buffer[0] == 0x01){
      Serial.print("Custom Massage   ");
      motor = ble.buffer[1];
      intensity = ble.buffer[2];
      Serial.print("Motor: ");
      Serial.print(motor,HEX);
      Serial.print("  Intensity (0-255): "); 
      Serial.print(intensity);
      Serial.println(" ");
  }
  //Preset Massage
  else if(ble.buffer[0] == 0x02){
      Serial.print("Preset Massage   ");
      massageRoutine = ble.buffer[1];
      intensity = ble.buffer[2];
      Serial.print("Massage: ");
      Serial.print(massageRoutine,HEX);
      Serial.print("  Intensity (0-255): "); 
      Serial.print(intensity);
      Serial.println(" ");
  }
  //Misc. info, currently unused
  else if(ble.buffer[0] == 0x03){
      Serial.println("Miscellanous");
  }
  //Default
  else{
      Serial.println("Unrecognized Command");
  }
  

// DEPRECATED 
//  //catches dummy command from android app
//  if(ble.buffer[0] == 0xFF){
//    for (int i = 0; i < sizeof(ble.buffer); i++) {
//      Serial.println(ble.buffer[i],HEX);
//      if (ble.buffer[i] == 0x12) {
//        break;
//      }
//
//      
//    }
//    Serial.println("FF Received!");
//    
//    Serial.println(ble.buffer[2],HEX);
//  }
// 
//     if (strcmp(ble.buffer, "START") == 0) {
//      Serial.println("Start Recived");
////      Drive_motors();
////      startFlag = 1;
//      analogWrite(R6, 190);
//      delay(1000);
//
//     }
//      
//
//      if (strcmp(ble.buffer, "Stop") == 0) {
//      Serial.println("Stop Recived");
//      analogWrite(R6, 0);
//  }

  
  ble.waitForOK();
}




/* 
 *  @brief   sets up bluetooth communication and configuration to serial monitor
 *  @param   none
 *  @return  none
*/
void Setup_bluetooth(void)
{

  while (!Serial);  // required for Flora & Micro
  delay(500);
  
//  Serial.begin(9600);
  Serial.begin(115200);
  Serial.println(F("Adafruit Bluefruit Command Mode Example"));
  Serial.println(F("---------------------------------------"));

  /* Initialise the module */
  Serial.print(F("Initialising the Bluefruit LE module: "));

  if ( !ble.begin(VERBOSE_MODE) )
  {
    error(F("Couldn't find Bluefruit, make sure it's in CoMmanD mode & check wiring?"));
  }
  Serial.println( F("OK!") );

  if ( FACTORYRESET_ENABLE )
  {
    /* Perform a factory reset to make sure everything is in a known state */
    Serial.println(F("Performing a factory reset: "));
    if ( ! ble.factoryReset() ){
      error(F("Couldn't factory reset"));
    }
  }

  /* Disable command echo from Bluefruit */
  ble.echo(false);

  Serial.println("Requesting Bluefruit info:");
  /* Print Bluefruit information */
  ble.info();

  Serial.println(F("Please use Adafruit Bluefruit LE app to connect in UART mode"));
  Serial.println(F("Then Enter characters to send to Bluefruit"));
  Serial.println();

  ble.verbose(false);  // debug info is a little annoying after this point!

  /* Wait for connection */
  while (! ble.isConnected()) {
      delay(500);
  }

  // LED Activity command is only supported from 0.6.6
  if ( ble.isVersionAtLeast(MINIMUM_FIRMWARE_VERSION) )
  {
    // Change Mode LED Activity
    Serial.println(F("******************************"));
    Serial.println(F("Change LED activity to " MODE_LED_BEHAVIOUR));
    ble.sendCommandCheckOK("AT+HWModeLED=" MODE_LED_BEHAVIOUR);
    Serial.println(F("******************************"));
  }
}
