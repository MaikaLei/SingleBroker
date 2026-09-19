package controller;
import model.MinhaPaginaModel;
public class MinhaPaginaController {
    public void salvar(MinhaPaginaModel pagina) {
        pagina.setEmail(util.Validador.email(pagina.getEmail(),false));
        if (pagina.getTitulo().length()>500 || pagina.getDescricao().length()>2000 || pagina.getBibliografia().length()>5000)
            throw new IllegalArgumentException("Limites: título 500, descrição 2000 e bibliografia 5000 caracteres.");
        new dao.MinhaPaginaDao().salvar(pagina);
    }
}
