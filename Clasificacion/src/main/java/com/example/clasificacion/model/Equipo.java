package com.example.clasificacion.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Equipo {
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty escudo = new SimpleStringProperty();
    private final StringProperty puntos = new SimpleStringProperty();
    private final StringProperty forma = new SimpleStringProperty();

    public Equipo(String nombre, String escudo, String puntos, String forma) {
        this.nombre.set(nombre);
        this.escudo.set(escudo);
        this.puntos.set(puntos);
        this.forma.set(forma);
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getEscudo() {
        return escudo.get();
    }

    public StringProperty escudoProperty() {
        return escudo;
    }

    public void setEscudo(String escudo) {
        this.escudo.set(escudo);
    }

    public String getPuntos() {
        return puntos.get();
    }

    public StringProperty puntosProperty() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos.set(puntos);
    }

    public String getForma() {
        return forma.get();
    }

    public StringProperty formaProperty() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma.set(forma);
    }

    @Override
    public String toString() {
        return nombre.get();
    }
}
