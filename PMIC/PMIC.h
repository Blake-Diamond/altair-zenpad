// header file for PMIC that defines constants

//default register settings (decimal)
#define REG1_value 0
#define REG2_value 0
#define REG3_value 136
#define REG4_value 153
#define REG5_value 146
#define REG6_value 0
#define REG7_value 0
#define REG8_value 0

//i2c settings
#define SDA_pin 18
#define SCL_pin 19
#define Clock_freq 100000 //100 khz
#define PMIC_addr 90      //7bit address of PMIC

//Read & Write Register Functions
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
