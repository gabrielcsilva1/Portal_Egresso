# Portal Egresso

Projeto da disciplina Laboratório de Programação (2024.2). O Portal de Egressos é um projeto que visa manter contato com os discentes que finalizaram o curso de Ciências da Computação, acompanhar no que estão atuando e fortalecer a comunidade.

# Funcionalidades Esperadas
Permitir cadastrar egresso e manter suas principais informações:
- Empresa que trabalha (caso não seja autônomo)
- Cargo que atua (dev, analista, ...)
- Faixa salarial (uma das opções ...) -- não obrigatória
- Curso realizado (Ciência da Computação e Licenciatura em informática - observar que ainda existem os mestrado e doutorado)
- Contatos (email, instagram, twitter, linkedin ...)
- Depoimento

- Apresentar um painel de egressos: 
    * Selecionando aleatoriamente os que devem ser exibidos primeiro
    * Permitir consulta
- Apresentar depoimentos
- Mini relatório histórico de cargos + salário

# Executando localmente
Clone do projeto
```bash
git@github.com:gabrielcsilva1/Portal_Egresso.git
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
