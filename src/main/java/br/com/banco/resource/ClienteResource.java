package br.com.banco.resource;

import br.com.banco.entity.Cliente;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("GERENTE")
public class ClienteResource {

    @POST
    @Transactional
    public Response criarCliente(@Valid Cliente cliente) {
        cliente.persist();

        return Response.status(Response.Status.CREATED).entity(cliente).build();
    }

    @GET
    public List<Cliente> listarClientes() {
        return Cliente.listAll();
    }

    @GET
    @Path("/{id}")
    public Response buscarClientePorId(@PathParam("id") Long id) {
        Cliente cliente = Cliente.findById(id);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(cliente).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizarCliente(@PathParam("id") Long id, @Valid Cliente dadosAtualizados) {
        Cliente clienteSalvo = Cliente.findById(id);
        if (clienteSalvo == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (dadosAtualizados.cpf != null && !dadosAtualizados.cpf.equals(clienteSalvo.cpf)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erro\": \"O CPF não pode ser alterado.\"}")
                    .build();
        }

        clienteSalvo.nome = dadosAtualizados.nome;
        clienteSalvo.email = dadosAtualizados.email;
        clienteSalvo.senha = dadosAtualizados.senha;

        return Response.ok(clienteSalvo).build();
    }
}