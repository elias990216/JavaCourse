package com.minsait.mockito.services;

import com.minsait.mockito.models.Examen;
import com.minsait.mockito.repositories.ExamenRepository;
import com.minsait.mockito.repositories.PreguntaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
    @Mock
    ExamenRepository examenRepository;
    @Mock
    PreguntaRepository preguntaRepository;
    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    @Test
    void testArgumentCaptor(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntaByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Historia");
        verify(preguntaRepository).findPreguntaByExamenId(captor.capture());
        assertEquals(3L,captor.getValue());
    }

    @Test
    void testFindExamenPorNombre() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertTrue(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntaByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertTrue(examen.getPreguntas().contains("Aritmetica"));
        verify(examenRepository, times(1)).findAll();
        verify(preguntaRepository).findPreguntaByExamenId(anyLong());
    }

    @Test
    void testPreguntasExamenNull() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        Examen examen = service.findExamenPorNombreConPreguntas("Fisica");
        assertNull(examen);
    }

    @Test
    void testExamenSinPreguntas() {
        when(examenRepository.save(Datos.EXAMEN)).thenReturn(Datos.EXAMEN);
        Examen examen = Datos.EXAMEN;
        assertEquals(service.save(examen).getId(), examen.getId());
    }

    @Test
    void testException() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntaByExamenId(anyLong())).thenThrow(IllegalArgumentException.class);
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });
        assertEquals(IllegalArgumentException.class, e.getClass());
    }

    @Test
    void testDoThrow() {
        doThrow(IllegalArgumentException.class).when(preguntaRepository).savePreguntas(anyList());
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.save(examen)
        );
    }

    @Test
    void testDoAnswer() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntaByExamenId(1L)).thenReturn(Datos.PREGUNTAS);
//        when(preguntaRepository.findPreguntaByExamenId(2L)).thenReturn(Collections.EMPTY_LIST);
        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return id == 1 ? Datos.PREGUNTAS : Collections.EMPTY_LIST;
        }).when(preguntaRepository).findPreguntaByExamenId(anyLong());
        Examen examen1 = service.findExamenPorNombreConPreguntas("Matematicas");
        Examen examen2 = service.findExamenPorNombreConPreguntas("Español");
//        Examen examen1 = service.findExamenPorNombreConPreguntas("Matematicas");
//        assertEquals(3,examen1.getPreguntas().size());
//        Examen examen2 = service.findExamenPorNombreConPreguntas("Español");
//        assertTrue(examen2.getPreguntas().isEmpty());
        assertAll(
                () -> assertEquals(3,examen1.getPreguntas().size()),
                () -> assertTrue(examen2.getPreguntas().isEmpty())
        );
    }

}