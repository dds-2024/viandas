package ar.edu.utn.dds.k3003.clients;

import java.util.List;

import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.Call;

public interface HeladerasRetrofitCliente {
    @GET("heladeras/{heladeraId}/temperaturas")
    Call<List<TemperaturaDTO>> get(@Path("heladeraId") Integer heladeraId);
}
