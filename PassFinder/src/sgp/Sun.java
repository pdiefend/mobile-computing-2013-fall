// Clase que representa al Sol, guardando su posici�n y los m�todos para obtener
// dicha posici�n
package sgp;

class Sun
{
	static double[] pos = new double[4];
	static double latitud, longitud, altitud, theta;

	static void calcularPosicion(Timestamp t)
	{
		calcularPosicion(Time.timeToJulianTime(t));
	}

	static void calcularPosicion(double time)
	{
		double mjd,year,T,M,L,e,C,O,Lsa,nu,R,eps;

		mjd = time - 2415020.0;
		year = 1900.0 + mjd/365.25;
		T = (mjd + Time.deltaET(year)/Constants.secday)/36525.0;
		M = Math.toRadians(MathematicalFunctions.modulus(358.47583 + MathematicalFunctions.modulus(35999.04975*T,360.0)
			- (0.000150 + 0.0000033*T)*(T*T),360.0));
		L = Math.toRadians(MathematicalFunctions.modulus(279.69668 + MathematicalFunctions.modulus(36000.76892*T,360.0)
			+ 0.0003025*(T*T),360.0));
		e = 0.01675104 - (0.0000418 + 0.000000126*T)*T;
		C = Math.toRadians((1.919460 - (0.004789 + 0.000014*T)*T)*Math.sin(M)
			+ (0.020094 - 0.000100*T)*Math.sin(2.0*M) + 0.000293*Math.sin(3.0*M));
		O = Math.toRadians(MathematicalFunctions.modulus(259.18 - 1934.142*T,360.0));
		Lsa = MathematicalFunctions.modulus(L + C - Math.toRadians(0.00569 - 0.00479*Math.sin(O)),(Math.PI*2.0));
		nu = MathematicalFunctions.modulus(M + C,(Math.PI*2.0));
		R = 1.0000002*(1.0 - (e*e))/(1.0 + e*Math.cos(nu));
		eps = Math.toRadians(23.452294 - (0.0130125 + (0.00000164 - 0.000000503*T)*T)*T
			+ 0.00256*Math.cos(O));
		R = Constants.AU*R;
		pos[0] = R*Math.cos(Lsa);
		pos[1] = R*Math.sin(Lsa)*Math.cos(eps);
		pos[2] = R*Math.sin(Lsa)*Math.sin(eps);
		pos[3] = R;

		calcularLatLonAlt(time);
	}

	private static void calcularLatLonAlt(double time)
	{
		double alt, lon, lat;
		double thetaAux, r, e2, phi, c;

		thetaAux = MathematicalFunctions.acTan(pos[1],pos[0]);
		lon = MathematicalFunctions.modulus(thetaAux - Time.thetaG_JD(time),Math.PI*2.0);
		r = Math.sqrt((pos[0]*pos[0]) + (pos[1]*pos[1]));
		e2 = Constants.f*(2.0 - Constants.f);
		lat = MathematicalFunctions.acTan(pos[2],r);
		do
		{
			phi = lat;
			c = 1.0/Math.sqrt(1.0 - e2* Math.pow(Math.sin(phi),2.0));
			lat = MathematicalFunctions.acTan(pos[2] + Constants.xkmper*c*e2*Math.sin(phi),r);
		}
		while (!(Math.abs(lat-phi) < 1e-10));

		alt = r/Math.cos(lat) - Constants.xkmper*c;

		latitud = MathematicalFunctions.toDegrees(lat);
		longitud = MathematicalFunctions.toDegrees(lon);
		altitud = alt;
		theta = thetaAux;
		if (longitud >= 180)
			longitud = longitud - 360;
	}
}