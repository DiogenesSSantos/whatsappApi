# WhatsApp API

API REST para gerenciamento de pacientes e envio de notificações via WhatsApp, desenvolvida para suprir necessidades de contatos com pacientes sobre marcação, reagendamento e outras necessidades de comunicação.

---

## Sumário

- [Visão Geral](#visão-geral)
- [Stack e Dependências](#stack-e-dependências)
- [Arquitetura](#arquitetura)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelo de Dados](#modelo-de-dados)
- [Configuração](#configuração)
- [Instalação e Execução](#instalação-e-execução)
- [Docker](#docker)
- [Endpoints](#endpoints)
- [Testes](#testes)
- [Contribuindo](#contribuindo)

---

## Visão Geral

Principais responsabilidades:

- **Cadastrar pacientes** com dados de contato (celular) e consulta agendada
- **Enviar notificações** via WhatsApp utilizando integração com n8n (automação)
- **Persistir dados** de pacientes, contatos e consultas em MySQL
- **Gerar relatórios** em PDF via JasperReports
- **Documentação interativa** via Swagger/OpenAPI

---

## Stack e Dependências

| Componente | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.3.5 (Web, Data JPA) |
| Build | Maven |
| Banco de Dados | MySQL |
| Migrations | Flyway |
| WhatsApp | Cobalt (Auties00) |
| Automação externa | n8n (via RestTemplate) |
| Documentação API | SpringDoc OpenAPI (Swagger) |
| Relatórios | JasperReports 7.0.1 |
| Testes | JUnit 5 + Testcontainers |
| Utilitários | Lombok |

---

## Arquitetura

O projeto segue **Clean Architecture** (Arquitetura Limpa / Hexagonal / Ports & Adapters), onde o domínio é o centro e dependências apontam de fora para dentro.

### Camadas

```
┌─────────────────────────────────────────────────────────┐
│                    INFRAESTRUTURA                        │
│                                                         │
│  ┌─────────────┐  ┌──────────────┐  ┌───────────────┐  │
│  │ Controllers │  │   Gateways   │  │  Persistence  │  │
│  │  (REST API) │  │  (Adapters)  │  │  (JPA/MySQL)  │  │
│  └──────┬──────┘  └──────┬───────┘  └───────┬───────┘  │
│         │                │                  │           │
│         │    ┌───────────┴──────────┐       │           │
│         │    │    Mappers (DTO↔Model │       │           │
│         │    │    & Model↔Entity)   │       │           │
│         │    └──────────────────────┘       │           │
├─────────┼──────────────────────────────────┼───────────┤
│         │          APLICAÇÃO               │           │
│         │                                  │           │
│  ┌──────┴──────────────────────┐  ┌────────┴────────┐  │
│  │      Use Cases              │  │  Input Ports    │  │
│  │  (CriarPaciente,            │  │  (interfaces)   │  │
│  │   NotificarPaciente)        │  │                 │  │
│  └─────────────────────────────┘  └─────────────────┘  │
│                                                         │
├─────────────────────────────────────────────────────────┤
│                       DOMÍNIO                            │
│                                                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐  │
│  │ Paciente │  │ Consulta │  │  Contato │  │ Numero │  │
│  │ (Root)   │  │ (Value)  │  │ (Value)  │  │(Value) │  │
│  └──────────┘  └──────────┘  └──────────┘  └────────┘  │
│                                                         │
│  Exceções de Domínio: CelularInvalidoException,         │
│  DataPassadoException, PacienteBuilderException          │
│                                                         │
│  ResultadoEnvio (sealed interface): Sucesso | Falha      │
└─────────────────────────────────────────────────────────┘
```

### Princípios aplicados

- **Inversão de Dependência**: Domain e Application dependem de interfaces (Ports), não de implementações concretas
- **Separation of Concerns**: Cada camada tem responsabilidade única
- **Value Objects**: `Consulta`, `Contato`, `Numero` são imutáveis com validação embutida
- **Aggregate Root**: `Paciente` é a raiz do agregado, construída via Builder pattern
- **Mappers dedicados**: `PacienteControllerMapper` (DTO↔Model) e `PacienteEntityMapper` (Model↔Entity) isolam a conversão entre camadas

### Fluxo de uma requisição

```
HTTP Request → Controller → Mapper (DTO→Model) → Use Case → Gateway (Port) → Mapper (Model→Entity) → JPA Repository → MySQL
```

---

## Estrutura do Projeto

```
src/main/java/com/github/dio/mensageria/
├── Start.java                          # Ponto de entrada (@SpringBootApplication)
│
├── domain/
│   ├── paciente/
│   │   ├── Paciente.java               # Aggregate Root (Builder pattern)
│   │   ├── PacienteBuilderException.java
│   │   ├── consulta/
│   │   │   ├── Consulta.java           # Value Object (Status: MARCADO, AGUARDANDO, etc.)
│   │   │   └── DataPassadoException.java
│   │   └── contato/
│   │       ├── Contato.java            # Value Object (lista de números + bairro)
│   │       ├── Numero.java             # Value Object (validação regex de celular BR)
│   │       └── CelularInvalidoException.java
│   └── mensagem/
│       └── ResultadoEnvio.java         # Sealed interface (Sucesso | Falha)
│
├── application/
│   ├── usecases/
│   │   ├── CriarPaciente.java          # Caso de uso: cadastrar paciente
│   │   └── NotificarPaciente.java      # Caso de uso: enviar notificação WhatsApp
│   └── gateways/
│       ├── input/
│       │   └── CriarPacienteUseCase.java   # Porta de entrada (interface)
│       └── output/
│           ├── PacienteRepository.java     # Porta de saída: persistência
│           └── Mensageria.java             # Porta de saída: envio de mensagens
│
├── infra/
│   ├── config/
│   │   ├── ConfigBeans.java            # Wiring manual de beans (@Configuration)
│   │   └── SwaggerConfig.java          # Configuração OpenAPI/Swagger
│   ├── controller/
│   │   ├── pacientecontroller/
│   │   │   ├── PacienteController.java         # REST controller (POST /api/pacientes)
│   │   │   ├── PacienteControllerMapper.java   # DTO ↔ Model mapper
│   │   │   ├── request/
│   │   │   │   ├── PacienteDTORequest.java
│   │   │   │   ├── ContatoDTORequest.java
│   │   │   │   ├── NumeroDTORequest.java
│   │   │   │   ├── ConsultaDTORequest.java
│   │   │   │   └── StatusDTORequest.java
│   │   │   └── response/
│   │   │       ├── PacienteDTOResponse.java
│   │   │       ├── ContatoDTOResponse.java
│   │   │       ├── NumeroDTOResponse.java
│   │   │       ├── ConsultaDTOResponse.java
│   │   │       └── StatusDTOReponse.java
│   │   └── advice/
│   │       ├── GlobalExceptionHandler.java   # Tratamento global de exceções
│   │       └── ErrorBodyResponse.java
│   ├── documentation/
│   │   └── PacienteControllerSwaggerOpenAPI.java  # Anotações Swagger
│   ├── gateways/
│   │   ├── PacienteRepositoryJPA.java     # Implementação de PacienteRepository
│   │   ├── PacienteEntityMapper.java      # Model ↔ Entity mapper
│   │   └── MensageriaN8N.java            # Implementação de Mensageria (n8n)
│   └── persistence/
│       ├── PacienteEntityRepository.java  # Spring Data JPA repository
│       └── entity/
│           ├── PacienteEntity.java        # @Entity (tb_paciente)
│           ├── ConsultaEmbeddable.java    # @Embeddable (valores de consulta)
│           ├── ContatoEmbeddable.java     # @Embeddable (valores de contato)
│           └── NumeroEmbeddable.java      # @Embeddable (valores de número)
```

---

## Modelo de Dados

### Tabelas (Flyway migrations)

```sql
-- Tabela principal
tb_paciente
├── id              BIGINT (PK, auto-increment)
├── codigo          VARCHAR(36) UNIQUE (UUID)
├── nome_paciente   VARCHAR(255)
├── bairro          VARCHAR(255)
├── consulta_nome   VARCHAR(255)
├── data_atendimento DATETIME
├── data_marcacao   DATETIME
└── status          VARCHAR(50)

-- Telefones (1:N via @ElementCollection)
paciente_telefones
├── id              BIGINT (PK, auto-increment)
├── paciente_id     BIGINT (FK → tb_paciente)
├── celular         VARCHAR(32)
└── is_whatsapp     BOOLEAN
```

### Valores possíveis para `Consulta.Status`

| Status | Descrição |
|---|---|
| `MARCADO` | Consulta marcada, aguardando notificação |
| `AGUARDANDO` | Notificação enviada, aguardando resposta |
| `NAO_POSSUI_WHATSAPP` | Paciente não possui WhatsApp |
| `REJEITADO` | Paciente rejeitou a consulta |

### Validação de número de celular

Formato: `55` + `DDD` (2 dígitos) + `número` (8 ou 9 dígitos)

Regex: `^55[1-9][0-9](?:9\d{8}|\d{8})$`

Exemplos válidos: `5581987654321`, `5511998765432`

---

## Configuração

### Variáveis de ambiente

| Variável | Descrição | Exemplo |
|---|---|---|
| `SPRING_DATASOURCE_URL` | JDBC URL do banco | `jdbc:mysql://localhost:3306/api-whatsapp-clean` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `root` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `****` |
| `SPRING_PROFILES_ACTIVE` | Perfil ativo | `dev`, `prod`, `test` |

### application.properties (padrão)

```properties
spring.application.name=whatsappApi
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/api-whatsapp-clean?createDatabaseIfNotExist=true
spring.jpa.hibernate.ddl-auto=update
springdoc.swagger-ui.path=/swagger-ui.html
```

### Swagger/OpenAPI

A documentação interativa da API fica disponível em:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

---

## Instalação e Execução

### Pré-requisitos

- Java 21+
- Maven (wrapper incluso: `mvnw` / `mvnw.cmd`)
- Docker (opcional, para banco local ou testes com Testcontainers)

### Build e execução (Windows)

```powershell
# Build (pulando testes)
.\mvnw.cmd clean package -DskipTests

# Executar via Spring Boot
.\mvnw.cmd spring-boot:run

# Ou gerar JAR e executar
.\mvnw.cmd clean package -DskipTests
java -jar target/whatsappApi-0.0.1-SNAPSHOT.jar
```

### Build e execução (Linux/Mac)

```bash
./mvnw clean package -DskipTests
java -jar target/whatsappApi-0.0.1-SNAPSHOT.jar
```

---

## Docker

### Build da imagem

```powershell
docker build -t whatsappapi .
```

### Executar

```powershell
docker run -p 8080:8080 `
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host:3306/whatsappdb `
  -e SPRING_DATASOURCE_USERNAME=myuser `
  -e SPRING_DATASOURCE_PASSWORD=mypass `
  whatsappapi
```

---

## Endpoints

### `POST /api/pacientes` — Criar paciente

**Request Body:**

```json
{
  "nome": "João Silva",
  "contato": {
    "numerosCelular": [
      { "celular": "5581987654321", "isWhatsapp": true }
    ],
    "bairro": "Centro"
  },
  "consulta": {
    "nome": "Consulta Clínica",
    "dataAtendimento": "2026-07-15T14:30:00",
    "dataMarcacao": "2026-06-24T10:00:00",
    "status": "MARCADO"
  }
}
```

**Response (201 Created):**

```json
{
  "nome": "João Silva",
  "contato": {
    "numerosCelular": [
      { "celular": "5581987654321", "whatsapp": true }
    ],
    "bairro": "Centro"
  },
  "consulta": {
    "nome": "Consulta Clínica",
    "dataAtendimento": "2026-07-15T14:30:00",
    "dataMarcacao": "2026-06-24T10:00:00",
    "status": "MARCADO"
  }
}
```

**Erros possíveis:**

| HTTP Status | Causa |
|---|---|
| 400 | Nome em branco, contato nulo, consulta nula |
| 400 | Número de celular inválido (formato) |
| 422 | Data de atendimento no passado |
| 409 | Violação de integridade (ex: código duplicado) |
| 500 | Erro interno do servidor |

---

## Testes

### Executar todos os testes

```powershell
.\mvnw.cmd test
```

### Executar teste específico

```powershell
.\mvnw.cmd -Dtest=CriarPacienteUseCaseTest test
```

### Estrutura dos testes

```
src/test/java/
├── domain/
│   ├── PacienteTest.java              # Testes unitários do agregado
│   ├── consulta/
│   │   └── ConsultaTest.java          # Testes unitários da consulta
│   └── contato/
│       ├── ContatoTest.java           # Testes unitários do contato
│       └── NumeroTest.java            # Testes unitários do número
├── persistence/
│   └── PacienteEntityRepositoryTest.java  # Testes de repositório
├── integration/
│   ├── usecase/
│   │   └── CriarPacienteUseCaseTest.java  # Integração: caso de uso
│   ├── mapper/
│   │   └── PacienteControllerMapperTest.java  # Integração: mapper
│   └── controller/
│       └── PacienteControllerIntegrationTest.java  # Integração: controller
└── config/
    └── TestcontainersConfig.java      # Configuração Testcontainers (MySQL)
```

> **Nota**: Os testes de integração utilizam Testcontainers. O Docker deve estar disponível e rodando para que executem corretamente.

---

## Contribuindo

1. Fork e clone o repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Adicione testes cobrindo as mudanças
4. Certifique-se que os testes passam (`.\mvnw.cmd test`)
5. Abra um Pull Request com descrição clara das mudanças

---

## Contato

**Autor:** Diógenes Santos
- GitHub: [DiogenesSSantos](https://github.com/DiogenesSSantos)

---

## Licença

MIT
