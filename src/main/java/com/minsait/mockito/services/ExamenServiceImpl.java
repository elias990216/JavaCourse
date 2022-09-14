package com.minsait.mockito.services;

import com.minsait.mockito.models.Examen;
import com.minsait.mockito.repositories.ExamenRepository;
import com.minsait.mockito.repositories.PreguntaRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{

    ExamenRepository examenRepository;
    PreguntaRepository preguntaRepository;

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return examenRepository.findAll().stream()
                .filter(examen -> examen.getNombre().contains(nombre))
                .findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examen = findExamenPorNombre(nombre);
        if(examen.isPresent()){
            examen.get().setPreguntas(
                    preguntaRepository.findPreguntaByExamenId(examen.get().getId())
            );
            return examen.get();
        }
        return null;
    }

    @Override
    public Examen save(Examen examen) {
        return null;
    }
}
