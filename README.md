# Portal Egresso

Projeto da disciplina Laboratório de Programação (2024.2). O Portal de Egressos é um projeto que visa manter contato com os discentes que finalizaram o curso de Ciências da Computação, acompanhar no que estão atuando e fortalecer a comunidade.

# Requisitos
- [✔] Deve ser possível fazer o login como coordenador
- [✔] Deve ser possível cadastrar cursos (Coordenador)
- [✔] O coordenador deve ser capaz de validar o cadastro de:
    - Egressos
    - Depoimentos
    - Oportunidades de Emprego
- [✔] Deve ser possível que o Egresso faça seu cadastro, para ser avaliado pelo coordenador, com os seguintes campos:
    - Nome
    - Email
    - Curso
    - Ano de início do curso
    - Ano de término do curso
    - Senha
- [✔] Deve ser possível que o Egresso edite suas informações citadas anteriormente e, além delas, as seguintes:
    - Url da foto
    - Descrição
    - Linkedin
    - Instagram
    - Currículo (Link)
- [✔] Deve ser possível que o Egresso cadastre seus cargos, SEM validação do coordenador
- [✔] Deve ser possível que o Egresso cadastre seus depoimentos, com validação do coordenador
- [✔] Deve ser possível listar egressos por curso, ano, nome.
- [✔] Deve ser possível listar egressos por cargo.
- [✔] Deve ser possível consultar um egresso.
- [✔] Deve ser possível listar depoimentos.
- [✔] Deve ser possível listar oportunidades de emprego.

# Executando localmente
Clone do projeto
```bash
git clone git@github.com:gabrielcsilva1/Portal_Egresso.git
```

Acessando a pasta do projeto 
```bash
cd Portal_Egresso
```

Duplique as variáveis ambientes
```bash
cp .env.example .env
```

Subindo o container com Docker
```bash
docker compose up -d
```

Acesse http://localhost:5173

# ❗ Importante
### Criando um coordenador
```http
  POST /coordenador
```

### Logando como coordenador
Acesse a url http://localhost:5173/admin/sign-in

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `login` | `string` | **Obrigatório**. O login do coordenador |
| `password` | `string` | **Obrigatório**. A senha do coordenador |

# 📄 Documentação da API
A documentação da API está disponível em:

🔗 http://localhost:8080/docs