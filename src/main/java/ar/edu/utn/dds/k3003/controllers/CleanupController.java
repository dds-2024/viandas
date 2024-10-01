package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.repositories.ViandaRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class CleanupController {
    private final ViandaRepository viandaRepository;

    public CleanupController() {
        this.viandaRepository = new ViandaRepository();
    }

    public void cleanup(Context context) {
        viandaRepository.deleteAll();
        context.status(HttpStatus.OK);
    }
}
