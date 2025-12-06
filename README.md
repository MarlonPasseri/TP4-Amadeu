# TP4 - Sistema Integrado com Refatoração e CI/CD

Este projeto integra **dois sistemas**:

1. **Catálogo de Produtos** (CRUD completo)  
2. **Sistema de Pedidos**, que consome o catálogo e atualiza o estoque

A integração é feita através da interface `CatalogoService`, permitindo que
o módulo de pedidos acesse e modifique produtos de forma desacoplada.

## Principais pontos de refatoração

- Uso de **Value Objects imutáveis** (`Money`, `StockQuantity`) para substituir valores primitivos.
- Aplicação de **SRP** e separação em camadas:
  - `domain` (entidades e objetos de valor),
  - `application` (serviços e interfaces),
  - `web` (controllers e views).
- Tratamento de falhas com `BusinessException` + `GlobalExceptionHandler`
  (fail early + fail gracefully).
- Nomes de classes e métodos claros, orientados ao domínio.

## Gradle e CI/CD

O projeto usa **Gradle** como ferramenta de build:

```bash
gradle clean build
gradle bootRun
```

O diretório `.github/workflows/ci.yml` contém um workflow de **GitHub Actions** que:

- Faz o checkout do código.
- Configura Java 17.
- Executa `gradle clean build` (build + testes).
- Publica o relatório de cobertura do **Jacoco** como artefato.

O workflow é disparado em:

- `push` para os branches `main`/`master`
- `pull_request`
- `workflow_dispatch` (execução manual)

## Como executar localmente

1. Certifique-se de ter **Java 17** e **Gradle** instalados.
2. Na raiz do projeto:

```bash
gradle bootRun
```

3. Acesse:

- Dashboard integrado: `http://localhost:8080/`
- CRUD de produtos: `http://localhost:8080/produtos`
- Pedidos: `http://localhost:8080/pedidos`
- Console H2 (banco em memória): `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:mem:tp4db`)

## Testes

Execute:

```bash
gradle test
```

Inclui:

- Testes de Value Objects (`MoneyTest`, `StockQuantityTest`).
- Teste de serviço integrado (`PedidoServiceImplTest`).
- Teste de controller (`ProdutoControllerTest`).
- Teste de integração simples entre os sistemas (`IntegracaoSistemasTest`).

## Runners

O workflow de exemplo usa **runner hospedado pelo GitHub**:

```yaml
runs-on: ubuntu-latest
```

Caso queira usar *self-hosted runners*, basta trocar para:

```yaml
runs-on: self-hosted
```

e garantir que a máquina tenha Java e Gradle configurados.
