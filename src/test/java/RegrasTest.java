import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.math.BigDecimal;
import util.*;

class RegrasTest {
    @TempDir Path pasta;
    @Test void senhasTemSaltERejeitamSenhaIncorreta() {
        String hash=Senhas.gerar("senha123");
        assertTrue(Senhas.verificar("senha123",hash));
        assertFalse(Senhas.verificar("outra",hash));
        assertNotEquals(hash,Senhas.gerar("senha123"));
        assertTrue(Senhas.verificar("antiga","antiga"));
        assertFalse(Senhas.verificar("senha","pbkdf2$invalido"));
        assertFalse(Senhas.verificar("senha",null));
    }
    @Test void validaDocumentosComDigitosVerificadores() {
        assertEquals("52998224725",Validador.documento("529.982.247-25","CPF"));
        assertEquals("11222333000181",Validador.documento("11.222.333/0001-81","CNPJ"));
        assertThrows(IllegalArgumentException.class,()->Validador.documento("52998224724","CPF"));
        assertThrows(IllegalArgumentException.class,()->Validador.documento("11111111111","CPF"));
        assertThrows(IllegalArgumentException.class,()->Validador.documento("11222333000182","CNPJ"));
    }
    @Test void validaDatasBrasileirasENaoAceitaDatasImpossiveis() {
        assertEquals(java.time.LocalDate.of(2000,2,29),Validador.data("29/02/2000","nascimento"));
        assertEquals(java.time.LocalDate.of(2000,2,29),Validador.data("2000-02-29","nascimento"));
        assertThrows(IllegalArgumentException.class,()->Validador.data("31/02/2000","nascimento"));
        assertNull(Validador.data("","nascimento"));
    }
    @Test void validaValoresBrasileirosENumerosInvalidos() {
        assertEquals(new BigDecimal("1250.50"),Validador.decimal("R$ 1.250,50","valor"));
        assertEquals(new BigDecimal("123.45"),Validador.decimal("123.45","valor"));
        assertThrows(IllegalArgumentException.class,()->Validador.inteiro("-1","vagas"));
        assertThrows(IllegalArgumentException.class,()->Validador.decimal("abc","valor"));
        assertThrows(IllegalArgumentException.class,()->Validador.email("sem-arroba",true));
    }
    @Test void anexoInvalidoNaoEhAceitoComoFoto() throws Exception {
        Path arquivo=pasta.resolve("imagem.png"); Files.writeString(arquivo,"não é uma imagem");
        assertThrows(IllegalArgumentException.class,()->controller.AnexoController.ler(arquivo,true));
        assertArrayEquals(Files.readAllBytes(arquivo),controller.AnexoController.ler(arquivo,false).getConteudo());
        Files.write(arquivo,new byte[0]);
        assertThrows(IllegalArgumentException.class,()->controller.AnexoController.ler(arquivo,false));
    }
    @Test void pdfTemAcentosPaginacaoEConteudoCompleto() throws Exception {
        Path arquivo=pasta.resolve("relatorio.pdf");
        String texto="Descrição: imóvel à venda, área privativa 120 m².\n".repeat(100)+"MARCADOR FINAL";
        ExportadorPdf.salvar(arquivo,"Relatório de imóveis",texto);
        try(var doc=org.apache.pdfbox.Loader.loadPDF(arquivo.toFile())) {
            assertEquals(3,doc.getNumberOfPages());
            String extraido=new org.apache.pdfbox.text.PDFTextStripper().getText(doc);
            assertTrue(extraido.contains("Descrição: imóvel à venda"));
            assertTrue(extraido.contains("MARCADOR FINAL"));
            Path preview=Path.of(".work/previews");Files.createDirectories(preview);
            javax.imageio.ImageIO.write(new org.apache.pdfbox.rendering.PDFRenderer(doc).renderImageWithDPI(0,90),"png",preview.resolve("RelatorioPdf.png").toFile());
        }
    }
    @Test void criativosUsamDadosReaisESaoDiferentesPorTipo() {
        model.ImovelModel i=new model.ImovelModel(); i.setId(15L); i.setTipoImovel("Casa"); i.setTransacao("Venda"); i.setCidade("Campo Bom"); i.setValor("500.000,00");
        controller.CriativoController c=new controller.CriativoController();
        String ficha=c.gerar(i,"Ficha resumida do imóvel");
        assertTrue(ficha.contains("Código: 15")); assertTrue(ficha.contains("Campo Bom"));
        assertNotEquals(ficha,c.gerar(i,"Texto para mensagem"));
        assertThrows(IllegalArgumentException.class,()->c.gerar(null,"Descrição para anúncio"));
    }
}
