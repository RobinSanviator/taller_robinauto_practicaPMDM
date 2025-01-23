package com.example.practica_2ev_pmdm_robingonzalez.modelo;

import java.util.List;

public class PedidoProveedor {
    private Long fechaPedido;
    private List<Pieza> piezasSolicitadas;
    private double precioTotal;


    public PedidoProveedor(){}


    public PedidoProveedor(Long fechaPedido, List<Pieza> piezasSolicitadas) {
        this.fechaPedido = fechaPedido;
        this.piezasSolicitadas = piezasSolicitadas;
        calcularPrecioTotal();
    }

    public Long getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Long fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public List<Pieza> getPiezasSolicitadas() {
        return piezasSolicitadas;
    }

    public void setPiezasSolicitadas(List<Pieza> piezasSolicitadas) {
        this.piezasSolicitadas = piezasSolicitadas;
        calcularPrecioTotal(); // Recalcular el precio total si se actualizan las piezas
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    private void calcularPrecioTotal() {
        precioTotal = 0.0;
        if (piezasSolicitadas != null) {
            for (Pieza pieza : piezasSolicitadas) {
                precioTotal += pieza.getCantidad() * pieza.getPrecio();
            }
        }
    }
}