package ar.edu.utn.dds.k3003.model;

import java.time.LocalDateTime;
import javax.persistence.*;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "viandas")
public class Vianda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String qr;

    private Long colaboradorId;

    private Integer heladeraId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoViandaEnum estado;

    private LocalDateTime fechaElaboracion;

    public Vianda(
        String qr,
        long colaboradorId,
        Integer heladeraId,
        EstadoViandaEnum estado,
        LocalDateTime fechaElaboracion
    ) {
        this.qr = qr;
        this.colaboradorId = colaboradorId;
        this.heladeraId = heladeraId;
        this.estado = estado;
        this.fechaElaboracion = fechaElaboracion;
    }

    public Vianda() {

    }
}
