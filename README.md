# BG Bank - Sistema Bancário

Projeto desenvolvido para a disciplina DIM0517 - GERÊNCIA DE CONFIGURAÇÃO E MUDANÇAS (UFRN).

## Equipe
* **Ana Beatriz Camilo da Costa** (20230081544) - [BeatrizCamlo](https://github.com/BeatrizCamlo)
* **Francisca Gabrielly Lopes Freire** (20230034464) - [gabrielly-freire](https://github.com/gabrielly-freire)

## Stack Tecnológica
* **Linguagem:** Java 21
* **Framework:** Spring Boot 4.0
* **Build Tool:** Maven
* **Testes:** JUnit 5
* **Documentação:** SpringDoc OpenAPI (Swagger UI)
* **Containerização:** Docker

## Imagem Docker

A imagem pública da aplicação está disponível no Docker Hub:

**[beatrizcamilo/bg-bank](https://hub.docker.com/r/beatrizcamilo/bg-bank)**

```bash
docker pull beatrizcamilo/bg-bank:latest
```

## Como executar

### Pré-requisitos
* Java 21 instalado
* Maven instalado
* Docker (opcional)

---

### Instrução para Git Hooks Locais

Para a verificação local dos commits, entre no Git Bash, certifique-se que está na raiz do projeto e insira:

**Para copiar e conceder permissão de execução:**
```bash
chmod +x scripts/setup-hooks.sh && ./scripts/setup-hooks.sh
```

### Execução via Docker (recomendado)

**Usando a imagem do Docker Hub:**
```bash
docker run -p 8080:8080 beatrizcamilo/bg-bank:latest
```

**Usando Docker Compose (build local):**
```bash
docker compose up --build
```

A aplicação ficará disponível em `http://localhost:8080`.

---

### Execução via Maven

1. **Compilar e iniciar a aplicação:**
    ```bash
    mvn spring-boot:run
    ```

2. Ou compilar e executar o JAR:
    ```bash
    mvn clean package -DskipTests
    java -jar target/bgbank-1.0-SNAPSHOT.jar
    ```

A aplicação ficará disponível em `http://localhost:8080`.

---

### Documentação interativa (Swagger UI)

Com a aplicação em execução, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Endpoints da API

Base URL: `http://localhost:8080/banco/conta`

| Ação                    | Método | Endpoint                     |
|-------------------------|--------|------------------------------|
| Cadastrar conta         | `POST` | `/banco/conta`               |
| Consultar conta         | `GET`  | `/banco/conta/{id}`          |
| Consultar saldo         | `GET`  | `/banco/conta/{id}/saldo`    |
| Creditar valor          | `PUT`  | `/banco/conta/{id}/credito`  |
| Debitar valor           | `PUT`  | `/banco/conta/{id}/debito`   |
| Transferir entre contas | `PUT`  | `/banco/conta/transferencia` |
| Render juros            | `PUT`  | `/banco/conta/rendimento`    |

---

### Executando os testes

```bash
mvn test
```
