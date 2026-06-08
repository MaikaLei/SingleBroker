package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Maikon
 */
@Entity
@Table(name = "imovel")
public class ImovelModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoImovel;
    private String statusImovel;
    private String transacao;

    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    @ManyToOne
    @JoinColumn(name = "proprietario_id")
    private ClienteModel proprietario;

    private String responsavel;

    private String dataEntrada;
    private String validadeContrato;

    private String valor;
    private String valorAdministracao;
    private String valorCondominio;
    private String valorIptu;

    private Integer dormitorios;
    private Integer salas;
    private Integer banheiros;
    private Integer suites;
    private Integer lavanderia;
    private Integer vagasGaragem;
    private Integer sacada;

    private Boolean patio;
    private Boolean varanda;
    private Boolean gradeado;
    private Boolean murado;
    private Boolean alarme;
    private Boolean quiosqueCasa;
    private Boolean canil;
    private Boolean portao;
    private Boolean piscinaCasa;

    private Boolean elevador;
    private Boolean portaria;
    private Boolean porteiro;
    private Boolean piscinaCond;
    private Boolean quiosqueCond;
    private Boolean playground;
    private Boolean salaoFestas;
    private Boolean brinquedoteca;
    private Boolean quadraEsportes;
    private Boolean churrasqueira;
    private Boolean lavanderiaCond;
    private Boolean academia;
    private Boolean espacoPet;
    private Boolean coworking;
    private Boolean cameras;

    private BigDecimal areaTotal;
    private BigDecimal areaPrivativa;
    private BigDecimal areaTerreno;

    @Column(length = 5000)
    private String apresentacao;

    @Column(length = 5000)
    private String negociacao;

    private Boolean ativo = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoImovel() {
        return tipoImovel;
    }

    public void setTipoImovel(String tipoImovel) {
        this.tipoImovel = tipoImovel;
    }

    public String getStatusImovel() {
        return statusImovel;
    }

    public void setStatusImovel(String statusImovel) {
        this.statusImovel = statusImovel;
    }

    public String getTransacao() {
        return transacao;
    }

    public void setTransacao(String transacao) {
        this.transacao = transacao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public ClienteModel getProprietario() {
        return proprietario;
    }

    public void setProprietario(ClienteModel proprietario) {
        this.proprietario = proprietario;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getValidadeContrato() {
        return validadeContrato;
    }

    public void setValidadeContrato(String validadeContrato) {
        this.validadeContrato = validadeContrato;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValorAdministracao() {
        return valorAdministracao;
    }

    public void setValorAdministracao(String valorAdministracao) {
        this.valorAdministracao = valorAdministracao;
    }

    public String getValorCondominio() {
        return valorCondominio;
    }

    public void setValorCondominio(String valorCondominio) {
        this.valorCondominio = valorCondominio;
    }

    public String getValorIptu() {
        return valorIptu;
    }

    public void setValorIptu(String valorIptu) {
        this.valorIptu = valorIptu;
    }

    public Integer getDormitorios() {
        return dormitorios;
    }

    public void setDormitorios(Integer dormitorios) {
        this.dormitorios = dormitorios;
    }

    public Integer getSalas() {
        return salas;
    }

    public void setSalas(Integer salas) {
        this.salas = salas;
    }

    public Integer getBanheiros() {
        return banheiros;
    }

    public void setBanheiros(Integer banheiros) {
        this.banheiros = banheiros;
    }

    public Integer getSuites() {
        return suites;
    }

    public void setSuites(Integer suites) {
        this.suites = suites;
    }

    public Integer getLavanderia() {
        return lavanderia;
    }

    public void setLavanderia(Integer lavanderia) {
        this.lavanderia = lavanderia;
    }

    public Integer getVagasGaragem() {
        return vagasGaragem;
    }

    public void setVagasGaragem(Integer vagasGaragem) {
        this.vagasGaragem = vagasGaragem;
    }

    public void setSacada(Integer sacada) {
        this.sacada = sacada;
    }

    public Integer getSacada() {
        return sacada;
    }

    public Boolean getPatio() {
        return patio;
    }

    public void setPatio(Boolean patio) {
        this.patio = patio;
    }

    public Boolean getVaranda() {
        return varanda;
    }

    public void setVaranda(Boolean varanda) {
        this.varanda = varanda;
    }

    public Boolean getGradeado() {
        return gradeado;
    }

    public void setGradeado(Boolean gradeado) {
        this.gradeado = gradeado;
    }

    public Boolean getMurado() {
        return murado;
    }

    public void setMurado(Boolean murado) {
        this.murado = murado;
    }

    public Boolean getAlarme() {
        return alarme;
    }

    public void setAlarme(Boolean alarme) {
        this.alarme = alarme;
    }

    public Boolean getQuiosqueCasa() {
        return quiosqueCasa;
    }

    public void setQuiosqueCasa(Boolean quiosqueCasa) {
        this.quiosqueCasa = quiosqueCasa;
    }

    public Boolean getPiscinaCasa() {
        return piscinaCasa;
    }

    public void setPiscinaCasa(Boolean piscinaCasa) {
        this.piscinaCasa = piscinaCasa;
    }

    public Boolean getCanil() {
        return canil;
    }

    public void setCanil(Boolean canil) {
        this.canil = canil;
    }

    public Boolean getPortao() {
        return portao;
    }

    public void setPortao(Boolean portao) {
        this.portao = portao;
    }

    public Boolean getElevador() {
        return elevador;
    }

    public void setElevador(Boolean elevador) {
        this.elevador = elevador;
    }

    public Boolean getPortaria() {
        return portaria;
    }

    public void setPortaria(Boolean portaria) {
        this.portaria = portaria;
    }

    public Boolean getPorteiro() {
        return porteiro;
    }

    public void setPorteiro(Boolean porteiro) {
        this.porteiro = porteiro;
    }

    public Boolean getPiscinaCond() {
        return piscinaCond;
    }

    public void setPiscinaCond(Boolean piscinaCond) {
        this.piscinaCond = piscinaCond;
    }

    public Boolean getQuiosqueCond() {
        return quiosqueCond;
    }

    public void setQuiosqueCond(Boolean quiosqueCond) {
        this.quiosqueCond = quiosqueCond;
    }

    public Boolean getPlayground() {
        return playground;
    }

    public void setPlayground(Boolean playground) {
        this.playground = playground;
    }

    public Boolean getSalaoFestas() {
        return salaoFestas;
    }

    public void setSalaoFestas(Boolean salaoFestas) {
        this.salaoFestas = salaoFestas;
    }

    public Boolean getBrinquedoteca() {
        return brinquedoteca;
    }

    public void setBrinquedoteca(Boolean brinquedoteca) {
        this.brinquedoteca = brinquedoteca;
    }

    public Boolean getQuadraEsportes() {
        return quadraEsportes;
    }

    public void setQuadraEsportes(Boolean quadraEsportes) {
        this.quadraEsportes = quadraEsportes;
    }

    public Boolean getChurrasqueira() {
        return churrasqueira;
    }

    public void setChurrasqueira(Boolean churrasqueira) {
        this.churrasqueira = churrasqueira;
    }

    public Boolean getLavanderiaCond() {
        return lavanderiaCond;
    }

    public void setLavanderiaCond(Boolean lavanderiaCond) {
        this.lavanderiaCond = lavanderiaCond;
    }

    public Boolean getAcademia() {
        return academia;
    }

    public void setAcademia(Boolean academia) {
        this.academia = academia;
    }

    public Boolean getEspacoPet() {
        return espacoPet;
    }

    public void setEspacoPet(Boolean espacoPet) {
        this.espacoPet = espacoPet;
    }

    public Boolean getCoworking() {
        return coworking;
    }

    public void setCoworking(Boolean coworking) {
        this.coworking = coworking;
    }

    public Boolean getCameras() {
        return cameras;
    }

    public void setCameras(Boolean cameras) {
        this.cameras = cameras;
    }

    public BigDecimal getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(BigDecimal areaTotal) {
        this.areaTotal = areaTotal;
    }

    public BigDecimal getAreaPrivativa() {
        return areaPrivativa;
    }

    public void setAreaPrivativa(BigDecimal areaPrivativa) {
        this.areaPrivativa = areaPrivativa;
    }

    public BigDecimal getAreaTerreno() {
        return areaTerreno;
    }

    public void setAreaTerreno(BigDecimal areaTerreno) {
        this.areaTerreno = areaTerreno;
    }

    public String getApresentacao() {
        return apresentacao;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }

    public String getNegociacao() {
        return negociacao;
    }

    public void setNegociacao(String negociacao) {
        this.negociacao = negociacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return endereco + ", " + numero;
    }
}
