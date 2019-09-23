package br.com.aula;

import java.util.ArrayList;
import java.util.List;

import br.com.aula.exception.ContaJaExistenteException;
import br.com.aula.exception.ContaNaoExistenteException;
import br.com.aula.exception.ContaSemSaldoException;
import br.com.aula.exception.NumeroContaInvalidoException;
import br.com.aula.exception.TransferenciaComValorNegativoException;

public class Banco {

	private List<Conta> contas = new ArrayList<Conta>();

	public Banco() {
	}

	public Banco(List<Conta> contas) {
		this.contas = contas;
	}

	public void cadastrarConta(Conta conta) throws ContaJaExistenteException, NumeroContaInvalidoException {

		for (Conta c : contas) {
			boolean isNomeClienteIgual = c.getCliente().getNome().equals(conta.getCliente().getNome());
			boolean isNumeroContaIgual = c.getNumeroConta() == conta.getNumeroConta();

			if (isNomeClienteIgual || isNumeroContaIgual) {
				throw new ContaJaExistenteException();
			}
		}
		
		if(conta.getNumeroConta() < 1) {
			throw new NumeroContaInvalidoException();
		}

		this.contas.add(conta);
	}

	public void efetuarTransferencia(int numeroContaOrigem, int numeroContaDestino, int valor)
			throws ContaNaoExistenteException, ContaSemSaldoException, TransferenciaComValorNegativoException {

		Conta contaOrigem = this.obterContaPorNumero(numeroContaOrigem);
		Conta contaDestino = this.obterContaPorNumero(numeroContaDestino);

		boolean isContaOrigemExistente = contaOrigem != null;
		boolean isContaDestinoExistente = contaDestino != null;

		if (isContaOrigemExistente && isContaDestinoExistente) {

			boolean isContaOrigemPoupança = contaOrigem.getTipoConta().equals(TipoConta.POUPANCA);
			boolean isSaldoContaOrigemNegativo = contaOrigem.getSaldo() - valor < 0;
			boolean isValorNegativo = valor < 0;

			if (isContaOrigemPoupança && isSaldoContaOrigemNegativo) {
				throw new ContaSemSaldoException();
			}
			
			if (isValorNegativo) {
				throw new TransferenciaComValorNegativoException();
			}

			contaOrigem.debitar(valor);
			contaDestino.creditar(valor);

		} else {
			throw new ContaNaoExistenteException();
		}
	}

	public Conta obterContaPorNumero(int numeroConta) {

		for (Conta c : contas) {
			if (c.getNumeroConta() == numeroConta) {
				return c;
			}
		}

		return null;
	}

	public List<Conta> obterContas() {
		return this.contas;
	}
}
