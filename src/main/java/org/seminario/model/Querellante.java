package org.seminario.model;

public class Querellante extends Participante{
    Querellante(int idPart, int idTipoPart, String nom, String ape, String dom, String email, String telef, String dni) {
        super(idPart, idTipoPart, nom, ape, dom, email, telef, dni);
    }

    @Override
    public String getTipoParticipante() {
        return "Querellante";
    }
}
