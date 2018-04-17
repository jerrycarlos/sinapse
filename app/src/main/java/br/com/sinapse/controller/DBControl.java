package br.com.sinapse.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.sinapse.DBHelper.DBComands;
import br.com.sinapse.DBHelper.DatabaseHelper;
import br.com.sinapse.model.Evento;
import br.com.sinapse.model.Instituicao;
import br.com.sinapse.model.User;
import br.com.sinapse.view.CadastroActivity;
import br.com.sinapse.view.FeedActivity;
import br.com.sinapse.view.MainActivity;

/**
 * Created by Jerry Jr on 26/03/2018.
 */

public class DBControl {

    private SQLiteDatabase db;
    private DatabaseHelper banco;

    public DBControl(Context context){
        banco = new DatabaseHelper(context);
        this.db = banco.getWritableDatabase();
        banco.onUpgrade(db, DBComands.DATABASE_VERSION,2);
    }
    /**
     * This method is to create user record
     *
     * @return dados do usuario encontrado
     * @param user
     */
    public String addUser(User user) {
        return UserControl.addUser(user, this.banco);
    }

    public String addUser(Instituicao inst) {
        return InstituicaoControl.addInstituicao(inst, this.banco);
    }

    public String addUser(Evento event) {
        return EventoControl.addEvento(event, this.banco);
    }

    /*
        COLUMN_USER_ID,        COLUMN_USER_NAME,        COLUMN_USER_EMAIL,        COLUMN_USER_PASSWORD,        COLUMN_USER_LOGIN,        COLUMN_USER_TEL,       COLUMN_USER_OCUP,        COLUMN_USER_INSTITUICAO,        COLUMN_USER_CURSO,        COLUMN_USER_PERIODO
    */

    /**
     * Autentica usuario com email e senha
     * @param email
     * @param senha
     * @param context
     * @return todos os dados so usuario autenticado
     */
    public User buscarUser(String email, String senha, Context context){
        return UserControl.buscarUser(email,senha,context,this.banco);
    }

    /**
     * Busca usuario pelo id
     * @param id do usuario a ser procurado
     * @return todos os dados do usuario encontrado
     */
    public User buscarUser(int id){
        return UserControl.buscarUser(id,banco);
    }

    /**
     * Todas as instituiçoes cadastradas
     * @return lista de instituicao cadastradas no banco
     */
    public ArrayList<String> buscaInstituicao(){
        return InstituicaoControl.buscaInstituicao(this.banco);
    }

    /**
     * Busca instituicao pelo id e retorna todos os dados desta instituicao
     * @param id da instituicao a ser cadastrada
     * @return todos os dados da instituicao encontrada
     */
    public Instituicao buscaInstituicao(int id){
        return InstituicaoControl.buscaInstituicao(id,this.banco);
    }

    /**
     * Todos os eventos cadastrados
     * @return lista de eventos cadastrados no banco
     */
    public ArrayList<Evento> buscaListEvento(){
        return EventoControl.buscaListEvento(this.banco);
    }


    /**
     * Busca id da instituicao pelo o nome
     * @param nome da instituicao procurada
     * @return id da instituicao procurada
     */
    public int buscaIdInstituicao(String nome){
        return InstituicaoControl.buscaIdInstituicao(nome, this.banco);
    }

    /**
     * Inscreve usuario no evento
     * @param uId id do usuario
     * @param eId id do evento
     * @return verdadeiro se cadastro foi efetuado, false se nao
     */
    public boolean inscreveUsuarioEvento(int uId, int eId){
        return EventoControl.inscrveUsuarioEvento(eId,uId,this.banco);
    }

    /**
     * Retorna lista com nome e instituição de todos usuarios cadastrado no evento atual
     * @param eId id do evento clicado
     * @return lista com nome e instituicao de todos usuarios do evento 'eId'
     */
    public ArrayList<String> listaUsuariosEvento(int eId){
        return EventoControl.retornoUserEvento(eId,this.banco);
    }

    /**
     * Verificação se usuario ja está cadastrado no evento em q deseja se cadastrar
     * @param uId id do usuario logado
     * @return verdadeiro se ja esta cadastrado, false se nao
     */
    public boolean verificaUsuarioNoEvento(int uId, int eId){
        return UserControl.verificaUserEvento(uId, eId, this.banco);
    }

    /**
     * Verificação se usuario ja é palestrante do evento em que deseja se cadastrar
     * @param uId id do usuario logado
     * @return verdadeiro se é palestrante, false se nao
     */
    public boolean verificaUsuarioPalestranteEvento(int uId, int eId){
        return UserControl.verificaUserEventoPalestrante(uId, eId, this.banco);
    }
}
