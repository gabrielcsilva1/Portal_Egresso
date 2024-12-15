# Portal Egresso

Projeto da disciplina Laboratório de Programação (2024.2). O Portal de Egressos é um projeto que visa manter contato com os discentes que finalizaram o curso de Ciências da Computação, acompanhar no que estão atuando e fortalecer a comunidade.

# Requisitos
- [ ] Deve ser possível fazer o login como coordenador
- [✔] Deve ser possível fazer o cadastro de egressos com:
    - Nome
    - Email
    - Descrição
    - Foto
    - Linkedin
    - Instagram
    - Currículo
- [✔] Deve ser possível cadastrar cursos
- [✔] Deve ser possível que um egresso registre mais de um curso
- [✔] Deve ser possível cadastrar cargos do egresso
- [✔] Deve ser possível cadastrar depoimentos do egresso
- [ ] Deve ser possível listar egressos por curso, ano, nome.
- [ ] Deve ser possível consultar um egresso com seus respectivos cargos.
- [✔] Deve ser possível listar depoimentos recentes e por ano.

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
