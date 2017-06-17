package app.vidas.salvando.com.cajamarca.salvandovidas.repository;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import app.vidas.salvando.com.cajamarca.salvandovidas.entities.Usuario;

public class SessionRepository {

    SharedPreferences preferences;
    Editor editor;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NAME = "nombres";

    public static final String KEY_APELLIDOS = "apellidos";

    public static final String KEY_EMAIL = "email";

    public SessionRepository(SharedPreferences preferences) {
        this.preferences = preferences;
        this.editor = this.preferences.edit();
    }

    public void createLoginSession(Usuario userLogged) {

        Log.i("login", userLogged.getNombres());

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NAME, userLogged.getNombres());

        editor.putString(KEY_EMAIL, userLogged.getEmail());

        editor.putString(KEY_APELLIDOS, userLogged.getApellidos());

        editor.commit();
    }

    public Usuario getUserDetails() {
        Usuario currentUser = new Usuario();
        currentUser.setNombres(preferences.getString(KEY_NAME, null));
        currentUser.setApellidos(preferences.getString(KEY_APELLIDOS, null));
        currentUser.setEmail(preferences.getString(KEY_EMAIL, null));

        Log.i("CurrentUser", currentUser.getNombres() + " -- " + currentUser.getApellidos());

        return currentUser;
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

}
