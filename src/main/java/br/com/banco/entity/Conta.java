package br.com.banco.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class Conta extends PanacheEntity {

    @NotNull(message = "O número da conta é obrigatório")
    public String numero;

    @NotNull(message = "O tipo de conta é obrigatório")
    @Enumerated(EnumType.STRING)
    public TipoConta tipo;

    @NotNull(message = "O saldo não pode ser nulo")
    public BigDecimal saldo = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @NotNull(message = "A conta precisa pertencer a um cliente")
    public Cliente titular;

    public enum TipoConta {
        CORRENTE, POUPANCA, ELETRONICA
    }
}