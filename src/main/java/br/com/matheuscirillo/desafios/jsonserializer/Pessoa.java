package br.com.matheuscirillo.desafios.jsonserializer;

import java.util.List;
import java.util.Set;

public class Pessoa {

    private String nome;
    private Integer idade;
    private int dataTypePrimitivo;
    private double outroDataTypePrimitivo;
    private int[] arrayDeNumerosInteirosPrimitivos;
    private List<Integer> listaDeWrapper;
    private Atividade[] atividadesEmArrayPrimitivo;
    private List<Atividade> listaDeAtividades;
    private Set<Atividade> setDeAtividades;

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public Integer getIdade() {
	return idade;
    }

    public void setIdade(Integer idade) {
	this.idade = idade;
    }

    public int getDataTypePrimitivo() {
	return dataTypePrimitivo;
    }

    public void setDataTypePrimitivo(int dataTypePrimitivo) {
	this.dataTypePrimitivo = dataTypePrimitivo;
    }

    public double getOutroDataTypePrimitivo() {
	return outroDataTypePrimitivo;
    }

    public void setOutroDataTypePrimitivo(double outroDataTypePrimitivo) {
	this.outroDataTypePrimitivo = outroDataTypePrimitivo;
    }

    public int[] getArrayDeNumerosInteirosPrimitivos() {
	return arrayDeNumerosInteirosPrimitivos;
    }

    public void setArrayDeNumerosInteirosPrimitivos(int[] arrayDeNumerosInteirosPrimitivos) {
	this.arrayDeNumerosInteirosPrimitivos = arrayDeNumerosInteirosPrimitivos;
    }

    public Atividade[] getAtividadesEmArrayPrimitivo() {
	return atividadesEmArrayPrimitivo;
    }

    public void setAtividadesEmArrayPrimitivo(Atividade[] atividadesEmArrayPrimitivo) {
	this.atividadesEmArrayPrimitivo = atividadesEmArrayPrimitivo;
    }

    public List<Atividade> getListaDeAtividades() {
	return listaDeAtividades;
    }

    public void setListaDeAtividades(List<Atividade> listaDeAtividades) {
	this.listaDeAtividades = listaDeAtividades;
    }

    public Set<Atividade> getSetDeAtividades() {
	return setDeAtividades;
    }

    public void setSetDeAtividades(Set<Atividade> setDeAtividades) {
	this.setDeAtividades = setDeAtividades;
    }

    public List<Integer> getListaDeWrapper() {
	return listaDeWrapper;
    }

    public void setListaDeWrapper(List<Integer> listaDeWrapper) {
	this.listaDeWrapper = listaDeWrapper;
    }

}
