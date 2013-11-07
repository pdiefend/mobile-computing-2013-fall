package com.Plan13_Test;

import java.util.Scanner;

public class ImportTest {

	private static final boolean DEBUG = true;

	public static void main(String[] args) {
		String tle = "ISS (ZARYA)\n1 25544U 98067A   08264.51782528 -.00002182  00000-0 -11606-4 0  2927\n2 25544  51.6416 247.4627 0006703 130.5360 325.0288 15.72125391563537";

		if (DEBUG) {
			System.out.println("Working with:");
			System.out.println(tle + "\n");
		}

		Scanner scan = new Scanner(tle);
		String line = scan.nextLine();

		String friend = "";
		// filter out the first Line
		if (!line.startsWith("1")) {
			friend = line + ""; // F'ing Java
			line = scan.nextLine();
		}
		if (DEBUG) {
			System.out.println("Line 1:");
			System.out.println(line + "\n");
		}

		int satNum = Integer.parseInt(line.substring(2, 7)); // SatNum
		double YE = Double.parseDouble(line.substring(18, 20)); // Epoch Year
		double TE = Double.parseDouble(line.substring(20, 32)); // Epoch Day

		String m2 = "";
		for (int i = Integer.parseInt(line.substring(60, 61)); i > 0; i--) {
			m2 += "0";
		}
		m2 = line.substring(53, 54) + "0." + m2 + line.substring(54, 59);

		System.out.println("<================" + m2);

		double M2 = Double.parseDouble(m2); // M2 = BSTAR drag term

		// Get next Line
		line = scan.nextLine();
		scan.close();
		if (DEBUG) {
			System.out.println("Line 2:");
			System.out.println(line + "\n");
		}

		double IN = Double.parseDouble(line.substring(8, 16)); // Inclination
		double RA = Double.parseDouble(line.substring(17, 25)); // Right
																// Ascension
		double EC = Double.parseDouble("0." + line.substring(26, 33)); // Eccentricity
		double WP = Double.parseDouble(line.substring(34, 42)); // Argument of
																// Perigee
		double MA = Double.parseDouble(line.substring(43, 51)); // Mean Anomaly
		double MM = Double.parseDouble(line.substring(52, 63)); // Mean Motion
		double RV = Double.parseDouble(line.substring(63, 68)); // Revolution
																// Number
		double ALON = 180; // Satellite Attitude

		if (DEBUG) {
			System.out.println("Friendly Name: " + friend);
			System.out.println("satNum: " + satNum);
			System.out.println("YE: " + YE);
			System.out.println("TE: " + TE);
			System.out.println("M2: " + M2);
			System.out.println("IN: " + IN);
			System.out.println("RA: " + RA);
			System.out.println("EC: " + EC);
			System.out.println("WP: " + WP);
			System.out.println("MA: " + MA);
			System.out.println("MM: " + MM);
			System.out.println("RV: " + RV);
			System.out.println("ALON: " + ALON);
		}
	}
}
