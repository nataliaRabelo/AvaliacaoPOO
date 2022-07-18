package br.winxbank.AvaliacaoOO2022;

import br.winxbank.repository.ArquivoBanco;
import br.winxbank.sistemabancario.Banco;
import br.winxbank.sistemaclientes.Cliente;

import java.io.*;
import java.util.ArrayList;

public class ArquivoAvaliacao {

    /**
     * @author Natália bruno Rabelo.
     * Classe responsável por persistir dados da avaliação em um arquivo
     */
    private static ArquivoAvaliacao instancia;

    /**
     * Método responsável por atualizar a única instância do objeto banco em um arquivo binário.
     * @param cliente
     * @throws FileNotFoundException
     */
    public void atualizarArquivo(Cliente cliente) throws FileNotFoundException {
        ObjectOutputStream ous = null;

        try {
            ous = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(cliente.getNome() + cliente.getCpf() + ".txt")));

            try {

                ous.writeObject(cliente.getAvaliacao());

            } catch (IOException e) {
            }

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {

            if (ous != null) {
                try {
                    ous.close();
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * Método responsável por atualizar os atributos da instância única de banco conforme instância salva em arquivo ao iniciar o programa.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void construirAvaliacao(Cliente cliente) throws IOException, ClassNotFoundException {
        ObjectInputStream ins = null;
        ArrayList<AvaliacaoOO2022> avaliacao = new ArrayList<>();

        try {
            ins = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(cliente.getNome() + cliente.getCpf() + ".txt"))));

            try {
                avaliacao.add((AvaliacaoOO2022) ins.readObject());

            } catch (ClassNotFoundException e1) {
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for(AvaliacaoOO2022 avaliacao2 : avaliacao){
                cliente.setAvaliacao(avaliacao2);
            }
        }

    }

    /**
     * Singleton que só permite uma instância do objeto ser criada, quando o atributo estático instancia tem o valor nulo.
     * @return
     */
    public static ArquivoAvaliacao getInstancia() {
        if (instancia == null) {
            instancia = new ArquivoAvaliacao();
        }
        return instancia;
    }
}
