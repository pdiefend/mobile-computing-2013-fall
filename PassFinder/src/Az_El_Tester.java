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

		// Does not Work
		// Timestamp ahora = new Timestamp(2013, 11, 12, 16, 0, 0, 0);
		// Time.localToUniversalTime(ahora, lugar.offsetUTC);

		// Works
		Timestamp ahora = Time.getCurrentUniversalTime(lugar.offsetUTC);

		lugar.calcularVariables(ahora);
		iss.calcularVariables(ahora);
		iss.calcularPosicionSatelite(lugar, ahora);

		System.out.print("AZ: " + iss.azimut);
		System.out.println(", EL: " + iss.elevacion);
		// System.out.print(", Al: " + iss.altitud);

	}
}
