package sgp;

/**
 * Clase encargada de almacenar y hacer accesibles los campos de las dos lineas
 * con formato TLE. Dichos campos contienen los datos necesarios para conocer la
 * posici�n de los sat�lites.
 * 
 * Class pulls the components from the Two Line Element need to calculate the
 * postion of the satellite.
 * 
 * @author Pedro J. Fern�ndez
 * @author Dr TS Kelso
 * @version 1.0
 * 
 *          Translated by P. R. Diefenderfer
 * 
 */

public class SGP_TLE {
	/** Name of the satellite */
	private String satelliteName;

	/** First Line of the TLE */
	private String line1TLE;

	/** Second Line of the TLE */
	private String line2TLE;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            Name of the Satellite (Typically before the first line)
	 * @param line1
	 *            First Line of the TLE
	 * @param line2
	 *            Second Line of the TLE
	 */
	public SGP_TLE(String name, String line1, String line2) {
		satelliteName = name;
		line1TLE = line1;
		line2TLE = line2;
	}

	/**
	 * Gets the satellites name
	 * 
	 * @return The satellite's name
	 * 
	 */
	public String getSateliteName() {
		return satelliteName;
	}

	/**
	 * Gets the first line of the TLE
	 * 
	 * @return TLE line 1
	 * 
	 */
	public String getLine1TLE() {
		return line1TLE;
	}

	/**
	 * Gets the second line of the TLE
	 * 
	 * @return TLE line 2
	 * 
	 */
	public String getLine2TLE() {
		return line2TLE;
	}

	/**
	 * Gets the catalog number of the satellite
	 * 
	 * @return the catalog number
	 * 
	 */
	String getCatnr() {
		return line1TLE.substring(2, 7);
	}

	/**
	 * Gets the Epoch
	 * 
	 * @return double "Epoch"
	 * @exception NumberFormatException
	 * 
	 */
	double getEpoch() throws NumberFormatException {
		return Double.parseDouble(line1TLE.substring(18, 32));
	}

	/**
	 * Gets the Xndt2o element
	 * 
	 * @return double "Xndt2o"
	 * @exception NumberFormatException
	 * 
	 */
	double getXndt2o() throws NumberFormatException {
		return Double.parseDouble(line1TLE.substring(33, 43));
	}

	/**
	 * Gets the Xndd6o element
	 * 
	 * @return double "Xndd6o"
	 * @exception NumberFormatException
	 * 
	 */
	double getXndd6o() throws NumberFormatException {
		return (Double.parseDouble(line1TLE.substring(44, 50)) * 1e-5d);
	}

	/**
	 * Gets the "Iexp" element
	 * 
	 * @return "Iexp"
	 * @exception NumberFormatException
	 * 
	 */
	int getIexp() throws NumberFormatException {
		if (line1TLE.substring(50, 51).equals("+"))
			return Integer.parseInt(line1TLE.substring(51, 52));
		else
			return Integer.parseInt(line1TLE.substring(50, 52));
	}

	/**
	 * Gets the BSTAR drag term
	 * 
	 * @return double "Bstar"
	 * @exception NumberFormatException
	 * 
	 */
	double getBstar() throws NumberFormatException {
		return (Double.parseDouble(line1TLE.substring(53, 59)) * 1e-5d);
	}

	/**
	 * Gets the "Ibexp" element
	 * 
	 * @return Ibexp
	 * @exception NumberFormatException
	 * 
	 */
	int getIbexp() throws NumberFormatException {
		if (line1TLE.substring(59, 60).equals("+"))
			return Integer.parseInt(line1TLE.substring(60, 61));
		else
			return Integer.parseInt(line1TLE.substring(59, 61));
	}

	/**
	 * Gets the "Elset" element
	 * 
	 * @return "Elset"
	 */
	String getElset() {
		return line1TLE.substring(65, 69);
	}

	/**
	 * Gets the "Xincl" element
	 * 
	 * @return "Xincl"
	 * @exception NumberFormatException
	 * 
	 */
	double getXincl() throws NumberFormatException {
		return Double.parseDouble(line2TLE.substring(8, 16));
	}

	/**
	 * M�todo que devuelve la variable "Xnodeo"
	 * 
	 * @return Devuelve "Xnodeo"
	 * @exception NumberFormatException
	 *                Salta si "Xnodeo" no es un real.
	 */
	double getXnodeo() throws NumberFormatException {
		return Double.parseDouble(line2TLE.substring(17, 25));
	}

	/**
	 * M�todo que devuelve la variable "Eo"
	 * 
	 * @return Devuelve "Eo"
	 * @exception NumberFormatException
	 *                Salta si "Eo" no es un real.
	 */
	double getEo() throws NumberFormatException {
		return (Double.parseDouble(line2TLE.substring(26, 33)) * 1e-7d);
	}

	/**
	 * M�todo que devuelve la variable "Omegao"
	 * 
	 * @return Devuelve "Omegao"
	 * @exception NumberFormatException
	 *                Salta si "Omegao" no es un real.
	 */
	double getOmegao() throws NumberFormatException {
		return Double.parseDouble(line2TLE.substring(34, 40));
	}

	/**
	 * M�todo que devuelve la variable "Xmo"
	 * 
	 * @return Devuelve "Xmo"
	 * @exception NumberFormatException
	 *                Salta si "Xmo" no es un real.
	 */
	double getXmo() throws NumberFormatException {
		return Double.parseDouble(line2TLE.substring(43, 51));
	}

	/**
	 * M�todo que devuelve la variable "Xno"
	 * 
	 * @return Devuelve "Xno"
	 * @exception NumberFormatException
	 *                Salta si "Xno" no es un real.
	 */
	double getXno() throws NumberFormatException {
		return Double.parseDouble(line2TLE.substring(52, 63));
	}

}
