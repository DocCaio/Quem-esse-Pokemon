# ❓ Quem é esse Pokémon?

Joguinho em **Kotlin** inspirado no clássico "Who's That Pokémon?" do anime. O jogo mostra a **silhueta** de um Pokémon e você tenta adivinhar o nome. Acertou, a imagem ganha cor. Errou, é só tentar de novo.

Interface feita com **Swing** (já vem no JDK) e projeto gerenciado com **Maven**. Sem dependências além da stdlib do Kotlin.

## ✨ Funcionalidades

- Sorteio entre os 151 primeiros Pokémon (1ª geração)
- Silhueta preta que revela a imagem colorida ao acertar
- Mensagem de **"Acertou!"** ou **"Tente novamente!"**
- Placar de pontos
- Botão **Desistir** (revela a resposta) e **Próximo** (sorteia outro)
- Aceita variações no nome: `mr mime`, `mr-mime` e `Mr. Mime` valem igual
- Funciona com **Enter** ou clicando em **Chutar**

## 📋 Requisitos

- JDK 17 ou superior
- Maven 3.8+
- Conexão com a internet (nomes e imagens vêm de uma API)

## 🚀 Como rodar

```bash
# rodar direto
mvn compile exec:java
```

Ou gerar um `.jar` executável:

```bash
mvn package
java -jar target/quem-e-esse-pokemon-1.0.0.jar
```

Dá para abrir a pasta no IntelliJ/GoLand também: ele reconhece o `pom.xml` sozinho. Rode a função `main` do arquivo `Main.kt`.

## 🎮 Como jogar

1. Aguarde a silhueta carregar.
2. Digite o nome do Pokémon **em inglês** (ex.: `pikachu`, `charmander`).
3. Aperte **Enter** ou clique em **Chutar**.
4. Se errar, tente de novo quantas vezes quiser. Se travar, clique em **Desistir**.
5. Clique em **Próximo** para outra rodada.

## 🗂️ Estrutura

```
quem-e-esse-pokemon/
├── pom.xml
├── README.md
└── src/main/kotlin/
    ├── Main.kt              # ponto de entrada
    ├── model/
    │   └── Pokemon.kt       # data class (id, nome, URL da imagem)
    ├── api/
    │   └── PokemonApi.kt    # busca a lista e as imagens na PokeAPI
    ├── util/
    │   ├── ImageUtils.kt    # redimensionar e gerar a silhueta
    │   └── TextUtils.kt     # normalização do texto digitado
    └── ui/
        └── GameWindow.kt    # janela Swing e lógica das rodadas
```

## ⚙️ Como funciona

- **Lista de Pokémon:** uma requisição para `https://pokeapi.co/api/v2/pokemon?limit=151` traz nomes e IDs (extraídos com regex, sem biblioteca de JSON).
- **Imagem:** o artwork oficial é baixado do repositório de sprites da PokeAPI.
- **Silhueta:** cada pixel da imagem é redesenhado em preto mantendo só o canal alpha, então o contorno do Pokémon é preservado.
- **Rede:** as requisições rodam em outra thread para não travar a janela.

## 🛠️ Problemas comuns

| Problema | Solução |
|---|---|
| "Erro ao buscar os Pokémon" | Verifique a internet e tente abrir o programa de novo |
| `release version 17 not supported` | Seu JDK é anterior ao 17. Atualize ou mude `maven.compiler.target` e `kotlin.compiler.jvmTarget` no `pom.xml` |
| Acentos estranhos no terminal | Rode com `-Dfile.encoding=UTF-8` |

## 💡 Ideias para evoluir

- Outras gerações (aumentar o `limit` e o offset da API)
- Modo múltipla escolha com 4 opções
- Dicas (tipo, primeira letra)
- Recorde salvo em arquivo
- Nomes em português

## 📄 Créditos

- Dados e imagens: [PokeAPI](https://pokeapi.co/) e [PokeAPI/sprites](https://github.com/PokeAPI/sprites)
- Pokémon e seus nomes são marcas da Nintendo / Game Freak / The Pokémon Company. Projeto feito apenas para fins de estudo, sem fins comerciais.
