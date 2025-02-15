package br.winxbank.sistemaclientes;

import br.winxbank.AvaliacaoOO2022.ArquivoAvaliacao;
import br.winxbank.AvaliacaoOO2022.AvaliacaoOO2022;
import br.winxbank.AvaliacaoOO2022.AvaliacaoOO2022NaoInformadaException;
import br.winxbank.sistemabancario.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import java.text.DecimalFormat;

/**
 * @author Natália
 * Esta classe é responsável por administrar um registro de clientes.
 */
public class RegistroDeClientes {

    private static RegistroDeClientes instancia;
    private ArrayList<Cliente> clientes = new ArrayList<Cliente>();

    /**
     * Este método é responsável por cadastrar um cliente no registro de clientes.
     * Se o cliente criar uma conta com mais de 100 mil, ele se torna ClienteWix.
     */
    public void cadastrarCliente(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Você está cadastrando um cliente\nDigite o nome:");
        String nome = sc.nextLine();
        System.out.println("Digite o cpf:");
        String cpf = sc.nextLine();
        boolean cpfExistente = checarCpf(cpf);
        if(cpfExistente || clientes.isEmpty()){
            Cliente cliente = new Cliente(nome, cpf);
            Conta conta = Banco.getInstancia().abrirNovaConta();
            cliente.setContas(conta);
            if(conta.getSaldo() >= 100000){
                System.out.println("Parabéns, você tem direito a ser ClienteWinx!");
                ClienteWinx clienteWinx = new ClienteWinx(nome, cpf, 0);
                Movimentacao movimentacao = new Movimentacao(conta.getSaldo(), Movimentacao.TipoDaMovimentacao.ENTRADA);
                conta.setExtrato(movimentacao);
                clienteWinx.setContas(conta);
                clientes.add(clienteWinx);
                if(conta.getClass() == ContaPoupanca.class){
                    ((ContaPoupanca) conta).setInformeRendimento(movimentacao);
                }
            }
            else if(conta.getSaldo() < 100000) {
                Movimentacao movimentacao = new Movimentacao(conta.getSaldo(), Movimentacao.TipoDaMovimentacao.ENTRADA);
                conta.setExtrato(movimentacao);
                clientes.add(cliente);
                if(conta.getClass() == ContaPoupanca.class){
                    ((ContaPoupanca) conta).setInformeRendimento(movimentacao);
                }
            }
        }else{
            System.out.println("Usuario nao pode ser criado. CPF ja existente no registro.");
        }
    }

    /**
     * Este método é responsável por atualizar dados de um cliente do registro de clientes
     * @param cliente
     */
    public void atualizarCliente(Cliente cliente) throws InterruptedException {
        System.out.println("Seu usuario está sendo atualizado...");
        for(int i = 0; i < clientes.size(); i++){
            if(clientes.get(i).cpf.equals(cliente.cpf)){
                clientes.remove(clientes.get(i));
                clientes.add(cliente);
            }

        }

    }

    /**
     * Este método é responsável por remover um cliente do registro de clientes.
     * @param cliente
     */
    public void removerCliente(Cliente cliente){
        System.out.println("Seu usuario está sendo apagado...");
        for(int i = 0; i < this.clientes.size(); i++){
            if(this.clientes.get(i).cpf.equals(cliente.cpf)){
                this.clientes.remove(this.clientes.get(i));
            }
        }
    }

    /**
     * Método responsável por checar se o cpf já existe no registro.
     * @param cpf
     * @return
     */
    public boolean checarCpf(String cpf){
        for (Cliente cliente : clientes){
            if(cliente.cpf.equals(cpf)){
                return false;
            }
        }
        return true;
    }

    /**
     * Este método é responsável por visualizar contas de um determinado cliente.
     * @param cliente
     */
    public void visualizarContas(Cliente cliente){
        for(Conta conta : cliente.getContas()){
            if(conta.getClass() == ContaPoupanca.class){
                System.out.println("[ Conta" + ((ContaPoupanca) conta).getTipoDaConta() + " no: " + conta.getNumeroConta() + " | Saldo: " + new DecimalFormat("0.00").format( conta.getSaldo()) + " | DividaEmprestimo: " + new DecimalFormat("0.00").format(conta.getDividaDeEmprestimo()) + " | Cartao Debito no: " + conta.getCartao().getNumero() +"| csv: "+ conta.getCartao().getCsv() + " ]");
            }
            else{
                System.out.println("[ Conta" + ((ContaCorrente) conta).getTipoDaConta() + "no: " + conta.getNumeroConta() + " | Saldo: " + new DecimalFormat("0.00").format(conta.getSaldo()) + " | DividaEmprestimo: " + new DecimalFormat("0.00").format(conta.getDividaDeEmprestimo()) + " | Cartao Debito no: " + conta.getCartao().getNumero() +"| csv: "+ conta.getCartao().getCsv() + " | Cartao Credito no: " + ((ContaCorrente) conta).getCartaoCredito().getNumero() + "| csv: "+ ((ContaCorrente) conta).getCartaoCredito().getCsv() + "| fatura: "+  new DecimalFormat("0.00").format(((ContaCorrente) conta).getCartaoCredito().getFatura()) +" ]");
            }
        }
    }

    /**
     * Este método é responsável por visualizar detalhes de um cliente do registro a partir do seu CPF.
     * @param cpf
     */
    public void visualizarDetalhesDoCliente(String cpf){
        for(Cliente cliente : clientes){
            if(cliente.getClass() == ClienteWinx.class && cliente.cpf.equals(cpf)){
                System.out.println("Nome: " + cliente.getNome() + " | CPF: " + cliente.getCpf() + " | Pontos por compra: " + ((ClienteWinx) cliente).getPontosDeCompra() + "\nContas:");
                visualizarContas(cliente);
            }
            else if(cliente.getClass() == Cliente.class && cliente.cpf.equals(cpf)){
                System.out.println("Nome: " + cliente.getNome() + "| CPF: " + cliente.getCpf() + "\nContas:");
                visualizarContas(cliente);
            }
        }
    }

    /**
     * Método responsável por visualizar os detalhes da avaliação quando um usuário tiver logado.
     * Método criado para a avaliação!
     */
    public void visualizarDetalhesDaAvaliacao(String cpf){
        for(Cliente cliente : clientes){
            if(cliente.cpf.equals(cpf)){
                try {
                    System.out.println("------------------------------------------------\nAvaliacao:");
                    if(cliente.getAvaliacao().getMatricula() == null){
                        throw new AvaliacaoOO2022NaoInformadaException();
                    }
                    System.out.println("Nome: " + cliente.getAvaliacao().getNome() + " | Matricula: " + cliente.getAvaliacao().getMatricula() + " | Nota: "+ cliente.getAvaliacao().getNota());
                }catch (AvaliacaoOO2022NaoInformadaException e){
                    System.out.println(e.getMessage());
                }

            }
        }
    }

    public void construirAvaliacoes() throws IOException, ClassNotFoundException {
        for(Cliente cliente : clientes){
            ArquivoAvaliacao.getInstancia().construirAvaliacao(cliente);
        }
    }

    /**
     * Método responsável por inserir uma avaliação ao banco pelo cliente.
     * Método criado para avaliação!.
     */
    public void inserirAvaliacao(Cliente cliente){
        Scanner sc = new Scanner(System.in);
        Random randomNum = new Random();
        int random = randomNum.nextInt(100000000, 999999999);
        System.out.println("Digite a nota que deseja avaliar o banco.");
        double nota = sc.nextDouble();
        AvaliacaoOO2022 avaliacaoOO2022 = new AvaliacaoOO2022(cliente.nome, String.valueOf(random), nota);
        cliente.setAvaliacao(avaliacaoOO2022);
    }

    /**
     * Este método é responsável por retornar um cliente do registro a partir do CPF.
     * @param cpf
     * @return Cliente
     */
    public Cliente retornarCliente(String cpf){
        for (Cliente cliente : clientes) {
            if(clientes.get(0) == null){
                throw new NullPointerException("A lista esta nula, nao foi possivel buscar cliente");
            }
            else if (cliente.cpf.equals(cpf)) {
                return cliente;
            }
        }
        return null;
    }

    /**
     * Este método é responsável por exibir a lista de clientes registrados.
     */
    public void printarListaDeClientes(){
        System.out.println("------------------ Clientes --------------------");
        for(Cliente cliente : clientes){
            if(cliente.getClass() == ClienteWinx.class){
                System.out.println("Nome: " + cliente.getNome() + " | CPF: " + cliente.getCpf() + " | Pontos por compra: " + ((ClienteWinx) cliente).getPontosDeCompra() + "\nContas:");
            }
            else if(cliente.getClass() == Cliente.class){
                System.out.println("Nome: " + cliente.getNome() + "| CPF: " + cliente.getCpf() + "\nContas:");

            }
            visualizarContas(cliente);
            System.out.println("------------------------------------------------");

        }
    }

    /**
     * Método responsável por resetar o registro de clientes.
     */
    public void limparListaDeClientes(){
        clientes.clear();
    }

    /**
     * Método responsável por adicionar uma coleção inteira ao atributo do tipo ArrayList de clientes da classe para carregar dados registrados em um arquivo de outras vezes que o programa foi executado.
     * @param clientes
     */
    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes.addAll(clientes);
    }


    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    /**
     * Singleton que só permite uma instância do objeto ser criada, quando o atributo estático instancia tem o valor nulo.
     */
    public static RegistroDeClientes getInstancia() {
        if (instancia == null) {
            instancia = new RegistroDeClientes();
        }
        return instancia;
    }
}
