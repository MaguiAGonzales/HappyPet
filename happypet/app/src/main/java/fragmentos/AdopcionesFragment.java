package fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.happypet.movil.happypet.MascotaDetalleActivity;
import com.happypet.movil.happypet.MismascotasActivity;
import com.happypet.movil.happypet.ProcesoAdopcion;
import com.happypet.movil.happypet.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adaptadores.adaptadorMascotas;
import adaptadores.adaptadorMascotasDisponibles;
import adaptadores.adaptadorMascotasMisAdopciones;
import clases.Adopcion;
import clases.AdopcionMascota;
import clases.Mascota;
import parseadores.JsonAdopcionMascotaParser;
import parseadores.JsonMascotaParser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdopcionesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AdopcionesFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public AdopcionesFragment() {
        // Required empty public constructor
    }

    public String USUARIO_ID;

    TabHost thAdopciones;

    private SwipeRefreshLayout scDisponibles;
    ListView listaDisponibles;
    ArrayAdapter adaptadorDisponibles;
    HttpURLConnection conDisponibles;
    ProgressDialog progressDisponibles;

    // --------- mIS aDOOPCIONES -------
    private SwipeRefreshLayout scMisAdopciones;
    ListView listaMisAdopciones;
    ArrayAdapter adaptadorMisAdopciones;
    HttpURLConnection conMisAdopciones;
    ProgressDialog progressMisAdopciones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View me = inflater.inflate(R.layout.fragment_adopciones, container, false);

        USUARIO_ID = getArguments().getString("id");

        thAdopciones = (TabHost) me.findViewById(R.id.thAdopciones);
        thAdopciones.setup();

        TabHost.TabSpec tabDis = thAdopciones.newTabSpec("tabDis");
        TabHost.TabSpec tabMis = thAdopciones.newTabSpec("tabMis");

        tabDis.setIndicator("DISPONIBLES");
        tabDis.setContent(R.id.tabDisponibles);

        tabMis.setIndicator("MIS ADOPCIONES");
        tabMis.setContent(R.id.tabMisAdopciones);

        thAdopciones.addTab(tabDis); //añadimos los tabs ya programados
        thAdopciones.addTab(tabMis);

        // ------------ TAB DISPONIBLES -------------
        scDisponibles = (SwipeRefreshLayout) me.findViewById(R.id.srlMascotasDisponibles);
        scDisponibles.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarAdopcionesDisponibles();
                scDisponibles.setRefreshing(false);
            }
        });

        listaDisponibles = (ListView) me.findViewById(R.id.lvMascotasDisponibles);
        cargarAdopcionesDisponibles();

        listaDisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                Mascota mascotaSel = (Mascota) pariente.getItemAtPosition(posicion);

                Intent intent = new Intent(getContext(), MascotaDetalleActivity.class);
                intent.putExtra("idUsuario", USUARIO_ID);

                intent.putExtra("id", String.valueOf(mascotaSel.getIdMascota()));
                intent.putExtra("foto", mascotaSel.getImagen());
                intent.putExtra("nombre", mascotaSel.getNombre());
                intent.putExtra("tipo", mascotaSel.getTipo());
                intent.putExtra("sexo", mascotaSel.getSexo());
                intent.putExtra("particularidad", mascotaSel.getParticularidades());
                intent.putExtra("salud", mascotaSel.getSalud());
                intent.putExtra("edad", String.valueOf(mascotaSel.getEdad()) );
                startActivity(intent);

            }
        });

        // ------------ TAB MIS ADOPCIONES -------------
        scMisAdopciones = (SwipeRefreshLayout) me.findViewById(R.id.srlMisAdopciones);
        scMisAdopciones.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarMisAdopciones();
                scMisAdopciones.setRefreshing(false);
            }
        });

        listaMisAdopciones= (ListView) me.findViewById(R.id.lvMisAdopciones);
        cargarMisAdopciones();

        listaMisAdopciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                AdopcionMascota mascotaSel = (AdopcionMascota) pariente.getItemAtPosition(posicion);

                Intent intent = new Intent(getContext(), ProcesoAdopcion.class);
                intent.putExtra("idUsuario", USUARIO_ID);
                intent.putExtra("idAdopcion", String.valueOf(mascotaSel.getIdAdopcion()));

                intent.putExtra("idMascota", String.valueOf(mascotaSel.getMascotaId()));
                intent.putExtra("foto", mascotaSel.getImagen());
                intent.putExtra("nombre", mascotaSel.getNombre());
                intent.putExtra("tipo", mascotaSel.getTipo());
                intent.putExtra("sexo", mascotaSel.getSexo());
                intent.putExtra("particularidad", mascotaSel.getParticularidades());
                intent.putExtra("salud", mascotaSel.getSalud());
                intent.putExtra("edad", String.valueOf(mascotaSel.getEdad()) );

                intent.putExtra("estado", mascotaSel.getEstado() );

                startActivity(intent);

            }
        });

        //--------------------------------------------

        return  me;
    }

    //====================== DATA ADOPCIONES ===========================

    private void cargarAdopcionesDisponibles (){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                String funcion = "?f=disponibles";
                String datos = "";

                String protocolo = "http://";
                String ip = getResources().getString(R.string.ipweb);
                String puerto = getResources().getString(R.string.puertoweb);
                puerto = puerto.equals("") ? "" : ":" + puerto;

                String direccion = protocolo + ip + puerto +  "/happypet-web/funciones/admin_adopcion.php" + funcion  + datos;
                System.out.println("direccion adopciones ------      " + direccion);

                URL url = new URL(direccion);

                new TareaMascotasDisponibles().execute(url);
            } else {
                Toast.makeText(getActivity(), "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e){}
    }

    public class TareaMascotasDisponibles extends AsyncTask<URL, Void, List<Mascota>> {
        @Override
        protected void onPreExecute() {
            progressDisponibles = ProgressDialog.show(getActivity(), "", "Cargando Mascotas Disponibles...");
        }

        @Override
        protected List<Mascota> doInBackground(URL... urls) {
            List<Mascota> mascotas = null;

            try {
                // Establecer la conexión
                conDisponibles = (HttpURLConnection)urls[0].openConnection();
                conDisponibles.setConnectTimeout(15000);
                conDisponibles.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = conDisponibles.getResponseCode();
//                System.out.println("codigo del estado ---->>  " + statusCode);

                if(statusCode!=200) {
                    mascotas = new ArrayList<>();
                    mascotas.add(new Mascota(0,"",null,null,null,null,null,null,null,null,1));
                } else {
                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(conDisponibles.getInputStream());
                    JsonMascotaParser parser = new JsonMascotaParser();

//                    System.out.println("ENTRADA ---- " + in.toString());

                    mascotas = parser.leerFlujoJson(in);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                conDisponibles.disconnect();
            }
            System.out.println(mascotas);
            return mascotas;
        }

        @Override
        protected void onPostExecute(List<Mascota> mascotas) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if(mascotas!=null) {
                adaptadorDisponibles = new adaptadorMascotasDisponibles(getActivity().getBaseContext(), mascotas);
                listaDisponibles.setAdapter(adaptadorDisponibles);
            }else{
                Toast.makeText(
                        getActivity().getBaseContext(),
                        "Ocurrió un error de Parsing Json en las Mascotas Disponibles",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            if (progressDisponibles.isShowing()) { progressDisponibles.dismiss(); }

        }
    }

    //====================== DATA MIS ADOPCIONES ===========================

    private void cargarMisAdopciones (){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                String funcion = "?f=listar&id_usuario=" + USUARIO_ID;
                String datos = "";

                String protocolo = "http://";
                String ip = getResources().getString(R.string.ipweb);
                String puerto = getResources().getString(R.string.puertoweb);
                puerto = puerto.equals("") ? "" : ":" + puerto;

                String direccion = protocolo + ip + puerto +  "/happypet-web/funciones/admin_adopcion.php" + funcion  + datos;
                System.out.println("direccion mis mascotas ------      " + direccion);

                URL url = new URL(direccion);

                new TareaMisMascotas().execute(url);
            } else {
                Toast.makeText(getActivity(), "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e){}
    }


    public class TareaMisMascotas extends AsyncTask<URL, Void, List<AdopcionMascota>> {
        @Override
        protected void onPreExecute() {
            progressMisAdopciones = ProgressDialog.show(getActivity(), "", "Cargando Mis Mascotas...");
        }

        @Override
        protected List<AdopcionMascota> doInBackground(URL... urls) {
            List<AdopcionMascota> mascotas = null;

            try {
                // Establecer la conexión
                conMisAdopciones = (HttpURLConnection)urls[0].openConnection();
                conMisAdopciones.setConnectTimeout(15000);
                conMisAdopciones.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = conMisAdopciones.getResponseCode();
//                System.out.println("codigo del estado ---->>  " + statusCode);

                if(statusCode!=200) {
                    mascotas = new ArrayList<>();
                    mascotas.add(new AdopcionMascota(0, null, null, 0, 0, null, null, null, null, null, null, null, 0));
                } else {
                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(conMisAdopciones.getInputStream());
                    JsonAdopcionMascotaParser parser = new JsonAdopcionMascotaParser();

//                    System.out.println("ENTRADA ---- " + in.toString());

                    mascotas = parser.leerFlujoJson(in);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                conMisAdopciones.disconnect();
            }
            System.out.println(mascotas);
            return mascotas;
        }

        @Override
        protected void onPostExecute(List<AdopcionMascota> mascotas) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if(mascotas!=null) {
                adaptadorMisAdopciones = new adaptadorMascotasMisAdopciones(getActivity().getBaseContext(), mascotas);
                listaMisAdopciones.setAdapter(adaptadorMisAdopciones);
            }else{
                Toast.makeText(
                        getActivity().getBaseContext(),
                        "Ocurrió un error de Parsing Json Mis Mascotas",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            if (progressMisAdopciones.isShowing()) { progressMisAdopciones.dismiss(); }

        }
    }
    // =================================================================

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
