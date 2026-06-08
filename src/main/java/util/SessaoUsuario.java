package util;

import model.UsuarioModel;

/**
 *
 * @author Maikon
 */
public class SessaoUsuario {

    private static UsuarioModel usuarioLogado;

    public static void setUsuarioLogado(UsuarioModel usuario) {
        usuarioLogado = usuario;
    }

    public static UsuarioModel getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void encerrarSessao() {
        usuarioLogado = null;
    }
}
