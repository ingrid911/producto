package com.example.products.controller;

import com.example.products.entity.Producto;
import com.example.products.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping(value = "list")
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @PostMapping(value = "create")
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto prod = productoRepository.findById(id).orElse(null);
        if (prod == null) {
            return ResponseEntity.notFound().build();
        }
        prod.setNombre(producto.getNombre());
        prod.setPrecio(producto.getPrecio());
        return ResponseEntity.ok(productoRepository.save(prod));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        try {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
