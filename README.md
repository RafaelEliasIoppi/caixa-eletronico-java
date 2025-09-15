# Caixa Eletrônico 💳

Este é um projeto Java com Spring Boot que simula um sistema de caixa eletrônico, permitindo operações bancárias básicas como login, consulta de saldo, depósitos e saques. A aplicação está publicada e funcionando em ambiente de produção.

🔗 **Acesse o sistema online**: https://caixa-eletronico-java.onrender.com

---

## 🚀 Tecnologias utilizadas

- Java 17
- Spring Boot
- H2 Database (modo arquivo)
- Maven
- Docker
- Render.com (deploy)

---

## 🔐 Funcionalidades

- Autenticação com senha
- Persistência de dados com H2 (`.mv.db` e `.trace.db`)
- Operações bancárias simuladas
- Console H2 disponível em `/h2-console`
- Configuração de porta personalizada (`10000`)
- Gráficos das operações realizadas

---

## 🐳 Executando com Docker

### Build da imagem

```bash
docker build -t caixa-eletronico .
