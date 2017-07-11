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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.happypet.movil.happypet.MascotaAgregarActivity;
import com.happypet.movil.happypet.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adaptadores.adaptadorDenuncias;
import adaptadores.adaptadorMascotas;
import clases.Denuncia;
import clases.Mascota;
import parseadores.JsonDenunciasParser;
import parseadores.JsonMascotaParser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DenunciasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DenunciasFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public DenunciasFragment() {
        // Required empty public constructor
    }

    String USUARIO_ID;
    FloatingActionButton fab;
    TabHost thDenuncias;


    ListView lista;
    ArrayAdapter adaptador;
    HttpURLConnection con;
    ProgressDialog progress;

    private SwipeRefreshLayout swipeContainer;

    private int REQUEST_AgregarDenuncia = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View me = inflater.inflate(R.layout.fragment_denuncias, container, false);

        USUARIO_ID = getArguments().getString("id");

        thDenuncias = (TabHost) me.findViewById(R.id.thDenuncias);
        thDenuncias.setup();

        TabHost.TabSpec tabDen = thDenuncias.newTabSpec("tabDenuncias");
        TabHost.TabSpec tabMisDen = thDenuncias.newTabSpec("tabMisDenuncias");

        tabDen.setIndicator("DENUNCIAS");
        tabDen.setContent(R.id.tabDenuncias);

        tabMisDen.setIndicator("MIS DENUNCIAS");
        tabMisDen.setContent(R.id.tabMisDenuncias);

        thDenuncias.addTab(tabDen); //añadimos los tabs ya programados
        thDenuncias.addTab(tabMisDen);

        //====================== TAB MIS DENUNCIAS ========================

        swipeContainer = (SwipeRefreshLayout) me.findViewById(R.id.srlContainerDenuncias);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargar("");
                swipeContainer.setRefreshing(false);
            }
        });

        lista = (ListView) me.findViewById(R.id.lvDenuncias);
        cargar("");

        fab = (FloatingActionButton) me.findViewById(R.id.btnAgregarDenuncia);
        fab.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent (view.getContext(), MascotaAgregarActivity.class );
//                intent.putExtra("id", USUARIO_ID);
//                startActivityForResult(intent, REQUEST_AgregarDenuncia);
            }
        });

        thDenuncias.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("tabDenuncias")) {
                    fab.hide();
                }
                if(tabId.equals("tabMisDenuncias")) {
                    fab.show();
                }
            }});

        return me;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_AgregarDenuncia){
            cargar("");
        }
    }

    public void cargar (String filtro){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                String filtroInicio = "?f=listar";
//                filtro = filtro.equals("")?"":"&nombre=" + filtro;
                String protocolo = "http://";
                String ip = getResources().getString(R.string.ipweb);
                String puerto = getResources().getString(R.string.puertoweb);
                puerto = puerto.equals("") ? "" : ":" + puerto;

                String url = protocolo + ip + puerto +  "/happypet-web/funciones/admin_denuncia.php" +filtroInicio + filtro;
                System.out.println("direccion ------      " + url);

                new JsonTask().execute(new URL(url) );
            } else {
                Toast.makeText(getActivity(), "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class JsonTask extends AsyncTask<URL, Void, List<Denuncia>> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "", "Cargando la lista de Denuncias...");
        }

        @Override
        protected List<Denuncia> doInBackground(URL... urls) {
            List<Denuncia> denuncias = null;

            try {
                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    denuncias = new ArrayList<>();
                    denuncias.add(new Denuncia());
                } else {
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JsonDenunciasParser parser = new JsonDenunciasParser();

                    denuncias = parser.leerFlujoJson(in);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                con.disconnect();
            }
            //System.out.println(denuncias);
            return denuncias;
        }

        @Override
        protected void onPostExecute(List<Denuncia> denuncias) {
            if(denuncias!=null) {
                adaptador = new adaptadorDenuncias(getActivity().getBaseContext(), denuncias);
                lista.setAdapter(adaptador);
            }else{
                Toast.makeText(
                        getActivity().getBaseContext(),
                        "Ocurrió un error de Parsing Json en Denuncias",
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
