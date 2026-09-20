package controller;
import java.nio.file.*;
import model.*;
public class AnexoController {
    public static AnexoModel ler(Path arquivo, boolean foto) {
        try {
            if (!Files.isRegularFile(arquivo) || Files.size(arquivo) == 0 || Files.size(arquivo) > 5 * 1024 * 1024)
                throw new IllegalArgumentException("Escolha um arquivo de até 5 MB, não vazio.");
            byte[] conteudo = Files.readAllBytes(arquivo);
            if (foto && javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(conteudo)) == null)
                throw new IllegalArgumentException("Escolha uma imagem JPG, PNG ou GIF válida.");
            AnexoModel anexo = foto ? new FotoModel() : new DocumentoModel();
            anexo.setNome(arquivo.getFileName().toString());
            anexo.setConteudo(conteudo);
            return anexo;
        } catch (java.io.IOException e) { throw new IllegalArgumentException("Não foi possível ler o arquivo.", e); }
    }
}
