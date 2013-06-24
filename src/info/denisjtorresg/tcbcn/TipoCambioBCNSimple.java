package info.denisjtorresg.tcbcn;

import info.denisjtorresg.wsproxy.bcn.RecuperaTCMesResponse.RecuperaTCMesResult;
import info.denisjtorresg.wsproxy.bcn.TipoCambioBCN;
import info.denisjtorresg.wsproxy.bcn.TipoCambioBCNSoap;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>Clase simple para acceder al servicio Web de tipos de cambio del 
 * Córdoba respecto al Dólar liberado al público 
 * por el Banco Central de Nicaragua.</p>
 * 
 * <p>El cliente que da soporte a esta clase se ha generado con Apache CXF.</p>
 * 
 * <p>Ver la documentación y las reglas de uso del servicio web 
 * publicadas en la web del BCN:</p>
 * 
 * @see 
 * 
 * <ol><li>http://www.bcn.gob.ni/tc/
 * <li>https://servicios.bcn.gob.ni/Tc_Servicio/
 * <li>https://servicios.bcn.gob.ni/Tc_Servicio/ServicioTC.asmx?WSDL
 * </ol>
 * 
 * @author Denis.Torres - <a href="http://blog.denisjtorresg.info">http://blog.denisjtorresg.info</a>
 * @version 21/06/2013
 *
 */
public final class TipoCambioBCNSimple {
	private static URL wsdlURL = null;
	private static TipoCambioBCN tcBCN = null;
	private static TipoCambioBCNSoap tcBCNPort = null; 
	
	/**
	 * Iniciar el proxy
	 */
	private static void init() {
		try {
			wsdlURL = new URL("https://servicios.bcn.gob.ni/Tc_Servicio/ServicioTC.asmx?WSDL");
			tcBCN = new TipoCambioBCN(wsdlURL);
			tcBCNPort = tcBCN.getTipoCambioBCNSoap();  
		} catch (Exception e) {
			wsdlURL = null;
			tcBCN = null;
			tcBCNPort = null;
			e.printStackTrace();
		}
	}
	
	/**
	 * Reiniciar el proxy
	 */
	public static final void reset(){
		wsdlURL = null;
		tcBCN = null;
		tcBCNPort = null;
		init();
	}
	
	/**
	 * Obtener el Tipo de Cambio al día indicado en los parámetros
	 * 
	 * @param anio Año
	 * @param mes Mes
	 * @param dia Día
	 * @return
	 */
	public static final double getTipoCambio(int anio, int mes, int dia){
		if(tcBCNPort==null){
			init();
		}
		
		if(tcBCNPort==null){
			return 0.0;
		}
		
	    return tcBCNPort.recuperaTCDia(anio, mes, dia);
	}
	
	/**
	 * Obtener el Tipo de Cambio al día indicado en los parámetros
	 * 
	 * @param fecha Fecha en formato java.util.Date
	 * @return
	 */
	public static final double getTipoCambio(Date fecha){
		
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		
		return getTipoCambio(c);
	}
    
	/**
	 * Obtener el Tipo de Cambio al día indicado en los parámetros
	 * 
	 * @param fecha Fecha como String con formato dd-mm-aaaa o dd/mm/aaaa
	 * @return
	 */
	public static final double getTipoCambio(String fecha){
		return getTipoCambio(string2Date(fecha));
	}
		
	/**
	 * Obtener el Tipo de Cambio al día indicado en los parámetros
	 * 
	 * @param c Fecha en formato java.util.Calendar
	 * @return
	 */
	public static final double getTipoCambio(Calendar c){
		return getTipoCambio(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
	}
	
	/**
	 * Obtener el Tipo de Cambio al día de hoy
	 * @return
	 */
	public static final double getTipoCambio(){
		return getTipoCambio(new Date());
	}
	
	/**
	 * Obtener los Tipos de Cambio en el rango de fechas especificado
	 * 
	 * @param inicio Fecha inicial en formato java.util.Calendar 
	 * @param fin Fecha final en formato java.util.Calendar
	 * @return
	 */
	public static final Map<Date, Double> getTipoCambio(Calendar inicio, Calendar fin){
		Map<Date, Double> resultado = new TreeMap<Date, Double>();
		
		if(inicio == null || fin == null){
			return resultado;
		}
		
		if(fin.before(inicio)){
			Calendar tmp = fin;
			fin = inicio;
			inicio = tmp;
		}
		
		while(inicio.compareTo(fin)<=0){
			Double tc = getTipoCambio(inicio);
			resultado.put(inicio.getTime(), tc);
			
			inicio.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return resultado;
	}
	
	/**
	 * Obtener los Tipos de Cambio en el rango de fechas especificado
	 * 
	 * @param inicio Fecha inicial en formato java.util.Date 
	 * @param fin Fecha final en formato java.util.Date
	 * @return
	 */
	public static final Map<Date, Double> getTipoCambio(Date inicio, Date fin){
		
		Calendar ci = Calendar.getInstance();
		ci.setTime(inicio);
		
		Calendar cf = Calendar.getInstance();
		cf.setTime(fin);
		
		return getTipoCambio(ci, cf);
	}
	
	/**
	 * Obtener los Tipos de Cambio en el rango de fechas especificado
	 * 
	 * @param inicio Fecha inicial en formato String
	 * @param fin Fecha final en formato String
	 * @return
	 */
	public static final Map<Date, Double> getTipoCambio(String inicio, String fin){
		return getTipoCambio(string2Date(inicio), string2Date(fin));
	}
	
	/**
	 * Obtener los Tipos de Cambio en el mes del año especificado
	 * 
	 * @param anio 
	 * @param mes
	 * @return
	 */
	public static final Map<Date, Double> getTipoCambio(int anio, int mes){
		Calendar inicio = Calendar.getInstance();
		inicio.set(Calendar.YEAR, anio);
		inicio.set(Calendar.MONTH, mes - 1);
		inicio.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar fin = Calendar.getInstance();
		fin.set(Calendar.YEAR, anio);
		fin.set(Calendar.MONTH, mes - 1);
		fin.set(Calendar.DAY_OF_MONTH, 1);
		fin.add(Calendar.MONTH, 1);
		fin.add(Calendar.DAY_OF_MONTH, -1);
		
		return getTipoCambio(inicio, fin);
	}
	
	/**
	 * Según la documentación del BCN el ws RecuperaTC_Mes
	 * retorna la lista de tipos de cambio del mes indicado.
	 * 
	 * Pero en mis pruebas el servicio web retorna un elemento vacio.
	 * 
	 * Alternativamente a este método, se puede usar uno de los métodos 
	 * sobrecargados getTipoCambio() con rango de fechas.
	 * 
	 * @param anio
	 * @param mes
	 * @return
	 */
	public static final RecuperaTCMesResult getTipoCambioMes(int anio, int mes){
		if(tcBCNPort==null){
			init();
		}
		
		if(tcBCNPort==null){
			return null;
		}
		
		RecuperaTCMesResult respuesta = tcBCNPort.recuperaTCMes(anio, mes);
		
		return respuesta;
	}
	
	/**
	 * Utilitario para convertir de String (fecha en formato dd-mm-aaaa o dd/mm/aaaa) a Date
	 * 
	 * @param fecha
	 * @return
	 */
	private static Date string2Date(String fecha){
		Date date = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat();
		
		try {
			sdf.applyPattern("dd-MM-yyyy");
			date = sdf.parse(fecha);
		} catch (ParseException e) {
			date = null;
		}
		
		if(date == null ) {
			try {
				sdf.applyPattern("dd/MM/yyyy");
				date = sdf.parse(fecha);
			} catch (ParseException e) {
				date = null;
			}
		}
		
		return date;

	}
}
