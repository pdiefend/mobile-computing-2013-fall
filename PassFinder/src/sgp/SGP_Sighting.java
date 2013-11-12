package sgp;

import java.text.*;

public class SGP_Sighting {
  public SGP_Satelite
      inicio=null,
      inicioLuz=null,
      fin=null,
      finLuz=null,
      maximaElevacion=null;
  public double offsetUTC = 0.0;


  private String printDouble(String pattern, double value ) {
     DecimalFormat myFormatter = new DecimalFormat(pattern);
     return myFormatter.format(value);
  }

  private String dd(int n){
    String s = "" + n;
    if (s.length()==1)
      s = "0"+s;
    return s;
  }

  public String toHtml(){
    SGP_Time.universalToLocalTime(inicio.marcaDeTiempo,offsetUTC);
    SGP_Time.universalToLocalTime(inicioLuz.marcaDeTiempo,offsetUTC);
    SGP_Time.universalToLocalTime(fin.marcaDeTiempo,offsetUTC);
    SGP_Time.universalToLocalTime(finLuz.marcaDeTiempo,offsetUTC);
    SGP_Time.universalToLocalTime(maximaElevacion.marcaDeTiempo,offsetUTC);

    String fecha = "" + dd(inicio.marcaDeTiempo.dy) +
        "/" + dd((inicio.marcaDeTiempo.mo+1)) +
        "/" + inicio.marcaDeTiempo.yr;
    String horaInicio = "" + dd(inicio.marcaDeTiempo.hr) +
        ":" + dd(inicio.marcaDeTiempo.mi);
    String horaInicioIlum = "" + dd(inicioLuz.marcaDeTiempo.hr) +
        ":" + dd(inicioLuz.marcaDeTiempo.mi);
    String horaFin = "" + dd(fin.marcaDeTiempo.hr) +
        ":" + dd(fin.marcaDeTiempo.mi);
    String horaFinIlum = "" + dd(finLuz.marcaDeTiempo.hr) +
        ":" + dd(finLuz.marcaDeTiempo.mi);
    String maxElev = printDouble("##.#", maximaElevacion.elevacion) +
        " " + maximaElevacion.puntoCardinal;

    return "<tr><td>"+ fecha + "</td><td>" + horaInicio +" "+ inicio.puntoCardinal +
        "</td><td>" + horaInicioIlum +" "+ inicioLuz.puntoCardinal +
        " - " + horaFinIlum +" "+ finLuz.puntoCardinal + "</td><td>"
        + horaFin +" "+fin.puntoCardinal +
        "</td><td>" + maxElev +"</td></tr>";

  }


  public String toString(){
    SGP_Time.universalToLocalTime(inicio.marcaDeTiempo,offsetUTC);
    SGP_Time.universalToLocalTime(inicioLuz.marcaDeTiempo,offsetUTC);
    SGP_Time.universalToLocalTime(fin.marcaDeTiempo,offsetUTC);
    SGP_Time.universalToLocalTime(finLuz.marcaDeTiempo,offsetUTC);
    SGP_Time.universalToLocalTime(maximaElevacion.marcaDeTiempo,offsetUTC);

    String fecha = "" + dd(inicio.marcaDeTiempo.dy) +
        "/" + dd((inicio.marcaDeTiempo.mo+1)) +
        "/" + inicio.marcaDeTiempo.yr;
    String horaInicio = "" + dd(inicio.marcaDeTiempo.hr) +
        ":" + dd(inicio.marcaDeTiempo.mi);
    String horaInicioIlum = "" + dd(inicioLuz.marcaDeTiempo.hr) +
        ":" + dd(inicioLuz.marcaDeTiempo.mi);
    String horaFin = "" + dd(fin.marcaDeTiempo.hr) +
        ":" + dd(fin.marcaDeTiempo.mi);
    String horaFinIlum = "" + dd(finLuz.marcaDeTiempo.hr) +
        ":" + dd(finLuz.marcaDeTiempo.mi);
    String maxElev = printDouble("##.#", maximaElevacion.elevacion) +
        " " + maximaElevacion.puntoCardinal;


    return fecha + " " + horaInicio +" "+ inicio.puntoCardinal +
        " (" + horaInicioIlum +" "+ inicioLuz.puntoCardinal +
        " - " + horaFinIlum +" "+ finLuz.puntoCardinal + ") "
        + horaFin +" "+fin.puntoCardinal +
        "  " + maxElev ;
  }
}