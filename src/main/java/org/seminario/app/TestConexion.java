package org.seminario.app;

import org.seminario.util.ConexionBD;

import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {

        try {

            Connection con =
                    ConexionBD.conectar();

            System.out.println("Conexión exitosa a BD (mydb)");

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}