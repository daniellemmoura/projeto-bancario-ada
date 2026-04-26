package br.com.banco.resource;

import br.com.banco.entity.Conta;
import br.com.banco.entity.Transacao;
import br.com.banco.service.ContaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.Map;

@Path("/contas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"GERENTE", "CLIENTE"})
public class ContaResource {

    @Inject
    ContaService contaService;

    @POST
    @RolesAllowed("GERENTE")
    public Response criar(@Valid Conta conta) {
        Conta novaConta = contaService.criarConta(conta);
        return Response.status(Response.Status.CREATED).entity(novaConta).build();
    }

    @GET
    public Response listarTodas() {
        return Response.ok(Conta.listAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Conta conta = Conta.findById(id);
        if (conta == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(conta).build();
    }

    @POST
    @Path("/{id}/deposito")
    public Response deposito(@PathParam("id") Long id, Map<String, BigDecimal> body) {
        BigDecimal valor = body.get("valor");
        Transacao t = contaService.realizarDeposito(id, valor);
        return Response.ok(t).build();
    }

    @POST
    @Path("/{id}/saque")
    public Response saque(@PathParam("id") Long id, Map<String, BigDecimal> body) {
        BigDecimal valor = body.get("valor");
        Transacao t = contaService.realizarSaque(id, valor);
        return Response.ok(t).build();
    }

    @POST
    @Path("/{id}/transferencia")
    public Response transferencia(@PathParam("id") Long id, Map<String, Object> body) {
        BigDecimal valor = new BigDecimal(body.get("valor").toString());

        @SuppressWarnings("unchecked")
        Map<String, Object> destinoMap = (Map<String, Object>) body.get("contaDestino");
        Long destinoId = Long.valueOf(destinoMap.get("id").toString());

        Transacao t = contaService.realizarTransferencia(id, destinoId, valor);
        return Response.ok(t).build();
    }
}