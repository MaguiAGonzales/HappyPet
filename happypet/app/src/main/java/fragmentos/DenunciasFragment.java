package fragmentos;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.happypet.movil.happypet.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adaptadores.adaptadorDenuncias;
import adaptadores.adaptadorMisDenuncias;
import clases.Denuncia;
import parseadores.JsonDenunciasParser;

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

    String[] tiposDenuncias = {"TODAS", "MALTRATOS", "ACCIDENTES", "OTROS"};
    private Spinner cbTipoDenuncias, cbTipoMisDenuncias;

    ProgressDialog progress;
    ListView listaDenuncias;
    ArrayAdapter adaptadorDenuncias;
    HttpURLConnection conDenuncias;
    private SwipeRefreshLayout swipeContainer;

    ProgressDialog progressMisDenuncias;
    ListView listaMisDenuncias;
    ArrayAdapter adaptadorMisDenunciasA;
    HttpURLConnection conMisDenuncias;
    private SwipeRefreshLayout swipeContainerMisDenuncias;


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

        // ----------------------------------------------------------------

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
            }
        });

        //====================== TAB DENUNCIAS ========================
        cbTipoDenuncias = (Spinner) me.findViewById(R.id.s_denuncia_tipo);
        cargarTipoDenuncias();

        listaDenuncias = (ListView) me.findViewById(R.id.lvDenuncias);

        cargarDenuncias("");
//        swipeContainer = (SwipeRefreshLayout) me.findViewById(R.id.srlContainerDenuncias);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                cargarDenuncias("");
//                swipeContainer.setRefreshing(false);
//            }
//        });

        //====================== TAB MIS DENUNCIAS ========================
        cbTipoMisDenuncias = (Spinner) me.findViewById(R.id.s_midenuncia_tipo);
        cargarTipoMisDenuncias();

        listaMisDenuncias = (ListView) me.findViewById(R.id.lvMisDenuncias);

        cargarMisDenuncias("");
        swipeContainerMisDenuncias = (SwipeRefreshLayout) me.findViewById(R.id.srlContainerMisDenuncias);
        swipeContainerMisDenuncias.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarMisDenuncias("");
                swipeContainerMisDenuncias.setRefreshing(false);
            }
        });



        return me;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_AgregarDenuncia){
            cargarDenuncias("");
        }
    }

    public void cargarTipoDenuncias(){
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_spinner_dropdown_item, tiposDenuncias );
        cbTipoDenuncias.setAdapter(ad);
    }
    public void cargarTipoMisDenuncias(){
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tiposDenuncias );
        cbTipoMisDenuncias.setAdapter(ad);
    }

    public void cargarDenuncias(String filtro){
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
            progress = ProgressDialog.show(getActivity(), "", "Cargando las Denuncias...");
        }

        @Override
        protected List<Denuncia> doInBackground(URL... urls) {
            List<Denuncia> denuncias = null;

            try {
                // Establecer la conexión
                conDenuncias = (HttpURLConnection)urls[0].openConnection();
                conDenuncias.setConnectTimeout(15000);
                conDenuncias.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = conDenuncias.getResponseCode();

                if(statusCode!=200) {
                    denuncias = new ArrayList<>();
                    denuncias.add(new Denuncia());
                } else {
                    InputStream in = new BufferedInputStream(conDenuncias.getInputStream());

                    JsonDenunciasParser parser = new JsonDenunciasParser();

                    denuncias = parser.leerFlujoJson(in);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                conDenuncias.disconnect();
            }
            //System.out.println(denuncias);
            return denuncias;
        }

        @Override
        protected void onPostExecute(List<Denuncia> denuncias) {
            if(denuncias!=null) {
                adaptadorDenuncias = new adaptadorDenuncias(getActivity().getBaseContext(), denuncias, DenunciasFragment.this);
                listaDenuncias.setAdapter(adaptadorDenuncias);
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

    public void cargarMisDenuncias(String filtro){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                String filtroInicio = "?f=listar&id=" + USUARIO_ID;
//                filtro = filtro.equals("")?"":"&nombre=" + filtro;
                String protocolo = "http://";
                String ip = getResources().getString(R.string.ipweb);
                String puerto = getResources().getString(R.string.puertoweb);
                puerto = puerto.equals("") ? "" : ":" + puerto;

                String url = protocolo + ip + puerto +  "/happypet-web/funciones/admin_denuncia.php" +filtroInicio + filtro;
                System.out.println("direccion ------      " + url);

                new JsonTaskMisDenuncias().execute(new URL(url) );
            } else {
                Toast.makeText(getActivity(), "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class JsonTaskMisDenuncias extends AsyncTask<URL, Void, List<Denuncia>> {
        @Override
        protected void onPreExecute() {
            progressMisDenuncias = ProgressDialog.show(getActivity(), "", "Cargando Mis Denuncias...");
        }

        @Override
        protected List<Denuncia> doInBackground(URL... urls) {
            List<Denuncia> denuncias = null;

            try {
                // Establecer la conexión
                conMisDenuncias = (HttpURLConnection)urls[0].openConnection();
                conMisDenuncias.setConnectTimeout(15000);
                conMisDenuncias.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = conMisDenuncias.getResponseCode();

                if(statusCode!=200) {
                    denuncias = new ArrayList<>();
                    denuncias.add(new Denuncia());
                } else {
                    InputStream in = new BufferedInputStream(conMisDenuncias.getInputStream());

                    JsonDenunciasParser parser = new JsonDenunciasParser();

                    denuncias = parser.leerFlujoJson(in);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                conDenuncias.disconnect();
            }
            return denuncias;
        }

        @Override
        protected void onPostExecute(List<Denuncia> denuncias) {
            if(denuncias!=null) {
                adaptadorMisDenunciasA = new adaptadorMisDenuncias(getActivity().getBaseContext(), denuncias);
                listaMisDenuncias.setAdapter(adaptadorMisDenunciasA);
            }else{
                Toast.makeText(
                        getActivity().getBaseContext(),
                        "Ocurrió un error de Parsing Json en Mis Denuncias",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            if (progressMisDenuncias.isShowing()) { progressMisDenuncias.dismiss(); }

        }
    }


    public void llamar(final String numeroTelefono) {
//        try{
//            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
//        }catch (Exception e){
//            Toast.makeText(getActivity(), "Ocrrió un problema al intentar llamar al Telefono: " + phoneNumber + ". Inténtelo Ud. mismo de manera manual", Toast.LENGTH_LONG).show();
//        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + numeroTelefono));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);
        }else{
            Toast.makeText(getActivity().getBaseContext(), "Active manualmente sus permisos para hacer LLamadas" , Toast.LENGTH_LONG).show();
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numeroTelefono));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getActivity().getBaseContext(), "No se pudo enviar el número de teléfono para que pueda llamar" , Toast.LENGTH_SHORT).show();
            }
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
