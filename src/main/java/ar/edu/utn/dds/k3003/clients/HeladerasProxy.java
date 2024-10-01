package ar.edu.utn.dds.k3003.clients;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import io.javalin.http.HttpStatus;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class HeladerasProxy implements FachadaHeladeras {
    private final String endpoint;
    private final HeladerasRetrofitCliente service;

    public HeladerasProxy(ObjectMapper objectMapper) {
        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_HELADERAS", "https://two024-tp-entrega-3-gastonpaz.onrender.com");

        
        var retrofit =
            new Retrofit.Builder()
                .baseUrl(this.endpoint)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        this.service = retrofit.create(HeladerasRetrofitCliente.class);
    }

    @Override
    public HeladeraDTO agregar(HeladeraDTO heladera) {
        return null;
    }

    @Override
    public void depositar(Integer heladeraId, String qrVianda) throws NoSuchElementException {}

    @Override
    public Integer cantidadViandas(Integer heladeraId) throws NoSuchElementException {
        return null;
    }

    @Override
    public void retirar(RetiroDTO retiro) throws NoSuchElementException {}

    @Override
    public void temperatura(TemperaturaDTO temperatura) {}

    @Override
    public List<TemperaturaDTO> obtenerTemperaturas(Integer heladeraId) {
        Response<List<TemperaturaDTO>> response;
        try {
            response = service.get(heladeraId).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            if (response.code() == HttpStatus.NOT_FOUND.getCode()) {
                throw new NoSuchElementException("No se encontró la heladera con el ID: " + heladeraId);
            }
            throw new RuntimeException("Error al obtener las temperaturas de la heladera: " + response.code());
        } catch (IOException e) {
            throw new RuntimeException("Error de IO al obtener las temperaturas de la heladera", e);
        }
    }

    @Override
    public void setViandasProxy(FachadaViandas viandas) {}
}
