package br.com.banco.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transacao extends PanacheEntity {

    @NotNull(message = "O tipo de transação é obrigatório")
    @Enumerated(EnumType.STRING)
    public TipoTransacao tipo;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor da transação deve ser maior que zero")
    public BigDecimal valor;

    public LocalDateTime dataHora = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "conta_origem_id")
    public Conta conta;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    public Conta contaDestino;

    public enum TipoTransacao {
        DEPOSITO, SAQUE, TRANSFERENCIA
    }
}