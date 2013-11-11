package sgp;

import java.io.*;
import java.util.*;

public class Testing
{
	public static void main(String[] args)
	{
		ArrayList satelites = new ArrayList();
		ArrayList lugares = new ArrayList();

		try
		{
			SateliteFactory.parseTLE("c:\\PassViewJava\\sgp\\visual.tle");
			int cuantosSat = SateliteFactory.howManySatelites();
			Satelite s;
			System.out.println("---------------------------------");
			System.out.println("N�mero de sat�lites: " + cuantosSat);
			System.out.println("---------------------------------");
			int j=0,numeroISS=0;
			while (SateliteFactory.hasMoreSatelites())
			{
				s = (Satelite)SateliteFactory.nextSatelite();
				if (s.nombre.startsWith("ISS (ZARYA)"))
					numeroISS = j;
				satelites.add(s);
				j++;
			}
			Satelite sat = (Satelite) satelites.get(numeroISS);
			// Ya tenemos la ISS seleccionada

			LocationFactory.parseLOC("c:\\PassViewJava\\sgp\\spain.loc");
			int cuantosLugares = LocationFactory.howManyLugares();
			Location l;
			System.out.println("N�mero de lugares: " + cuantosLugares);
			System.out.println("---------------------------------");
			while (LocationFactory.hasMoreLugares())
			{
				l = (Location)LocationFactory.nextLugar();
				lugares.add(l);
			}
			l = (Location)lugares.get(3);
			// Ya tenemos lugar (Murcia)

			System.out.println("Sat�lite: " + sat.nombre);
			System.out.println("Lugar: " + l.nombre);

			System.out.println("---------------------------------");

			// Miramos la hora local y universal
			Timestamp ahora;
			Time.setHorarioVerano(false);
			ahora = Time.getCurrentLocalTime();
			System.out.println("Hora local:\n\t"+ahora);
			ahora = Time.getCurrentUniversalTime(l.offsetUTC);
			System.out.println("Hora universal:\n\t"+ahora);
			double j1 = Time.timeToJulianTime(ahora);
			System.out.println("Hora universal juliana:\n\t"+j1);
			Time.julianTimeToTime(j1,ahora);
			System.out.println("Hora universal:\n\t"+ahora);
			System.out.println("---------------------------------");

			// Lo primero es calcular la posici�n solar
			Sun.calcularPosicion(ahora); // ahora tiene que ser universal

			System.out.println("Datos sobre el sol");
			System.out.println("\tpos[0]: " + Sun.pos[0]);
			System.out.println("\tpos[1]: " + Sun.pos[1]);
			System.out.println("\tpos[2]: " + Sun.pos[2]);
			System.out.println("\tpos[3]: " + Sun.pos[3]);
			System.out.println("\tLatitud: " + Sun.latitud);
			System.out.println("\tlongitud: " + Sun.longitud);
			System.out.println("\tAltitud: " + Sun.altitud);
			System.out.println("---------------------------------");

			// Ahora calculamos la posicion y velocidad del sat�lite
			sat.calcularVariables(ahora);
			System.out.println("Datos sobre el sat�lite "+sat.nombre+" :");

			System.out.println("\tpos[0]: "+sat.pos[0]);
			System.out.println("\tpos[1]: "+sat.pos[1]);
			System.out.println("\tpos[2]: "+sat.pos[2]);
			System.out.println("\tpos[3]: "+sat.pos[3]);
			System.out.println("\tvel[0]: "+sat.vel[0]);
			System.out.println("\tvel[1]: "+sat.vel[1]);
			System.out.println("\tvel[2]: "+sat.vel[2]);
			System.out.println("\tvel[3]: "+sat.vel[3]);
			System.out.println("\tLatitud: " + sat.latitud);
			System.out.println("\tLongitud: " + sat.longitud);
			System.out.println("\tAltitud: " + sat.altitud);
			System.out.println("---------------------------------");

			// Ahora tenemos que calcular el azimut y elevaci�n del sol y sat�lite
			// respecto a la posici�n del observador

			l.calcularPosicionSol(ahora);
			System.out.println("Datos relativos al observador:");
			System.out.println("Sol:");
			System.out.println("\tAzimut sol:" + l.azimutSol);
			System.out.println("\tElevacion sol:" + l.elevacionSol);
			System.out.println("\tRango sol:" + l.rangoSol);
			System.out.println("\tRatioRango sol:" + l.ratioRangoSol);

			System.out.println(""+ sat.nombre);
			sat.calcularPosicionSatelite(l,ahora);
			System.out.println("\tAzimut sat:" + sat.azimut);
			System.out.println("\tElevacion sat:" + sat.elevacion);
			System.out.println("\tRango sat:" + sat.rango);
			System.out.println("\tRatioRango sat:" + sat.ratioRango);
			System.out.println("---------------------------------");

		}
		catch (FileNotFoundException e)
		{
			System.out.println("No se encuentra el fichero.");
		}






	}
}