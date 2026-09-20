package model;
import jakarta.persistence.*;
@MappedSuperclass
public abstract class AnexoModel {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="imovel_id") private ImovelModel imovel;
    @Column(nullable=false) private String nome;
    @Lob @Column(nullable=false, columnDefinition="LONGBLOB") private byte[] conteudo;
    public Long getId() { return id; }
    public void setId(Long id) { this.id=id; }
    public ImovelModel getImovel() { return imovel; }
    public void setImovel(ImovelModel imovel) { this.imovel=imovel; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome=nome; }
    public byte[] getConteudo() { return conteudo; }
    public void setConteudo(byte[] conteudo) { this.conteudo=conteudo; }
    public String toString() { return nome; }
}
