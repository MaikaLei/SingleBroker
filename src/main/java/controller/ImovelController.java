package controller;
import dao.ImovelDao;
import model.ImovelModel;
import util.Validador;
public class ImovelController {
    public void salvar(ImovelModel imovel) {
        if (imovel.getProprietario() == null || imovel.getProprietario().getId() == null)
            throw new IllegalArgumentException("Selecione um proprietário cadastrado.");
        imovel.setEndereco(Validador.obrigatorio(imovel.getEndereco(), "o endereço"));
        imovel.setCidade(Validador.obrigatorio(imovel.getCidade(), "a cidade"));
        imovel.setEstado(Validador.obrigatorio(imovel.getEstado(), "o estado").toUpperCase());
        Validador.decimal(Validador.obrigatorio(imovel.getValor(), "o valor do imóvel"), "valor do imóvel");
        Validador.decimal(imovel.getValorAdministracao(), "administração");
        Validador.decimal(imovel.getValorCondominio(), "condomínio");
        Validador.decimal(imovel.getValorIptu(), "IPTU");
        Validador.data(imovel.getDataEntrada(), "a data de entrada");
        Validador.data(imovel.getValidadeContrato(), "a validade do contrato");
        ImovelDao dao = new ImovelDao();
        if (imovel.getId() == null) {
            imovel.setDataCadastro(java.time.LocalDate.now());
            imovel.setUsuarioCadastro(util.SessaoUsuario.getUsuarioLogado());
        }
        String statusAnterior = imovel.getId() == null ? "" : dao.buscarPorId(imovel.getId()).getStatusImovel();
        if ("Vendido".equals(imovel.getStatusImovel()) && !"Vendido".equals(statusAnterior)) imovel.setDataVenda(java.time.LocalDate.now());
        if (!"Vendido".equals(imovel.getStatusImovel())) imovel.setDataVenda(null);
        if (imovel.getId() == null) dao.salvar(imovel); else dao.atualizar(imovel);
    }
}
