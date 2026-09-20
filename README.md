# SingleBroker

Sistema desktop de gestão imobiliária desenvolvido por Maikon Leiria, com Java Swing, JPA/Hibernate e MySQL. A versão atual implementa os fluxos das telas existentes do projeto do curso.

## Executar no NetBeans

1. Utilize JDK 24 ou superior (validado com JDK 25) e MySQL 8 (validado com MySQL 8.4).
2. Crie o banco `singlebrokerdb` em UTF-8 (`utf8mb4`), caso ainda não exista.
3. Copie `singlebroker.local.properties.example` para `singlebroker.local.properties`, na raiz do projeto, e preencha usuário e senha do MySQL.
4. Abra o projeto Maven no NetBeans e execute `main.Main` (F6 usa a configuração de `nbactions.xml`).
5. Em um banco vazio, o sistema solicita os dados do primeiro administrador. Não há senha padrão nova.

O arquivo de configuração desta máquina já foi preparado. Não envie esse arquivo, cópias do banco ou backups do projeto ao GitHub. As credenciais que existiam em versões antigas continuam no histórico; troque a senha do banco caso ela ainda seja utilizada fora do ambiente local.

Também é possível configurar `SINGLEBROKER_DB_URL`, `SINGLEBROKER_DB_USER`, `SINGLEBROKER_DB_PASSWORD` e `SINGLEBROKER_DB_DDL` por variáveis de ambiente. Elas prevalecem sobre o arquivo local. A opção `db.ddl=update` adiciona tabelas e colunas necessárias quando o sistema abre a conexão. Faça backup antes de atualizar um banco existente.

## Funcionalidades

- Login, sessão e cadastro/edição de usuários por administrador. Senhas novas são protegidas com PBKDF2 e salt individual; senhas antigas são migradas após um login correto. Deixe o campo senha vazio durante a edição para manter a atual.
- Clientes PF e PJ: cadastro, edição e busca; validações de CPF/CNPJ, e-mail e nascimento. Endereço e data de nascimento são preservados na edição.
- Imóveis: cadastro, listagem, filtros, detalhes e edição do mesmo registro. Inclui proprietário, áreas, identificação da garagem, características e status. Para inativar um imóvel, altere seu status na edição.
- Fotos e documentos por imóvel: adicionar, visualizar fotos, remover e salvar cópia. Clique em **Salvar Fotos/Documentos** para confirmar as alterações. O limite é de 5 MB por arquivo; os conteúdos ficam no MySQL. Ao incluir anexos em um imóvel novo, é necessário salvar primeiro o cadastro.
- Agenda pessoal: escolha mês e ano, clique no dia para listar/agendar tarefas. Dê duplo clique na tarefa para editar, concluir, reabrir ou excluir. Cada usuário acessa apenas sua própria agenda. Horário no formato `HH:mm`.
- Criativos: quatro modelos de texto com os dados do imóvel, texto editável, copiar e exportar PDF. A geração é local, sem serviço externo de IA.
- Minha Página: título profissional, descrição, bibliografia, contatos e foto, salvos por usuário.
- Relatórios: totais, listas, filtros por período e usuário e atalhos, com exportação PDF. O usuário do relatório é quem cadastrou o registro.

Datas aceitam `dd/MM/aaaa` e `aaaa-MM-dd`. Valores aceitam vírgula decimal e separador brasileiro de milhar, como `1.250,50`.

### Registros antigos e relatórios

A versão antiga não registrava usuário/data de cadastro nem a data da venda. Esses campos não são inventados durante a atualização. Registros sem a informação ficam fora dos filtros correspondentes, mas permanecem nas listas gerais. Para imóveis antigos, a data de entrada válida pode ser utilizada no filtro de cadastro. Novos registros passam a guardar esses dados; a data de venda é registrada ao mudar o status para Vendido.

## Testes

```text
mvn test
```

Executa as regras de validação, senhas, anexos, criativos e exportação PDF. Os testes MySQL são opt-in:

```text
mvn -Dsinglebroker.integration=true test
```

Os testes de integração usam a configuração local para criar um schema aleatório `singlebroker_test_*`, validam os fluxos e removem somente esse schema ao terminar. Precisam de permissão de criar/remover bancos e de ambiente gráfico para construir os formulários Swing. Não escrevem no banco `singlebrokerdb`.

Se NetBeans/OneDrive mantiver arquivos de `target` ocupados, é possível testar em outra pasta:

```text
mvn -Dsinglebroker.buildDirectory=.work/build -Dsinglebroker.integration=true test
```

Os arquivos `.form` e os blocos de layout gerados pelo NetBeans foram preservados. A conexão dos eventos e os ajustes necessários são feitos fora desses blocos.

## Interface desktop

As 17 telas usam FlatLaf 3.7, com uma identidade visual comum definida em `util.Tema`: azul da marca, fundo claro, fontes Segoe UI, indicadores de foco e tabelas com linhas mais altas. O menu mantém a mesma ordem e destaca a seção aberta; também aceita Tab e Enter.

`util.LayoutTela` organiza cabeçalhos, seções, campos e rodapés com os gerenciadores de layout do Swing. Os métodos `configurarVisual()` reutilizam os controles e eventos existentes, fora dos blocos gerados. O cadastro de imóvel está dividido em Dados e endereço, Características, e Valores e anexos. Formulários longos têm rolagem; as ações de salvar e cancelar permanecem no rodapé.

Ao abrir o projeto pelo NetBeans, o Maven baixa o FlatLaf automaticamente. Execute `main.Main` (F6). Para alterar o visual em execução, ajuste `Tema`, `LayoutTela` ou `configurarVisual()`; os arquivos `.form` continuam disponíveis como a definição original dos controles.

O teste de integração renderiza todas as telas, as abas do imóvel e o cadastro PF/PJ em `.work/previews`, e verifica se os controles visíveis mantêm tamanho utilizável. Os dados usados nessas imagens são do banco de teste isolado.

## Backup desta etapa

Antes das alterações, foram criados na raiz do projeto um ZIP completo, incluindo `.git`, e um dump SQL do banco. São arquivos locais ignorados pelo Git. Os nomes e a verificação constam em `DESENVOLVIMENTO.md`.
