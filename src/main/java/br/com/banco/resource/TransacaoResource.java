package br.com.banco.resource;

import br.com.banco.entity.Transacao;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/transacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"GERENTE", "CLIENTE"})
public class TransacaoResource {

    @GET
    @Path("/{id}")
    public Response buscarTransacao(@PathParam("id") Long id) {
        Transacao transacao = Transacao.findById(id);
        if (transacao == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(transacao).build();
    }

    @GET
    public Response listarPorConta(@QueryParam("contaId") Long contaId) {
        if (contaId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erro\": \"É obrigatório informar o contaId na URL\"}")
                    .build();
        }

        List<Transacao> transacoes = Transacao.find("conta.id = ?1 or contaDestino.id = ?1", contaId).list();
        return Response.ok(transacoes).build();
    }
}