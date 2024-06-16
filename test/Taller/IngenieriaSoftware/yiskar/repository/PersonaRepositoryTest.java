package Taller.IngenieriaSoftware.yiskar.repository;

import Taller.IngenieriaSoftware.yiskar.entities.Cliente;
import Taller.IngenieriaSoftware.yiskar.entities.Persona;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonaRepositoryTest {

    @Test
    void loguearPersona()
    {
        PersonaRepository personaRepository = PersonaRepository.getInstance();
        assertAll(
                () -> assertEquals("Jefe", PersonaRepository.loguearPersona("francisco.llanos@gmail.com","lalala123")),
                () -> assertEquals("Cliente", PersonaRepository.loguearPersona("a","a")),
                () -> assertEquals(null, PersonaRepository.loguearPersona(null,null))
        );
    }

}