package sgp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


public class SGP_PassFinderISS {

	public static void main(String[] args) {
		String nombreLocalidad = null;
		String latitud = null;
		String longitud = null;
		String altitud = null;
		String offsetUTC = null;
		double dias = 1.0;
		double maxelev_umbral = 10.0;

		SGP_PassView.actualizacionForzadaTLE(true); // forces internet use
		Date d = new Date();
		SGP_HTMLInfo.cabecera(d.toString());
		// Porcesado de los argumentos
		if (args.length != 7) {
			SGP_HTMLInfo.error("Error: El n�mero de argumentos no es adecuado."); // number
																					// of
																					// arguements
																					// no
																					// es
																					// bueno
			SGP_HTMLInfo.uso();
			SGP_HTMLInfo.fin();
			System.exit(1);
		} else {
			nombreLocalidad = args[0];
			latitud = args[1];
			longitud = args[2];
			altitud = args[3];
			offsetUTC = args[4];
			try {
				dias = Double.parseDouble(args[5]);
			} catch (Exception e) {
				SGP_HTMLInfo
						.error("Error: El argumento Dias (6�) no es un n�mero.");
				SGP_HTMLInfo.uso();
				SGP_HTMLInfo.fin();
				System.exit(1);
			}
			try {
				maxelev_umbral = Double.parseDouble(args[6]);
			} catch (Exception e) {
				SGP_HTMLInfo
						.error("Error: El argumento Maxima Elevaci�n umbral (7�) no es un n�mero.");
				SGP_HTMLInfo.uso();
				SGP_HTMLInfo.fin();
				System.exit(1);
			}
		}

		boolean status = true;
		status = SGP_PassView.setLugar(nombreLocalidad, latitud, longitud, altitud,
				offsetUTC);
		if (status)
			SGP_HTMLInfo.localidad(SGP_PassView.getLugar());
		else {
			SGP_HTMLInfo
					.error("Error en los datos de entrada para el lugar elegido.");
			SGP_HTMLInfo.uso();
			SGP_HTMLInfo.fin();
			System.exit(1);
		}

		status = SGP_PassView.setTLE();
		if (status) {
			File tle = new File("stations.txt");
			if (tle.exists()) {
				d = new Date(tle.lastModified());
			} else {
				d = new Date();
			}
			SGP_Satellite s = SGP_PassView.getSatelite();

			SGP_HTMLInfo.satelite(s.nombre, s.tle.getLine1TLE(),
					s.tle.getLine2TLE(), d.toString());
		} else {
			SGP_HTMLInfo
					.error("    Archivo no disponible en disco. Falta conexi�n a Internet.");
			SGP_HTMLInfo.uso();
			SGP_HTMLInfo.fin();
			System.exit(1);
		}

		ArrayList avistamientos = SGP_PassView.getAvistamientos(dias);

		SGP_HTMLInfo.avistamientos(avistamientos, args[5], args[6]);

	}
}