import sgp.Location;
import sgp.Satelite;
import sgp.TLE;
import sgp.Time;
import sgp.Timestamp;

public class Az_El_Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String name = "ISS (ZARYA)";
		String l1 = "1 25544U 98067A   13315.62124936  .00007508  00000-0  13914-3 0  9953";
		String l2 = "2 25544  51.6490  97.8906 0000903  85.4294 338.5297 15.50050758857575";

		Satelite iss = new Satelite(new TLE(name, l1, l2));

		Location lugar = new Location("Bucknell", 40.95530, -76.88206, 134, -5);

		// Works Note that month is offset by one (month = month - 1)
		// (Nov: 11 - 1 = 10) but nothing else... WTF
		// This is a passing on Nov 16, 2013 at 5:59:55 (my) local time
		// confirmed against heavens-above.com
		Timestamp time = new Timestamp(2013, 10, 16, 5, 59, 55, 0);
		Time.localToUniversalTime(time, lugar.offsetUTC);

		// Works
		Timestamp ahora = Time.getCurrentUniversalTime(lugar.offsetUTC);

		lugar.calcularVariables(ahora);
		iss.calcularVariables(ahora);
		iss.calcularPosicionSatelite(lugar, ahora);

		System.out.println(ahora);
		System.out.print("AZ: " + iss.azimut);
		System.out.println(", EL: " + iss.elevacion);
		// System.out.print(", Al: " + iss.altitud);

		lugar.calcularVariables(time);
		iss.calcularVariables(time);
		iss.calcularPosicionSatelite(lugar, time);

		System.out.println(time);
		System.out.print("time: AZ: " + iss.azimut);
		System.out.println(", EL: " + iss.elevacion);
		// System.out.print(", Al: " + iss.altitud);
	}
}
