package com.Plan13_Test;

/**
 * 
 * @author prd005
 * 
 *         https://code.google.com/p/qrptracker/source/browse/trunk/Arduino/
 *         libraries/Plan13/?r=37#Plan13%253Fstate%253Dclosed
 */
public class Plan13 {

	private final boolean DEBUG = false;
	private final boolean TEST = false;

	String TLE = "";

	double rx, tx;
	double observer_lon;
	double observer_lat;
	int observer_height;
	long rxOutLong;
	long txOutLong;
	long rxFrequencyLong;
	long txFrequencyLong;
	double dopplerFactor;
	final static double YM = 365.25; /* Days in a year */
	double EL; /* Elevation */
	double TN; /*                                    */

	double E;
	double N;
	double AZ;
	double SLON;
	double SLAT;
	double RR;

	double CL;
	double CS;
	double SL;
	double CO;
	double SO;
	double RE;
	double FL;
	double RP;
	double XX;
	double ZZ;
	double D;
	double R;
	double Rx;
	double Ry;
	double Rz;
	double Ex;
	double Ey;
	double Ez;
	double Ny;
	double Nx;
	double Nz;
	double Ox;
	double Oy;
	double Oz;
	double U;
	double Ux;
	double Uy;
	double Uz;
	final static double YT = 365.2421970;
	double WW;
	double WE;
	double W0;
	double VOx;
	double VOy;
	double VOz;
	double DE;
	double GM;
	double J2;
	double N0;
	double A0;
	double b0;
	double SI;
	double CI;
	double PC;
	double QD;
	double WD;
	double DC;
	double YG;
	double G0;
	double MAS0;
	double MASD;
	double INS;
	double CNS;
	double SNS;
	double EQC1;
	double EQC2;
	double TEG;
	double GHAE;
	double MRSE;
	double MASE;
	double ax;
	double ay;
	double az;
	int OLDRN;

	double T;
	double DT;
	double KD;
	double KDP;
	double M;
	int DR;
	long RN;
	double EA;
	double C;
	double S;
	double DNOM;
	double A;
	double B;
	double RS;
	double Sx;
	double Sy;
	// double Sz;
	double Vx;
	double Vy;
	double Vz;
	double AP;
	double CWw;
	double SW;
	double RAAN;
	double CQ;
	double SQ;
	double CXx;
	double CXy;
	double CXz;
	double CYx;
	double CYy;
	double CYz;
	double CZx;
	double CZy;
	double CZz;
	double SATx;
	double SATy;
	double SATz;
	double ANTx;
	double ANTy;
	double ANTz;
	double VELx;
	double VELy;
	double VELz;
	double Ax;
	double Ay;
	double Az;
	double Sz;
	// double Vz;
	double GHAA;

	double DS;
	double DF;

	/* keplerians */

	char[] SAT = new char[20];
	long SATNO;
	double YE;
	double TE;
	double IN;
	double RA;
	double EC;
	double WP;
	double MA;
	double MM;
	double M2;
	long RV;
	double ALON;
	double ALAT;
	double rxOut;
	double txOut;

	/* location */
	char[] LOC = new char[20];
	double LA;
	double LO;
	double HT;

	double HR; /* Hours */
	double DN;

	/**
	 * @param deg
	 * @return
	 */
	double rad(double deg) {
		return (Math.PI / 180.0 * deg);
	}

	/**
	 * @param rad
	 * @return
	 */
	double deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	/**
	 * @param y
	 * @param x
	 * @return
	 */
	double FNatn(double y, double x) {
		double a;
		if (x != 0) {
			a = Math.atan(y / x);
		} else {
			a = Math.PI / 2.0 * Math.sin(y);
		}
		if (x < 0) {
			a = a + Math.PI;
		}
		if (a < 0) {
			a = a + 2.0 * Math.PI;
		}
		return a;
	}

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	double FNday(double year, int month, int day) {
		double JulianDate;
		if (DEBUG) {
			System.out.print("## FNDay:  ");
			System.out.print("Year: ");
			System.out.print(year);
			System.out.print(" Month: ");
			System.out.print(month);
			System.out.print(" Day: ");
			System.out.print(day);
		}
		if (month <= 2) {
			year -= 1;
			month += 12;
		}

		JulianDate = (long) (year * YM) + (int) ((month + 1) * 30.6)
				+ (day - 428);
		if (DEBUG) {
			System.out.print(" JD: ");
			System.out.println(JulianDate);
		}
		return (JulianDate);
	}

	// double myFNday(int year, int month, int day, int uh, int um, int us);
	// double getElement(char *gstr, int gstart, int gstop);
	// void readElements(char *satellite);

	/**
	 * 
	 */
	void initSat() {
		// readElements(SAT); //now done beforehand

		/* Observer's location */
		if (DEBUG) {
			System.out.println("Start initSat()");
		}
		LA = rad(observer_lat);
		LO = rad(observer_lon);
		HT = ((double) observer_height) / 1000.0; // this needs to be in km
		CL = Math.cos(LA);
		SL = Math.sin(LA);
		CO = Math.cos(LO);
		SO = Math.sin(LO);
		/* WGS-84 Earth Ellipsoid */
		RE = 6378.137;
		FL = 1.0 / 298.257224;

		/* IAU-76 Earth Ellipsoid */
		// RE = 6378.140;
		// FL = 1.0 / 298.257;

		RP = RE * (1.0 - FL);
		XX = RE * RE;
		ZZ = RP * RP;

		D = Math.sqrt(XX * CL * CL + ZZ * SL * SL);

		Rx = XX / D + HT;
		Rz = ZZ / D + HT;

		/* Observer's unit vectors Up EAST and NORTH in geocentric coordinates */
		Ux = CL * CO;
		Ex = -SO;
		Nx = -SL * CO;

		Uy = CL * SO;
		Ey = CO;
		Ny = -SL * SO;

		Uz = SL;
		Ez = 0;
		Nz = CL;

		/* Observer's XYZ coordinates at earth's surface */
		Ox = Rx * Ux;
		Oy = Rx * Uy;
		Oz = Rz * Uz;

		/* Convert angles to radians, etc. */
		RA = rad(RA);
		IN = rad(IN);
		WP = rad(WP);
		MA = rad(MA);
		MM = MM * 2.0 * Math.PI;
		M2 = M2 * 2.0 * Math.PI;

		// YM = 365.25; /* Mean year, days */
		// YT = 365.2421970; /* Tropical year, days */
		WW = 2.0 * Math.PI / YT; /* Earth's rotation rate, rads/whole day */
		WE = 2.0 * Math.PI + WW; /* Earth's rotation rate, rads/day */
		W0 = WE / 86400; /* Earth's rotation rate, rads/sec */

		/* Observer's velocity, geocentric coordinates */
		VOx = -Oy * W0;
		VOy = Ox * W0;

		/* Convert satellite epoch to Day No. and fraction of a day */
		DE = FNday(YE, 1, 0) + (int) TE;

		TE = TE - (int) TE;
		if (DEBUG) {
			System.out.print("DE: ");
			System.out.println(DE);
			System.out.print("TE: ");
			System.out.println(TE);
		}
		/* Average Precession rates */
		GM = 3.986E5; /* Earth's gravitational constant km^3/s^2 */
		J2 = 1.08263E-3; /* 2nd Zonal coeff, Earth's gravity Field */
		N0 = MM / 86400.0; /* Mean motion rads/s */
		A0 = Math.pow(GM / N0 / N0, 1.0 / 3.0); /* Semi major axis km */
		b0 = A0 * Math.sqrt(1.0 - EC * EC); /* Semi minor axis km */
		SI = Math.sin(IN);
		CI = Math.cos(IN);
		PC = RE * A0 / (b0 * b0);
		PC = 1.5 * J2 * PC * PC * MM; /* Precession const, rad/day */
		QD = -PC * CI; /* Node Precession rate, rad/day */
		WD = PC * (5.0 * CI * CI - 1.0) / 2.0; /*
												 * Perigee Precession rate,
												 * rad/day
												 */
		DC = -2.0 * M2 / MM / 3.0; /* Drag coeff */

		/* Sideral and solar data. Never needs changing. Valid to year 2000+ */

		/* GHAA, Year YG, Jan 0.0 */
		YG = 2010;
		G0 = 99.5578;
		/* MA Sun and rate, deg, deg/day */
		MAS0 = 356.4485;
		MASD = 0.98560028;
		/* Sun's inclination */
		INS = rad(23.4380);
		CNS = Math.cos(INS);
		SNS = Math.sin(INS);
		/* Sun's equation of center terms */
		EQC1 = 0.03341;
		EQC2 = 0.00035;

		/* Bring Sun data to satellite epoch */
		TEG = (DE - FNday(YG, 1, 0)) + TE; /* Elapsed Time: Epoch - YG */
		GHAE = rad(G0) + TEG * WE; /* GHA Aries, epoch */
		MRSE = rad(G0) + (TEG * WW) + Math.PI; /* Mean RA Sun at Sat Epoch */
		MASE = rad(MAS0 + MASD * TEG); /* Mean MA Sun */

		/* Antenna unit vector in orbit plane coordinates */
		CO = Math.cos(rad(ALON));
		SO = Math.sin(rad(ALON));
		CL = Math.cos(rad(ALAT));
		SL = Math.sin(rad(ALAT));
		ax = -CL * CO;
		ay = -CL * SO;
		az = -SL;

		/* Miscellaneous */
		OLDRN = -99999;
		if (DEBUG) {
			System.out.println("End initSat()");
		}
	}

	/**
	 * 
	 */
	void satvec() {
		if (DEBUG) {
			System.out.println("Start satvec()");
		}
		T = (DN - DE) + (TN - TE);// 83.848 ; /* Elapsed T since epoch */
		if (DEBUG) {
			System.out.print("T: ");
			System.out.println(T);
		}
		DT = DC * T / 2.0; /* Linear drag terms */
		KD = 1.0 + 4.0 * DT;
		KDP = 1.0 - 7.0 * DT;
		M = MA + MM * T * (1.0 - 3.0 * DT); /* Mean anomaly at YR,/ TN */
		DR = (int) (M / (2.0 * Math.PI)); /* Strip out whole no of revs */
		M = M - DR * 2.0 * Math.PI; /* M now in range 0 - 2PI */
		RN = RV + DR + 1; /* Current orbit number */

		/* Solve M = EA - EC * sin(EA) for EA given M, by Newton's method */
		EA = M; /* Initail solution */
		do {
			C = Math.cos(EA);
			S = Math.sin(EA);
			DNOM = 1.0 - EC * C;
			D = (EA - EC * S - M) / DNOM; /* Change EA to better resolution */
			EA = EA - D; /* by this amount until converged */
		} while (Math.abs(D) > 1.0E-5);

		/* Distances */
		A = A0 * KD;
		B = b0 * KD;
		RS = A * DNOM;

		/* Calculate satellite position and velocity in plane of ellipse */
		Sx = A * (C - EC);
		Vx = -A * S / DNOM * N0;
		Sy = B * S;
		Vy = B * C / DNOM * N0;

		AP = WP + WD * T * KDP;
		CWw = Math.cos(AP);
		SW = Math.sin(AP);
		RAAN = RA + QD * T * KDP;
		CQ = Math.cos(RAAN);
		SQ = Math.sin(RAAN);

		/* Plane -> celestial coordinate transformation, [C] = [RAAN]*[IN]*[AP] */
		CXx = CWw * CQ - SW * CI * SQ;
		CXy = -SW * CQ - CWw * CI * SQ;
		CXz = SI * SQ;
		CYx = CWw * SQ + SW * CI * CQ;
		CYy = -SW * SQ + CWw * CI * CQ;
		CYz = -SI * CQ;
		CZx = SW * SI;
		CZy = CWw * SI;
		CZz = CI;

		/* Compute satellite's position vector, ANTenna axis unit vector */
		/* and velocity in celestial coordinates. (Note: Sz = 0, Vz = 0) */
		SATx = Sx * CXx + Sy * CXy;
		ANTx = ax * CXx + ay * CXy + az * CXz;
		VELx = Vx * CXx + Vy * CXy;
		SATy = Sx * CYx + Sy * CYy;
		ANTy = ax * CYx + ay * CYy + az * CYz;
		VELy = Vx * CYx + Vy * CYy;
		SATz = Sx * CZx + Sy * CZy;
		ANTz = ax * CZx + ay * CZy + az * CZz;
		VELz = Vx * CZx + Vy * CZy;

		/* Also express SAT, ANT, and VEL in geocentric coordinates */
		GHAA = GHAE + WE * T; /* GHA Aries at elaprsed time T */
		C = Math.cos(-GHAA);
		S = Math.sin(-GHAA);
		Sx = SATx * C - SATy * S;
		Ax = ANTx * C - ANTy * S;
		Vx = VELx * C - VELy * S;
		Sy = SATx * S + SATy * C;
		Ay = ANTx * S + ANTy * C;
		Vy = VELx * S + VELy * C;
		Sz = SATz;
		Az = ANTz;
		Vz = VELz;
		if (DEBUG) {
			System.out.println("End satvec()");
		}
	}

	/**
	 * 
	 */
	void rangevec() {
		if (DEBUG) {
			System.out.println("Start rangevec()");
		}
		/* Range vector = sat vector - observer vector */
		Rx = Sx - Ox;
		Ry = Sy - Oy;
		Rz = Sz - Oz;

		R = Math.sqrt(Rx * Rx + Ry * Ry + Rz * Rz); /* Range Magnitute */

		/* Normalize range vector */
		Rx = Rx / R;
		Ry = Ry / R;
		Rz = Rz / R;
		U = Rx * Ux + Ry * Uy + Rz * Uz;
		E = Rx * Ex + Ry * Ey;
		N = Rx * Nx + Ry * Ny + Rz * Nz;

		AZ = deg(FNatn(E, N));
		EL = deg(Math.asin(U));

		/* Solve antenna vector along unit range vector, -r.a = cos(SQ) */
		/*
		 * SQ = deg(acos(-(Ax * Rx + Ay * Ry + Az * Rz)));
		 */
		/* Calculate sub-satellite Lat/Lon */
		SLON = deg(FNatn(Sy, Sx)); /* Lon, + East */
		SLAT = deg(Math.asin(Sz / RS)); /* Lat, + North */

		/* Resolve Sat-Obs velocity vector along unit range vector. (VOz = 0) */
		RR = (Vx - VOx) * Rx + (Vy - VOy) * Ry + Vz * Rz; /* Range rate, km/sec */
		// FR = rxFrequency * (1 - RR / 299792);
		dopplerFactor = RR / 299792.0;
		int rxDoppler = getDoppler(rxFrequencyLong);
		int txDoppler = getDoppler(txFrequencyLong);
		rxOutLong = rxFrequencyLong - rxDoppler;
		txOutLong = txFrequencyLong + txDoppler;

		if (DEBUG) {
			System.out.println("End rangevec()");
		}
	}

	/**
	 * TODO
	 */
	void sunvec() {
		// TODO
		;
	}

	/**
	 * 
	 */
	void calculate() {
		initSat();
		satvec();
		rangevec();
	}

	/*
	 * void footprintOctagon(double *octagon, double SLATin, double SLONin,
	 * double REin, double RAin){ //static float points[16];
	 * Serial.print("SLAT: "); Serial.print(SLATin); Serial.print(", SLON: ");
	 * Serial.print(SLONin); Serial.print(", RE: "); Serial.print(REin);
	 * Serial.print(", RS: "); Serial.println(RSin); float srad =
	 * acos(REin/RSin); // Beta in Davidoff diag. 13.2, this is in rad
	 * Serial.print("srad: "); Serial.println(srad); float cla=
	 * cos(rad(SLATin)); float sla = sin(rad(SLATin)); float clo =
	 * cos(rad(SLONin)); float slo = sin(rad(SLONin)); float sra = sin(srad);
	 * float cra = cos(srad); for (int i = 0; i < 16; i = i +2) { float a = 2 *
	 * M_PI * i / 16; Serial.print("\ta: "); Serial.println(a); float X = cra;
	 * Serial.print("\t first X: "); Serial.println(X);
	 * Serial.print("\t first Y: "); float Y = sra*sin(a); Serial.println(Y);
	 * float Z = sra*cos(a); Serial.print("\t first Z: "); Serial.println(Z);
	 * float x = X*cla - Z*sla; float y = Y; float z = X*sla + Z*cla; X = x*clo
	 * - y*slo; Serial.print("\tX: "); Serial.println(X); Y = x*slo + y*clo;
	 * Serial.print("\tY: "); Serial.println(Y); Z = z; Serial.print("\tZ: ");
	 * Serial.println(Z); points[i] = deg(FNatn(Y,X));
	 * Serial.print("\t Long: "); Serial.print(points[i]); points[i+1] =
	 * deg(asin(Z)); Serial.print("\t Lat: "); Serial.println(points[i+1]); } }
	 */

	/**
	 * 
	 */
	void printdata() {
		System.out.print("AZ:");
		System.out.print(AZ);
		System.out.print(" EL: ");
		System.out.print(EL);
		System.out.print(" RX: ");
		System.out.print(rxOutLong);
		System.out.print(" TX: ");
		System.out.print(txOutLong);
		System.out.print(" Sat Lat: ");
		System.out.print(SLAT);
		System.out.print(" Sat Lon.: ");
		System.out.print(SLON);
		System.out.print(" RR: ");
		System.out.print(RR);

		/**
		 * Setter method for uplink (tx) and downlink (rx) frequencies in Hz.
		 * These data need not be set if the doppler shifted frequencies are not
		 * needed.
		 * 
		 * \param rxFrequncy_in the downlink frequency \param txFrequency_in the
		 * uplink frequency
		 **/
	}

	/**
	 * @param rxFrequency_in
	 * @param txFrequency_in
	 */
	void setFrequency(long rxFrequency_in, long txFrequency_in) {
		rxFrequencyLong = rxFrequency_in;
		txFrequencyLong = txFrequency_in;
		if (TEST) {
			rxFrequencyLong = 435300000L;
			txFrequencyLong = 45920000L;
		}
	}

	/**
	 * @param lon
	 *            east is positive
	 * @param lat
	 *            north is positive
	 * @param height
	 *            altitude
	 */
	void setLocation(double lon, double lat, int height) {
		observer_lon = lon;// -64.375; //0.06; // lon east is
							// positive, west is negative
		observer_lat = lat;// 45.8958; //52.21; //Cambridge UK
		observer_height = height; // 60m height in meters
		if (TEST) {
			observer_lon = -64.375;
			observer_lat = 45.8958;
			observer_height = 60;
		}
	}

	/**
	 * @param yearIn
	 *            year
	 * @param monthIn
	 *            month
	 * @param mDayIn
	 *            day
	 * @param hourIn
	 *            hour (24 military)
	 * @param minIn
	 *            minute
	 * @param secIn
	 *            second
	 */
	void setTime(int yearIn, int monthIn, int mDayIn, int hourIn, int minIn,
			int secIn) {
		if (DEBUG) {
			System.out.println("Start setTime()");
		}
		int aYear = yearIn;
		int aMonth = monthIn;
		int aMday = mDayIn;
		int aHour = hourIn;
		int aMin = minIn;
		int aSec = secIn;
		if (TEST) {
			aYear = 2009;
			aMonth = 10;
			aMday = 1;
			aHour = 19;
			aMin = 5;
			aSec = 0;
		}

		DN = FNday(aYear, aMonth, aMday);
		TN = ((float) aHour + ((float) aMin + ((float) aSec / 60.0)) / 60.0) / 24.0;
		DN = (long) DN;
		if (DEBUG) {
			System.out.print(aYear);
			System.out.print("/");
			System.out.print(aMonth);
			System.out.print("/");
			System.out.print(aMday);
			System.out.print(" ");
			System.out.print(aHour);
			System.out.print(":");
			System.out.print(aMin);
			System.out.print(":");
			System.out.print(aSec);
			System.out.print(" ");
			System.out.print("DN: ");
			System.out.println(DN);
			System.out.print("TN: ");
			System.out.println(TN);
			System.out.println("End setTime()");
		}
	}

	/**
	 * @param tle
	 *            Two-Line Element
	 */
	void setElements(String tle) {
		System.err.println("Not Yet Implemented");
		System.exit(-1);

		this.TLE = tle;

		/*
		 * ISS (ZARYA) 1 25544U 98067A 08264.51782528 -.00002182 00000-0
		 * -11606-4 0 2927 2 25544 51.6416 247.4627 0006703 130.5360 325.0288
		 * 15.72125391563537
		 */
		// TODO
	}

	/**
	 * Check all these descriptions
	 * http://www.amsat.org/amsat/articles/g3ruh/111.html
	 * 
	 * @param YE_in
	 *            Year epoch? <===================
	 * @param TE_in
	 *            Time epoch? <===================
	 * @param IN_in
	 *            Inclination [Degrees] (L2 9-16)
	 * @param RA_in
	 *            Right Ascension (L2 18-25)
	 * @param EC_in
	 *            Eccentricity (L2 27-33)
	 * @param WP_in
	 *            Arguement of Perigee [Degrees] (L2 35-42)
	 * @param MA_in
	 *            Mean Anomlay [Degrees] (L2 44-51)
	 * @param MM_in
	 *            Mean Motion [Revs/day] (L2 53-63)
	 * @param M2_in
	 * @param RV_in
	 *            Revolution number at epoch [Revs] (L2 64-68)
	 * @param ALON_in
	 */
	void setElements(double YE_in, double TE_in, double IN_in, double RA_in,
			double EC_in, double WP_in, double MA_in, double MM_in,
			double M2_in, long RV_in, double ALON_in) {
		YE = YE_in;
		TE = TE_in;
		IN = IN_in;
		RA = RA_in;
		EC = EC_in;
		WP = WP_in;
		MA = MA_in;
		MM = MM_in;
		M2 = M2_in;
		RV = RV_in;
		ALON = ALON_in;

		if (TEST) {// sample elements for AO-51
			YE = 1997.0;
			TE = 126.71404377;
			IN = 98.5440;
			RA = 201.9624;
			EC = 0.0009615;
			WP = 356.5498;
			MA = 3.5611;
			MM = 14.27977735;
			M2 = 2.500E-07;
			RV = 18818;
			ALON = 180.0;
		}
	}

	/**
	 * @param freq
	 * @return
	 */
	int getDoppler(long freq) {
		freq = (freq + 50000L) / 100000L;
		double factor = dopplerFactor * 1E11;
		int digit;
		double tally = 0;
		for (int x = 4; x > -1; x--) {
			digit = (int) (freq / Math.pow(10, x));
			long bare = (long) (digit * Math.pow(10, x));
			freq = freq - bare;
			double inBetween = factor * ((double) (bare) / 1E6);
			tally += inBetween;
		}
		return (int) (tally + .5); // round
	}

	/**
	 * @param freq
	 * @return
	 */
	int getDoppler64(long freq) {
		@SuppressWarnings("unused")
		double factor = dopplerFactor * 1E11;
		double doppler_sixfour = freq * dopplerFactor;
		return (int) (doppler_sixfour / 1E11);
	}

	double getAzimuth() {
		return this.AZ;
	}

	double getElevation() {
		return this.EL;
	}

	String getTLE() {
		return this.TLE;
	}
}
