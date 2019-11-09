/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IA;

import java.io.File;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author Jorge Iván Pérez Pérez
 */
public class Config {

    private Connection conexion;//
    private Statement st;//
    private ResultSet rs;//Variable para guardar el resultado de la consulta
    ArrayList generos = new ArrayList();//Arreglo para guardar los géneros de las canciones
    ArrayList energia = new ArrayList();//Arreglo para guardar la energía de las canciones
    ArrayList valencia = new ArrayList();//Arreglo para guardar la valencia de las canciones
    double ee1,ee2,ee3,vv1,vv2,vv3;//Variables para guardar la energía y valencia de las canciones y enviarla a la 2da ventana
    double maxe,mine, maxv,minv;//Variables para guardar la energía maxima minima y valencia maxima minima de las canciones y enviarla a la 2da ventana
    int gen;//Contar los géneros de la consulta
    
    /*
    *Método parahacer la conexión con la base de datos
    */
    public void ConectarBD() throws ClassNotFoundException, SQLException {
        
        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection("jdbc:sqlite:lib/Musica.db");
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error en la conexión de la base de datos");
        }

    }
    
    /*
    *Método para checar que las canciones se encuentren en la BD
    *@param song Nombre de la canción
    *@param artist Nombre de el artista
    */
    public void Checar(String song, String artist) {
        
      
        try {
            st = conexion.createStatement();//Variable para poder hacer una sentencia en SQL
            //Creamos la consulta SQL para la BD que busca la canción y el artista que se le pasa
            String query = "SELECT Artista,Cancion FROM Musica WHERE Cancion LIKE \"" + song + "%\" and Artista LIKE \"" + artist + "%\";";
            rs = st.executeQuery(query);//Aplicamos la consulta
            //Checamos los resultados 
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Canción encontrada");//Si se encuentra mandamos este mensaje

            } else {
                JOptionPane.showMessageDialog(null, "Verifica que tus datos esten bien escritos o intenta con otra canción");//Si no se encuentra mandamos este mensaje
            }

            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /*
    *Método para recopilar los géneros de las 3 canciones 
    *@param c1 Nombre de la canción 1
    *@param c2 Nombre de el canción 2 
    *@param c3 Nombre de la canción 3
    *@param a1 Nombre de el artista 1
    *@param a2 Nombre de la artista 2
    *@param a3 Nombre de el artista 3
    */
    public void RecopilaGeneros(String c1, String c2, String c3, String a1, String a2, String a3) throws SQLException {

        st = conexion.createStatement();//Variable para poder hacer una sentencia en SQL
        //Creamos la consulta SQL para la BD que busca el género de acuerdo a la canción y el artista que se le pasa
        String query1 = "SELECT DISTINCT Genero FROM Musica WHERE Cancion LIKE \"" + c1 + "%\" and Artista LIKE \"" + a1 + "%\";";
        rs = st.executeQuery(query1);//Aplicamos la consulta
        //Revisamos los resultados y los guardamos en el arreglo
        while (rs.next()) {

            String genero = rs.getString("genero");//Guardamos el resultado en el String genero

            generos.add(genero);//Guardamos el resultado en el arreglo
        }
        
        //Se repite el proceso para las demás canciones
        st = conexion.createStatement();
        String query2 = "SELECT DISTINCT Genero FROM Musica WHERE Cancion LIKE \"" + c2 + "%\" and Artista LIKE \"" + a2 + "%\";";
        rs = st.executeQuery(query2);
        while (rs.next()) {

            String genero = rs.getString("genero");

            generos.add(genero);
        }

        st = conexion.createStatement();
        String query3 = "SELECT DISTINCT Genero FROM Musica WHERE Cancion LIKE \"" + c3 + "%\" and Artista LIKE \"" + a3 + "%\";";
        rs = st.executeQuery(query3);
        while (rs.next()) {

            String genero = rs.getString("genero");

            generos.add(genero);
        }
    }
    
     /*
    *Método que quita los géneros repetidos del Arreglo que contiene a los géneros de las 3 canciones
    *@return ArrayList Arreglo con los géneros distintos
    */
    public ArrayList QuitarGenRep() {

        Set<String> hs = new HashSet<>();//Se crea una tabla hash para copiar el arreglo aqui ya que en las hash table no se aceptan valores repetidos
        hs.addAll(generos);//Añadimos los géneros a la tabla hash
        generos.clear();//Borramos los datos del Arreglo
        generos.addAll(hs);//Copiamos los datos de la hash table al arreglo esta vez sin géneros repetidos

        gen = generos.size();//Guardamos el tamaño del arreglo que nos da el total de géneros que hay en las 3 canciones
        return generos;

    }
    
    /*
    *Método para recopilar la energía de las 3 canciones 
    *@param c1 Nombre de la canción 1
    *@param c2 Nombre de el canción 2 
    *@param c3 Nombre de la canción 3
    *@param a1 Nombre de el artista 1
    *@param a2 Nombre de la artista 2
    *@param a3 Nombre de el artista 3
    */
    public void RecopilaEnergia(String c1, String c2, String c3, String a1, String a2, String a3) throws SQLException {

        ArrayList e1 = new ArrayList();//Se crea un arreglo auxiliar donde se guardará la enrgía de la canción
        st = conexion.createStatement();//Variable para poder hacer una sentencia en SQL
        //Creamos la consulta SQL para la BD que busca la energía de acuerdo a la canción y el artista que se le pasa
        String query1 = "SELECT DISTINCT Energia FROM Musica WHERE Cancion LIKE \"" + c1 + "%\" and Artista LIKE \"" + a1 + "%\";";
        rs = st.executeQuery(query1);//Aplicamos la consulta
        
        //Revisamos los resultados y los guardamos en el arreglo y en una variable auxiliar
        while (rs.next()) {

            double en = rs.getDouble("energia");//Guardamos los resultados en la var en

            e1.add(en);//Guardamos los resultados en el arreglo
        }
        
        //Dado que una canción puede tener más de una energía se comprueba cuantas tiene
        //Si se tiene 1 se guarda directamente en el arreglo de energias si no se manda el arreglo al método Maximo para sacar la energía máxima de esa canción 
        if (e1.size() == 1) {

           //Se guarda el elemento en el arreglo de energias
            ee1 = (double) e1.get(0);//Se guarda el elemento en la variable ee1 para mandar la información a la tabla de explicación(Info) 

        } else {
            for (int x = 0; x < e1.size(); x++) {
                energia.add(e1.get(x));
            }
            ee1 = Maximo(e1);
            
        }
        
        //Se repite el proceso para las demás canciones
        ArrayList e2 = new ArrayList();
        st = conexion.createStatement();
        String query2 = "SELECT DISTINCT Energia FROM Musica WHERE Cancion LIKE \"" + c2 + "%\" and Artista LIKE \"" + a2 + "%\";";
        rs = st.executeQuery(query2);
        while (rs.next()) {

            double en = rs.getDouble("energia");

            e2.add(en);
        }

        if (e2.size() == 1) {

            
            ee2 = (double) e2.get(0);

        } else {
            for (int x = 0; x < e2.size(); x++) {
                energia.add(e2.get(x));
            }
             ee2 = Maximo(e2);

        }

        ArrayList e3 = new ArrayList();
        st = conexion.createStatement();
        String query3 = "SELECT DISTINCT Energia FROM Musica WHERE Cancion LIKE \"" + c3 + "%\" and Artista LIKE \"" + a3 + "%\";";
        rs = st.executeQuery(query3);
        while (rs.next()) {

            double en = rs.getDouble("energia");

            e3.add(en);
        }

        if (e3.size() == 1) {

            
            ee3 = (double) e3.get(0);

        } else {
            for (int x = 0; x < e3.size(); x++) {
                energia.add(e3.get(x));
            }
             ee3 = Maximo(e3);
        }

    }
    
    /*
    *Método que saca el valor máximo de los arreglo de energía y valencia
    *@double Valor máximo de los arreglos
    *@param x ArrayList de energía o valencia
    */
    public double Maximo(ArrayList x) {

        double z;//Variable donde se guardara el valor máximo
        //Ordenamos el arreglo de tal manera que nos regrese el valor máximo
        Comparator<Integer> comparador = Collections.reverseOrder();
        Collections.sort(x, comparador);

        z = (double) x.get(0);//Sacar el primer valor del arreglo ya ordenado y Guardamos el valor máximo en z

        return z;

    }
    
    /*
    *Método que saca el valor mínimo de los arreglo de energía y valencia
    *@double Valor mínimo de los arreglos
    *@param x ArrayList de energía o valencia
    */
    public double Minimo(ArrayList y) {

        double m;//Variable donde se guardara el valor mínimo
        Collections.sort(y);//Se ordena el arreglo
        m = (double) y.get(0);//Sacar el primer valor del arreglo ya ordenado y Guardamos el valor máximo en z
        return m;
    }

    public void RecopilaValencia(String c1, String c2, String c3, String a1, String a2, String a3) throws SQLException {

        ArrayList v1 = new ArrayList();
        st = conexion.createStatement();
        String query1 = "SELECT DISTINCT Valencia FROM Musica WHERE Cancion LIKE \"" + c1 + "%\" and Artista LIKE \"" + a1 + "%\";";
        rs = st.executeQuery(query1);
        while (rs.next()) {

            double va = rs.getDouble("valencia");

            v1.add(va);
        }

        if (v1.size() == 1) {

            valencia.add(v1.get(0));
            vv1 = (double) v1.get(0);

        } else {
            for (int x = 0; x < v1.size(); x++) {
                valencia.add(v1.get(x));
            }
             vv1 = Maximo(v1);
        }

        ArrayList v2 = new ArrayList();
        st = conexion.createStatement();
        String query2 = "SELECT DISTINCT Valencia FROM Musica WHERE Cancion LIKE \"" + c2 + "%\" and Artista LIKE \"" + a2 + "%\";";
        rs = st.executeQuery(query2);
        while (rs.next()) {

            double va = rs.getDouble("valencia");

            v2.add(va);
        }

        if (v2.size() == 1) {

            valencia.add(v2.get(0));
            vv2 = (double) v2.get(0);

        } else {
            for (int x = 0; x < v2.size(); x++) {
                valencia.add(v2.get(x));
            }
            vv2 = Maximo(v2);
        }

            ArrayList v3 = new ArrayList();
            st = conexion.createStatement();
            String query3 = "SELECT DISTINCT Valencia FROM Musica WHERE Cancion LIKE \"" + c3 + "%\" and Artista LIKE \"" + a3 + "%\";";
            rs = st.executeQuery(query3);
            while (rs.next()) {

                double va = rs.getDouble("valencia");

                v3.add(va);
            }

            if (v3.size() == 1) {

                valencia.add(v3.get(0));
                vv3 = (double) v3.get(0);

            } else {
                for (int x = 0; x < v3.size(); x++) {
                    valencia.add(v3.get(x));
                }
                vv3 = Maximo(v3);
            }
    }

    public ResultSet Resultados() throws SQLException {
        int num = generos.size();
        
        maxe = Maximo(energia);
        mine = Minimo(energia);
        maxv = Maximo(valencia);
        minv = Minimo(valencia);

        switch (num) {
            case 1:
                String gr = (String) generos.get(0);
                st = conexion.createStatement();
                String query1 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + gr + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query1);
                break;
            case 2:
                String ger1 = (String) generos.get(0);
                String ger2 = (String) generos.get(1);
                st = conexion.createStatement();
                String query2 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + ger1 + "\" OR Genero LIKE \"" + ger2 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query2);
                break;
            case 3:
                String g1 = (String) generos.get(0);
                String g2 = (String) generos.get(1);
                String g3 = (String) generos.get(2);
                st = conexion.createStatement();
                String query3 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1 + "\" OR Genero LIKE \"" + g2 + 
                        "\" OR Genero LIKE \"" + g3 +"\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query3);
                break;
            case 4:
                String g1_1 = (String) generos.get(0);
                String g2_1 = (String) generos.get(1);
                String g3_1 = (String) generos.get(2);
                String g4_1 = (String) generos.get(3);
                st = conexion.createStatement();
                String query4 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_1 + "\" OR Genero LIKE \"" + g2_1 + 
                        "\" OR Genero LIKE \"" + g3_1 +"\" OR Genero LIKE \"" + g4_1 +"\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query4);
                break;
            case 5:
                String g1_2 = (String) generos.get(0);
                String g2_2 = (String) generos.get(1);
                String g3_2 = (String) generos.get(2);
                String g4_2 = (String) generos.get(3);
                String g5_2 = (String) generos.get(4);
                st = conexion.createStatement();
                String query5 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_2 + 
                        "\" OR Genero LIKE \"" + g2_2 + "\" OR Genero LIKE \"" + g3_2 + 
                        "\" OR Genero LIKE \"" + g4_2 + "\" OR Genero LIKE \"" + g5_2 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query5);
                break;
            case 6:
                String g1_3 = (String) generos.get(0);
                String g2_3 = (String) generos.get(1);
                String g3_3 = (String) generos.get(2);
                String g4_3 = (String) generos.get(3);
                String g5_3 = (String) generos.get(4);
                String g6_3 = (String) generos.get(5);
                st = conexion.createStatement();
                String query6 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_3 + 
                        "\" OR Genero LIKE \"" + g2_3 + "\" OR Genero LIKE \"" + g3_3 + 
                        "\" OR Genero LIKE \"" + g4_3 + "\" OR Genero LIKE \"" + g5_3 + 
                        "\" OR Genero LIKE \"" + g6_3 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query6);
                break;
            case 7:
                String g1_4 = (String) generos.get(0);
                String g2_4 = (String) generos.get(1);
                String g3_4 = (String) generos.get(2);
                String g4_4 = (String) generos.get(3);
                String g5_4 = (String) generos.get(4);
                String g6_4 = (String) generos.get(5);
                String g7_4 = (String) generos.get(6);
                st = conexion.createStatement();
                String query7 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_4 + 
                        "\" OR Genero LIKE \"" + g2_4 + "\" OR Genero LIKE \"" + g3_4 + 
                        "\" OR Genero LIKE \"" + g4_4 + "\" OR Genero LIKE \"" + g5_4 +
                        "\" OR Genero LIKE \"" + g6_4 + "\" OR Genero LIKE \"" + g7_4 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query7);
                break;
            case 8:
                String g1_5 = (String) generos.get(0);
                String g2_5 = (String) generos.get(1);
                String g3_5 = (String) generos.get(2);
                String g4_5 = (String) generos.get(3);
                String g5_5 = (String) generos.get(4);
                String g6_5 = (String) generos.get(5);
                String g7_5 = (String) generos.get(6);
                String g8_5 = (String) generos.get(7);
                st = conexion.createStatement();
                String query8 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_5 + 
                        "\" OR Genero LIKE \"" + g2_5 + "\" OR Genero LIKE \"" + g2_5 + 
                        "\" OR Genero LIKE \"" + g3_5 + "\" OR Genero LIKE \"" + g4_5 + 
                        "\" OR Genero LIKE \"" + g5_5 + "\" OR Genero LIKE \"" + g6_5 + 
                        "\" OR Genero LIKE \"" + g7_5 + "\" OR Genero LIKE \"" + g8_5 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query8);
                break;
            case 9:
                String g1_6 = (String) generos.get(0);
                String g2_6 = (String) generos.get(1);
                String g3_6 = (String) generos.get(2);
                String g4_6 = (String) generos.get(3);
                String g5_6 = (String) generos.get(4);
                String g6_6 = (String) generos.get(5);
                String g7_6 = (String) generos.get(6);
                String g8_6 = (String) generos.get(7);
                String g9_6 = (String) generos.get(8);
                st = conexion.createStatement();
                String query9 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_6 + 
                        "\" OR Genero LIKE \"" + g2_6 + "\" OR Genero LIKE \"" + g3_6 + 
                        "\" OR Genero LIKE \"" + g4_6 + "\" OR Genero LIKE \"" + g5_6 + 
                        "\" OR Genero LIKE \"" + g6_6 + "\" OR Genero LIKE \"" + g7_6 + 
                        "\" OR Genero LIKE \"" + g8_6 + "\" OR Genero LIKE \"" + g9_6 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query9);
                break;
            case 10:
                String g1_7 = (String) generos.get(0);
                String g2_7 = (String) generos.get(1);
                String g3_7 = (String) generos.get(2);
                String g4_7 = (String) generos.get(3);
                String g5_7 = (String) generos.get(4);
                String g6_7 = (String) generos.get(5);
                String g7_7 = (String) generos.get(6);
                String g8_7 = (String) generos.get(7);
                String g9_7 = (String) generos.get(8);
                String g10_7 = (String) generos.get(9);
                st = conexion.createStatement();
                String query10 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_7 + 
                        "\" OR Genero LIKE \"" + g2_7 + "\" OR Genero LIKE \"" + g3_7 + 
                        "\" OR Genero LIKE \"" + g4_7 + "\" OR Genero LIKE \"" + g5_7 + 
                        "\" OR Genero LIKE \"" + g6_7 + "\" OR Genero LIKE \"" + g7_7 + 
                        "\" OR Genero LIKE \"" + g8_7 + "\" OR Genero LIKE \"" + g9_7 + 
                        "\" OR Genero LIKE \"" + g10_7 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query10);
                break;
            case 11:
                String g1_8 = (String) generos.get(0);
                String g2_8 = (String) generos.get(1);
                String g3_8 = (String) generos.get(2);
                String g4_8 = (String) generos.get(3);
                String g5_8 = (String) generos.get(4);
                String g6_8 = (String) generos.get(5);
                String g7_8 = (String) generos.get(6);
                String g8_8 = (String) generos.get(7);
                String g9_8 = (String) generos.get(8);
                String g10_8 = (String) generos.get(9);
                String g11_8 = (String) generos.get(10);
                st = conexion.createStatement();
                String query11 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_8 + 
                        "\" OR Genero LIKE \"" + g2_8 + "\" OR Genero LIKE \"" + g3_8 + 
                        "\" OR Genero LIKE \"" + g4_8 + "\" OR Genero LIKE \"" + g5_8 + 
                        "\" OR Genero LIKE \"" + g6_8 + "\" OR Genero LIKE \"" + g7_8 + 
                        "\" OR Genero LIKE \"" + g8_8 + "\" OR Genero LIKE \"" + g9_8 + 
                        "\" OR Genero LIKE \"" + g10_8 + "\" OR Genero LIKE \"" + g11_8 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query11);
                break;
            case 12:
                String g1_9 = (String) generos.get(0);
                String g2_9 = (String) generos.get(1);
                String g3_9 = (String) generos.get(2);
                String g4_9 = (String) generos.get(3);
                String g5_9 = (String) generos.get(4);
                String g6_9 = (String) generos.get(5);
                String g7_9 = (String) generos.get(6);
                String g8_9 = (String) generos.get(7);
                String g9_9 = (String) generos.get(8);
                String g10_9 = (String) generos.get(9);
                String g11_9 = (String) generos.get(10);
                String g12_9 = (String) generos.get(11);
                st = conexion.createStatement();
                String query12 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_9 +
                        "\" OR Genero LIKE \"" + g2_9 + "\" OR Genero LIKE \"" + g3_9 + 
                        "\" OR Genero LIKE \"" + g4_9 + "\" OR Genero LIKE \"" + g5_9 + 
                        "\" OR Genero LIKE \"" + g6_9 + "\" OR Genero LIKE \"" + g7_9 + 
                        "\" OR Genero LIKE \"" + g8_9 + "\" OR Genero LIKE \"" + g9_9 + 
                        "\" OR Genero LIKE \"" + g10_9 + "\" OR Genero LIKE \"" + g11_9 + 
                        "\" OR Genero LIKE \"" + g12_9 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query12);
                break;
            case 13:
                String g1_10 = (String) generos.get(0);
                String g2_10 = (String) generos.get(1);
                String g3_10 = (String) generos.get(2);
                String g4_10 = (String) generos.get(3);
                String g5_10 = (String) generos.get(4);
                String g6_10 = (String) generos.get(5);
                String g7_10 = (String) generos.get(6);
                String g8_10 = (String) generos.get(7);
                String g9_10 = (String) generos.get(8);
                String g10_10 = (String) generos.get(9);
                String g11_10 = (String) generos.get(10);
                String g12_10 = (String) generos.get(11);
                String g13_10 = (String) generos.get(12);
                st = conexion.createStatement();
                String query13 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_10 + 
                        "\" OR Genero LIKE \"" + g2_10 + "\" OR Genero LIKE \"" + g3_10 + 
                        "\" OR Genero LIKE \"" + g4_10 + "\" OR Genero LIKE \"" + g5_10 + 
                        "\" OR Genero LIKE \"" + g6_10 + "\" OR Genero LIKE \"" + g7_10 + 
                        "\" OR Genero LIKE \"" + g8_10 + "\" OR Genero LIKE \"" + g9_10 + 
                        "\" OR Genero LIKE \"" + g10_10 + "\" OR Genero LIKE \"" + g11_10 + 
                        "\" OR Genero LIKE \"" + g12_10 + "\" OR Genero LIKE \"" + g13_10 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query13);
                break;
            case 14:
                String g1_11 = (String) generos.get(0);
                String g2_11 = (String) generos.get(1);
                String g3_11 = (String) generos.get(2);
                String g4_11 = (String) generos.get(3);
                String g5_11 = (String) generos.get(4);
                String g6_11 = (String) generos.get(5);
                String g7_11 = (String) generos.get(6);
                String g8_11 = (String) generos.get(7);
                String g9_11 = (String) generos.get(8);
                String g10_11 = (String) generos.get(9);
                String g11_11 = (String) generos.get(10);
                String g12_11 = (String) generos.get(11);
                String g13_11 = (String) generos.get(12);
                String g14_11 = (String) generos.get(13);
                st = conexion.createStatement();
                String query14 = "SELECT DISTINCT genero,artista,cancion FROM Musica WHERE Genero LIKE \"" + g1_11 + 
                        "\" OR Genero LIKE \"" + g2_11 + "\" OR Genero LIKE \"" + g3_11 + 
                        "\" OR Genero LIKE \"" + g4_11 + "\" OR Genero LIKE \"" + g5_11 + 
                        "\" OR Genero LIKE \"" + g6_11 + "\" OR Genero LIKE \"" + g7_11 + 
                        "\" OR Genero LIKE \"" + g8_11 + "\" OR Genero LIKE \"" + g9_11 + 
                        "\" OR Genero LIKE \"" + g10_11 + "\" OR Genero LIKE \"" + g11_11 + 
                        "\" OR Genero LIKE \"" + g12_11 + "\" OR Genero LIKE \"" + g13_11 + 
                        "\" OR Genero LIKE \"" + g14_11 + "\" AND energia between " + mine + " and " + maxe + " AND valencia between " + minv + " and " + maxv + ";";
                rs = st.executeQuery(query14);
                break;
            default:

        }
        return rs;

    }
    
}
