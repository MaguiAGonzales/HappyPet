package clases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.happypet.movil.happypet.R;

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



public class GuardarDenuncia extends AppCompatActivity{

        String tipo, fecha, titulo, descripcion, telefono, estado, idUsuario;

        String funcion, encodedImage, data, image, cargarDatos;
        Activity context;
        ArrayList<DatosImage> listaImage = new ArrayList();
        DatosImage dataImage;
        JSONObject json_data;

        Boolean ok;
        String msg;

        Intent intenPadre;
        ProgressDialog progress;

    public GuardarDenuncia(Activity context, String funcion, String encodedImage, String fecha,  String tipo, String titulo, String descripcion, String telefono, String estado, String idUsuario){
        this.context = context;
        this.funcion = funcion;
        this.encodedImage = encodedImage;

        this.fecha = fecha;
        this.tipo = tipo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.estado = estado;
        this.idUsuario = idUsuario;

        this.ok = false;
        this.msg = "";

        new GuardarDenuncia.GetAndSet(this.context).execute();
    }

    private String obtenerDatos(){

        StringBuffer response = null;
        try {
            String protocolo = "http://";
            String ip = this.context.getResources().getString(R.string.ipweb);
            String puerto = this.context.getResources().getString(R.string.puertoweb);
            puerto = puerto.equals("") ? "" : ":" + puerto;
            String rutaUrl = protocolo + ip + puerto + "/happypet-web/funciones/admin_denuncia.php";

            URL obj = new URL( rutaUrl);
            if (funcion.equals("insertar")){
                data = "f=" + URLEncoder.encode(funcion, "UTF-8")
                        + "&fecha=" + URLEncoder.encode(fecha, "UTF-8")
                        + "&tipo=" + URLEncoder.encode(tipo, "UTF-8")
                        + "&titulo=" + URLEncoder.encode(titulo, "UTF-8")
                        + "&descripcion=" + URLEncoder.encode(descripcion, "UTF-8")
                        + "&telefono=" + URLEncoder.encode(telefono, "UTF-8")
                        + "&estado=" + URLEncoder.encode(estado, "UTF-8")
                        + "&foto=" + URLEncoder.encode(encodedImage, "UTF-8")
                        + "&id_usuario=" + this.idUsuario;
                System.out.println("datos obtenerdatos -------- >    " + data);
            }

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en; q=0.5");
            con.setDoOutput(true);

            con.setFixedLengthStreamingMode(data.getBytes().length);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            System.out.println("Direccion ----> " + con.getURL().toString());

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
                if(funcion.equals("insertar")){
                    json_data = new JSONObject(cargarDatos);
                    ok =  Boolean.valueOf(json_data.getString("success"));
                    msg = json_data.getString("msg");
                }
                return true;
            }
        }catch (JSONException e){
            if (progress.isShowing()) { progress.dismiss();}
            e.printStackTrace();

        }
        return false;
    }

    private void actividad(){
        progress.dismiss();

//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("msg",msg);
        returnIntent.putExtra("success", String.valueOf(ok));
        setResult(Activity.RESULT_OK,returnIntent);
//        setResult(2, intenPadre);

        if(ok){
            Toast.makeText(
                    context,
                    "Denuncia Guardada Correctamente",
                    Toast.LENGTH_LONG)
                    .show();
        }

        context.finish();
    }

    class GetAndSet extends AsyncTask<String, String, String> {
        private Context myContext;

        public GetAndSet(Context context){
            this.myContext = context;
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(this.myContext, "", "Guarando Denuncia...");
        }

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

//        @Override
//        protected void onPostExecute() {
//            if (progress.isShowing()) { progress.dismiss(); }
//
//        }
    }


}
