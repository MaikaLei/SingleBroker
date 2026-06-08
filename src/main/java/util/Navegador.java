/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Maikon
 */
public class Navegador {

    public static void abrirTela(
            JFrame telaAtual,
            JFrame novaTela,
            boolean alterado
    ) {

        if (alterado) {

            int opcao = JOptionPane.showConfirmDialog(
                    telaAtual,
                    "Você alterou os dados. Deseja salvar?",
                    "Confirmação",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );

            if (opcao == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        novaTela.setVisible(true);
        telaAtual.dispose();
    }

}
