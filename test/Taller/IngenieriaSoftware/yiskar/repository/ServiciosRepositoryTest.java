package Taller.IngenieriaSoftware.yiskar.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiciosRepositoryTest {

    @Test
    void agregarServicio()
    {
        ServiciosRepository serviciosRepository = ServiciosRepository.getInstancia();
        assertAll(
                () -> assertFalse(serviciosRepository.agregarServicio("Lavado de Alfombras",5000)),
                () -> assertTrue(serviciosRepository.agregarServicio("Test",5000))
        );
        serviciosRepository.eliminarServicio("Test");
    }

    @Test
    void eliminarServicio()
    {
        ServiciosRepository serviciosRepository = ServiciosRepository.getInstancia();
        serviciosRepository.agregarServicio("Test",5000);
        assertAll(
                () -> assertFalse(serviciosRepository.eliminarServicio("prueba")),
                () -> assertTrue(serviciosRepository.eliminarServicio("Test"))
        );
    }

    @Test
    void editarNombre()
    {
        ServiciosRepository serviciosRepository = ServiciosRepository.getInstancia();
        serviciosRepository.agregarServicio("Test",5000);
        assertAll(
                () -> assertFalse(serviciosRepository.editarNombre("Test","Test")),
                () -> assertTrue(serviciosRepository.editarNombre("Test","Test1"))
        );
        serviciosRepository.eliminarServicio("Test1");
    }

    @Test
    void editarPrecio()
    {
        ServiciosRepository serviciosRepository = ServiciosRepository.getInstancia();
        serviciosRepository.agregarServicio("Test",5000);
        assertAll(
                () -> assertFalse(serviciosRepository.editarPrecio("test1",4000)),
                () -> assertTrue(serviciosRepository.editarPrecio("Test",3500))
        );
        serviciosRepository.eliminarServicio("Test");
    }
}