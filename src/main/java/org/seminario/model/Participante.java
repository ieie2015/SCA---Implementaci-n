package org.seminario.model;

public  abstract class Participante {
    private int idParticipante;
    private int idTipoParticipante;
    private String nombre;
    private String apellido;
    private String domicilio;
    private String email;
    private String telefono;
    private String dni;

    Participante(int idPart,int idTipoPart,String nom,String ape,String dom,String email,String telef,String dni){
        this.idParticipante=idPart;
        this.idTipoParticipante=idTipoPart;
        this.nombre=nom;
        this.apellido=ape;
        this.domicilio=dom;
        this.email=email;
        this.telefono=telef;
        this.dni=dni;

    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public abstract String getTipoParticipante();
    public int getIdParticipante() {
        return idParticipante;
    }

    public int getIdTipoParticipante() {
        return idTipoParticipante;
    }
}
