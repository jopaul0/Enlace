# Enlace
Gerencie os encontros de mães que oram pelos seus filhos. Este projeto é uma aplicação desktop desenvolvida em Java com interface Swing.

## Sobre o Projeto

O **Enlace** é um sistema de gerenciamento desktop (GUI com Java Swing) projetado para administrar o cadastro de Mães, Serviços e os Encontros (Meets) que estabelecem os vínculos (Enlaces) entre eles.
O sistema foi estruturado seguindo o padrão DAO (Data Access Object) para comunicação com o banco de dados MySQL e possui uma camada de controle para a lógica de negócios.

### Funcionalidades Principais

- **Configuração Dinâmica de DB:** Ao iniciar, o sistema solicita a URL, usuário e senha do MySQL, eliminando a dependência de arquivos .env ou de configuração estática.
- **Gestão de Mães (Mother) e Serviços (Service):** CRUD completo.
- **Soft Delete (Status):** Mães e Serviços utilizam um status Ativo/Inativo, permitindo exclusão lógica e reativação de cadastros.
- **Gestão de Encontros (Meet):** Cadastro de encontros com a vinculação de múltiplas mães e serviços através da tabela de Enlaces.
- **Controle de Status do Encontro:** Permite marcar encontros como Pendente, Concluído ou Cancelado.

## Tecnologias Utilizadas

O projeto é construído sobre a plataforma **Java** e utiliza o **Maven** para gerenciamento de dependências e construção.

| Tecnologia | Descrição | Versão no pom.xml |
| ----------- | ----------- | ----------- |
| Linguagem | Java | 17/21 (Recomendado 17/21) |
| Interface | Swing (javax.swing) | Nativo do Java | 
| Banco de Dados | MySQL + JDBC | Connector/J 9.5.0 | 
| Build Tool | Apache Maven | - | 
| Anotações | Project Lombok | 1.18.42 (Para Getter/Setter/ToString) | 
| JSON | Google Gson | 2.10.1 (Para serializar Enlaces em MeetDAO) |


## Configuração do Ambiente

### Pré-requisitos

Certifique-se de ter instalado:
- **Java Development Kit (JDK):** Versão 17 ou superior.
- **Servidor MySQL:** Uma instância ativa para criar o banco de dados.
- **Apache Maven:** Para compilar e empacotar o projeto.

### Configuração do Banco de Dados

1. Crie o banco de dados chamado **enlace** no seu servidor MySQL.
2. Importe o Script SQL: Execute o script SQL contido no arquivo **docs/data/database/enlace.sql** para criar as tabelas mothers, services, meets e enlace.

### Compilação e Build

O projeto utiliza o maven-shade-plugin para criar um Fat Jar (JAR executável com todas as dependências embutidas).

Navegue até o diretório raiz do projeto (/Enlace) e execute o comando Maven:

```bash
mvn clean package
```
Isto irá:

1. Limpar compilações anteriores.
2. Compilar todo o código-fonte (para target/classes).
3. Gerar o executável final na pasta target/.

**O arquivo executável que você deve usar é: target/Enlace-1.0-SNAPSHOT.jar.**

### Execução da Aplicação

Após a compilação, execute o JAR final via terminal para garantir a correta inicialização da JVM:

```bash
java -jar target/Enlace-1.0-SNAPSHOT.jar
```

### Primeira Inicialização (Tela de Configuração)

Ao iniciar, uma tela de diálogo será exibida solicitando as credenciais do seu banco de dados local:

| Campo | Exemplo | Versão no pom.xml |
| ----------- | ----------- | ----------- |
| URL do Banco | jdbc:mysql://localhost:3306/enlace |
| Usuário | root |
| Senha | sua_senha_mysql |

**Após inserir e testar as credenciais, a aplicação principal (MainFrame) será iniciada.**

## Contribuidores

Nome  | LinkedIn | GitHub
-|-|-
Breno Reis | [![LinkedIn](https://img.shields.io/badge/linkedin-blue?style=for-the-badge)](https://www.linkedin.com/in/breno-reis-893009321) | <a href="https://github.com/brenovisk"> <img src="https://img.shields.io/badge/github-black?style=for-the-badge&logo=github"/> </a>
João Paulo | [![LinkedIn](https://img.shields.io/badge/linkedin-blue?style=for-the-badge)](https://www.linkedin.com/in/joaosantos02) | <a href="https://github.com/jopaul0"> <img src="https://img.shields.io/badge/github-black?style=for-the-badge&logo=github"/> </a>



