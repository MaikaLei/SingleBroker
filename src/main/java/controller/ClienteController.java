package controller;
import dao.ClienteDao;
import model.*;
import util.Validador;
public class ClienteController {
    public void salvar(ClienteModel cliente) {
        cliente.setEmail(Validador.email(cliente.getEmail(), false));
        if (cliente instanceof ClientePfModel pf) {
            pf.setNome(Validador.obrigatorio(pf.getNome(), "o nome"));
            pf.setCpf(Validador.documento(pf.getCpf(), "CPF"));
            if (pf.getDataNascimento() != null && pf.getDataNascimento().isAfter(java.time.LocalDate.now()))
                throw new IllegalArgumentException("A data de nascimento não pode estar no futuro.");
        } else if (cliente instanceof ClientePjModel pj) {
            pj.setRazaoSocial(Validador.obrigatorio(pj.getRazaoSocial(), "a razão social"));
            pj.setCnpj(Validador.documento(pj.getCnpj(), "CNPJ"));
        } else throw new IllegalArgumentException("Selecione o tipo de cliente.");
        ClienteDao consulta = new ClienteDao();
        boolean duplicado = cliente instanceof ClientePfModel pf
            ? consulta.listarPf().stream().anyMatch(c -> !java.util.Objects.equals(c.getId(), pf.getId()) && c.getCpf() != null && c.getCpf().replaceAll("[^0-9]", "").equals(pf.getCpf()))
            : consulta.listarPj().stream().anyMatch(c -> !java.util.Objects.equals(c.getId(), cliente.getId()) && c.getCnpj() != null && c.getCnpj().replaceAll("[^0-9]", "").equals(((ClientePjModel)cliente).getCnpj()));
        if (duplicado) throw new IllegalArgumentException("Já existe um cliente com este CPF/CNPJ.");
        if (cliente.getId() == null) {
            cliente.setDataCadastro(java.time.LocalDate.now());
            cliente.setUsuarioCadastro(util.SessaoUsuario.getUsuarioLogado());
        }
        ClienteDao dao = new ClienteDao();
        if (cliente.getId() == null) dao.salvar(cliente); else dao.atualizar(cliente);
    }
}
