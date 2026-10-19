    package org.seminario.model;


    public class TipoSala {

        private int idTipoSala;

        private String codigo;

        private String descripcion;

        private boolean activa;

        public TipoSala(
                int idTipoSala,
                String codigo,
                String descripcion,
                boolean activa) {

            this.idTipoSala = idTipoSala;
            this.codigo = codigo;
            this.descripcion = descripcion;
            this.activa = activa;
        }

        public int getIdTipoSala() {
            return idTipoSala;
        }

        public void setIdTipoSala(int idTipoSala) {
            this.idTipoSala = idTipoSala;
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

        public boolean isActiva() {
            return activa;
        }

        public void setActiva(boolean activa) {
            this.activa = activa;
        }

        @Override
        public String toString() {
            return descripcion;
        }
    }
