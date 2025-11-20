# api de mensagem por email



### como rodar
``` git clone https://github.com/Romariolima1998/api_mensagem.git ```

adicione o email e a senha de app no applicatin.yml.

rode o comando abaixo no terminal na pasta comunicacao_api

``` docker compose --build ```

***

<img src="https://raw.githubusercontent.com/Romariolima1998/api_mensagem/refs/heads/main/usuario.png">

***

``` http://localhost:8080/usuario?email=romario%40email.com ```  metodo ```GET```

busca usuario por email atraves de parametro da uri


retorno
```
{
  "nome": "string",
  "email": "romario@email.com",
  "senha": "$2a$10$eHiSpf65N9aVM2jhGw76xeab0dqWLYJIeyWghyGZcewsdfsdR"
}

```

***


```  http://localhost:8080/usuario  ```  metodo ```PUT```

atualiza usuario

entrada

```

{
  "nome": "string",
  "email": "string",
  "senha": "string"
}

```


retorno
```
{
  "nome": "string",
  "email": "string",
  "senha": "$2a$10$ya2vFTidyF1RU0YyB6Qb5OljL0LxnKMtf11YEWlSfdeGTHudlT.3Q2"
}

```

***

```  http://localhost:8080/usuario  ```  metodo ```POST```

atualiza usuario

entrada

```

{
  "nome": "string",
  "email": "string",
  "senha": "string"
}

```


retorno
```
{
  "nome": "string",
  "email": "string",
  "senha": "$2a$10$ya2vFTidyF1RU0YyB6Qb5OljL0LxnKMtf11YEWlSfdeGTHudlT.3Q2"
}

```

***


```  http://localhost:8080/usuario/login  ```  metodo ```POST```

atualiza usuario

cria um token de autenticacao

```

{
  "email": "string",
  "senha": "string"
}

```


retorno
```
Bearer eyJhbGciOiJIzI1NiJ9d.eyJzdWIiOiwyb21hcmlvQGVtYWlsLmNvbSIsIlhdCI6MTc2MzY0MTA1MywiZeXhwIjoxNzYzNjQ0Njzfj.PMZ-GlHyxVtm6fTSWqTYaiJZcWcTcg_Bt7DzCDfSlzLkI8

```

***


``` 'http://localhost:8080/usuario/string' ```  metodo ```DELETE```


deleta usuario atraves do email passado como parametro


retorno
```
  200 OK

```




***

<img src="https://raw.githubusercontent.com/Romariolima1998/api_mensagem/refs/heads/main/comunicacao.png">

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

``` http://localhost:8080/comunicacao/status?emailDestinatario=romariolima1006%40gmail.com ``` metodo ``` GET ```

busca as mensagens que sera enviada para o email passado como parametro

retorno

```
[
  {
    "id": 1,
    "dataHoraEnvio": "04-11-2025 11:30:00",
    "nomeDestinatario": "string",
    "emailDestinatario": "romariolima1006@gmail.com",
    "emailOwner": "romario1@email.com"
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
    "emailOwner": "romario2@email.com"
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
    "emailOwner": "romario3@email.com"
    "telefoneDestinatario": "string",
    "mensagem": "mensagem enviada via email",
    "modoDeEnvio": "EMAIL",
    "statusEnvio": "PENDENTE"
  }
]
```

***

``` http://localhost:8080/comunicacao ``` metodo ``` GET ```

busca as mensagem do autor pela autenticacao

retorno


```
[
  {
    "id": 1,
    "dataHoraEnvio": "04-11-2025 11:30:00",
    "nomeDestinatario": "string",
    "emailDestinatario": "romariolima1006@gmail.com",
    "emailOwner": "romario1@email.com"
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
    "emailOwner": "romario2@email.com"
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
    "emailOwner": "romario3@email.com"
    "telefoneDestinatario": "string",
    "mensagem": "mensagem enviada via email",
    "modoDeEnvio": "EMAIL",
    "statusEnvio": "PENDENTE"
  }
]
```

