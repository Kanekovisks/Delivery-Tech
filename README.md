
# 🔗 Delivery Tech API
Sistema de delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias usadas

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white)

- **Java 21 LTS**
- **Spring Boot 3.5.13-SNAPSHOT**
- **Spring Web**
- **Spring Data JPA**
- **H2 Database**

## 🏃‍♂️ Como executar
1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
3. Execute: `./mvn spring-boot:run`
4. Acesse: http://localhost:8080/health

## 📋 Endpoints
- **GET /health** - Status da aplicação (inclui versão Java)
- **GET /info** - Informações da aplicação
- **GET /h2-console** - Console do banco H2
- **GET /clientes** - Lista de clientes **Ativos**
- **GET /clientes/{ID}** - Procura de cliente por ID
- **POST /clientes/register** - Registro de clientes no padrão: **{"nome": "String","email": "String","telefone": "String","endereco": "String"}**
- **PUT /clientes/{ID}/deactivate** - Desativação de cliente por ID
- **PUT /cliente/{ID}/update** - Atualização de dados do cliente por ID no padrão: **{"nome": "String","email": "String","telefone": "String","endereco": "String"}**

## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória
- Profile: development

## 👨‍💻 Desenvolvedor
**[Gabriel Duarte Roxo](https://github.com/Kanekovisks)** - **04018**

