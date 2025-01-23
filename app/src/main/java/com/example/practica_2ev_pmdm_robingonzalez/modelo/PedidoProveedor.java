package com.example.practica_2ev_pmdm_robingonzalez.modelo;

import java.util.List;

public class PedidoProveedor {
    private Long fechaPedido;
    private List<Pieza> piezasSolicitadas;


    public PedidoProveedor(){}

    public PedidoProveedor(Long fechaPedido, List<Pieza> piezasSolicitadas) {
        this.fechaPedido = fechaPedido;
        this.piezasSolicitadas = piezasSolicitadas;
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
    }


}
