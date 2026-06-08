package util;

import model.UsuarioModel;
import enums.PerfilUsuario;

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
    
    public static boolean isAdministrador() {

        return usuarioLogado != null
                && usuarioLogado.getPerfil() == PerfilUsuario.ADMINISTRADOR;
    }
}
