**Illustrario** é uma plataforma web para compartilhar ilustrações com **tema diário**.
Todos os dias, uma palavra-tema é selecionada automaticamente e a comunidade pode publicar artes inspiradas nela.

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-22-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

---

## ✨ Funcionalidades

- 🎯 **Tema do dia automático** com rotação equilibrada das palavras
- 🖼️ **Upload de artes** com título, artista e descrição
- 🔎 **Busca** por título e descrição
- 👤 **Cadastro e login** com Spring Security
- ❤️ **Curtidas** 
- 💬 **Comentários**
- 🛡️ **Moderação** para curadoria de obras e comentários
- 🧭 **Explorar** artes e perfis públicos

---

## 🧱 Stack do projeto

- **Backend:** Java 22 + Spring Boot 3.5.7
- **Web MVC:** Spring Web + Thymeleaf
- **Segurança:** Spring Security 6
- **Persistência:** Spring Data JPA + Hibernate
- **Banco em desenvolvimento:** H2 (arquivo local)
- **Banco opcional para produção:** PostgreSQL (configuração comentada no `application.properties`)
- **Upload e processamento de imagem:** Multipart + Thumbnailator
- **Build:** Maven Wrapper (`./mvnw`)

---

## 📁 Estrutura (visão geral)

```text
illustrario/
├── src/main/java/com/illustrario/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── src/main/resources/
│   ├── templates/
│   ├── static/
│   └── application.properties
└── pom.xml
```

---

## 🚀 Como executar localmente

### Pré-requisitos

- Java 22+
- Git

> O Maven não precisa estar instalado globalmente, pois o projeto já inclui o **Maven Wrapper**.

### 1) Clonar o repositório

```bash
git clone https://github.com/kinomari/illustrario.git
cd illustrario/illustrario
```

### 2) Executar a aplicação

```bash
./mvnw spring-boot:run
```

Aplicação disponível em: **http://localhost:8080**

### 3) Executar testes

```bash
./mvnw test
```

---

## 🗄️ Banco de dados e configurações

### Desenvolvimento (padrão)

Atualmente, o projeto está configurado para usar H2 persistido em arquivo local:

- URL JDBC: `jdbc:h2:file:./data/illustrariodb`
- Console H2: `http://localhost:8080/h2-console`
- Usuário: `sa`
- Senha: *(vazio)*

### Produção (exemplo)

No arquivo `src/main/resources/application.properties`, já existe um bloco comentado com exemplo de configuração para PostgreSQL.

---

## 🛣️ Rotas principais

| Método | Rota | Descrição |
|---|---|---|
| GET | `/` | Home com tema diário e artes recentes |
| POST | `/gallery/upload` | Envio de nova arte |
| GET | `/search?q=...` | Busca de artes |
| GET | `/register` | Cadastro |
| GET | `/login` | Login |
| GET | `/explore` | Explorar temas anteriores |
| GET | `/profile/{username}` | Perfil público |
| POST | `/api/likes/{id}` | Curtir / descurtir |

> Existem também rotas administrativas para moderação, acessíveis conforme permissões de usuário.

---

## 🌱 Tema diário: regra de seleção

Cada palavra possui um contador de uso (`timesUsed`).
O sorteio diário escolhe aleatoriamente dentre as palavras com **menor número de usos**, evitando repetição excessiva e equilibrando a distribuição ao longo do tempo.

A atualização roda diariamente à meia-noite no fuso `America/Sao_Paulo` e há fallback para selecionar tema na primeira requisição do dia caso o servidor estivesse indisponível no horário do agendamento.

---

## 🧪 Qualidade e testes

- Testes com JUnit (starter `spring-boot-starter-test`)
- Dependência de `spring-security-test` para cenários com autenticação/autorização

---

## 🗺️ Roadmap (sugestões)

- [ ] Melhorias de perfil de artista
- [ ] Deploy e observabilidade em produção
- [ ] Curadoria automática de artes e comentários que não sejam adequados
- [ ] Assistente virtual da galeria

---

## 📄 Licença

MIT License © 2026 kinomari

