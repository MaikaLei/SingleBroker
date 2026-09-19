package main;
import dao.UsuarioDao;
import enums.PerfilUsuario;
import model.UsuarioModel;
import util.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(JPAUtil::close));
        SwingUtilities.invokeLater(() -> {
            try {
                UsuarioDao dao = new UsuarioDao();
                if (dao.listar().isEmpty() && !criarAdministrador(dao)) return;
                new view.LoginView().setVisible(true);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null, "Não foi possível iniciar. Verifique o MySQL e o arquivo singlebroker.local.properties.", "SingleBroker", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private static boolean criarAdministrador(UsuarioDao dao) {
        JTextField nome = new JTextField("Administrador");
        JTextField email = new JTextField();
        JPasswordField senha = new JPasswordField();
        while (JOptionPane.showConfirmDialog(null, new Object[]{"Crie o primeiro administrador", "Nome", nome, "E-mail", email, "Senha (mínimo 6 caracteres)", senha}, "Primeiro acesso", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                UsuarioModel admin = new UsuarioModel();
                admin.setNome(Validador.obrigatorio(nome.getText(), "o nome"));
                admin.setEmail(Validador.email(email.getText(), true));
                admin.setSenha(Senhas.gerar(new String(senha.getPassword())));
                admin.setPerfil(PerfilUsuario.ADMINISTRADOR);
                admin.setAtivo(true);
                admin.setDataCadastro(java.time.LocalDateTime.now());
                dao.salvar(admin);
                return true;
            } catch (RuntimeException e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
        }
        return false;
    }
}
