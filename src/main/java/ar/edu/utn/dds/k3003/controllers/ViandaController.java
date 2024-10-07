package ar.edu.utn.dds.k3003.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class ViandaController {
    private final Fachada fachada;

    public ViandaController(Fachada fachada) {
        this.fachada = fachada;
    }

    public void add(Context context) {
        ViandaDTO viandaDTO = context.bodyAsClass(ViandaDTO.class);
        ViandaDTO viandaDTORta = this.fachada.agregar(viandaDTO);
        context.json(viandaDTORta);
        context.status(HttpStatus.CREATED);
    }
    
    public void findByColaboradorIdAndAnioAndMes(Context context) {
        Long colaboradorId = context.queryParamAsClass("colaboradorId", Long.class).get();
        Integer anio = context.queryParamAsClass("anio", Integer.class).get();
        Integer mes = context.queryParamAsClass("mes", Integer.class).get();

        try {
            List<ViandaDTO> viandaDTORta = this.fachada.viandasDeColaborador(colaboradorId, mes, anio);
            context.json(viandaDTORta);
        } catch (NoSuchElementException ex) {
            context.result(ex.getLocalizedMessage());
            context.status(HttpStatus.NOT_FOUND);
        }
    }

    public void findByQR(Context context) {
        String QR = context.pathParamAsClass("qr", String.class).get();
        try {
            ViandaDTO viandaDTORta = this.fachada.buscarXQR(QR);
            context.json(viandaDTORta);
        } catch (NoSuchElementException ex) {
            context.result(ex.getLocalizedMessage());
            context.status(HttpStatus.NOT_FOUND);
        }
    }

    public void viandaVencida(Context context) {
        String QR = context.pathParamAsClass("qr", String.class).get();
        try {
            Boolean viandaDTORta = this.fachada.evaluarVencimiento(QR);
            context.json(viandaDTORta);
        } catch (NoSuchElementException ex) {
            context.result(ex.getLocalizedMessage());
            context.status(HttpStatus.NOT_FOUND);
        }
    }
    
    public void updateHeladera(Context context) throws Exception {
        String QR = context.pathParamAsClass("qr", String.class).get();
        try {
            String body = context.body();
            JSONObject jsonBody = new JSONObject(body);
            int heladeraId = jsonBody.getInt("heladeraId");
            ViandaDTO viandaDTORta = this.fachada.modificarHeladera(QR, heladeraId);
            context.json(viandaDTORta);
        } catch (NoSuchElementException ex) {
            context.result(ex.getLocalizedMessage());
            context.status(HttpStatus.NOT_FOUND);
        }
    }

    public void updateEstado(Context context) throws Exception {
        String QR = context.pathParamAsClass("qr", String.class).get();
        try {
            String body = context.body();
            JSONObject jsonBody = new JSONObject(body);
            String estadoString = jsonBody.getString("estado");
            EstadoViandaEnum estado = EstadoViandaEnum.valueOf(estadoString.toUpperCase());
            ViandaDTO viandaDTORta = this.fachada.modificarEstado(QR, estado);
            context.json(viandaDTORta);
        } catch (NoSuchElementException ex) {
            context.result(ex.getLocalizedMessage());
            context.status(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            context.result("Estado de vianda inválido");
            context.status(HttpStatus.BAD_REQUEST);
        }
    }
}
