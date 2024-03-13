package br.com.empresaFaturamento;

import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class EmpresaFaturamento {

	public static void main(String[] args) {
		try {

			DecimalFormat df = new DecimalFormat("#,##0.00");
			List<Faturamento> listFaturamento = getDadosJson();

			List<Faturamento> listSemSaldoZerado = listFaturamento.stream().filter(f -> f.getValor() != 0.0).toList();
			Integer dias = listSemSaldoZerado.size();
			Double maiorValor = listFaturamento.get(0).getValor();
			Double menorValor = listFaturamento.get(0).getValor();
			Double totalFaturamento = 0.0;
			Double mediaFaturamento = 0.0;
			
			for (Faturamento faturamento : listSemSaldoZerado) {
				
				Double valor = faturamento.getValor();
				if(maiorValor < valor) {
					maiorValor = valor;
				}
				
				if(menorValor > valor && valor != 0.0) {
					menorValor = valor;
				}
				
				totalFaturamento += valor;
			}
			
			mediaFaturamento = totalFaturamento / dias;
			
			System.out.println("Maior valor do faturamento: " + df.format(maiorValor));
			System.out.println("\nMenor valor do faturamento: " + df.format(menorValor));
			System.out.println("\nMédia faturamento " + df.format(mediaFaturamento));
			
			List<Integer> diasAbaixoFaturamento = new ArrayList<>();
			for (Faturamento faturamento : listSemSaldoZerado) {
				if(faturamento.getValor() < mediaFaturamento) {
					diasAbaixoFaturamento.add(faturamento.getDia());
				}
			}
			
			System.out.println("\n"+diasAbaixoFaturamento.size() + " dias ficaram abaixo do faturamento, Dias: " + diasAbaixoFaturamento );

		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static List<Faturamento> getDadosJson() {
		List<Faturamento> listFaturamento = new ArrayList<>();
		try {
			FileReader reader = new FileReader("dados.json");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(reader);
			JSONArray jsonArray = (JSONArray) obj;
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while(iterator.hasNext()) {
				JSONObject jsonObject = iterator.next();
				Faturamento fat = new Faturamento();
				fat.setDia(Integer.parseInt(jsonObject.get("dia").toString()));
				fat.setValor(Double.parseDouble(jsonObject.get("valor").toString()));
				listFaturamento.add(fat);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return listFaturamento;
	}
}
