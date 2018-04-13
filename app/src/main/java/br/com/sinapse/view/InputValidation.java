package br.com.sinapse.view; /**
 * Created by Jerry Jr on 25/03/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by lalit on 9/13/2016.
 */
public class InputValidation {

    public static boolean validaEmail(EditText input) {
        String value = input.getText().toString().trim();
        return (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) ? false : true;
    }

    public static boolean validaSenha(EditText input) {
        String value = input.getText().toString().trim();
        return (value.isEmpty() || (value.length() > 6)) ? false : true;
    }

    public static boolean validaTelefone(EditText input){
        String value = input.getText().toString().trim();
        return (value.isEmpty() || (value.length() > 9)) ? false : true;
    }

    public static boolean validaCampo(EditText input){
        return true;
    }

    public static void validaCampos(){

    }
}