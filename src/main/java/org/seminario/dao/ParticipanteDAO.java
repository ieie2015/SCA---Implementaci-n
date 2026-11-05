package org.seminario.dao;

import org.seminario.model.Defensor;
import org.seminario.model.Fiscal;
import org.seminario.model.Imputado;
import org.seminario.model.Juez;
import org.seminario.model.Participante;
import org.seminario.model.Querellante;
import org.seminario.model.Victima;
import org.seminario.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ParticipanteDAO {

    public List<Participante> listarTodos() throws SQLException {
        String sql = """
                SELECT p.idPARTICIPANTE, p.idTIPO_PARTICIPANTE,
                       p.NOMBRE, p.APELLIDO, p.DOMICILIO,
                       p.TELEFONO, p.EMAIL, p.DNI,
                       tp.CODIGO AS TIPO_CODIGO
                  FROM PARTICIPANTE p
                  JOIN TIPO_PARTICIPANTE tp
                    ON tp.idTIPO_PARTICIPANTE = p.idTIPO_PARTICIPANTE
                 ORDER BY p.APELLIDO, p.NOMBRE
                """;
        List<Participante> participantes = new ArrayList<>();

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                participantes.add(mapear(rs));
            }
        }
        return participantes;
    }

    public List<Participante> listarDisponiblesPorFecha(
            LocalDate fecha) throws SQLException {
        String sql = """
                SELECT DISTINCT p.idPARTICIPANTE, p.idTIPO_PARTICIPANTE,
                       p.NOMBRE, p.APELLIDO, p.DOMICILIO,
                       p.TELEFONO, p.EMAIL, p.DNI,
                       tp.CODIGO AS TIPO_CODIGO
                  FROM PARTICIPANTE p
                  JOIN TIPO_PARTICIPANTE tp
                    ON tp.idTIPO_PARTICIPANTE = p.idTIPO_PARTICIPANTE
                  JOIN DISPONIBILIDAD_PARTICIPANTE dp
                    ON dp.PARTICIPANTE_idPARTICIPANTE = p.idPARTICIPANTE
                 WHERE dp.FECHA = ?
                   AND dp.ESTADO = 'DISPONIBLE'
                 ORDER BY p.APELLIDO, p.NOMBRE
                """;
        List<Participante> participantes = new ArrayList<>();

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    participantes.add(mapear(rs));
                }
            }
        }
        return participantes;
    }

    private Participante mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("idPARTICIPANTE");
        int tipo = rs.getInt("idTIPO_PARTICIPANTE");
        String nombre = rs.getString("NOMBRE");
        String apellido = rs.getString("APELLIDO");
        String domicilio = rs.getString("DOMICILIO");
        String telefono = rs.getString("TELEFONO");
        String email = rs.getString("EMAIL");
        String dni = rs.getString("DNI");

        return switch (rs.getString("TIPO_CODIGO").toUpperCase()) {
            case "JUEZ" -> new Juez(id, tipo, nombre, apellido, domicilio, email, telefono, dni);
            case "FISCAL" -> new Fiscal(id, tipo, nombre, apellido, domicilio, email, telefono, dni);
            case "DEFENSOR" -> new Defensor(id, tipo, nombre, apellido, domicilio, email, telefono, dni);
            case "IMPUTADO" -> new Imputado(id, tipo, nombre, apellido, domicilio, email, telefono, dni);
            case "VICTIMA" -> new Victima(id, tipo, nombre, apellido, domicilio, email, telefono, dni);
            default -> new Querellante(id, tipo, nombre, apellido, domicilio, email, telefono, dni);
        };
    }
}
