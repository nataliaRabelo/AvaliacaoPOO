package br.winxbank.AvaliacaoOO2022;


import br.winxbank.sistemabancario.Banco;

import java.io.Serializable;

/**
 * @author Natália Bruno Rabelo.
 * Classe responsável por representar uma entidade Avaliacao002022.
 *
 * OBS: este atributo foi inserido como agragação na classe Cliente.
 */
public class AvaliacaoOO2022 implements Serializable {

    private String nome;
    private String matricula;
    private double nota;


    public AvaliacaoOO2022(){

    }

    public AvaliacaoOO2022(String nome, String matricula, double nota){
        this.nome = nome;
        this.matricula =  matricula;
        this.nota = nota;
    }

    public String getNome() {
        return this.nome;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public double getNota() {
        return this.nota;
    }

    public void setAvaliacao(AvaliacaoOO2022 avaliacao){
        this.nome = avaliacao.nome;
        this.matricula = avaliacao.matricula;
        this.nota = avaliacao.nota;
    }

}
