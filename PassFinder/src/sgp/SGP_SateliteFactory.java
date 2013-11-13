// Clase est�tica que obtiene los sat�lites de archivos .tle obtenidos de internet

// This class takes a list of TLEs and creates satellite objects using the TLEs, I think we can manage without this.
package sgp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class SGP_SateliteFactory {
	private static ArrayList satelites = new ArrayList();;

	static void parseTLE(String archivoTLE) throws FileNotFoundException {
		FileReader fr = new FileReader(archivoTLE);
		BufferedReader br = new BufferedReader(fr);
		String nombre, linea1, linea2;
		try {
			do {
				nombre = br.readLine();
				if (nombre == null)
					break;
				linea1 = br.readLine();
				if (linea1 == null)
					break;
				linea2 = br.readLine();
				if (linea2 == null)
					break;
				satelites.add(new SGP_Satellite(new SGP_TLE(nombre, linea1, linea2)));
			} while (true);
			br.close();
			fr.close();
			Object[] satArray = satelites.toArray();
			Arrays.sort(satArray);
			satelites.clear();
			for (int i = 0; i < satArray.length; i++)
				satelites.add(satArray[i]);
		} catch (IOException e) {
			System.err.println("error: Leyendo archivo TLE : " + e);
		}
	}

	static boolean hasMoreSatelites() {
		if (howManySatelites() == 0)
			return false;
		else
			return true;
	}

	static SGP_Satellite nextSatelite() {
		try {
			SGP_Satellite sat = (SGP_Satellite) satelites.get(0);
			satelites.remove(0);

			return sat;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	static int howManySatelites() {
		return satelites.size();
	}

}