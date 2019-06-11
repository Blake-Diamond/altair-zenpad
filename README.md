# UC Santa Barbara - ECE Deparrtment 
# EE Capstone 2018-2019


### Contributors: Evan Blasband, Blake Diamond, Adam Gulliver, Zack Klebanoff

https://capstone.engineering.ucsb.edu/projects/altair-zenpad

This repository contains all of our arduino and android studio code for Altair Zenpad, a smart massage pad that you can control from your fingertips. There are 3 imporant aspect of this respository that contiain all of the necessary reference documents for future development and use of the ZenPad, described below:

## Hardware

PCB/Altair Massage Pad: contains all PCB projects (EAGLE) for Main and Peripheral PCB.

## Firmware

Winter_Demo: This folder contains all of the firmware that is need to flash the Teensy 3.6 using the Arduino IDE. This includes all associated header files that support use of the Bluetooth and PMIC. Some small changes are needed in order to program a Teensy board with the Arduino IDE, which can be found here: https://www.pjrc.com/teensy/tutorial.html

## Software

Zenpad-App/ZenPad: This folder contains the software project for the ZenPad app, which can be accessed by downloading and opening with Android Studio. Someone with Android SW development experience is needed in order to properly open and run the app, which was developed on a Nexus 5 phone.
