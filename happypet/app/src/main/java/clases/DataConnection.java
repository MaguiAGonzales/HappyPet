package clases;

import android.app.Activity;
import android.media.MediaCodec;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


public class DataConnection extends AppCompatActivity {
    String nombre, tipo, sexo, anio, particularidades, salud, adoptado;

    String funcion, encodedImage, data, image, cargarDatos;
    Activity context;
    ArrayList<DatosImage> listaImage = new ArrayList();
    DatosImage dataImage;
    JSONObject json_data;

    Boolean ok;
    String msg;


    public DataConnection(Activity context, String funcion, String encodedImage,  String nombre, String tipo, String sexo, String anio, String particularidades, String salud, String adoptado){
        this.context = context;
        this.funcion = funcion;
        this.encodedImage = encodedImage;

        this.nombre = nombre;
        this.tipo = tipo;
        this.sexo = sexo;
        this.anio = anio;
        this.particularidades = particularidades;
        this.salud = salud;
        this.adoptado = adoptado;

        this.ok = false;
        msg = "";

        new GetAndSet().execute();
    }

    private String obtenerDatos(){
        StringBuffer response = null;
        try {
            String protocolo = "http://";
            String ip = "192.168.0.102";
            URL obj = new URL( protocolo + ip + ":8081/happypet-web/funciones/admin_mascota.php");
            System.out.println("Funcion: " + funcion);
            if (funcion.equals("setImage")){
                data = "f=" + URLEncoder.encode(funcion, "UTF-8")
                        + "&nombre=" + URLEncoder.encode(nombre, "UTF-8")
                        + "&tipo=" + URLEncoder.encode(tipo, "UTF-8")
                        + "&sexo=" + URLEncoder.encode(sexo, "UTF-8")
                        + "&anio=" + URLEncoder.encode(anio, "UTF-8")
                        + "&particularidades=" + URLEncoder.encode(particularidades, "UTF-8")
                        + "&salud=" + URLEncoder.encode(salud, "UTF-8")
                        + "&adoptado=" + URLEncoder.encode(adoptado, "UTF-8")
                        + "&imagen=" + URLEncoder.encode(encodedImage, "UTF-8");
                System.out.println("datos obtenerdatos -------- >    " + data);
            }

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en; q=0.5");
            con.setDoOutput(true);

            con.setFixedLengthStreamingMode(data.getBytes().length);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream out = new BufferedOutputStream(con.getOutputStream());
            out.write(data.getBytes());
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    private boolean filtrarDatos(){
        cargarDatos = obtenerDatos();

        System.out.println("RESPUESTA -------- >    " + cargarDatos.toString());
        try{
            if(!cargarDatos.equalsIgnoreCase("")){
                if(funcion.equals("setImage")){
                    json_data = new JSONObject(cargarDatos);
                    ok =  Boolean.valueOf(json_data.getString("success"));
                    msg = json_data.getString("msg");
                }
                return true;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return false;
    }
    private void actividad(){
        if(ok){
            Toast.makeText(context, "Datos Guardados Correctamente", Toast.LENGTH_LONG).show();
        }

        if(funcion.equals("insert")){
            Toast.makeText(context, "Imagen Insertada al servidor ", Toast.LENGTH_LONG).show();
        }
    }

    class GetAndSet extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            if(filtrarDatos()){
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        actividad();
                    }
                });
            }
            return null;
        }
    }


}
