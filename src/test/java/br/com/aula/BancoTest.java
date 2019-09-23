package br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import br.com.aula.exception.ContaJaExistenteException;
import br.com.aula.exception.ContaNaoExistenteException;
import br.com.aula.exception.ContaSemSaldoException;
import br.com.aula.exception.NumeroContaInvalidoException;
import br.com.aula.exception.TransferenciaComValorNegativoException;

public class BancoTest {

	@Test
	public void deveCadastrarConta() throws ContaJaExistenteException, NumeroContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroRepetido() throws ContaJaExistenteException, NumeroContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}

	// TODO: HOMEWORK
	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNomeClienteRepetido()
			throws ContaJaExistenteException, NumeroContaInvalidoException {
		// Cenário
		Cliente cliente = new Cliente("Rafael");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Conta conta2 = new Conta(cliente, 456, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();
		// Ação
		banco.cadastrarConta(conta);
		banco.cadastrarConta(conta2);
		// Verificação
		Assert.fail();
	}

	// TODO: HOMEWORK - (Alterar a implementação para tratar esse caso)
	@Test(expected = NumeroContaInvalidoException.class)
	public void naoDeveCadastrarContaComNumeroInvalido()
			throws ContaJaExistenteException, NumeroContaInvalidoException {
		// Cenário
		Cliente cliente = new Cliente("Rafael");
		Conta conta = new Conta(cliente, -1, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();
		// Ação
		banco.cadastrarConta(conta);
		// Verificação
		Assert.fail();
	}

	@Test
	public void deveEfetuarTransferenciaContasCorrentes() throws ContaJaExistenteException, ContaSemSaldoException,
			ContaNaoExistenteException, TransferenciaComValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, banco.obterContaPorNumero(contaOrigem.getNumeroConta()).getSaldo());
		assertEquals(100, banco.obterContaPorNumero(contaDestino.getNumeroConta()).getSaldo());
	}

	// TODO: HOMEWORK - (Alterar a implementação para tratar esse caso)
	@Test(expected = TransferenciaComValorNegativoException.class)
	public void naoDeveEfetuarTransferenciaComValorNegativo() throws ContaJaExistenteException, ContaSemSaldoException,
			ContaNaoExistenteException, TransferenciaComValorNegativoException, NumeroContaInvalidoException {
		// Cenário
		Cliente cliente = new Cliente("Rafael");
		Cliente cliente2 = new Cliente("Derblaz");
		Conta conta = new Conta(cliente, 123, 200, TipoConta.POUPANCA);
		Conta conta2 = new Conta(cliente2, 456, 200, TipoConta.CORRENTE);
		Banco banco = new Banco();
		banco.cadastrarConta(conta);
		banco.cadastrarConta(conta2);
		// Ação
		banco.efetuarTransferencia(123, 456, -100);
		// Verificação
		Assert.fail();

	}

	// Verificação
	@Test(expected = ContaSemSaldoException.class)
	public void naoDeveEfetuarTransferenciaContaOrigemPoupancaSemSaldo() throws ContaJaExistenteException,
			ContaSemSaldoException, ContaNaoExistenteException, TransferenciaComValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 99, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		Assert.fail();
	}

	// Verificação
	@Test(expected = ContaNaoExistenteException.class)
	public void naoDeveEfetuarTransferenciaContaOrigemNaoExistente() throws ContaJaExistenteException,
			ContaSemSaldoException, ContaNaoExistenteException, TransferenciaComValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 99, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(111, 456, 100);

		Assert.fail();
	}

	// TODO: HOMEWORK
	@Test(expected = ContaNaoExistenteException.class)
	public void naoDeveEfetuarTransferenciaContaDestinoNaoExistente()
			throws ContaJaExistenteException, ContaSemSaldoException, ContaNaoExistenteException, NumeroContaInvalidoException, TransferenciaComValorNegativoException {
		//Cenário
		Cliente cliente = new Cliente("Rafael");
		Conta conta = new Conta(cliente, 123, 200, TipoConta.CORRENTE);
		Banco banco = new Banco();
		banco.cadastrarConta(conta);
		//Ação
		banco.efetuarTransferencia(123, 456, 100);
		//Verificação
		Assert.fail();
		
	}
}
