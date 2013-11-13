package sgp;

/**
 * Class creates an English user-friendly API that utilizes the SGP program
 * created by Pedro J. Fern�ndez and Dr TS Kelso for the calculation of the
 * relative location of RSOs from a location using the RSO's TLE.
 * 
 * Some pieces of the code are still in Spanish, but the necessary pieces were
 * translated to English or have obvious enough translations.
 * 
 * This code was originally meant to replicate
 * <ul>
 * heavens-above.com
 * </ul>
 * but has since been modified to allow for relative pointing vector computation
 * of RSOs given their TLEs. The code for the heavens-above replication is in
 * SGP_PassFinderISS.java and is included
 * 
 * @author prd005
 */
public class SGP_API {
	/**
	 * The RSO being tracked
	 */
	private SGP_Satellite sat;
	/**
	 * The Two Line Element of the RSO
	 */
	private SGP_TLE TLE;
	/**
	 * The location of the observer
	 */
	private SGP_Location loc;

	/**
	 * Constructs an API object that can be used for the calculation of an RSO's
	 * relative location
	 * 
	 * @param tle
	 *            The Two Line Element in SGP_TLE form
	 * @param loc
	 *            The location of the observer
	 */
	public SGP_API(SGP_TLE tle, SGP_Location loc) {
		this.TLE = tle;
		this.loc = loc;
		this.sat = new SGP_Satellite(TLE);
	}

	/**
	 * Calculates the RSO's relative position to the set location using the
	 * parameter time.
	 * 
	 * @param time
	 *            the time to be used in the calculation
	 */
	public void calculatePosition(SGP_Timestamp time) {
		// this is how I fix the one month offset to the User
		time.setValues(time.yr, time.mo - 1, time.dy, time.hr, time.mi,
				time.se, time.hu);

		// convert the parameter time to UTC (necessary for calculations)
		SGP_Time.localToUniversalTime(time, loc.offsetUTC);
		// calculate the Sun's position, necessary for visibility calculations
		SGP_Sun.calcularPosicion(time);
		// perform the calculations to find the relative location of the
		// satellite
		loc.calcularVariables(time);
		loc.calcularPosicionSol(time);
		sat.calcularVariables(time);
		sat.calcularPosicionSatelite(loc, time);
	}

	/**
	 * Calculates the RSO's relative position to the set location using the
	 * current time.
	 */
	public void calculatePosition() {
		// Get the current time in UTC
		SGP_Timestamp time = SGP_Time.getCurrentUniversalTime(loc.offsetUTC);
		// calculate the Sun's position, necessary for visibility calculations
		SGP_Sun.calcularPosicion(time);
		// perform the calculations to find the relative location of the
		// satellite
		loc.calcularVariables(time);
		loc.calcularPosicionSol(time);
		sat.calcularVariables(time);
		sat.calcularPosicionSatelite(loc, time);
	}

	/**
	 * Gets the Azimuth angle for the RSO's pointing vector
	 * 
	 * @return the relative azimuth (or compass heading of the RSO's relative
	 *         pointing vector)
	 * 
	 */
	public double getAzimuth() {
		return sat.azimut;
	}

	/**
	 * Gets the Elevation angle for the RSO's pointing vector
	 * 
	 * @return the relative elevation (or angle from the horizon of the RSO's
	 *         relative pointing vector)
	 */
	public double getElevation() {
		return sat.elevacion;
	}

	/**
	 * Gets whether of not the RSO is Visible
	 * 
	 * @return true is the RSO is visible
	 */
	public boolean isVisible() {
		return sat.visible;
	}

	// These functions are really only for testing but they are provided in case
	// the user needs them
	/**
	 * Gets the Current Local Time*
	 * 
	 * <b>*WARNING:</b> This software needs the month to be decremented by 1 to
	 * operate due to Time conversions. Using Julian time which is decimal
	 * years, January is zero total months elapsed so far. Because of this the
	 * returned time will be off by one month.
	 * 
	 * @return the current time
	 */
	public SGP_Timestamp getCurrentLocalTime() {
		return SGP_Time.getCurrentLocalTime();
	}

	/**
	 * Gets the Current UTC*
	 * 
	 * <b>*WARNING:</b> This software needs the month to be decremented by 1 to
	 * operate due to Time conversions. Using Julian time which is decimal
	 * years, January is zero total months elapsed so far. Because of this the
	 * returned time will be off by one month.
	 * 
	 * @return the current time in UTC format
	 */
	public SGP_Timestamp getCurrentUTC() {
		return SGP_Time.getCurrentUniversalTime(loc.offsetUTC);
	}

	/**
	 * Gets the SGP_Satellite object used for calculations. You should have a
	 * good reason for needing this.
	 * 
	 * @return the RSO object
	 */
	public SGP_Satellite getSat() {
		return sat;
	}

	/**
	 * Manually sets the SGP_Satellite object used for calculations. You should
	 * have a good reason for using this.
	 * 
	 * @param sat
	 *            the sat to set
	 */
	public void setSat(SGP_Satellite sat) {
		this.sat = sat;
	}

	/**
	 * Gets the SGP_TLE object used for calculations. You had better have a good
	 * reason for needing this.
	 * 
	 * @return the TLE
	 */
	public SGP_TLE getTLE() {
		return TLE;
	}

	/**
	 * Manually sets the SGP_TLE object used for calculations. You should have a
	 * good reason for using this.
	 * 
	 * @param tLE
	 *            the tLE to set
	 */
	public void setTLE(SGP_TLE tLE) {
		TLE = tLE;
	}

	/**
	 * Gets the SGP_Location object used for calculations. You had better have a
	 * good reason for needing this.
	 * 
	 * @return the Location
	 */
	public SGP_Location getLoc() {
		return loc;
	}

	/**
	 * Manually sets the SGP_Location object used for calculations. You should
	 * have a good reason for using this.
	 * 
	 * @param loc
	 *            the Location to set
	 */
	public void setLoc(SGP_Location loc) {
		this.loc = loc;
	}

	/**
	 * Testing program for the SGP_API. Change the parameters and check against
	 * <ul>
	 * http://www.wolframalpha.com/input/?i=location+of+iss
	 * </ul>
	 * 
	 * @param args
	 *            none
	 */
	public static void main(String[] args) {
		// TLE used for testing
		String name = "ISS (ZARYA)";
		String l1 = "1 25544U 98067A   13315.62124936  .00007508  00000-0  13914-3 0  9953";
		String l2 = "2 25544  51.6490  97.8906 0000903  85.4294 338.5297 15.50050758857575";

		// Creates the tle object. If no name is available make something up.
		// Don't leave it null.
		SGP_TLE tle = new SGP_TLE(name, l1, l2);
		// Create the location object. Set to the center of the Science and
		// Engineering Quad
		SGP_Location location = new SGP_Location("Bucknell", 40.95530,
				-76.88206, 134, -5);
		// create the API object
		SGP_API iss = new SGP_API(tle, location);

		// calculate the positon of the iss now (results may vary)
		iss.calculatePosition();
		System.out.println("Current relative pointing vector:");
		System.out.print("Time: " + iss.getCurrentUTC()); // remember, month is
															// offset
		System.out.print("; AZ: " + iss.getAzimuth());
		System.out.print(", EL: " + iss.getElevation());
		System.out.println(", Visible: " + iss.isVisible());

		System.out.println("\nTest");
		// calculate the position of the iss at a specific time (results should
		// not vary)
		SGP_Timestamp time = new SGP_Timestamp(2013, 11, 13, 14, 16, 10, 0);
		iss.calculatePosition(time);
		System.out.print("Time: " + time); // remember, month is
		// offset when it prints
		System.out.print("; AZ: " + iss.getAzimuth());
		System.out.print(", EL: " + iss.getElevation());
		System.out.println(", Visible: " + iss.isVisible());
		// check your answer
		System.out.println("Should say:");
		System.out
				.println("Time: 2013-10-13 19:16:10.0; AZ: 352.2768891269791, EL: -66.1566359045153, Visible: false");
	}
}
