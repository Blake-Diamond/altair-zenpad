#include <Wire.h>

void setup() {
  // put your setup code here, to run once:
  
  Wire.begin();
  Serial.begin(9600);
  Wire.setSDA(18);
  Wire.setSCL(19);
  Wire.setClock(400000);

  delay(3000);

  SetDefault();

}

void loop() {

}

int ReadReg(int SlaveAddress, int Reg, int NumBytesRequest){
  byte RegRead;
  Wire.beginTransmission(SlaveAddress);
  Wire.write(Reg);
  Wire.requestFrom(SlaveAddress,NumBytesRequest);
  RegRead = Wire.read();
  Wire.endTransmission();
  Serial.println(RegRead);
}

int WriteReg(int SlaveAddress, int Reg, int WriteValue){
  Wire.beginTransmission(SlaveAddress);
  Wire.write(Reg);
  Wire.write(WriteValue);
  Wire.endTransmission();
}

int SetDefault(){
  WriteReg(90,1,0);
  WriteReg(90,2,0);
  WriteReg(90,3,136);
  WriteReg(90,4,153);
  WriteReg(90,5,146);
  WriteReg(90,6,0);
  WriteReg(90,7,0);
  WriteReg(90,8,0);
}
