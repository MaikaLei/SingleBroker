package view;
import java.awt.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.*;
public class AnexosPanel extends JPanel {
    private final boolean fotos;
    private final Long imovelId;
    private final DefaultListModel<AnexoModel> modelo = new DefaultListModel<>();
    private final JList<AnexoModel> lista = new JList<>(modelo);
    private final List<Long> removidos = new ArrayList<>();
    public AnexosPanel(Long imovelId, boolean fotos) {
        super(new BorderLayout(8,8)); this.imovelId=imovelId; this.fotos=fotos;
        List<? extends AnexoModel> existentes = fotos ? new dao.FotoDao().listar(imovelId) : new dao.DocumentoDao().listar(imovelId);
        existentes.forEach(modelo::addElement);
        add(new JScrollPane(lista), BorderLayout.CENTER);
        JPanel botoes = new JPanel();
        JButton adicionar = new JButton("Adicionar");
        JButton remover = new JButton("Remover selecionado");
        JButton baixar = new JButton("Salvar cópia");
        botoes.add(adicionar); botoes.add(remover); botoes.add(baixar);
        add(botoes, BorderLayout.SOUTH);
        adicionar.addActionListener(e -> adicionar());
        remover.addActionListener(e -> {
            for (AnexoModel a : lista.getSelectedValuesList()) { if (a.getId() != null) removidos.add(a.getId()); modelo.removeElement(a); }
        });
        baixar.addActionListener(e -> baixar());
        if (fotos) {
            JLabel preview = new JLabel("Selecione uma foto", SwingConstants.CENTER);
            preview.setPreferredSize(new Dimension(220,180)); add(preview, BorderLayout.EAST);
            lista.addListSelectionListener(e -> {
                AnexoModel a = lista.getSelectedValue();
                preview.setText(a == null ? "Selecione uma foto" : "");
                preview.setIcon(a == null ? null : new ImageIcon(new ImageIcon(a.getConteudo()).getImage().getScaledInstance(220, 165, Image.SCALE_SMOOTH)));
            });
        }
    }
    private void adicionar() {
        JFileChooser seletor = new JFileChooser(); seletor.setMultiSelectionEnabled(true);
        if (fotos) seletor.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagens", "jpg", "jpeg", "png", "gif"));
        if (seletor.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        for (java.io.File arquivo : seletor.getSelectedFiles()) {
            try { modelo.addElement(controller.AnexoController.ler(arquivo.toPath(), fotos)); }
            catch (RuntimeException e) { JOptionPane.showMessageDialog(this, arquivo.getName() + ": " + e.getMessage()); }
        }
    }
    private void baixar() {
        AnexoModel a = lista.getSelectedValue(); if (a == null) return;
        JFileChooser seletor = new JFileChooser(); seletor.setSelectedFile(new java.io.File(a.getNome()));
        if (seletor.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        Path destino = seletor.getSelectedFile().toPath();
        if (Files.exists(destino) && JOptionPane.showConfirmDialog(this, "Substituir o arquivo existente?", "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try { Files.write(destino, a.getConteudo()); }
        catch (java.io.IOException e) { JOptionPane.showMessageDialog(this, "Não foi possível salvar a cópia."); }
    }
    public void salvar() {
        if (fotos) {
            List<FotoModel> novos = new ArrayList<>();
            for (int i=0; i<modelo.size(); i++) if (modelo.get(i).getId() == null) novos.add((FotoModel)modelo.get(i));
            new dao.FotoDao().salvar(imovelId, novos, removidos);
        } else {
            List<DocumentoModel> novos = new ArrayList<>();
            for (int i=0; i<modelo.size(); i++) if (modelo.get(i).getId() == null) novos.add((DocumentoModel)modelo.get(i));
            new dao.DocumentoDao().salvar(imovelId, novos, removidos);
        }
        removidos.clear();
    }
}
