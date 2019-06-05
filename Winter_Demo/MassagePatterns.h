//Altair ZenPad Vertical Wave Prototype Script

const int _A1 = 5;
const int _A2 = 21;
const int _A3 = 20;
const int _A4 = 6;
const int _A5 = 14;
const int _A6 = 7;
const int _A7 = 8;
const int _B1 = 2;
const int _B2 = 38;
const int _B3 = 37;
const int _B4 = 36;
const int _B5 = 9;
const int _B6 = 10;
const int _B7 = 35;
const int _C1 = 23;
const int _C2 = 22;
const int _C3 = 30;
const int _C4 = 29;
const int _C5 = 4;
const int _C6 = 16;
const int _C7 = 17;

void Set_pinsOutput() {
  //Sets PWM Pins as Outputs, called in setup of Winter_Demo
  pinMode(_A1, OUTPUT);
  pinMode(_A2, OUTPUT);
  pinMode(_A3, OUTPUT);
  pinMode(_A4, OUTPUT);
  pinMode(_A5, OUTPUT);
  pinMode(_A6, OUTPUT);
  pinMode(_A7, OUTPUT);
  pinMode(_B1, OUTPUT);
  pinMode(_B2, OUTPUT);
  pinMode(_B3, OUTPUT);
  pinMode(_B4, OUTPUT);
  pinMode(_B5, OUTPUT);
  pinMode(_B6, OUTPUT);
  pinMode(_B7, OUTPUT);
  pinMode(_C1, OUTPUT);
  pinMode(_C2, OUTPUT);
  pinMode(_C3, OUTPUT);
  pinMode(_C4, OUTPUT);
  pinMode(_C5, OUTPUT);
  pinMode(_C6, OUTPUT);
  pinMode(_C7, OUTPUT);
}


void Drive_motors() {
//
//    //analogWrite(L1, iValue);
//    analogWrite(L2, iValue);
//    analogWrite(L3, iValue);
//    analogWrite(L4, iValue);
//    analogWrite(L5, iValue);
//    analogWrite(L6, iValue);
//    analogWrite(A1, iValue);
//    analogWrite(R1, 0);
//    analogWrite(R2, 0);
//    analogWrite(R3, 0);
//    analogWrite(R4, 0);
//    analogWrite(R5, 0);
//    analogWrite(R6, 0);
//    analogWrite(R7, 0);
//    delay(500);
//    
//    analogWrite(C1, iValue);
//    analogWrite(C2, iValue);
//    analogWrite(C3, iValue);
//    analogWrite(C4, iValue);
//    analogWrite(C5, iValue);
//    analogWrite(C6, iValue);
//    analogWrite(C7, iValue);
//    //analogWrite(L1, 0);
//    analogWrite(L2, 0);
//    analogWrite(L3, 0);
//    analogWrite(L4, 0);
//    analogWrite(L5, 0);
//    analogWrite(L6, 0);
//    analogWrite(A1, 0);
//    delay(500);
//    
//    analogWrite(R1, iValue);
//    analogWrite(R2, iValue);
//    analogWrite(R3, iValue);
//    analogWrite(R4, iValue);
//    analogWrite(R5, iValue);
//    analogWrite(R6, iValue);
//    analogWrite(R7, iValue);
//    analogWrite(C1, 0);
//    analogWrite(C2, 0);
//    analogWrite(C3, 0);
//    analogWrite(C4, 0);
//    analogWrite(C5, 0);
//    analogWrite(C6, 0);
//    analogWrite(C7, 0);
//    delay(500);
}

void writeOneMotor(int iMotor, int iValue){
    if(iMotor==0xA1){analogWrite(_A1,iValue);}
    else if(iMotor==0xA2){analogWrite(_A2,iValue);}
    else if(iMotor==0xA3){analogWrite(_A3,iValue);}
    else if(iMotor==0xA4){analogWrite(_A4,iValue);}
    else if(iMotor==0xA5){analogWrite(_A5,iValue);}
    else if(iMotor==0xA6){analogWrite(_A6,iValue);}
    else if(iMotor==0xA7){analogWrite(_A7,iValue);}
    else if(iMotor==0xB1){analogWrite(_B1,iValue);}
    else if(iMotor==0xB2){analogWrite(_B2,iValue);}
    else if(iMotor==0xB3){analogWrite(_B3,iValue);}
    else if(iMotor==0xB4){analogWrite(_B4,iValue);}
    else if(iMotor==0xB5){analogWrite(_B5,iValue);}
    else if(iMotor==0xB6){analogWrite(_B6,iValue);}
    else if(iMotor==0xB7){analogWrite(_B7,iValue);}
    else if(iMotor==0xC1){analogWrite(_C1,iValue);}
    else if(iMotor==0xC2){analogWrite(_C2,iValue);}
    else if(iMotor==0xC3){analogWrite(_C3,iValue);}
    else if(iMotor==0xC4){analogWrite(_C4,iValue);}
    else if(iMotor==0xC5){analogWrite(_C5,iValue);}
    else if(iMotor==0xC6){analogWrite(_C6,iValue);}
    else if(iMotor==0xC7){analogWrite(_C7,iValue);}
    else{Serial.println("Not a recongnized motor value!");}
}

//void writeVerticalWave(int iValue){
//    analogWrite(_A1 ,iValue);
//    analogWrite(_B1, iValue);
//    analogWrite(_C1, iValue);
//    analogWrite(_A7, 0);
//    analogWrite(_B7, 0);
//    analogWrite(_C7, 0);
//    delay(500);
//    
//    analogWrite(_A2, iValue);
//    analogWrite(_B2, iValue);
//    analogWrite(_C2, iValue);
//    analogWrite(_A1, 0);
//    analogWrite(_B1, 0);
//    analogWrite(_C1, 0);
//    delay(500);
//    
//    analogWrite(_A3, iValue);
//    analogWrite(_B3, iValue);
//    analogWrite(_C3, iValue);
//    analogWrite(_A2, 0);
//    analogWrite(_B2, 0);
//    analogWrite(_C2, 0);
//    delay(500);
//    
//    analogWrite(_A4, iValue);
//    analogWrite(_B4, iValue);
//    analogWrite(_C4, iValue);
//    analogWrite(_A3, 0);
//    analogWrite(_B3, 0);
//    analogWrite(_C3, 0);
//    delay(500);
//    
//    analogWrite(_A5, iValue);
//    analogWrite(_B5, iValue);
//    analogWrite(_C5, iValue);
//    analogWrite(_A4, 0);
//    analogWrite(_B4, 0);
//    analogWrite(_C4, 0);
//    delay(500);
//    
//    analogWrite(_A6, iValue);
//    analogWrite(_B6, iValue);
//    analogWrite(_C6, iValue);
//    analogWrite(_A5, 0);
//    analogWrite(_B5, 0);
//    analogWrite(_C5, 0);
//    delay(500);
//    
//    analogWrite(_A7, iValue);
//    analogWrite(_B7, iValue);
//    analogWrite(_C7, iValue);
//    analogWrite(_A6, 0);
//    analogWrite(_B6, 0);
//    analogWrite(_C6, 0);
//    delay(500);
//}

void writeVerticalWave(int iValue){
    analogWrite(_A1 ,(0.33)*iValue);
    analogWrite(_B1, (0.33)*iValue);
    analogWrite(_C1, (0.33)*iValue);
    analogWrite(_A2 ,(0.67)*iValue);
    analogWrite(_B2, (0.67)*iValue);
    analogWrite(_C2, (0.67)*iValue);
    analogWrite(_A3 ,iValue);
    analogWrite(_B3, iValue);
    analogWrite(_C3, iValue);
    analogWrite(_A7, 0);
    analogWrite(_B7, 0);
    analogWrite(_C7, 0);
    delay(500);

    analogWrite(_A2 ,(0.33)*iValue);
    analogWrite(_B2, (0.33)*iValue);
    analogWrite(_C2, (0.33)*iValue);
    analogWrite(_A3 ,(0.67)*iValue);
    analogWrite(_B3, (0.67)*iValue);
    analogWrite(_C3, (0.67)*iValue);
    analogWrite(_A4 ,iValue);
    analogWrite(_B4, iValue);
    analogWrite(_C4, iValue);
    analogWrite(_A1, 0);
    analogWrite(_B1, 0);
    analogWrite(_C1, 0);
    delay(500);

    analogWrite(_A3 ,(0.33)*iValue);
    analogWrite(_B3, (0.33)*iValue);
    analogWrite(_C3, (0.33)*iValue);
    analogWrite(_A4 ,(0.67)*iValue);
    analogWrite(_B4, (0.67)*iValue);
    analogWrite(_C4, (0.67)*iValue);
    analogWrite(_A5 ,iValue);
    analogWrite(_B5, iValue);
    analogWrite(_C5, iValue);
    analogWrite(_A2, 0);
    analogWrite(_B2, 0);
    analogWrite(_C2, 0);
    delay(500);

   analogWrite(_A4 ,(0.33)*iValue);
    analogWrite(_B4, (0.33)*iValue);
    analogWrite(_C4, (0.33)*iValue);
    analogWrite(_A5 ,(0.67)*iValue);
    analogWrite(_B5, (0.67)*iValue);
    analogWrite(_C5, (0.67)*iValue);
    analogWrite(_A6,iValue);
    analogWrite(_B6, iValue);
    analogWrite(_C6, iValue);
    analogWrite(_A3, 0);
    analogWrite(_B3, 0);
    analogWrite(_C3, 0);
    delay(500);

    analogWrite(_A5 ,(0.33)*iValue);
    analogWrite(_B5, (0.33)*iValue);
    analogWrite(_C5, (0.33)*iValue);
    analogWrite(_A6 ,(0.67)*iValue);
    analogWrite(_B6, (0.67)*iValue);
    analogWrite(_C6, (0.67)*iValue);
    analogWrite(_A7 ,iValue);
    analogWrite(_B7, iValue);
    analogWrite(_C7, iValue);
    analogWrite(_A4, 0);
    analogWrite(_B4, 0);
    analogWrite(_C4, 0);
    delay(500);

    analogWrite(_A6 ,(0.33)*iValue);
    analogWrite(_B6, (0.33)*iValue);
    analogWrite(_C6, (0.33)*iValue);
    analogWrite(_A7 ,(0.67)*iValue);
    analogWrite(_B7, (0.67)*iValue);
    analogWrite(_C7, (0.67)*iValue);
    analogWrite(_A1 ,iValue);
    analogWrite(_B1, iValue);
    analogWrite(_C1, iValue);
    analogWrite(_A5, 0);
    analogWrite(_B5, 0);
    analogWrite(_C5, 0);
    delay(500);

    analogWrite(_A7 ,(0.33)*iValue);
    analogWrite(_B7, (0.33)*iValue);
    analogWrite(_C7, (0.33)*iValue);
    analogWrite(_A1 ,(0.67)*iValue);
    analogWrite(_B1, (0.67)*iValue);
    analogWrite(_C1, (0.67)*iValue);
    analogWrite(_A2 ,iValue);
    analogWrite(_B2, iValue);
    analogWrite(_C2, iValue);
    analogWrite(_A6, 0);
    analogWrite(_B6, 0);
    analogWrite(_C6, 0);
    delay(500);
  
}

void writeHorizontalWave(int iValue){
    
    analogWrite(_A1, iValue);
    analogWrite(_A2, iValue);
    analogWrite(_A3, iValue);
    analogWrite(_A4, iValue);
    analogWrite(_A5, iValue);
    analogWrite(_A6, iValue);
    analogWrite(_A7, iValue);
    analogWrite(_C1, 0);
    analogWrite(_C2, 0);
    analogWrite(_C3, 0);
    analogWrite(_C4, 0);
    analogWrite(_C5, 0);
    analogWrite(_C6, 0);
    analogWrite(_C7, 0);
    delay(500);
    
    analogWrite(_B1, iValue);
    analogWrite(_B2, iValue);
    analogWrite(_B3, iValue);
    analogWrite(_B4, iValue);
    analogWrite(_B5, iValue);
    analogWrite(_B6, iValue);
    analogWrite(_B7, iValue);
    analogWrite(_A1, 0);
    analogWrite(_A2, 0);
    analogWrite(_A3, 0);
    analogWrite(_A4, 0);
    analogWrite(_A5, 0);
    analogWrite(_A6, 0);
    analogWrite(_A7, 0);
    delay(500);
    
    analogWrite(_C1, iValue);
    analogWrite(_C2, iValue);
    analogWrite(_C3, iValue);
    analogWrite(_C4, iValue);
    analogWrite(_C5, iValue);
    analogWrite(_C6, iValue);
    analogWrite(_C7, iValue);
    analogWrite(_B1, 0);
    analogWrite(_B2, 0);
    analogWrite(_B3, 0);
    analogWrite(_B4, 0);
    analogWrite(_B5, 0);
    analogWrite(_B6, 0);
    analogWrite(_B7, 0);
    delay(500);
    
}

void writeStarburst(int iValue){
  //Starburst pattern
    
    analogWrite(_B4, iValue);
    analogWrite(_B3, iValue);
    analogWrite(_B5, iValue);
    analogWrite(_A4, iValue);
    analogWrite(_C4, iValue);
    analogWrite(_A1, 0);
    analogWrite(_B1, 0);
    analogWrite(_C1, 0);
    analogWrite(_A7, 0);
    analogWrite(_B7, 0);
    analogWrite(_C7, 0);

    delay(500);
    
    analogWrite(_A3, iValue);
    analogWrite(_C3, iValue);
    analogWrite(_A5, iValue);
    analogWrite(_C5, iValue);
    analogWrite(_B4, 0);
    analogWrite(_B3, 0);
    analogWrite(_B5, 0);
    analogWrite(_A4, 0);
    analogWrite(_C4, 0);
    
    delay(500);
    
    analogWrite(_A2, iValue);
    analogWrite(_B2, iValue);
    analogWrite(_C2, iValue);
    analogWrite(_A6, iValue);
    analogWrite(_B6, iValue);
    analogWrite(_C6, iValue);
    analogWrite(_A3, 0);
    analogWrite(_C3, 0);
    analogWrite(_A5, 0);
    analogWrite(_C5, 0);
    
    delay(500);

    analogWrite(_A1, iValue);
    analogWrite(_B1, iValue);
    analogWrite(_C1, iValue);
    analogWrite(_A7, iValue);
    analogWrite(_B7, iValue);
    analogWrite(_C7, iValue);
    analogWrite(_A2, 0);
    analogWrite(_B2, 0);
    analogWrite(_C2, 0);
    analogWrite(_A6, 0);
    analogWrite(_B6, 0);
    analogWrite(_C6, 0);

    delay(500);
  
}

void writeSnake(int iValue){
  //Snake pattern
    analogWrite(_A1, iValue);
    analogWrite(_A2, iValue);
    analogWrite(_B1, iValue);
    analogWrite(_B2, iValue);
    delay(500);

    analogWrite(_A1, 0);
    analogWrite(_A2, 0);
    analogWrite(_B1, 0);
    analogWrite(_B3, iValue);
    analogWrite(_C2, iValue);
    analogWrite(_C3, iValue);
    delay(500);

    analogWrite(_B2, 0);
    analogWrite(_C2, 0);
    analogWrite(_C3, 0);
    analogWrite(_B4, iValue);
    analogWrite(_A3, iValue);
    analogWrite(_A4, iValue);
    delay(500);

    analogWrite(_B3, 0);
    analogWrite(_A3, 0);
    analogWrite(_A4, 0);
    analogWrite(_B5, iValue);
    analogWrite(_C4, iValue);
    analogWrite(_C5, iValue);
    delay(500);

    analogWrite(_C5, 0);
    analogWrite(_C4, 0);
    analogWrite(_B4, 0);
    analogWrite(_B6, iValue);
    analogWrite(_A5, iValue);
    analogWrite(_A6, iValue);
    delay(500);
    
    analogWrite(_B5, 0);
    analogWrite(_A5, 0);
    analogWrite(_A6, 0);
    analogWrite(_B7, iValue);
    analogWrite(_C6, iValue);
    analogWrite(_C7, iValue);
    delay(500);

    analogWrite(_C7, 0);
    analogWrite(_C6, 0);
    analogWrite(_A6, iValue);
    analogWrite(_A7, iValue);
    delay(500); 
  
}

void stopAllMotors(){
    analogWrite(_A1, 0);
    analogWrite(_A2, 0);
    analogWrite(_A3, 0);
    analogWrite(_A4, 0);
    analogWrite(_A5, 0);
    analogWrite(_A6, 0);
    analogWrite(_A7, 0);
    analogWrite(_B1, 0);
    analogWrite(_B2, 0);
    analogWrite(_B3, 0);
    analogWrite(_B4, 0);
    analogWrite(_B5, 0);
    analogWrite(_B6, 0);
    analogWrite(_B7, 0);
   analogWrite(_C1, 0);
    analogWrite(_C2, 0);
    analogWrite(_C3, 0);
    analogWrite(_C4, 0);
    analogWrite(_C5, 0);
    analogWrite(_C6, 0);
    analogWrite(_C7, 0);
    
}
