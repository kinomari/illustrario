# Illustrario

**Illustrario** é um projeto de aplicação web desenvolvido em **Spring Boot** que apresenta uma página inicial com uma **palavra-tema diária**, servindo como ponto de partida para um futuro sistema de galeria de artes.

---

## 🔹 Funcionalidades atuais

- Página inicial com **palavra-tema do dia**.
- Layout básico com **header e footer** reutilizáveis.
- Estrutura de projeto preparada para evolução futura (upload de imagens, galeria de artes, etc.).

---

## 📂 Estrutura do projeto

| Caminho | Descrição |
|----------|------------|
| `src/main/java/com/illustrario/controller/HomeController.java` | Controller principal |
| `src/main/resources/templates/index.html` | Página inicial |
| `src/main/resources/templates/fragments/header.html` | Cabeçalho do site |
| `src/main/resources/templates/fragments/footer.html` | Rodapé do site |
| `src/main/resources/static/css/style.css` | Estilos visuais |
| `pom.xml` | Configuração do Maven |

---

## ⚙️ Tecnologias

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen)
![Maven](https://img.shields.io/badge/Maven-4.0.0-orange)
![License](https://img.shields.io/badge/license-MIT-lightgrey)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

---

## 🚀 Como executar

1. Clonar o repositório:

`git clone https://github.com/kinomari/illustrario.git`

2. Navegar para o diretório do projeto:

`cd illustrario`


3. Executar com Maven:

``mvn spring-boot:run``


4. Acessar a aplicação no navegador:

``http://localhost:8080/``

---

🛠 Próximos passos

* Adicionar galeria de artes com imagens enviadas pelos usuários.

* Implementar upload de imagens de forma segura.

* Criar layout responsivo e visual mais atraente.

* Adicionar funcionalidades de usuário e interação.

---

📄 Licença

MIT License © 2025
