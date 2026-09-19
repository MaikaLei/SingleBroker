package controller;
import model.AgendaModel;
import dao.AgendaDao;
import util.Validador;
public class AgendaController {
    public void salvar(AgendaModel tarefa) {
        if (tarefa.getData() == null || tarefa.getHorario() == null) throw new IllegalArgumentException("Informe a data e o horário.");
        tarefa.setTipo(Validador.obrigatorio(tarefa.getTipo(), "o tipo"));
        tarefa.setDescricao(Validador.obrigatorio(tarefa.getDescricao(), "a descrição"));
        if (tarefa.getDescricao().length() > 2000) throw new IllegalArgumentException("A descrição deve ter até 2000 caracteres.");
        new AgendaDao().salvar(tarefa);
    }
}
