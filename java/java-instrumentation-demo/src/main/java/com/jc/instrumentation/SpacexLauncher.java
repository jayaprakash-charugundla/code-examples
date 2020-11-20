package com.jc.instrumentation;

public class SpacexLauncher {
	public static void main(String args[]) throws InterruptedException {
		Falcon l = new Falcon();
		l.launch();
	}
}
