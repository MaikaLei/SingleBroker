package model;
import jakarta.persistence.*;
@Entity @Table(name="minha_pagina")
public class MinhaPaginaModel {
    @Id private Long usuarioId;
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long v) { usuarioId=v; }
    @Column(length=500) private String titulo;
    public String getTitulo() { return titulo; }
    public void setTitulo(String v) { titulo=v; }
    @Column(length=2000) private String descricao;
    public String getDescricao() { return descricao; }
    public void setDescricao(String v) { descricao=v; }
    @Column(length=5000) private String bibliografia;
    public String getBibliografia() { return bibliografia; }
    public void setBibliografia(String v) { bibliografia=v; }
     private String telefone;
    public String getTelefone() { return telefone; }
    public void setTelefone(String v) { telefone=v; }
     private String email;
    public String getEmail() { return email; }
    public void setEmail(String v) { email=v; }
     private String instagram;
    public String getInstagram() { return instagram; }
    public void setInstagram(String v) { instagram=v; }
    @Lob @Column(columnDefinition="LONGBLOB") private byte[] foto;
    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] v) { foto=v; }
}
