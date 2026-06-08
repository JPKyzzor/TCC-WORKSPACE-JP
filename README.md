# TCC Workspace

Este projeto possui duas partes:

- `back`: API em Spring Boot.
- `front`: aplicacao web em Angular.

## Requisitos

Antes de rodar o projeto, instale:

- Java 17.
- Node.js e npm.
- PostgreSQL.

## Backend

O backend fica na pasta `back` e roda por padrao em `http://localhost:8080`.

1. Entre na pasta do backend:

```bash
cd back
```

2. Crie o arquivo de variaveis de ambiente a partir do exemplo:

```bash
copy .env-example .env
```

No PowerShell, tambem pode usar:

```powershell
Copy-Item .env-example .env
```

3. Edite o arquivo `.env` com os dados do seu ambiente local:

```env
SPRING_PROFILES_ACTIVE=dev
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/NOME_DO_BANCO
SPRING_DATASOURCE_USERNAME=USUARIO_DO_POSTGRES
SPRING_DATASOURCE_PASSWORD=SENHA_DO_POSTGRES
APP_JWT_SECRET=UMA_CHAVE_GRANDE_COM_PELO_MENOS_32_CARACTERES
APP_JWT_EXPIRATION_MS=28800000
LANGCHAIN4J_GOOGLE_AI_GEMINI_CHAT_MODEL_API_KEY=SUA_CHAVE_DO_GEMINI
LANGCHAIN4J_GOOGLE_AI_GEMINI_CHAT_MODEL_MODEL_NAME=gemini-2.5-flash
APP_CORS_ALLOWED_ORIGINS=http://localhost:4200
```

4. Crie o banco no PostgreSQL com o mesmo nome usado em `SPRING_DATASOURCE_URL`.

5. Rode a API:

```bash
./mvnw spring-boot:run
```

No Windows, se estiver usando PowerShell ou Prompt de Comando:

```powershell
.\mvnw.cmd spring-boot:run
```

## Frontend

O frontend fica na pasta `front` e roda por padrao em `http://localhost:4200`.

1. Entre na pasta do frontend:

```bash
cd front
```

2. Instale as dependencias:

```bash
npm install
```

3. Rode a aplicacao:

```bash
npm start
```

Depois acesse:

```text
http://localhost:4200
```

O frontend local usa a API configurada em `front/src/environments/environment.ts`:

```ts
apiBaseUrl: 'http://localhost:8080'
```

## Ordem recomendada para rodar localmente

1. Inicie o PostgreSQL.
2. Rode o backend em `http://localhost:8080`.
3. Rode o frontend em `http://localhost:4200`.
4. Acesse o frontend no navegador.

## Comandos uteis

Backend:

```bash
cd back
./mvnw test
./mvnw clean package
```

Frontend:

```bash
cd front
npm test
npm run build
```
