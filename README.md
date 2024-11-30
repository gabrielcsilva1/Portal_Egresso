# Portal Egresso

Projeto da disciplina Laboratório de Programação (2024.2). O Portal de Egressos é um projeto que visa manter contato com os discentes que finalizaram o curso de Ciências da Computação, acompanhar no que estão atuando e fortalecer a comunidade.

# Requisitos
- Deve ser possível cadastrar um egresso com as seguintes informações:
    - Nome
    - Contatos (email, instagram, twitter, linkedin ...)
    - Curso realizado (Ciência da Computação e Licenciatura em informática - observar que ainda existem os mestrado e doutorado)

- Deve ser possível que o egresso cadastre um depoimento

- Deve ser possível cadastrar cargos em que o egresso atuou, informando:
    - Empresa que trabalha (caso não seja autônomo)
    - Cargo que atua (dev, analista, ...)
    - Faixa salarial (uma das opções ...) -- não obrigatória

- Deve ser possível listar os egressos aleatoriamente: 
- Deve ser possível filtrar egressos pelo nome
- Deve ser possível listar depoimentos com o respectivo egresso
- Deve ser possível listar o histórico de cargos + salário de um egresso

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

Subindo o container
```bash
docker compose up -d
```
