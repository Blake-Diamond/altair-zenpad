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
void ReadReg(int SlaveAddress, int Reg, int NumBytesRequest){
  byte RegRead;
  Wire.beginTransmission(SlaveAddress);
  Wire.write(Reg);
  Wire.requestFrom(SlaveAddress,NumBytesRequest);
  RegRead = Wire.read();
  Wire.endTransmission();
  Serial.println(RegRead);
}

void WriteReg(int SlaveAddress, int Reg, int WriteValue){
  Wire.beginTransmission(SlaveAddress);
  Wire.write(Reg);
  Wire.write(WriteValue);
  Wire.endTransmission();
}

//Set PMIC Registers to default values
void Set_PMICReg(){
  WriteReg(90,1,REG1_value);
  WriteReg(90,2,REG1_value);
  WriteReg(90,3,REG1_value);
  WriteReg(90,4,REG1_value);
  WriteReg(90,5,REG1_value);
  WriteReg(90,6,REG1_value);
  WriteReg(90,7,REG1_value);
  WriteReg(90,8,REG1_value);
}

// I2C Setup 
void Setup_i2c(){
  Wire.begin();
  Serial.begin(9600);     //speed that data is printed to serial display?
  Wire.setSDA(18);        //pin 18 on teensy
  Wire.setSCL(19);        //pin 19 on Teensy
  Wire.setClock(400000);  //400 kHz
}

void Print_allReg(){
    //Print Register set to Serial Monitor
  for( int i = 1; i < 9 ; i++){
  Serial.println(i);
  ReadReg(PMIC_addr,i,1);
  }
}
