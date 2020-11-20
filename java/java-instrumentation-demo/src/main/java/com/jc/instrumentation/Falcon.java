package com.jc.instrumentation;

public class Falcon {
	//Ideally if we travel with speed of light, on an average it will take 12.5 minutes to reach mars
	public void launch() throws InterruptedException {
		System.out.println("Falcon has launched successfully");
		Thread.sleep(2000L);
		System.out.println("Falcon has reached Mars");
	}
}
