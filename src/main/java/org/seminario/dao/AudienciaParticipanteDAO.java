package org.seminario.dao;

import org.seminario.model.Participante;
import org.seminario.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AudienciaParticipanteDAO {

    public boolean guardarTodos(
            long idAudiencia,
            List<Participante> participantes) throws SQLException {
        String sql = """
                INSERT INTO AUDIENCIA_PARTICIPANTE
                    (idAUDIENCIA, idPARTICIPANTE, ROL_EN_AUDIENCIA)
                VALUES (?, ?, ?)
                """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (Participante participante : participantes) {
                ps.setLong(1, idAudiencia);
                ps.setInt(2, participante.getIdParticipante());
                ps.setString(3, participante.getTipoParticipante());
                if (ps.executeUpdate() != 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
