package controller;
import model.ImovelModel;
public class CriativoController {
    private String texto(Object valor) { return valor == null ? "Não informado" : valor.toString(); }
    public String gerar(ImovelModel i, String tipo) {
        if(i==null) throw new IllegalArgumentException("Selecione um imóvel.");
        String resumo=texto(i.getTipoImovel())+" para "+texto(i.getTransacao()).toLowerCase()+" em "+texto(i.getCidade());
        String dados="Código: "+i.getId()+"\nValor: "+texto(i.getValor())+"\nBairro: "+texto(i.getBairro())
            +"\nÁrea privativa: "+texto(i.getAreaPrivativa())+" m²\nDormitórios: "+texto(i.getDormitorios())+" | Banheiros: "+texto(i.getBanheiros())+" | Vagas: "+texto(i.getVagasGaragem());
        String descricao=i.getApresentacao()==null?"":i.getApresentacao();
        return switch(tipo) {
            case "Texto para mensagem" -> "Olá! Confira este imóvel: "+resumo+".\n\n"+dados+"\n\n"+descricao+"\n\nEntre em contato para saber mais e agendar uma visita.";
            case "Ficha resumida do imóvel" -> resumo+"\n\n"+dados+"\nCondomínio: "+texto(i.getValorCondominio())+"\nIPTU: "+texto(i.getValorIptu())+"\nSituação: "+texto(i.getStatusImovel());
            case "Legenda Redes Sociais" -> resumo+"\n\n"+dados+"\n\n"+descricao+"\n\nSaiba mais: entre em contato.\n#Imóveis #CorretorDeImóveis";
            default -> resumo+"\n\n"+descricao+"\n\n"+dados+"\n\nAgende uma visita para conhecer o imóvel.";
        };
    }
}
