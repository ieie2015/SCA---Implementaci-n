package org.seminario.model;

public class Imputado extends Participante{

    Imputado(int idPart, int idTipoPart, String nom, String ape, String dom, String email, String telef, String dni) {
        super(idPart, idTipoPart, nom, ape, dom, email, telef, dni);
    }

    @Override
    public String getTipoParticipante() {
        return "Imputado";
    }
}
