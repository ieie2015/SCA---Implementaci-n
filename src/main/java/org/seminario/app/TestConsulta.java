package org.seminario.app;

import org.seminario.util.ConexionBD;


import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class TestConsulta {

    public static void main(String[] args) {

        try {

            Connection con =
                    ConexionBD.conectar();

            Statement st =
                    con.createStatement();

            ResultSet rs =
                    st.executeQuery(
                            "SELECT COUNT(*) FROM SOLICITUD_AGENDAMIENTO"
                    );

            if(rs.next()) {

                System.out.println(
                        rs.getInt(1)
                );
            }

            con.close();

        } catch(Exception e) {

            e.printStackTrace();

        }
    }
}