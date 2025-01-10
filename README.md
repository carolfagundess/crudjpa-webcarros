# Sistema de Cadastro de Carros

Este é um sistema web simples para **gerenciamento de carros**, onde você pode cadastrar, editar, excluir e listar carros. A aplicação utiliza **Java**, **Servlets**, **Jetty** (servidor web embutido) no backend e **HTML**, **CSS** e **JavaScript** no frontend.

## Funcionalidades

- **Cadastro de Carros**: Permite adicionar novos carros com informações como modelo, marca, ano e cor.
- **Edição de Carros**: Possibilita editar informações de carros já cadastrados.
- **Exclusão de Carros**: Remove um carro da base de dados.
- **Listagem de Carros**: Exibe todos os carros cadastrados de forma organizada.

## Tecnologias Utilizadas

### Backend
- **Java**: Linguagem principal do projeto.
- **Servlets**: Responsável pela lógica de backend.
- **Jetty**: Servidor web que hospeda as páginas e a API RESTful.
- **JDBC**: Para interação com o banco de dados (usado para salvar e recuperar carros).

### Frontend
- **HTML**: Estrutura básica das páginas.
- **CSS**: Estilos para tornar as páginas mais agradáveis.
- **JavaScript**: Interatividade das páginas, incluindo consumo da API.
- **Bootstrap 4**: Framework CSS utilizado para facilitar o design e tornar as páginas responsivas.

## Funcionalidade Detalhada

### 1. Cadastro de Carros
Na página `index.html`, você pode adicionar um novo carro preenchendo os campos de **Modelo**, **Marca**, **Ano** e **Cor** e clicando no botão "Salvar". O carro será registrado e exibido na lista de carros.

### 2. Edição de Carros
Na página `editar.html`, você pode editar os dados de um carro já cadastrado. Após editar, basta clicar em "Salvar Alterações", e as mudanças serão atualizadas.

### 3. Exclusão de Carros
Cada carro exibido na lista pode ser excluído com o botão "Excluir", que remove o carro do banco de dados.

### 4. Listagem de Carros
A página `lista.html` exibe todos os carros cadastrados, com opções para editar ou excluir.

## Imagens

### Página Inicial (Cadastro de Carro)
![Tela Inicial](images/tela-inicial.png)

### Página de Edição
![Tela de Edição](images/tela-edicao.png)

### Lista de Carros
![Lista de Carros](images/lista-carros.png)

## Estrutura do Projeto

Abaixo está a estrutura de diretórios e arquivos do projeto:

