# Totvs-dev
Desafio da totvs
## Descrição

Este projeto é uma aplicação Java desenvolvida com Spring Boot e Maven. Ele inclui uma implementação genérica de um serviço que pode ser usado para manipular entidades em um banco de dados.

## Tecnologias Utilizadas

- Java
- Spring Boot
- Maven
- H2 Database

## Configuração do Projeto

Para configurar o projeto, você precisará ter Java 17 e Maven instalados em seu ambiente de desenvolvimento.

## Como Executar

Para executar o projeto, você pode usar o comando `mvn spring-boot:run` no diretório raiz do projeto.

## Estrutura do Projeto

O projeto segue o padrão Model-View-Controller (MVC) e inclui as seguintes componentes principais:

- **Model**: As classes de modelo representam as entidades do banco de dados. Elas são usadas para manipular os dados e contêm a lógica de negócios.

- **View**: A camada de visualização é responsável por exibir os dados ao usuário. No contexto deste projeto, a "view" pode ser considerada como a representação dos dados enviados ao cliente.

- **Controller**: As classes de controle manipulam as interações do usuário e atualizam o modelo e a visualização com base nessas interações. O projeto inclui uma classe `AbstractController` que fornece uma implementação genérica de um controlador. Esta classe pode ser estendida para criar controladores para manipular entidades específicas.

- **Handler**: A classe `Handler` é usada para manipular exceções que podem ocorrer durante a execução do projeto. Ela fornece uma maneira centralizada de lidar com erros e pode fornecer mensagens de erro personalizadas ao usuário.

Além disso, o projeto inclui uma classe `GenericServiceAbstract` que fornece uma implementação genérica de um serviço. Esta classe inclui métodos para salvar, deletar, encontrar por id, encontrar todos, e obter o id de uma entidade. Ela também inclui métodos abstratos para validar uma entidade antes de salvar ou atualizar, e um método para preparar uma entidade para atualização.

## Funcionalidades

As principais funcionalidades do projeto são fornecidas pela classe `GenericServiceAbstract`. Esta classe pode ser estendida para criar serviços para manipular entidades específicas.
