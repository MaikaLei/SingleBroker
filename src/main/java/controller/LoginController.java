package controller;
import dao.UsuarioDao;
import model.UsuarioModel;
import util.*;
public class LoginController {
    public UsuarioModel entrar(String email, String senha) {
        Validador.obrigatorio(email, "o e-mail");
        Validador.obrigatorio(senha, "a senha");
        UsuarioModel usuario = new UsuarioDao().autenticar(email, senha);
        if (usuario != null) SessaoUsuario.setUsuarioLogado(usuario);
        return usuario;
    }
}
