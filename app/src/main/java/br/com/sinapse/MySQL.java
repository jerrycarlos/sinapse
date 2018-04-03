package br.com.sinapse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import android.app.Activity;
import android.util.Log;

/**
 * Created by Jerry Jr on 24/03/2018.
 */


public class MySQL {
    private Connection conn = null;
    private Statement st;
    private ResultSet rs;
    private String sql;

    public void conectar(String host, String porta, String banco, String usuario, String senha){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(Exception erro){
            Log.e("MYSQL","Erro: "+erro);
        }
        try{
            conn=DriverManager.getConnection("jdbc:mysql://"+host+":"+porta+"/"+banco,usuario,senha);
            Log.i("MYSQL","Conectado.");
        }catch(Exception erro){
            Log.e("MYSQL","Erro: "+erro);
        }
    }

    public void desconectar(){
        try {
            conn.close();
            Log.i("MYSQL","Desconectado.");
        } catch (Exception erro) {
            Log.e("MYSQL","Erro: "+erro);
        }
    }

    public void queryMySQL(String query){
        try{
            st=conn.createStatement();
            sql=query;
            rs=st.executeQuery(sql);
            rs.first();
            Log.i("MYSQL","Resultado: "+rs.getString("nome"));
        } catch (Exception erro){
            Log.e("MYSQL","Erro: "+erro);
        }
    }



}
