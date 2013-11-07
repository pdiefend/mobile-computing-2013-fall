package com.Plan13_Test;

public class Plan13_Test {
	// public static final double ONEPPM = 1.0e-6;
	// public static final boolean DEBUG = true;

	public static void main(String[] args) {
		Plan13 p13 = new Plan13();
		// prd005: Don't need
		// p13.setFrequency(435300000, 145920000);// AO-51 frequency

		p13.setLocation(-76.886064, 40.972975, 134); // Bucknell University

		// prd005: Don't need
		// Serial.begin(38400);

		p13.setTime(2013, 11, 7, 21, 53, 0);
		// p13.setElements(2009, 232.55636497, 98.0531, 238.4104, 83652 *
		// 1.0e-7,
		// 290.6047, 68.6188, 14.406497342, -0.00000001, 27022, 180.0);
		// p13.setElements("ISS\n1 25544U 98067A   13104.86288288  .00014856  00000-0  24654-3 0  6129\n2 25544  51.6469  62.5942 0010689 141.3178 311.5110 15.52291388824875");
		p13.setElements("ISS\n1 25544U 98067A   13310.51629608  .00016717  00000-0  10270-3 0  9002\n2 25544  51.6440 123.1754 0001219 117.1504 242.9771 15.49860824 16788");

		/*
		 * 1 25544U 98067A 13310.51629608 .00016717 00000-0 10270-3 0 9002 2
		 * 25544 51.6440 123.1754 0001219 117.1504 242.9771 15.49860824 16788"
		 */

		/*
		 * TLE1 =
		 * "1 25544U 98067A   13104.86288288  .00014856  00000-0  24654-3 0  6129"
		 * TLE2 =
		 * "2 25544  51.6469  62.5942 0010689 141.3178 311.5110 15.52291388824875"
		 */

		p13.initSat();
		p13.satvec();
		p13.rangevec();
		p13.printdata();
		System.out.println("\n");
		System.out.println("AZ: " + p13.getAzimuth() + ", EL: "
				+ p13.getElevation());
		// System.out.print("Should be: ");
		// System.out.println("AZ:57.07 EL: 4.05");
	}
}
