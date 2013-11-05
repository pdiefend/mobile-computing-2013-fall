package com.Plan13_Test;

public class Plan13_Test {
	// public static final double ONEPPM = 1.0e-6;
	// public static final boolean DEBUG = true;

	public static void main(String[] args) {
		Plan13 p13 = new Plan13();
		p13.setFrequency(435300000, 145920000);// AO-51 frequency
		p13.setLocation(-64.375, 45.8958, 20); // Sackville, NB
		// Serial.begin(38400);

		p13.setTime(2009, 10, 1, 19, 5, 0); // Oct 1, 2009 19:05:00 UTC
		p13.setElements(2009, 232.55636497, 98.0531, 238.4104, 83652 * 1.0e-7,
				290.6047, 68.6188, 14.406497342, -0.00000001, 27022, 180.0); // fairly
																				// recent
																				// keps
																				// for
																				// AO-51
																				// //readElements();
		p13.initSat();
		p13.satvec();
		p13.rangevec();
		p13.printdata();
		System.out.println("\n");
		System.out.print("Should be: ");
		System.out.println("AZ:57.07 EL: 4.05 RX 435301728 TX 145919440");
	}
}
