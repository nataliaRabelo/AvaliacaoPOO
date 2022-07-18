package br.winxbank.AvaliacaoOO2022;

public class AvaliacaoOO2022NaoInformadaException extends NullPointerException{

    public AvaliacaoOO2022NaoInformadaException(){
        super("Avaliacao do banco nao pode ser visualizada. Porque nao foi informada.");
    }
}
