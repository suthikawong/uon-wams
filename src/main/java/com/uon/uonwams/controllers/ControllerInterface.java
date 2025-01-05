/**
 Program: UON WAMS Application
 Filename: ControllerInterface.java
 @author: © Suthika Wongsiridech
 Course: MSc Computing
 Module: Visual Object Software
 Tutor: Suraj Ajit
 */

package com.uon.uonwams.controllers;

public interface ControllerInterface {
    AppController appController = null;

    void setAppController(AppController appController);

    void setup();

}
