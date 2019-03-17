//Altair ZenPad Vertical Wave Prototype Script

const int L1 = 14;
const int C1 = 9;
const int R1 = 4;
const int L2 = 7;
const int C2 = 10;
const int R2 = 16;
const int L3 = 8;
const int C3 = 35;
const int R3 = 17;
const int L4 = 6;
const int C4 = 36;
const int R4 = 29;
const int L5 = 20;
const int C5 = 37;
const int R5 = 30;
const int L6 = 21;
const int C6 = 38;
const int R6 = 22;
const int L7 = 5;
const int C7 = 2;
const int R7 = 23;

void setup() {
  // put your setup code here, to run once:
  pinMode(L1, OUTPUT);
  pinMode(C1, OUTPUT);
  pinMode(R1, OUTPUT);
  pinMode(L2, OUTPUT);
  pinMode(C2, OUTPUT);
  pinMode(R2, OUTPUT);
  pinMode(L3, OUTPUT);
  pinMode(C3, OUTPUT);
  pinMode(R3, OUTPUT);
  pinMode(L4, OUTPUT);
  pinMode(C4, OUTPUT);
  pinMode(R4, OUTPUT);
  pinMode(L5, OUTPUT);
  pinMode(C5, OUTPUT);
  pinMode(R5, OUTPUT);
  pinMode(L6, OUTPUT);
  pinMode(C6, OUTPUT);
  pinMode(R6, OUTPUT);
  pinMode(L7, OUTPUT);
  pinMode(C7, OUTPUT);
  pinMode(R7, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
//array PWMspeed = [0, 255/2, 255*.75, 255];
//while 1{
//  for (int i =0; i=length(PWMspeed); i++){

    analogWrite(L1, 190);
    analogWrite(C1, 190);
    analogWrite(R1, 190);
    analogWrite(L7, 0);
    analogWrite(C7, 0);
    analogWrite(R7, 0);
    delay(1000);
    
    analogWrite(L2, 190);
    analogWrite(C2, 190);
    analogWrite(R2, 190);
    analogWrite(L1, 0);
    analogWrite(C1, 0);
    analogWrite(R1, 0);
    delay(1000);
    
    analogWrite(L3, 190);
    analogWrite(C3, 190);
    analogWrite(R3, 190);
    analogWrite(L2, 0);
    analogWrite(C2, 0);
    analogWrite(R2, 0);
    delay(1000);
    
    analogWrite(L4, 190);
    analogWrite(C4, 190);
    analogWrite(R4, 190);
    analogWrite(L3, 0);
    analogWrite(C3, 0);
    analogWrite(R3, 0);
    delay(1000);
    
    analogWrite(L5, 190);
    analogWrite(C5, 190);
    analogWrite(R5, 190);
    analogWrite(L4, 0);
    analogWrite(C4, 0);
    analogWrite(R4, 0);
    delay(1000);
    
    analogWrite(L6, 190);
    analogWrite(C6, 190);
    analogWrite(R6, 190);
    analogWrite(L5, 0);
    analogWrite(C5, 0);
    analogWrite(R5, 0);
    delay(1000);
    
    analogWrite(L7, 190);
    analogWrite(C7, 190);
    analogWrite(R7, 190);
    analogWrite(L6, 0);
    analogWrite(C6, 0);
    analogWrite(R6, 0);
    delay(1000);
    
  
//  }
//}
