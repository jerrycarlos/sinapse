package br.com.sinapse.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.sinapse.model.User;


/**
 * Created by Jerry Jr on 25/03/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    // drop table sql query
    private final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + DBComands.TABLE_USER;
    private final String DROP_PJ_TABLE = "DROP TABLE IF EXISTS " + DBComands.TABLE_INSTITUICAO;
    private final String DROP_EVENTO_TABLE = "DROP TABLE IF EXISTS " + DBComands.TABLE_EVENT;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DBComands.DATABASE_NAME, null, DBComands.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBComands.CREATE_USER_TABLE);
        db.execSQL(DBComands.CREATE_TABLE_INSTITUICAO);
        db.execSQL(DBComands.CREATE_TABLE_EVENTO);
        db.execSQL(DBComands.CREATE_TABLE_EVENTO_USER);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < newVersion){//Drop User Table if exist
            //db.execSQL(DROP_EVENTO_TABLE);
            //db.execSQL(DROP_PJ_TABLE);
            //db.execSQL(DROP_USER_TABLE);
            // Create tables again
            onCreate(db);
        }

    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                DBComands.COLUMN_USER_ID,
                DBComands.COLUMN_USER_EMAIL,
                DBComands.COLUMN_USER_NAME,
                DBComands.COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                DBComands.COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(DBComands.TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBComands.COLUMN_USER_ID))));
                user.setNome(cursor.getString(cursor.getColumnIndex(DBComands.COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(DBComands.COLUMN_USER_EMAIL)));
                user.setSenha(cursor.getString(cursor.getColumnIndex(DBComands.COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBComands.COLUMN_USER_NAME, user.getNome());
        values.put(DBComands.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DBComands.COLUMN_USER_PASSWORD, user.getSenha());

        // updating row
        db.update(DBComands.TABLE_USER, values, DBComands.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(DBComands.TABLE_USER, DBComands.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                DBComands.COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = DBComands.COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(DBComands.TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                DBComands.COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = DBComands.COLUMN_USER_EMAIL + " = ?" + " AND " + DBComands.COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(DBComands.TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public static String getTableUser() {
        return DBComands.TABLE_USER;
    }

    public static String getColumnUserId() {
        return DBComands.COLUMN_USER_ID;
    }

    public static String getColumnUserName() {
        return DBComands.COLUMN_USER_NAME;
    }

    public static String getColumnUserEmail() {
        return DBComands.COLUMN_USER_EMAIL;
    }

    public static String getColumnUserPassword() {
        return DBComands.COLUMN_USER_PASSWORD;
    }
}
