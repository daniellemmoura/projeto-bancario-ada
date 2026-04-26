package br.com.banco.service;

import br.com.banco.entity.Conta;
import br.com.banco.entity.Transacao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@ApplicationScoped
public class ContaService {

    @Transactional
    public Conta criarConta(Conta conta) {
        if (conta.titular == null || conta.titular.id == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("erro", "A conta precisa ter um cliente vinculado"))
                            .build()
            );
        }

        conta.saldo = BigDecimal.ZERO;
        conta.persist();
        return conta;
    }

    @Transactional
    public Transacao realizarDeposito(Long contaId, BigDecimal valor) {
        Conta conta = Conta.findById(contaId);

        if (conta == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }

        if (conta.tipo == Conta.TipoConta.ELETRONICA) {
            throw new WebApplicationException(
                    Response.status(422).entity(Map.of("erro", "Conta do tipo ELETRONICA não permite depósitos.")).build()
            );
        }

        conta.saldo = conta.saldo.add(valor);

        Transacao transacao = new Transacao();
        transacao.conta = conta;
        transacao.tipo = Transacao.TipoTransacao.DEPOSITO;
        transacao.valor = valor;
        transacao.dataHora = LocalDateTime.now();
        transacao.persist();

        return transacao;
    }

    @Transactional
    public Transacao realizarSaque(Long contaId, BigDecimal valor) {
        Conta conta = Conta.findById(contaId);

        if (conta == null) {
            throw new WebApplicationException(
                    Response.status(404).entity(Map.of("erro", "Conta não encontrada no sistema")).build()
            );
        }

        if (conta.tipo == Conta.TipoConta.ELETRONICA) {
            throw new WebApplicationException(
                    Response.status(422).entity(Map.of("erro", "Conta do tipo ELETRONICA não permite saques.")).build()
            );
        }

        if (conta.saldo.compareTo(valor) < 0) {
            throw new WebApplicationException(
                    Response.status(422).entity(Map.of("erro", "Saldo insuficiente para realizar o saque.")).build()
            );
        }

        conta.saldo = conta.saldo.subtract(valor);

        Transacao transacao = new Transacao();
        transacao.conta = conta;
        transacao.tipo = Transacao.TipoTransacao.SAQUE;
        transacao.valor = valor;
        transacao.dataHora = LocalDateTime.now();
        transacao.persist();

        return transacao;
    }

    @Transactional
    public Transacao realizarTransferencia(Long origemId, Long destinoId, BigDecimal valor) {
        Conta origem = Conta.findById(origemId);
        Conta destino = Conta.findById(destinoId);

        if (origem == null || destino == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }

        if (origem.saldo.compareTo(valor) < 0) {
            throw new WebApplicationException(
                    Response.status(422).entity(Map.of("erro", "Saldo insuficiente para realizar a transferência.")).build()
            );
        }

        origem.saldo = origem.saldo.subtract(valor);
        destino.saldo = destino.saldo.add(valor);

        Transacao transacao = new Transacao();
        transacao.conta = origem;
        transacao.contaDestino = destino;
        transacao.tipo = Transacao.TipoTransacao.TRANSFERENCIA;
        transacao.valor = valor;
        transacao.dataHora = LocalDateTime.now();
        transacao.persist();

        return transacao;
    }
}