import sgp.SGP_Location;
import sgp.SGP_Satellite;
import sgp.SGP_Sun;
import sgp.SGP_TLE;
import sgp.SGP_Time;
import sgp.SGP_Timestamp;

public class Az_El_Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String name = "ISS (ZARYA)";
		String l1 = "1 25544U 98067A   13315.62124936  .00007508  00000-0  13914-3 0  9953";
		String l2 = "2 25544  51.6490  97.8906 0000903  85.4294 338.5297 15.50050758857575";

		SGP_Satellite iss = new SGP_Satellite(new SGP_TLE(name, l1, l2));

		SGP_Location lugar = new SGP_Location("Bucknell", 40.95530, -76.88206,
				134, -5);

		// Works Note that month is offset by one (month = month - 1)
		// (Nov: 11 - 1 = 10) but nothing else... WTF
		// This is a passing on Nov 16, 2013 at 5:59:55 (my) local time
		// confirmed against heavens-above.com
		SGP_Timestamp time = new SGP_Timestamp(2013, 10, 14, 4, 25, 30, 0);
		SGP_Time.localToUniversalTime(time, lugar.offsetUTC);

		// Works
		SGP_Timestamp ahora = SGP_Time.getCurrentUniversalTime(lugar.offsetUTC);

		SGP_Sun.calcularPosicion(ahora);
		lugar.calcularVariables(ahora);
		lugar.calcularPosicionSol(ahora);
		iss.calcularVariables(ahora);
		iss.calcularPosicionSatelite(lugar, ahora);
		// iss.setEclipsacionSatelite();

		System.out.print("Time: " + ahora);
		System.out.print("; AZ: " + iss.azimut);
		System.out.print(", EL: " + iss.elevacion);
		System.out.println(", Visible: " + iss.visible);

		SGP_Sun.calcularPosicion(time);
		lugar.calcularVariables(time);
		lugar.calcularPosicionSol(time);
		iss.calcularVariables(time);
		iss.calcularPosicionSatelite(lugar, time);

		System.out.print("Time: " + time);
		System.out.print("; AZ: " + iss.azimut);
		System.out.print(", EL: " + iss.elevacion);
		System.out.println(", Visible: " + iss.visible);

		// System.out.print(", Al: " + iss.altitud);
	}
}
