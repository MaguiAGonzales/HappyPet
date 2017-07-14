package fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.happypet.movil.happypet.MascotaAgregarActivity;
import com.happypet.movil.happypet.MascotaDetalle2Activity;
import com.happypet.movil.happypet.MascotaDetalleActivity;
import com.happypet.movil.happypet.MismascotasActivity;
import com.happypet.movil.happypet.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adaptadores.adaptadorMascotas;
import clases.Mascota;
import parseadores.JsonMascotaParser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MisMascotasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MisMascotasFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public MisMascotasFragment() {
        // Required empty public constructor
    }

    String USUARIO_ID;

    ListView lista;
    ArrayAdapter adaptador;
    HttpURLConnection con;
    ProgressDialog progress;

    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View me =  inflater.inflate(R.layout.fragment_mis_mascotas, container, false);

        USUARIO_ID = getArguments().getString("id");

        swipeContainer = (SwipeRefreshLayout) me.findViewById(R.id.srlContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargar("");
                swipeContainer.setRefreshing(false);
            }
        });

        lista = (ListView) me.findViewById(R.id.lvMascotas);
        cargar("");

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                Mascota mascotaSel = (Mascota) pariente.getItemAtPosition(posicion);

                Intent intent = new Intent(getContext(), MascotaDetalle2Activity.class);
                intent.putExtra("idUsuario", USUARIO_ID);

                intent.putExtra("id", String.valueOf(mascotaSel.getIdMascota()));
                intent.putExtra("foto", mascotaSel.getImagen());
                intent.putExtra("nombre", mascotaSel.getNombre());
                intent.putExtra("tipo", mascotaSel.getTipo());
                intent.putExtra("sexo", mascotaSel.getSexo());
                intent.putExtra("particularidad", mascotaSel.getParticularidades());
                intent.putExtra("salud", mascotaSel.getSalud());
                intent.putExtra("tamanio", mascotaSel.getTamanio());
                intent.putExtra("nacimiento", mascotaSel.getAnio() );
                intent.putExtra("edad", String.valueOf(mascotaSel.getEdad()) );
                intent.putExtra("adoptado", mascotaSel.getAdoptado() );
                intent.putExtra("esterelizado", mascotaSel.getEsterelizado() );
                System.out.println("asignado todos los paramateros");
                startActivity(intent);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) me.findViewById(R.id.btnAgregarMascota);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), MascotaAgregarActivity.class );
                intent.putExtra("id", USUARIO_ID);
                startActivityForResult(intent, 0);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        return me;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            cargar("");
        }
    }

    public void cargar (String filtro){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                String filtroInicio = "?f=listar&id_usuario=" + USUARIO_ID;
                filtro = filtro.equals("")?"":"&nombre=" + filtro;
                String protocolo = "http://";
                String ip = getResources().getString(R.string.ipweb);
                String puerto = getResources().getString(R.string.puertoweb);
                puerto = puerto.equals("") ? "" : ":" + puerto;

                String url = protocolo + ip + puerto +  "/happypet-web/funciones/admin_mascota.php" +filtroInicio + filtro;
                System.out.println("direccion ------      " + url);

                new JsonTask().execute(new URL(url) );
            } else {
                Toast.makeText(getActivity(), "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class JsonTask extends AsyncTask<URL, Void, List<Mascota>> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "", "Cargando la listaDenuncias de Mascotas...");
        }

        @Override
        protected List<Mascota> doInBackground(URL... urls) {
            List<Mascota> mascotas = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();
//                System.out.println("codigo del estado ---->>  " + statusCode);

                if(statusCode!=200) {
                    mascotas = new ArrayList<>();
                    mascotas.add(new Mascota(0,"",null,null,null,null,null,null,null,null,null,null,1));
                } else {
//                    System.out.println("a leer el msje json");
                    // Parsear el flujo conDenuncias formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JsonMascotaParser parser = new JsonMascotaParser();

//                    System.out.println("ENTRADA ---- " + in.toString());

                    mascotas = parser.leerFlujoJson(in);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            System.out.println(mascotas);
            return mascotas;
        }

        @Override
        protected void onPostExecute(List<Mascota> mascotas) {
            if(mascotas!=null) {
                adaptador = new adaptadorMascotas(getActivity().getBaseContext(), mascotas);
                lista.setAdapter(adaptador);
            }else{
                Toast.makeText(
                        getActivity().getBaseContext(),
                        "Ocurrió un error de Parsing Json en las Mascotas",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            if (progress.isShowing()) { progress.dismiss(); }

        }
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
