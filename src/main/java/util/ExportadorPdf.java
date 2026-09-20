package util;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;

public final class ExportadorPdf {
    private ExportadorPdf() { }
    public static void salvar(Path destino, String titulo, String texto) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDFont fonte = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDFont negrito = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            List<String> linhas = quebrar(texto, fonte, 10, PDRectangle.A4.getWidth()-96);
            if (linhas.isEmpty()) linhas.add("");
            for (int inicio=0; inicio<linhas.size(); inicio+=44) {
                PDPage pagina=new PDPage(PDRectangle.A4); doc.addPage(pagina);
                try (PDPageContentStream out=new PDPageContentStream(doc,pagina)) {
                    out.beginText(); out.setFont(negrito,16); out.newLineAtOffset(48,790);
                    out.showText(limpar(titulo,negrito)); out.endText();
                    out.beginText(); out.setFont(fonte,10); out.setLeading(15); out.newLineAtOffset(48,758);
                    for(int i=inicio;i<Math.min(inicio+44,linhas.size());i++) { out.showText(linhas.get(i)); out.newLine(); }
                    out.endText();
                    out.beginText(); out.setFont(fonte,9); out.newLineAtOffset(48,40);
                    out.showText("SingleBroker | " + java.time.LocalDate.now() + " | Página " + (inicio/44+1)); out.endText();
                }
            }
            Path temporario=Files.createTempFile(destino.toAbsolutePath().getParent(),"singlebroker-", ".pdf");
            try { doc.save(temporario.toFile()); Files.move(temporario,destino,StandardCopyOption.REPLACE_EXISTING); }
            finally { Files.deleteIfExists(temporario); }
        }
    }
    private static List<String> quebrar(String texto, PDFont fonte, float tamanho, float largura) throws IOException {
        List<String> linhas=new ArrayList<>();
        for(String paragrafo:texto.split("\\R",-1)) {
            StringBuilder linha=new StringBuilder();
            for(char c:limpar(paragrafo,fonte).toCharArray()) {
                if(fonte.getStringWidth(linha.toString()+c)/1000*tamanho>largura) {
                    int espaco=linha.lastIndexOf(" ");
                    if(espaco>0) { linhas.add(linha.substring(0,espaco)); linha=new StringBuilder(linha.substring(espaco+1)); }
                    else { linhas.add(linha.toString()); linha.setLength(0); }
                }
                linha.append(c);
            }
            linhas.add(linha.toString());
        }
        return linhas;
    }
    private static String limpar(String texto,PDFont fonte) throws IOException {
        StringBuilder resultado=new StringBuilder();
        for(char c:texto.replace('\t',' ').toCharArray()) {
            try { fonte.encode(String.valueOf(c)); resultado.append(c); }
            catch(IllegalArgumentException e) { resultado.append('?'); }
        }
        return resultado.toString();
    }
    public static void escolherESalvar(java.awt.Component pai,String titulo,String texto) {
        javax.swing.JFileChooser seletor=new javax.swing.JFileChooser();
        seletor.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF","pdf"));
        seletor.setSelectedFile(new java.io.File("singlebroker.pdf"));
        if(seletor.showSaveDialog(pai)!=javax.swing.JFileChooser.APPROVE_OPTION) return;
        Path destino=seletor.getSelectedFile().toPath();
        if(!destino.toString().toLowerCase().endsWith(".pdf")) destino=Path.of(destino+".pdf");
        if(Files.exists(destino)&&javax.swing.JOptionPane.showConfirmDialog(pai,"Substituir o arquivo existente?","Confirmar",javax.swing.JOptionPane.YES_NO_OPTION)!=javax.swing.JOptionPane.YES_OPTION) return;
        try { salvar(destino,titulo,texto); javax.swing.JOptionPane.showMessageDialog(pai,"PDF salvo com sucesso."); }
        catch(IOException e) { javax.swing.JOptionPane.showMessageDialog(pai,"Não foi possível salvar o PDF."); }
    }
}
