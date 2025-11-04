# api de mensagem por email



### como rodar
adicione o email e a senha de app no applicatin.yml.

rode o comando abaixo no terminal na pasta comunicacao_api

``` docker compose --build ```

***

<img src="https://raw.githubusercontent.com/Romariolima1998/api_mensagem/refs/heads/main/api_comunicacao.png">

***
``` http://localhost:8080/comunicacao/agendar ```  metodo ```POST```

cadastra uma mensagem

entrada de dados

```
{
  "dataHoraEnvio": "04-11-2025 12:00:00",
  "nomeDestinatario": "string",
  "emailDestinatario": "romariolima1006@gmail.com",
  "telefoneDestinatario": "string",
  "mensagem": "mensagem que sera enviada por email",
  "modoDeEnvio": "EMAIL"
}

```
retorno
```
{
  "id": 1
  "dataHoraEnvio": "04-11-2025 12:00:00",
  "nomeDestinatario": "string",
  "emailDestinatario": "romariolima1006@gmail.com",
  "telefoneDestinatario": "string",
  "mensagem": "mensagem que sera enviada por email",
  "modoDeEnvio": "EMAIL"
}

```
***

``` http://localhost:8080/comunicacao/cancelar/1  ```  metodo ``` PATCH```
cancela o envio de uma mensagem pelo id da mensagem na uri

retorno

```
{
  "id": 1,
  "dataHoraEnvio": "04-11-2025 11:30:00",
  "nomeDestinatario": "string",
  "emailDestinatario": "romariolima1006@gmail.com",
  "telefoneDestinatario": "string",
  "mensagem": "mensagem",
  "modoDeEnvio": "EMAIL",
  "statusEnvio": "CANCELADO"
}

```

***

``` http://localhost:8080/comunicacao?emailDestinatario=romariolima1006%40gmail.com ``` metodo ``` GET ```

busca as mensagem peelo email passado como parametro

retorno

```
[
  {
    "id": 1,
    "dataHoraEnvio": "04-11-2025 11:30:00",
    "nomeDestinatario": "string",
    "emailDestinatario": "romariolima1006@gmail.com",
    "telefoneDestinatario": "string",
    "mensagem": "mensagem enviada via email",
    "modoDeEnvio": "EMAIL",
    "statusEnvio": "CANCELADO"
  },
  {
    "id": 2,
    "dataHoraEnvio": "04-11-2025 12:00:00",
    "nomeDestinatario": "string",
    "emailDestinatario": "romariolima1006@gmail.com",
    "telefoneDestinatario": "string",
    "mensagem": "mensagem enviada via email",
    "modoDeEnvio": "EMAIL",
    "statusEnvio": "ENVIADO"
  },
  {
    "id": 3,
    "dataHoraEnvio": "04-11-2025 12:00:00",
    "nomeDestinatario": "string",
    "emailDestinatario": "romariolima1006@gmail.com",
    "telefoneDestinatario": "string",
    "mensagem": "mensagem enviada via email",
    "modoDeEnvio": "EMAIL",
    "statusEnvio": "PENDENTE"
  }
]
Response headers
