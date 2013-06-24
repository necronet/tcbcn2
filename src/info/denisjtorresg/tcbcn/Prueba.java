package info.denisjtorresg.tcbcn;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class Prueba {

	public static void main(String[] args) {
		System.setProperty("java.net.useSystemProxies", "true");
		
		// Tipo de cambio del día
		System.out.println(TipoCambioBCNSimple.getTipoCambio());
		
		// Tipo de cambio en la fecha indicada (año, mes, día)
		System.out.println(TipoCambioBCNSimple.getTipoCambio(2013,6,21));

		System.out.println(TipoCambioBCNSimple.getTipoCambio(2012,1,1));
		
		// Tipos de cambio del mes indicado
		Map<Date, Double> tcRango = TipoCambioBCNSimple.getTipoCambio(2013, 2);
		
		for(Entry<Date, Double> elemento: tcRango.entrySet()){
			Date fecha = elemento.getKey();
			Double tc = elemento.getValue();
			
			System.out.println(fecha + " => " + tc);
		}
		
	}

}
