package app.vidas.salvando.com.cajamarca.salvandovidas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import app.vidas.salvando.com.cajamarca.salvandovidas.agents.AgentUtils;
import app.vidas.salvando.com.cajamarca.salvandovidas.agents.IMascotaAgent;
import app.vidas.salvando.com.cajamarca.salvandovidas.agents.IUserAgent;
import app.vidas.salvando.com.cajamarca.salvandovidas.entities.Mascota;
import app.vidas.salvando.com.cajamarca.salvandovidas.entities.Usuario;
import rx.Subscriber;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InicioRegistro extends AppCompatActivity implements View.OnClickListener{

    Button btnIngresar,btnCrear;
    EditText txtusu,txtpass;

    private IMascotaAgent mascotaAgent;
    private IUserAgent userAgent;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_registro);

        mascotaAgent = AgentUtils.getAgent(IMascotaAgent.class);
        userAgent = AgentUtils.getAgent(IUserAgent.class);
        if(sessionManager == null) {
            sessionManager = new SessionManager(getApplicationContext());
        }

        this.sessionManager.checkLogin(InicioRegistro.class);

        txtusu=(EditText)findViewById(R.id.txtusu);
        txtpass=(EditText)findViewById(R.id.txtpass);
        btnIngresar=(Button)findViewById(R.id.btnIngresar);
        btnCrear=(Button)findViewById(R.id.btnCrear);

        btnIngresar.setOnClickListener(this);
        btnCrear.setOnClickListener(this);


    }


    public void onClick(View v){

        if(v.getId()==R.id.btnIngresar){
            String email = txtusu.getText().toString().trim();
            String password = txtpass.getText().toString().trim();

            if(email.length() > 0 && password.length() > 0) {
                Usuario loginModel = new Usuario();
                loginModel.setEmail(email);
                loginModel.setPassword(password);

                doLogin(loginModel);
            } else {
                Toast.makeText(getApplicationContext(),"Por favor ingresa correo y clave", Toast.LENGTH_LONG)
                        .show();
            }


        }
        if(v.getId()==R.id.btnCrear){
            Intent i = new Intent(getApplicationContext(),CrearCuenta.class);
            startActivity(i);

            testWebService();
            getMascota();
            getMisMascotas();

        }
    }

    public String enviarDatosGET(String usu, String pass){
        URL url = null;
        String linea = "";
        int respuesta=0;
        StringBuilder resul = null;

        try{
            url = new URL("http://karinacabrera.idesoft.co/apis/happypet/app/Valida.php?usu="+usu+"&pass="+pass);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            respuesta=connection.getResponseCode();

            resul= new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while((linea=reader.readLine())!=null){
                    resul.append(linea);
                }
            }

            return  resul.toString();
        }
        catch (Exception e){
            System.out.print("[ERROR] No fue posible conectar con servidor, verfica tu conexion a internet.");
        }

        return  "";

    }

    public int obtenerDatosJSON(String response){
        int res =0;

        try{
            JSONArray json = new JSONArray(response);
            if(json.length()>0){
                res=1;
            }
        }
        catch (Exception e){}
        return res;
    }

    private void doLogin(Usuario usuario) {
        userAgent.login(usuario)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Usuario>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("LOGIN", e.getMessage());
                        Toast.makeText(getApplicationContext(),"El correo o la contrasena son incorrectos", Toast.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onNext(Usuario usuario) {
                        Log.i("LOGIN", "username: " + usuario.getNombres());
                        sessionManager.createSession(usuario);

                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);

                    }
                });
    }

    private void testWebService() {
        mascotaAgent.getMascotas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Mascota>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("K", "Unable to submit Get to API. " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<Mascota> mascotas) {
                        Log.i("K", "Get submitted to API. " + mascotas.get(0).getNombre());
                    }
                });
    }

    private void getMascota() {
        mascotaAgent.getMascota(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Mascota>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Mascota mascota) {
                        Log.i("Mascota", "Mascota detalle nombre. " + mascota.getNombre() + " sexo: " + mascota.getSexo());
                    }
                });
    }

    private void getMisMascotas() {
        mascotaAgent.getMisMascotas(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Mascota>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Mascota> mascotas) {
                        Log.i("MisMacotas", "Mis mascotas. " + mascotas.get(0).getNombre());
                    }
                });
    }
}
