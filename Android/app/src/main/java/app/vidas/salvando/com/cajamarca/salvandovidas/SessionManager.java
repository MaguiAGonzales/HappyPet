package app.vidas.salvando.com.cajamarca.salvandovidas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

import app.vidas.salvando.com.cajamarca.salvandovidas.entities.Usuario;
import app.vidas.salvando.com.cajamarca.salvandovidas.repository.SessionRepository;

public class SessionManager {

    private static final String PREF_NAME = "SalvandoVidasPref";
    private int PRIVATE_MODE = 0;

    private Context context;
    private SessionRepository sessionRepository;

    public SessionManager(Context context) {
        this.context = context;

        if(this.sessionRepository == null) {
            SharedPreferences preferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            this.sessionRepository = new SessionRepository(preferences);
        }
    }

    public void createSession(Usuario currentUser) {
        this.sessionRepository.createLoginSession(currentUser);
    }

    public void checkLogin(Class currentClass) {
        if(!this.sessionRepository.isLoggedIn()) {
            if(InicioRegistro.class != currentClass) {
                redirectLoginActivity();
            }
        }
        else {
            if(currentClass != Home.class) {
                redirectToHomeActivity();
            }
        }
    }

    public void logout() {
        this.sessionRepository.logout();

        redirectLoginActivity();
    }

    public Usuario getUserDetails() {
        return this.sessionRepository.getUserDetails();
    }

    private void redirectLoginActivity() {
        // After logout redirect user to Loing Activity
        Intent intent = new Intent(this.context, InicioRegistro.class);

        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new flag to start new activiy
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        this.context.startActivity(intent);
    }

    private void redirectToHomeActivity() {
        Intent intent = new Intent(this.context, Home.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        this.context.startActivity(intent);
    }
}
