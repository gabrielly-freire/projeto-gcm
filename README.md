# BG Bank - Sistema Bancário

Projeto desenvolvido para a disciplina DIM0517 - GERÊNCIA DE CONFIGURAÇÃO E MUDANÇAS (UFRN).

## Equipe
* **Ana Beatriz Camilo da Costa** (20230081544) - [BeatrizCamlo](https://github.com/BeatrizCamlo)
* **Francisca Gabrielly Lopes Freire** (20230034464) - [gabrielly-freire](https://github.com/gabrielly-freire)

## Stack Tecnológica
* **Linguagem:** Java 21
* **Build Tool:** Maven
* **Testes:** JUnit 5
* **Interface:** Console (CLI)

## Como executar

### Pré-requisitos
* Java 21 instalado
* Maven instalado

### Execução via Maven

1. **Compilar o projeto:**
    ```bash
    mvn clean install
    ```
2. **Executar a aplicação:**
    ```bash
    mvn exec:java "-Dexec.mainClass=br.ufrn.imd.ui.ContaBancariaUI"
    ```

### Execução via IDE (Intellij ou vs code)

1. Certifique-se de que a extensão de Java está instalada (no caso do vs code).
2. Localize a classe `br.ufrn.imd.ui.ContaBancariaUI`.
3. Clique em **Run** ou use o atalho padrão da sua IDE para iniciar a classe Main.

### Executando os testes

Para executar os testes automatizados:

```bash
mvn test
```
