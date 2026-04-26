package br.com.banco.resource;

import br.com.banco.entity.Cliente;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @POST
    @Path("/login")
    @PermitAll
    public Response login(Map<String, String> credenciais) {
        String email = credenciais.get("email");
        String senha = credenciais.get("senha");

        if ("gerente@banco.com".equals(email) && "admin123".equals(senha)) {
            String token = Jwt.upn(email)
                    .groups(new HashSet<>(Arrays.asList("GERENTE")))
                    .expiresIn(3600)
                    .sign();

            return Response.ok(Map.of("token", token)).build();
        }

        Cliente cliente = Cliente.find("email = ?1 and senha = ?2", email, senha).firstResult();

        if (cliente != null) {
            String token = Jwt.upn(cliente.email)
                    .groups(new HashSet<>(Arrays.asList("CLIENTE")))
                    .claim("clienteId", cliente.id)
                    .expiresIn(3600)
                    .sign();

            return Response.ok(Map.of("token", token)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("erro", "Credenciais inválidas. Verifique seu e-mail e senha."))
                .build();
    }
}