package ar.edu.utn.dds.k3003.app;

import java.util.List;
import java.util.NoSuchElementException;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.model.Vianda;
import ar.edu.utn.dds.k3003.repositories.ViandaRepository;
import ar.edu.utn.dds.k3003.repositories.ViandaMapper;

public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaViandas{

    private FachadaHeladeras fachadaHeladeras;
    private ViandaRepository viandaRepository;
    private ViandaMapper viandaMapper;
    
    public Fachada() {
        this.viandaRepository = new ViandaRepository();
        this.viandaMapper = new ViandaMapper();
    }

    @Override
    public ViandaDTO agregar(ViandaDTO viandaDTO) {
        Vianda vianda = new Vianda(
            viandaDTO.getCodigoQR(),
            viandaDTO.getColaboradorId(),
            viandaDTO.getHeladeraId(),
            viandaDTO.getEstado(),
            viandaDTO.getFechaElaboracion()
        );

        vianda = this.viandaRepository.save(vianda);
        return this.viandaMapper.map(vianda);
    }

    @Override 
    public ViandaDTO modificarEstado(String qrVianda, EstadoViandaEnum estado) throws NoSuchElementException {
        Vianda vianda = this.viandaRepository.findByQR(qrVianda);
        vianda = this.viandaRepository.updateEstado(vianda, estado);
        return this.viandaMapper.map(vianda);
    }

    @Override
    public List<ViandaDTO> viandasDeColaborador(Long colaboradorId, Integer mes, Integer anio) throws NoSuchElementException {
        List<Vianda> viandas = this.viandaRepository.findByColaboradorMesAnio(colaboradorId, mes, anio);
        return viandas.stream().map(this.viandaMapper::map).toList();
    }

    @Override
    public ViandaDTO buscarXQR(String qr) throws NoSuchElementException {
        Vianda vianda = this.viandaRepository.findByQR(qr);
        return this.viandaMapper.map(vianda);
    }

    @Override
    public boolean evaluarVencimiento(String QR) throws NoSuchElementException {
        Vianda vianda = this.viandaRepository.findByQR(QR);
        List<TemperaturaDTO> temperaturas = this.fachadaHeladeras.obtenerTemperaturas(vianda.getHeladeraId());
        for (TemperaturaDTO temperatura : temperaturas) {
            if (temperatura.getTemperatura() > 5) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ViandaDTO modificarHeladera(String qrVianda, int heladeraDestino) {
        Vianda vianda = this.viandaRepository.findByQR(qrVianda);
        vianda = this.viandaRepository.updateHeladera(vianda, heladeraDestino);
        return this.viandaMapper.map(vianda);
    }

    @Override
    public void setHeladerasProxy(FachadaHeladeras fachadaHeladeras) {
        this.fachadaHeladeras = fachadaHeladeras;
    }

}
