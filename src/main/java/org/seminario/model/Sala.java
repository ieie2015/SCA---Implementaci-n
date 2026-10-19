package org.seminario.model;


public class Sala {

    private int idSala;

    private String codigo;

    private String nombre;

    private String ubicacion;

    private int capacidad;

    private boolean activa;

    private TipoSala TipoSala;

    public Sala(
            int idSala,
            String codigo,
            String nombre,
            String ubicacion,
            int capacidad,
            boolean activa,
            TipoSala TipoSala) {

        this.idSala = idSala;
        this.codigo = codigo;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
        this.activa = activa;
        this.TipoSala = TipoSala;

    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }


    public TipoSala getTipoSala() {
        return TipoSala;
    }

    public void setTipoSala(TipoSala tipoSala) {
        TipoSala = tipoSala;
    }
}