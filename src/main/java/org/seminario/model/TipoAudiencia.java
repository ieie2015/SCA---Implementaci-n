package org.seminario.model;

public class TipoAudiencia {

    private long idTipoAudiencia;

    private TipoSala tipoSala;

    private String codigo;

    private String descripcion;

    private int duracionReferencia;

    private boolean activa;

    public TipoAudiencia(
            long idTipoAudiencia,
            TipoSala tipoSala,
            String codigo,
            String descripcion,
            int duracionReferencia,
            boolean activa) {

        this.idTipoAudiencia = idTipoAudiencia;
        this.tipoSala = tipoSala;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.duracionReferencia = duracionReferencia;
        this.activa = activa;
    }

    public long getIdTipoAudiencia() {
        return idTipoAudiencia;
    }

    public void setIdTipoAudiencia(long idTipoAudiencia) {
        this.idTipoAudiencia = idTipoAudiencia;
    }

    public TipoSala getTipoSala() {
        return tipoSala;
    }

    public void setTipoSala(TipoSala tipoSala) {
        this.tipoSala = tipoSala;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracionReferencia() {
        return duracionReferencia;
    }
}


