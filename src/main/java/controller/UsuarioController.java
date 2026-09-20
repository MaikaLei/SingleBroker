package controller;
import dao.UsuarioDao;
import model.UsuarioModel;
import util.*;
public class UsuarioController {
    public void salvar(UsuarioModel usuario, String novaSenha) {
        if (!SessaoUsuario.isAdministrador()) throw new SecurityException("Somente o administrador pode alterar usuários.");
        usuario.setNome(Validador.obrigatorio(usuario.getNome(), "o nome"));
        usuario.setEmail(Validador.email(usuario.getEmail(), true));
        if (usuario.getPerfil() == null) throw new IllegalArgumentException("Selecione o perfil.");
        if (usuario.getId() != null && usuario.getId().equals(SessaoUsuario.getUsuarioLogado().getId())
                && (!Boolean.TRUE.equals(usuario.getAtivo()) || usuario.getPerfil() != enums.PerfilUsuario.ADMINISTRADOR))
            throw new IllegalArgumentException("Você não pode desativar ou remover seu próprio acesso de administrador.");
        if (novaSenha != null && !novaSenha.isBlank()) usuario.setSenha(Senhas.gerar(novaSenha));
        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) throw new IllegalArgumentException("Informe a senha do novo usuário.");
        UsuarioDao dao = new UsuarioDao();
        if (usuario.getId() == null) dao.salvar(usuario); else dao.atualizar(usuario);
    }
}
