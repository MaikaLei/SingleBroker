# Continuidade do SingleBroker — 17/09/2026

## Base preservada

- Projeto: `SingleBroker-backup-20260917-213134.zip` — 1.186 arquivos conferidos individualmente por SHA-256, incluindo o histórico Git.
- SHA-256 do ZIP: `1FFE7318EE7F2263E8B4AA4BB57CE314423C166E99C650D44F03F17356DEB035`.
- Banco: `SingleBroker-database-backup-20260917-213233.sql` — dump concluído pelo mysqldump antes das mudanças.
- SHA-256 do dump SQL: `615AF029C367F89B45C5FDF6D37BDA44FF90F9A9C027695F14684EA87C31C750`.
- Banco original no início: 1 cliente, 1 imóvel e 6 usuários.
- Branch de desenvolvimento: `codex/continuidade-singlebroker`.

## Escopo

Funcionalidades correspondentes às telas Swing já criadas. O documento da etapa 1 serve de contexto; a orientação atual é manter a versão simplificada Java + MySQL. Não foram adicionados MongoDB, interface web, vídeos ou serviços externos de geração de arte.

## Correções de base

- Edição de imóvel preserva ID e atualiza o registro.
- Áreas total e privativa, piscina, câmeras e identificação da garagem são carregadas/salvas.
- Cliente em edição carrega endereço e nascimento; tipo PF/PJ fica bloqueado.
- Falhas de persistência são propagadas para evitar sucesso falso.
- Campos de tabelas de consulta não são editáveis; listas atualizam após cadastros.
- Navegação dos formulários de imóvel e perfil permite salvar, descartar ou cancelar.
- Login diferencia falha de banco e credenciais inválidas e não abre janelas duplicadas.
- Senhas PBKDF2 com migração de credenciais legadas no login.
- Configuração local do banco separada do código e inicialização centralizada em `main.Main`.

## Módulos conectados

Agenda por usuário, fotos/documentos por imóvel, Minha Página, geração local de textos e exportação PDF, relatórios gerais e filtrados. As tabelas adicionais são `agenda`, `foto`, `documento` e `minha_pagina`; imóveis/clientes recebem campos opcionais de auditoria para os relatórios.

## Verificação

Testes automatizados de regras e integração MySQL, incluindo edição de formulários Swing, identidade do imóvel, campos de cliente, duplicidade, proteção de senha, isolamento da agenda/perfil, anexos, filtros e exportação PDF com acentos e múltiplas páginas. Renderizações de telas são mantidas apenas em `.work/previews` para revisão local.

### Resultado final

- `clean package`: sucesso no diretório padrão `target`.
- 18 testes executados: 0 falhas, 0 erros e 0 ignorados (11 integração MySQL + 7 regras).
- Estrutura do banco local atualizada. As colunas originais de todas as tabelas preexistentes foram comparadas por SHA-256 antes/depois: mesmos 6 usuários, 1 cliente PF, 0 clientes PJ e 1 imóvel, com valores originais preservados.
- Telas de anexos, agenda, perfil, relatórios e criativos conferidas em renderizações; PDF de teste conferido com acentos e paginação.
- Artefatos de compilação retirados do versionamento; continuam gerados localmente e a versão anterior permanece no ZIP de backup.

## Limites conhecidos

- Dados antigos sem data/usuário continuam sem esses campos; filtros históricos não podem reconstruir informações inexistentes.
- Anexos limitados a 5 MB por arquivo, armazenados no MySQL.
- O layout existente usa dimensões fixas e continua adequado ao uso desktop; responsividade não faz parte desta etapa.
- Os controles de cadastro de clientes e imóveis são compartilhados pelos usuários autenticados; a administração de usuários exige perfil administrador.
- A validação automatizada não substitui a revisão final dos fluxos pelo autor no NetBeans.
