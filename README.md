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
- **Enviar notificações** via WhatsApp utilizando Evolution Go API
- **Gerar mensagens personalizadas** via Ollama (LLM local)
- **Persistir dados** de pacientes, contatos e consultas em MySQL
- **Gerar relatórios** em PDF via JasperReports
- **Documentação interativa** via Swagger/OpenAPI

### Fluxo Principal

```
Paciente Cadastrado → Ollama gera variação da mensagem → Evolution Go envia WhatsApp
```

---

## Stack e Dependências

| Componente | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.3.5 (Web, Data JPA) |
| Build | Maven |
| Banco de Dados | MySQL 8.0 |
| Migrations | Flyway |
| WhatsApp | Evolution Go API (whatsmeow) |
| LLM Local | Ollama (llama3, mistral:7b) |
| Automação externa | n8n (webhook receiver) |
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
│  │   NotificarPaciente,        │  │                 │  │
│  │   GeradorDeMensagemAI)      │  │                 │  │
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

### Fluxo de notificação

```
Paciente Cadastrado → NotificarPaciente → MensageriaN8N
    → OllamaHttpGateway (gera variação da mensagem)
    → EvolutionGoClient (envia via WhatsApp)
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
│   │   ├── NotificarPaciente.java      # Caso de uso: enviar notificação WhatsApp
│   │   └── GeradorDeMensagemAI.java    # Caso de uso: gerar mensagem via IA
│   └── gateways/
│       ├── input/
│       │   ├── CriarPacienteUseCase.java    # Porta de entrada (interface)
│       │   └── GerarMensagemAIUseCase.java  # Porta de entrada (interface)
│       └── output/
│           ├── PacienteRepository.java      # Porta de saída: persistência
│           ├── Mensageria.java              # Porta de saída: envio de mensagens
│           └── OllamaGateway.java           # Porta de saída: geração de mensagens
│
├── infra/
│   ├── config/
│   │   ├── ConfigBeans.java            # Wiring manual de beans (@Configuration)
│   │   ├── EvolutionGoConfig.java      # Configuração Evolution Go (@ConfigurationProperties)
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
│   │   ├── MensageriaN8N.java            # Implementação de Mensageria (Evolution Go + Ollama)
│   │   ├── EvolutionGoClient.java        # Cliente HTTP para Evolution Go API
│   │   ├── OllamaHttpGateway.java        # Implementação de OllamaGateway (Java HttpClient)
│   │   └── dto/
│   │       ├── SendTextRequest.java      # DTO para envio de mensagem
│   │       └── SendTextResponse.java     # DTO de resposta do envio
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
| `DB_PASSWORD` | Senha do banco MySQL | `senha_aqui` |
| `EVOLUTION_API_KEY` | Token da instância Evolution Go | `chave_aqui` |
| `SPRING_DATASOURCE_URL` | JDBC URL do banco | `jdbc:mysql://localhost:3306/api-whatsapp-clean` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `root` |
| `SPRING_PROFILES_ACTIVE` | Perfil ativo | `dev`, `prod`, `test` |

### application.properties

```properties
spring.application.name=whatsappApi
server.port=8081

# Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/api-whatsapp-clean?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD:senha_aqui}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true

# Swagger/OpenAPI
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true

# Ollama (LLM Local)
ollama.url=http://localhost:11434
ollama.model=llama3

# Evolution Go (WhatsApp)
evolution.go.base-url=http://localhost:8080
evolution.go.api-key=${EVOLUTION_API_KEY:chave_aqui}
evolution.go.instance=whatsapp-conexao
```

### Documentação da API

A documentação interativa da API fica disponível em:

- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8081/v3/api-docs`

---

## Instalação e Execução

### Pré-requisitos

- Java 21+
- Maven (wrapper incluso: `mvnw` / `mvnw.cmd`)
- Docker (para banco local, Evolution Go e Ollama)
- Ollama (para geração de mensagens via IA)

### Serviços externos

O projeto depende dos seguintes serviços:

| Serviço | Porta | Descrição |
|---|---|---|
| MySQL | 3306 | Banco de dados |
| Evolution Go | 8080 | API WhatsApp |
| Ollama | 11434 | LLM local para geração de mensagens |
| n8n | 5678 | Automação (opcional) |

### Iniciar serviços via Docker

```bash
# Criar rede para os serviços
docker network create whatsapp-network

# MySQL
docker run -d --name mysql-whatsapp \
  --network whatsapp-network \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=api-whatsapp-clean \
  mysql:8.0

# Evolution Go
docker run -d --name evolution-go \
  --network whatsapp-network \
  -p 8080:8080 \
  -e GLOBAL_API_KEY=sua_api_key \
  evoapicloud/evolution-go:latest

# Ollama
docker run -d --name ollama \
  --network whatsapp-network \
  -p 11434:11434 \
  ollama/ollama

# Criar instância no Evolution Go (após iniciar)
curl -X POST http://localhost:8080/instance/create \
  -H "apikey: sua_api_key" \
  -H "Content-Type: application/json" \
  -d '{"name": "whatsapp-conexao"}'

# Baixar modelo no Ollama
docker exec ollama ollama pull llama3
docker exec ollama ollama pull mistral:7b
```

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

### Dockerfile

```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
COPY target/*.jar /api.jar
EXPOSE 8080
CMD ["java", "-jar", "/api.jar"]
```

### Build da imagem

```powershell
docker build -t whatsappapi .
```

### Executar

```powershell
docker run -p 8081:8081 `
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host:3306/api-whatsapp-clean `
  -e SPRING_DATASOURCE_USERNAME=root `
  -e SPRING_DATASOURCE_PASSWORD=sua_senha `
  -e EVOLUTION_API_KEY=sua_api_key `
  -e EVOLUTION_GO_BASE_URL=http://evolution-go:8080 `
  whatsappapi
```

---

## Endpoints

### `POST /api/pacientes` — Criar paciente

Cria um paciente e envia notificação via WhatsApp automaticamente.

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
.\mvnw.cmd -Dtest=PacienteTest test
.\mvnw.cmd -Dtest=MensageriaN8NIntegrationTest test
```

### Estrutura dos testes

```
src/test/java/
├── config/
│   └── TestcontainersConfig.java          # Configuração Testcontainers (MySQL)
├── domain/
│   ├── paciente/
│   │   └── PacienteTest.java             # Testes unitários do agregado
│   ├── consulta/
│   │   └── ConsultaTest.java             # Testes unitários da consulta
│   └── contato/
│       ├── ContatoTest.java              # Testes unitários do contato
│       └── NumeroTest.java               # Testes unitários do número
├── persistence/
│   └── PacienteEntityRepositoryTest.java # Testes de repositório
├── integration/
│   ├── usecase/
│   │   └── CriarPacienteUseCaseTest.java # Integração: caso de uso
│   ├── mapper/
│   │   └── PacienteControllerMapperTest.java  # Integração: mapper
│   └── controller/
│       └── PacienteControllerIntegrationTest.java  # Integração: controller
└── infraestrutura/
    ├── MensageriaN8NIntegrationTest.java  # Integração: envio WhatsApp
    └── OllamaHttpGatewayIntegrationTest.java  # Integração: Ollama
```

### Tipos de teste

| Tipo | Descrição | Pré-requisitos |
|---|---|---|
| Unitário | Testes de domínio (Paciente, Consulta, Contato, Numero) | Nenhum |
| Integração | Testes de repositório, controller, mapper | Docker (Testcontainers) |
| Integração externa | Testes de envio WhatsApp e Ollama | Docker + Evolution Go + Ollama rodando |

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
