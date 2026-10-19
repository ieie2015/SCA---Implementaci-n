package org.seminario.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/mydb";

    private static final String USUARIO =
            "root";

    private static final String PASSWORD =
            "";

    public static Connection conectar()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USUARIO,
                PASSWORD
        );
    }

    /** se define el constructor por defecto como privado para evitar instanciar la clase con new
     * solo se necesita establecer la conexión y dado el caso devolver una Excepción de SQL si hubo error en la misma
     *
     *
     */
    private ConexionBD() {}

}