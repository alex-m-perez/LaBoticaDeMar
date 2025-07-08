package unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.laboticademar.webstore.entities.Destacado;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.repositories.DestacadoDAO;
import es.laboticademar.webstore.services.impl.DestacadoServiceImpl;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class DestacadoServiceImplTest {

    @Mock
    private DestacadoDAO destacadoDAO;

    @InjectMocks
    private DestacadoServiceImpl destacadoService;

    private Destacado dest1;
    private Destacado dest2;
    private Producto prod1;
    private Producto prod2;

    @BeforeEach
    void setUp() {
        prod1 = new Producto();
        prod1.setId(null);
        prod1.setNombre("P1");
        prod2 = new Producto();
        prod2.setId(null);
        prod2.setNombre("P2");

        dest1 = new Destacado();
        dest1.setId(1L);           // <-- usar literal long
        dest1.setProducto(prod1);

        dest2 = new Destacado();
        dest2.setId(2L);           // <-- usar literal long
        dest2.setProducto(prod2);
    }


    @Test
    void saveOrUpdateDestacado_delegaEnDaoYDevuelveEntidad() {
        when(destacadoDAO.save(dest1)).thenReturn(dest1);

        Destacado saved = destacadoService.saveOrUpdateDestacado(dest1);

        assertThat(saved).isSameAs(dest1);
        verify(destacadoDAO).save(dest1);
    }

    @Test
    void getDestacadoById_existente_devuelveElDestacado() {
        when(destacadoDAO.findById(1)).thenReturn(Optional.of(dest1));

        Destacado found = destacadoService.getDestacadoById(1);

        assertThat(found).isSameAs(dest1);
        verify(destacadoDAO).findById(1);
    }

    @Test
    void getDestacadoById_inexistente_lanzaEntityNotFoundException() {
        when(destacadoDAO.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> destacadoService.getDestacadoById(99))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Producto no encontrado con ID: 99");

        verify(destacadoDAO).findById(99);
    }

    @Test
    void getAllDestacados_extraeSoloProductos() {
        when(destacadoDAO.findAll()).thenReturn(List.of(dest1, dest2));

        List<Producto> productos = destacadoService.getAllDestacados();

        assertThat(productos)
            .hasSize(2)
            .allSatisfy(p -> assertThat(p).isInstanceOf(Producto.class))
            .extracting(Producto::getNombre)
            .containsExactlyInAnyOrder("P1", "P2");

        verify(destacadoDAO).findAll();
    }

    @Test
    void deleteDestacadoById_invocaAlDao() {
        // deleteById no devuelve nada, sólo comprobamos la llamada
        destacadoService.deleteDestacadoById(5);
        verify(destacadoDAO).deleteById(5);
    }
}
