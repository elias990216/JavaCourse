package com.minsait.mockito.repositories;

import java.util.List;

public interface PreguntaRepository {
    List<String> findPreguntaByExamenId(Long id);
    void savePreguntas(List<String> preguntas);
}
