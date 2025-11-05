## Projeto Spring Boot que calcula os produtores com **menor** e **maior** intervalo da lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards.

---

## Como rodar a aplicação

> Requisitos: **Java 21** e **Maven** (ou use o Maven Wrapper do projeto).

### Rode o comando abaixo em um terminal
```bash
./mvnw spring-boot:run
```

A aplicação sobe por padrão em: **http://localhost:8080**

### Como trocar a porta
- Defina no `application.yml`:
  ```yaml
  server:
    port: 9090
  ```

---

## Como rodar os testes de integração

### Todos os testes
```bash
./mvnw test
```

---

## Banco de dados (H2 em memória)

- **URL JDBC**: `jdbc:h2:mem:worst-movie`
- **Driver**: `org.h2.Driver`
- **Usuário**: `sa`
- **Senha**: `123`

### Após subir a aplicação, acesse no navegador:
- **H2 Console**: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:worst-movie`
- User Name: `sa`
- Password: `123`

Os dados são carregados a partir do arquivo `Movielist.csv` via `data.sql` na inicialização.

---

## Endpoint principal

### `GET /producers`
Retorna os produtores com **menor** e **maior** intervalo.

**Exemplo de resposta:**
```json
[
  {
    "min": [
      {
        "producer": "Nome",
        "interval": 1,
        "previousWin": 1990,
        "followingWin": 1991
      }
    ],
    "max": [
      {
        "producer": "Nome",
        "interval": 9,
        "previousWin": 1985,
        "followingWin": 1994
      }
    ]
  }
]
```

---

## Testando via Postman ou cURL

### cURL
```bash
curl --location 'localhost:8080/producers'
```

### Postman
1. Abra o Postman e crie uma nova **request**.
2. Método: **GET**  
   URL: `http://localhost:8080/producers`
