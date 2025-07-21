# TribarinHC_BackEnd

Este projeto é uma plataforma de e-commerce marketplace desenvolvida em [Grails 6.2.3] com integração a um microserviço financeiro baseado em [Spring Boot 5]. O sistema foi projetado para facilitar a gestão de usuários, produtos, pedidos, pagamentos e atendimentos, etc... oferecendo uma arquitetura moderna e escalável.

## Principais Características

- **Marketplace Completo:** Cadastro e gerenciamento de usuários, produtos, pedidos e pagamentos.
- **Autenticação Segura:** Utilização de JWT para autenticação e autorização de usuários.
- **Microserviço Financeiro:** Operações financeiras desacopladas, implementadas em um microserviço Spring Boot, permitindo escalabilidade e manutenção independente.
- **Administração e Atendimento:** Funcionalidades para atendimento ao cliente e validação de novos usuários.
- **Frontend Responsivo:** Interface baseada em Vue.js 3, com estilos customizados para desktop e mobile.
- **APIs RESTful:** Endpoints organizados para integração com frontends ou outros sistemas.
- **Testes Automatizados:** Estrutura pronta para testes de integração e aceitação.

## Estrutura do Projeto

- `grails-app/`: Código principal Grails (controllers, domain, services, views, assets).
- `src/`: Testes e código auxiliar.
- `front-tribarin/`: Frontend (pode ser integrado ou separado).
- `build.gradle`, `docker-compose.yml`: Scripts de build e orquestração.
- Microserviço financeiro Spring Boot (em repositório ou diretório separado).

## Como Executar

1. Clone o repositório.
2. Execute o backend Grails:
   ```sh
   ./grailsw run-app
   ```
3. Execute o microserviço financeiro Spring Boot conforme instruções do respectivo módulo.
4. Acesse a aplicação via navegador em `http://localhost:8080`.

## Inicializando o MySQL com Docker

O projeto utiliza um banco de dados MySQL, que pode ser facilmente iniciado via Docker. Execute o comando abaixo na raiz do projeto:

```sh
docker-compose up -d
```

Isso irá subir o container do MySQL conforme definido no arquivo [docker-compose.yml](docker-compose.yml). Certifique-se de que as configurações de usuário, senha e porta estejam de acordo com o esperado pela aplicação.

Para verificar se o container está rodando, utilize o comando:

```sh
docker ps
```

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

---

> Projeto desenvolvido para fins de estudo e demonstração de arquitetura moderna para e-commerce.

