package br.com.matheuscirillo.desafios.jsonserializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Demo {

    public static void main(String[] args) {
	Pessoa pessoa = new Pessoa();

	Atividade[] atividadesArr = new Atividade[] { new Atividade("Estudar"), new Atividade("Ler") };
	List<Atividade> atividadeList = new ArrayList<>(
		Arrays.asList(new Atividade("Ir para a aula de inglês"), new Atividade("Ir para a natação")));
	Set<Atividade> atividadesSet = new HashSet<>(Arrays.asList(new Atividade("Ir para a aula de inglês"), new Atividade("Ir para a natação")));

	int[] numerosArr = new int[] { 1, 2, 3, 4, 5 };
	List<Integer> numerosList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
	
	pessoa.setArrayDeNumerosInteirosPrimitivos(numerosArr);
	pessoa.setAtividadesEmArrayPrimitivo(atividadesArr);
	pessoa.setDataTypePrimitivo(new Random().nextInt());
	pessoa.setIdade(12);
	pessoa.setListaDeAtividades(atividadeList);
	pessoa.setListaDeWrapper(numerosList);
	pessoa.setNome("Matheus");
	pessoa.setOutroDataTypePrimitivo(new Random().nextDouble());
	pessoa.setSetDeAtividades(atividadesSet);
	
	JsonWriter writer = new JsonWriter();
	String json = writer.writeAsJson(pessoa);
	
	System.out.println(json);	
    }

}
