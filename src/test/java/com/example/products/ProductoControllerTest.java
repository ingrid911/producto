package com.example.products;

import com.example.products.controller.ProductoController;
import com.example.products.entity.Producto;
import com.example.products.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ProductoControllerTest {

	private MockMvc mockMvc;

	@Mock
	private ProductoRepository productoRepository;

	@InjectMocks
	private ProductoController productoController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(productoController).build();
	}

	@Test
	public void testListarProductos() throws Exception {
		// Caso exitoso: Lista de productos
		given(productoRepository.findAll()).willReturn(Arrays.asList(
				new Producto(1L, "Producto Test 1", 100.0),
				new Producto(2L, "Producto Test 2", 200.0)
		));

		mockMvc.perform(get("/api/productos/list")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nombre").value("Producto Test 1"))
				.andExpect(jsonPath("$[1].nombre").value("Producto Test 2"));
	}

	@Test
	public void testCrearProductoExito() throws Exception {
		Producto nuevoProducto = new Producto(null, "Nuevo Producto", 50.0);
		Producto productoGuardado = new Producto(1L, "Nuevo Producto", 50.0);

		given(productoRepository.save(any(Producto.class))).willReturn(productoGuardado);

		mockMvc.perform(post("/api/productos/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(nuevoProducto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.nombre").value("Nuevo Producto"));
	}

	@Test
	public void testActualizarProductoExito() throws Exception {
		// Caso exitoso: Actualización de producto existente
		Producto productoExistente = new Producto(1L, "Producto Original", 60.0);
		Producto productoActualizado = new Producto(1L, "Producto Actualizado", 70.0);

		given(productoRepository.findById(anyLong())).willReturn(Optional.of(productoExistente));
		given(productoRepository.save(any(Producto.class))).willReturn(productoActualizado);

		mockMvc.perform(put("/api/productos/update/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(productoActualizado)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Producto Actualizado"))
				.andExpect(jsonPath("$.precio").value(70.0));
	}

	@Test
	public void testActualizarProductoNoEncontrado() throws Exception {
		// Caso fallido: Producto no encontrado
		Producto productoActualizado = new Producto(1L, "Producto Actualizado", 70.0);

		given(productoRepository.findById(anyLong())).willReturn(Optional.empty());

		mockMvc.perform(put("/api/productos/update/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(productoActualizado)))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testEliminarProductoExito() throws Exception {
		// Caso exitoso: Eliminar producto existente
		doNothing().when(productoRepository).deleteById(anyLong());

		mockMvc.perform(delete("/api/productos/delete/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testEliminarProductoNoEncontrado() throws Exception {
		// Caso fallido: Intentar eliminar un producto inexistente
		doThrow(new IllegalArgumentException("Producto no encontrado")).when(productoRepository).deleteById(anyLong());

		mockMvc.perform(delete("/api/productos/delete/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	// Método auxiliar para convertir objetos a JSON String
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
