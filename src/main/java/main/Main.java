package main;

import dao.UsuarioDao;
import enums.PerfilUsuario;
import java.time.LocalDateTime;
import model.UsuarioModel;
import view.LoginView;

/**
 *
 * @author Maikon
 */
public class Main {

    public static void main(String[] args) {

        UsuarioDao usuarioDao = new UsuarioDao();

        if (usuarioDao.listar().isEmpty()) {

            UsuarioModel admin = new UsuarioModel();

            admin.setNome("Administrador");
            admin.setEmail("admin@imobiliaria.com");
            admin.setSenha("123456");
            admin.setPerfil(PerfilUsuario.ADMINISTRADOR);
            admin.setAtivo(true);
            admin.setDataCadastro(LocalDateTime.now());

            usuarioDao.salvar(admin);
        }

        new LoginView().setVisible(true);

    }

}
