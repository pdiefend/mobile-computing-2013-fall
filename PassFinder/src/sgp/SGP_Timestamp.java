// Clase que representa un instante de tiempo dado, con precisi�n de milisegundos.
// Las clases de este paquete pueden acceder directamente a los atributos.
package sgp;

public class SGP_Timestamp {
	public int yr, mo, dy, hr, mi, se, hu;

	SGP_Timestamp() {
		yr = 0;
		mo = 0;
		dy = 0;
		hr = 0;
		mi = 0;
		se = 0;
		hu = 0;
	}

	public SGP_Timestamp(int yr, int mo, int dy, int hr, int mi, int se, int hu) {
		this.yr = yr;
		this.mo = mo;
		this.dy = dy;
		this.hr = hr;
		this.mi = mi;
		this.se = se;
		this.hu = hu;
	}

	public void setValues(int yr, int mo, int dy, int hr, int mi, int se, int hu) {
		this.yr = yr;
		this.mo = mo;
		this.dy = dy;
		this.hr = hr;
		this.mi = mi;
		this.se = se;
		this.hu = hu;
	}

	public String toString() {
		String s = "" + yr + "-" + mo + "-" + dy + " " + hr + ":" + mi + ":"
				+ se + "." + hu;

		return s;
	}

}
