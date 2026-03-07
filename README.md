# Illustrario

**Illustrario** é uma galeria de arte web com tema diário — a cada dia uma palavra-tema é sorteada automaticamente, e artistas podem enviar suas obras inspiradas nela.

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-22-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

---

## ✨ Funcionalidades

- 🎨 **Tema do dia automático** — palavra sorteada diariamente de um pool, sem repetição até todas serem usadas
- 🖼️ **Upload de obras** — envio de imagens com título, nome do artista e descrição
- 🔍 **Busca** — encontre obras por título ou descrição
- 👤 **Cadastro e login** — autenticação com Spring Security
- 🛡️ **Moderação** — curador pode ocultar obras e comentários inadequados
- ❤️ **Curtidas** — visitantes podem curtir obras (por IP, sem necessidade de login)
- 💬 **Comentários** — comentários por obra

---

## 🗂️ Estrutura do projeto

```
src/main/java/com/illustrario/
├── config/          # Segurança, scheduling, inicialização de dados
├── controller/      # Rotas web e API REST
├── dto/             # Objetos de transferência de dados (formulários)
├── model/           # Entidades JPA
├── repository/      # Interfaces Spring Data JPA
└── service/         # Lógica de negócio

src/main/resources/
├── templates/       # Templates Thymeleaf
│   ├── fragments/   # Header e footer reutilizáveis
│   ├── gallery/     # Galeria do tema do dia
│   └── upload/      # Formulário de envio
└── static/css/      # Estilos
```

---

## ⚙️ Tecnologias

| Camada | Tecnologia |
|---|---|
| Backend | Java 22 + Spring Boot 3.5.7 |
| Persistência | Spring Data JPA + Hibernate |
| Banco (dev) | H2 (em memória) |
| Banco (prod) | PostgreSQL |
| Segurança | Spring Security 6 |
| Templates | Thymeleaf + thymeleaf-extras-springsecurity6 |
| Upload | Spring Multipart + Thumbnailator |
| Build | Maven |

---

## 🚀 Como executar

### Pré-requisitos
- Java 22+
- Maven 4+

### Passos

```bash
# 1. Clonar o repositório
git clone https://github.com/kinomari/illustrario.git
cd illustrario/illustrario

# 2. Executar
mvn spring-boot:run
```

Acesse em **http://localhost:8080**

### Console do banco H2 (dev)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:illustrariodb`
- Usuário: `sa` | Senha: *(vazio)*

---

## 🗺️ Rotas disponíveis

| Rota | Descrição |
|---|---|
| `GET /` | Homepage com tema do dia e obras recentes |
| `GET /gallery` | Galeria completa + formulário de upload |
| `POST /gallery/upload` | Envia uma obra |
| `GET /search?q=...` | Busca obras por título ou descrição |
| `GET /register` | Cadastro de usuário |
| `GET /login` | Login |
| `GET /upload` | Formulário de upload standalone |
| `GET /api/likes/{id}` | Curtidas de uma obra |
| `POST /api/likes/{id}` | Curtir / descurtir |
| `POST /admin/art/{id}/remove` | Ocultar obra (role: CURATOR) |
| `POST /admin/comment/{id}/remove` | Ocultar comentário (role: CURATOR) |

---

## 🌱 Como funciona o tema automático

Cada palavra do pool tem um contador de usos (`timesUsed`). O sorteio sempre escolhe aleatoriamente entre as palavras com o **menor contador** — garantindo que nenhuma palavra se repete antes de todas as outras terem sido usadas ao menos uma vez.

O sorteio roda automaticamente todo dia à meia-noite (fuso `America/Sao_Paulo`). Se o servidor estiver fora do ar na meia-noite, o tema é sorteado automaticamente na primeira requisição do dia.

---

## 🔮 Próximos passos

- [ ] Página de perfil do artista
- [ ] Galeria por tema/data
- [ ] Painel admin para gerenciar palavras do pool
- [ ] Deploy com PostgreSQL
- [ ] Armazenamento de imagens em nuvem (S3 / Cloudflare R2)

---

## 📄 Licença

MIT License © 2025 kinomari
