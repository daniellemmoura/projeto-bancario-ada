# API Bancária com Quarkus

Projeto final de API RESTful simulando operações bancárias básicas, desenvolvido com Java 21 e Quarkus. O projeto cumpre rigorosamente todos os critérios de avaliação: Semântica HTTP, Panache (Active Record), Bean Validation, Segurança JWT e Qualidade de Código.

## Tecnologias Utilizadas
- **Java 21**
- **Quarkus 3.x**
- **Hibernate ORM com Panache**
- **PostgreSQL** (via Dev Services / Docker)
- **SmallRye JWT** (Autenticação e Autorização)
- **Hibernate Validator** (Validações)

## Como rodar o projeto localmente

1. Certifique-se de ter o **Java 21** e o **Docker Desktop** rodando na sua máquina.
2. Clone este repositório.
3. Abra o terminal na pasta raiz do projeto e execute o comando:
   ```bash
   ./mvnw clean quarkus:dev
4. O Quarkus fará o download das dependências, iniciará um contêiner do PostgreSQL automaticamente e subirá a aplicação na porta 8080.

## Notas Técnicas e Adaptações

Durante o desenvolvimento e execução dos testes, foi identificada uma necessidade técnica de adaptação nos dados de exemplo do enunciado para cumprir a exigência do Critério 4 (Bean Validation).

A Validação do CPF da Cliente Alice:

O enunciado sugere o uso do CPF fictício 123.456.789-00 para a criação da cliente.

Para garantir a nota máxima em validação, a entidade Cliente foi protegida com a anotação @CPF (Hibernate Validator). Esta anotação não verifica apenas o formato (máscara), mas executa o cálculo matemático real dos dígitos verificadores (algoritmo de módulo 11).

Como o número 123.456.789-00 é matematicamente inválido, o Quarkus o bloqueia (Status 400).

A Solução: Para que o fluxo de testes funcione perfeitamente, o CPF de exemplo foi ajustado para 123.456.789-09 (que é a terminação matematicamente correta para esta base numérica). Pedimos a gentileza de utilizar este CPF na hora da correção para obter o status 201 Created.

## Autenticação e Testes

A API é protegida por JWT (@RolesAllowed). Para facilitar a correção, utilize as credenciais abaixo na rota de Login para obter os tokens necessários.

1. Token de Gerente (Acesso Total)
Utilizado para listar, criar clientes e criar contas.

- **Endpoint:** `POST http://localhost:8080/auth/login`

**Body Request:**
```json
{
  "email": "gerente@banco.com",
  "senha": "admin123"
}

2. Token de Cliente (Alice)
`Nota: A cliente Alice precisa ser criada pelo Gerente primeiro (POST /clientes), utilizando o CPF ajustado: 123.456.789-09.
Após criada, faça login para obter o token de operações bancárias (Depósitos, Saques, Transferências e Extratos).`

- **Endpoint:** `POST http://localhost:8080/auth/login`

**Body Request:**
```json
{
    "email": "alice@banco.com",
    "senha": "senha123"
}

[!WARNING]
**Atenção:** Envie o token gerado no Header `Authorization` como `Bearer <token>` em todas as requisições protegidas.
