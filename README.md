# 💳 Payments API — Desafio Backend PicPay


![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x--4.x-brightgreen?style=flat-square&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?style=flat-square&logo=docker)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

API de pagamentos de alta performance desenvolvida em **Java 17** e **Spring Boot**, que permite transferências financeiras entre usuários comuns e lojistas, com validação de regras de negócio, autorizador externo, resiliência assíncrona para notificações e observabilidade completa.

---

## 📐 Fluxo da Aplicação & Arquitetura

O sistema adota uma **Arquitetura em Camadas (Layered Architecture)** organizada em controllers, services, repositories, DTOs e mappers, seguindo as diretrizes do **Clean Code** e **SOLID**.

### 🔄 Fluxo de Liquidação de Transferência

1. **`POST /transactions/transfer`**: O cliente envia os dados da transferência (`amount`, `senderId`, `receiverId`).
2. **Validação de Domínio**:
   - O `TransactionService` busca os clientes pagador e recebedor.
   - Valida se o pagador é do tipo **COMUM** (Lojistas não podem realizar transferências).
   - Valida se o pagador possui **saldo suficiente**.
3. **Autorização Externa**: O serviço consulta o `AuthorizationService`.
4. **Liquidação Atômica (`@Transactional`)**:
   - Debita o valor do saldo do pagador e credita no recebedor.
   - Persiste a nova transação no banco de dados **H2**.
5. **Notificação Assíncrona (`@Async`)**:
   - Dispara uma notificação para o recebedor sem bloquear a resposta HTTP principal.
6. **Resposta HTTP**: Retorna o DTO da transação realizada (`200 OK`) ou o erro correspondente (`403 Forbidden`, `422 Unprocessable Content`).

```text
   ┌─────────────────┐
   │  CLIENTE / APP  │
   └────────┬────────┘
            │  POST /transactions/transfer
            ▼
 ┌─────────────────────────────────────────────────────────────┐
 │                    TransactionController                    │
 └──────────────────────────┬──────────────────────────────────┘
                            │  realizarTransferencia(dto)
                            ▼
 ┌─────────────────────────────────────────────────────────────┐
 │                     TransactionService                      │
 │   • Validação de Saldo                                      │
 │   • Validação de Autorização (Bloqueia Lojistas)            │
 └──────┬───────────────────┬───────────────────┬──────────────┘
        │                   │                   │
        │ 1. Autorização    │ 2. Persistência   │ 3. Notificação
        ▼                   ▼                   ▼
 ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
 │Authorization │    │   Banco H2   │    │ Notification │
 │   Service    │    │  (Database)  │    │   Service    │
 └──────────────┘    └──────────────┘    └──────────────┘
  (External Mock)     (Debita/Credita)     (@Async Event)
```

---

## 🧩 Princípios SOLID & Design Patterns Aplicados

- **Single Responsibility Principle (SRP)**:
  - `TransactionController`: Apenas gerenciamento de rotas e status HTTP.
  - `TransactionService`: Regras de negócio de liquidação e orquestração.
  - `GlobalUserExceptionHandler`: Captura centralizada e padronizada de exceções.
- **Dependency Inversion Principle (DIP)**:
  - Injeção de dependências realizada 100% via construtores Java.
- **DTO Pattern (Data Transfer Object)**:
  - O isolamento entre entidades do domínio (`User`, `Transaction`) e contratos externos via DTOs (`TransactionRequestDTO`, `UserResponseDTO`).
- **Mapper Pattern**:
  - `UserResponseMapper` e `TransactionResponseMapper` isolando a conversão dos modelos.
- **Resiliency & Async Pattern**:
  - Processamento de notificações via `@Async` isolando o fluxo crítico da transação financeira.

---

## 🛠️ Tecnologias e Ferramentas

- **Java 17** (LTS)
- **Spring Boot 3.x / 4.x** (Spring Web MVC, Spring Data JPA, Spring Cache)
- **Caffeine Cache** (In-memory caching para alta performance)
- **Spring Boot Actuator & Micrometer** (Métricas Prometheus e métricas de banco/DataSource)
- **OpenAPI 3 / Swagger UI** (Documentação interativa da API)
- **H2 Database** (Desenvolvimento/Testes locais)
- **JUnit 5 & Mockito** (Suíte completa de testes unitários e de integração)
- **Docker & Docker Compose** (Containerização pronta para produção)

---

## 🚀 Como Executar a Aplicação

### Pré-requisitos
- Docker e Docker Compose **ou** Java 17 e Maven.

### Opção 1: Com Docker (Recomendado)

```bash
# 1. Clone o repositório
git clone https://github.com/luizfernandoOliveiraa/Desafio-Backend-PicPay.git
cd Desafio-Backend-PicPay

# 2. Suba o container da aplicação
docker-compose up --build
```
A aplicação estará disponível em `http://localhost:8080`.

### Opção 2: Localmente via Maven Wrapper

```bash
# Executar a aplicação
./mvnw spring-boot:run

# Executar a suíte de testes automatizados
./mvnw clean test
```

---

## 📚 Documentação da API (Swagger) & Observabilidade

| Recurso | Endpoint | Descrição |
| :--- | :--- | :--- |
| **Swagger UI** | `http://localhost:8080/swagger-ui.html` | Interface interativa para testar os endpoints da API |
| **OpenAPI Specs** | `http://localhost:8080/v3/api-docs` | Especificação JSON da API |
| **H2 Console** | `http://localhost:8080/h2-console` | Console do banco em memória (JDBC URL: `jdbc:h2:mem:testdb`, User: `sa`, Pass: `password`) |
| **Health Check** | `http://localhost:8080/actuator/health` | Status de saúde da aplicação e conexões |
| **Metrics Prometheus** | `http://localhost:8080/actuator/prometheus` | Exportador de métricas no padrão Prometheus |

---

## 📋 Endpoints RESTful

| Recurso | Método HTTP | Endpoint | Descrição |
| :--- | :--- | :--- | :--- |
| **Usuários** | `POST` | `/users` | Cadastra um novo cliente |
| **Usuários** | `GET` | `/users` | Lista todos os clientes cadastrados |
| **Usuários** | `GET` | `/users/{id}` | Busca os detalhes de um cliente por ID |
| **Usuários** | `PUT` | `/users/{id}` | Atualiza o cadastro de um cliente |
| **Usuários** | `DELETE` | `/users/{id}` | Deleta um cliente por ID |
| **Transações** | `POST` | `/transactions/transfer` | Realiza uma transferência financeira |
| **Transações** | `GET` | `/transactions` | Lista todas as transações realizadas |
| **Transações** | `GET` | `/transactions/{id}` | Busca detalhes de uma transação por ID |

### 1. Criar Usuário (`POST /users`)
```json
{
  "firstName": "Luiz",
  "lastName": "Oliveira",
  "documentoIdentificacao": "12345678901",
  "email": "luiz@example.com",
  "senha": "password123",
  "saldo": 500.00,
  "tipoCliente": "COMUM",
  "tipoPessoa": "PF"
}
```

### 2. Realizar Transferência (`POST /transactions/transfer`)
```json
{
  "amount": 100.00,
  "senderId": 1,
  "receiverId": 2
}
```

#### Respostas de Erro Mapeadas:
- `422 Unprocessable Content`: Saldo insuficiente para realizar a transferência.
- `403 Forbidden`: Lojistas não podem realizar transferências ou falha no autorizador externo.
- `404 Not Found`: Usuário ou transação não encontrada.
- `409 Conflict`: Usuário com CPF/CNPJ ou Email já cadastrado.

---

## 🔮 Evolução da Arquitetura (Visão Futura & Escala)

1. **Mensageria Assíncrona (RabbitMQ / Apache Kafka)**:
   - Substituição do acoplamento HTTP no envio de notificação por publicação de eventos (`TransactionCreatedEvent`).
2. **Arquitetura de Microsserviços**:
   - Divisão em dois serviços independentes: `Wallet-Service` (gerenciamento financeiro) e `Notification-Service` (envio de emails/SMS).
3. **Padrão CQRS (Command Query Responsibility Segregation)**:
   - Separação de banco de dados otimizado para gravação de transações (Relacional) e banco orientado à leitura para relatórios e extratos (ex.: MongoDB/Elasticsearch).

---

## 📄 Licença

Este projeto está sob a licença MIT. Sinta-se livre para usar e modificar.
