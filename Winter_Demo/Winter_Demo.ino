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
char lastCMD[3];

 // Create the bluefruit object, either software serial...uncomment these lines
  SoftwareSerial bluefruitSS = SoftwareSerial(BLUEFRUIT_SWUART_TXD_PIN, BLUEFRUIT_SWUART_RXD_PIN);

  Adafruit_BluefruitLE_UART ble(bluefruitSS, BLUEFRUIT_UART_MODE_PIN,
                      BLUEFRUIT_UART_CTS_PIN, BLUEFRUIT_UART_RTS_PIN);
                      
void setup() {
  stopAllMotors();
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
  //Serial.print(ble.buffer[0],HEX); Serial.print(ble.buffer[1],HEX); Serial.print(ble.buffer[3],HEX);
  //template for receiving a particular command
  if (strcmp(ble.buffer, "OK") == 0) {
    // no data
    return;
  }

  // Some data was found, its in the buffer
  Serial.print(("[Recv] ")); 
  Serial.print("Last Command:");
  Serial.print(lastCMD[0],HEX);
  Serial.print(lastCMD[1],HEX);
  Serial.println(lastCMD[2],HEX);
  
  //Custom Massage
  if(ble.buffer[0] == 0x01 ||(ble.buffer[0] != 0x01 && lastCMD[0] == 0x01)){
      lastCMD[0] == ble.buffer[0];
      lastCMD[1] == ble.buffer[1];
      lastCMD[2] == ble.buffer[2];
      //Get Massage Information
      motor = ble.buffer[1];
      intensity = ble.buffer[2];
      
    if(ble.buffer[1] == 0xF0 ||(ble.buffer[1] != 0xF0 && lastCMD[0] == 0xF0)){
      //STOP BUTTON
      //TODO: Write a function that stops all motors
      Serial.println("Custom Massage STOP BUTTON");
      stopAllMotors();
    }
    else{
      //CUSTOM MOTOR CMD
      writeOneMotor(motor,intensity);
      //Printing to Serial Monitor for Debug
      Serial.print("Custom Massage   ");
      Serial.print("Motor: ");
      Serial.print(motor,HEX);
      Serial.print("  Intensity (0-255): "); 
      Serial.print(intensity);
      Serial.println(" ");
    }
  }
  //Preset Massage
  //add if statement so if its not ==2 and lastmassage == 2 then execute,
  //make a function that does the last command w/ similar command structure
  else if(ble.buffer[0] == 0x02){
      //Get Massage Information
      massageRoutine = ble.buffer[1];
      intensity = ble.buffer[2];
      
      //Printing to Serial Monitor for Debug
      Serial.print("Preset Massage   ");
      Serial.print("Massage: ");
      Serial.print(massageRoutine,HEX);
      Serial.print("  Intensity (0-255): "); 
      Serial.print(intensity);
      Serial.println(" ");
     
      if(ble.buffer[1] == 0x01){
        //Horizontal Wave
        Serial.println("Horizontal Wave");
        writeHorizontalWave(intensity);
        //return;
      }
      else if(ble.buffer[1] == 0x02){
        //Vertical Wave
        writeMassagePattern(massageRoutine,intensity);
        checkContinue(massageRoutine,intensity);
//        ble.readline();
//        while(ble.buffer[0] == 0x4F){
//          Serial.print("Readline ");
//          Serial.println(ble.buffer[0],HEX);
//          writeVerticalWave(intensity);
//          Serial.println("finished wave");
//          ble.readline();
//          ble.waitForOK();
//        }
      }
      else if(ble.buffer[1] == 0x03){
        //Starburst
        Serial.println("Starburst");
        writeStarburst(intensity);
        //return;
      }
      else if(ble.buffer[1] == 0x04){
        //Snake Pattern
        Serial.println("Snake Pattern");
        writeSnake(intensity);
        //return;
      }
      else if(ble.buffer[1] == 0xF0){
        //STOP
        Serial.println("STOP BUTTON");
        stopAllMotors();
      }
  }
  //Misc. info, currently unused
  else if(ble.buffer[0] == 0x03){
      Serial.println("Miscellanous");
  }
//  //Default
//  else{
//      Serial.println("Unrecognized Command");
//  }
  
 // ble.waitForOK(); //What does this do?
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

//check if there is new command, if not then continue the pattern
void checkContinue(int Pattern, int intensity){
    ble.readline();
    printBuffer();
    //buffer[0] == 0x4F if nothing is received, kinda buggy tbh
    while(ble.buffer[0] == 0x4F){
          Serial.print("buffer: ");
          printBuffer();
          writeMassagePattern(Pattern,intensity);
          Serial.print("... finished wave");
          ble.readline();
          
          Serial.print("  New Buffer Value:");
          printBuffer();
         // ble.waitForOK();
         //only accepts command when ble.buffer[0] == 0
    }
}

//writes any preset massage pattern, taking the pattern # and intensity as inputs
void writeMassagePattern(int Pattern, int intensity){
  if(Pattern == 1){writeHorizontalWave(intensity);
  Serial.println("Horizontal Wave");}
  else if(Pattern == 2){writeVerticalWave(intensity);
  Serial.println("Vertical Wave");}
  else if(Pattern == 3){writeStarburst(intensity);
  Serial.println("Starburst Wave");}
  else if(Pattern == 4){writeSnake(intensity);
  Serial.println("Snake Pattern");}
}

void printBuffer(){
  Serial.print(" 0x");
  Serial.print(ble.buffer[0],HEX); 
  Serial.print(" 0x");
  Serial.print(ble.buffer[1],HEX); 
  Serial.print(" 0x");
  Serial.println(ble.buffer[2],HEX);
}
