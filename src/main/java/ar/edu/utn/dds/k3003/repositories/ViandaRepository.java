package ar.edu.utn.dds.k3003.repositories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.model.Vianda;

public class ViandaRepository {
    private EntityManagerFactory _emf;

    public ViandaRepository() {
        _emf = Persistence.createEntityManagerFactory("dds");
    }

    public Vianda save(Vianda vianda) {
        EntityManager em = _emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(vianda);
        em.getTransaction().commit();
        return vianda;
    }

    public Vianda findByQR(String qr) {
        EntityManager em = _emf.createEntityManager();
        TypedQuery<Vianda> query = em.createQuery("SELECT v FROM Vianda v WHERE v.qr = :qr ORDER BY v.id desc", Vianda.class);
        query.setParameter("qr", qr);
        List<Vianda> results = query.getResultList();
        em.close();
        return results.isEmpty() ? null : results.get(0);
    }

    public Vianda updateEstado(Vianda vianda, EstadoViandaEnum estado) {
        EntityManager em = _emf.createEntityManager();
        em.getTransaction().begin();
        vianda.setEstado(estado);
        vianda = em.merge(vianda);
        em.getTransaction().commit();
        em.close();
        return vianda;
    }

    public List<Vianda> getViandas() {
        EntityManager em = _emf.createEntityManager();
        TypedQuery<Vianda> query = em.createQuery("SELECT v FROM Vianda v", Vianda.class);
        List<Vianda> results = query.getResultList();
        em.close();
        return results;
    }

    public List<Vianda> findByColaboradorMesAnio(Long colaboradorId, Integer mes, Integer anio) {
        List<Vianda> viandasDeColaborador = new ArrayList<>();

        for (Vianda vianda : this.getViandas()) {
            LocalDateTime fechaVianda = vianda.getFechaElaboracion();
            if (vianda.getColaboradorId().equals(colaboradorId) &&
                    fechaVianda.getMonthValue() == mes &&
                    fechaVianda.getYear() == anio) {
                viandasDeColaborador.add(vianda);
            }
        }

        if (viandasDeColaborador.isEmpty()) {
            throw new NoSuchElementException("No se encontraron viandas para el colaborador y fecha especificados.");
        }

        return viandasDeColaborador;
    }
    

    public Vianda updateHeladera(Vianda vianda, int heladeraDestino) {
        EntityManager em = _emf.createEntityManager();
        em.getTransaction().begin();
        vianda.setHeladeraId(heladeraDestino);
        vianda = em.merge(vianda);
        em.getTransaction().commit();
        em.close();
        return vianda;
    }

    public void deleteAll() {
        EntityManager em = _emf.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Vianda> delete = cb.createCriteriaDelete(Vianda.class);
        delete.from(Vianda.class);
        em.createQuery(delete).executeUpdate();
        // Reiniciar los IDs
        em.createNativeQuery("ALTER SEQUENCE viandas_id_seq RESTART WITH 1").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
