package com.jc.java.assist.classloading;

public class Car {
    int speed;
    char driveType;

    public Car() {
    }

    public Car(int doors) {

    }

    public int drive(char driveType, int desiredSpeed) {
        selectDrive(driveType);

        while(speed != desiredSpeed) {
            accelerate();
        }
        return speed;
    }

    private int accelerate() {
        return ++speed;
    }

    private void selectDrive(char driveType) {
        this.driveType = driveType;
    }

    public static void print() {
        System.out.println("Hello from Car class...");
    }
}
