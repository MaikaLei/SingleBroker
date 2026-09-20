package model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Table(name="agenda")
public class AgendaModel {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private LocalDate data;
    @Column(nullable=false) private LocalTime horario;
    @Column(nullable=false) private String tipo;
    @Column(nullable=false, length=2000) private String descricao;
    @Column(nullable=false) private boolean concluida;
    @ManyToOne(optional=false) @JoinColumn(name="usuario_id") private UsuarioModel usuario;
    public Long getId() { return id; }
    public void setId(Long v) { id=v; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate v) { data=v; }
    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime v) { horario=v; }
    public String getTipo() { return tipo; }
    public void setTipo(String v) { tipo=v; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String v) { descricao=v; }
    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean v) { concluida=v; }
    public UsuarioModel getUsuario() { return usuario; }
    public void setUsuario(UsuarioModel v) { usuario=v; }
}
