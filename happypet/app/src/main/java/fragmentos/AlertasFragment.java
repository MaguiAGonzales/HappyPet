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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.happypet.movil.happypet.DenunciaAgregarActivity;
import com.happypet.movil.happypet.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adaptadores.adaptadorDenuncias;
import adaptadores.adaptadorExtraviados;
import adaptadores.adaptadorMisDenuncias;
import clases.Denuncia;
import clases.Extraviado;
import parseadores.JsonDenunciasParser;
import parseadores.JsonExtraviadosParser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlertasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AlertasFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public AlertasFragment() {
        // Required empty public constructor
    }

    String USUARIO_ID;
    FloatingActionButton fab;
    TabHost thAlertas;

    String[] tiposDenuncias = {"TODAS", "EXTRAVIADOS", "ENCONTRADOS"};
    private Spinner cbTipoDenuncias, cbTipoMisDenuncias;

    ProgressDialog progress;
    ListView listaAlertas;
    ArrayAdapter adaptadorAlertas;
    HttpURLConnection conAlertas;
    private SwipeRefreshLayout swipeContainer;

    ProgressDialog progressMisDenuncias;
    ListView listaMisDenuncias;
    ArrayAdapter adaptadorMisDenunciasA;
    HttpURLConnection conMisDenuncias;
    private SwipeRefreshLayout swipeContainerMisDenuncias;


    private int REQUEST_AgregarDenuncia = 10;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View me = inflater.inflate(R.layout.fragment_alertas, container, false);


        USUARIO_ID = getArguments().getString("id");

        thAlertas = (TabHost) me.findViewById(R.id.thAlertas);
        thAlertas.setup();

        TabHost.TabSpec tabDen = thAlertas.newTabSpec("tabAlertas");
        TabHost.TabSpec tabMisDen = thAlertas.newTabSpec("tabMisAlertas");

        tabDen.setIndicator("ALERTAS");
        tabDen.setContent(R.id.tabAlertas);

        tabMisDen.setIndicator("MIS ALERTAS");
        tabMisDen.setContent(R.id.tabMisMisAlertas);

        thAlertas.addTab(tabDen); //añadimos los tabs ya programados
        thAlertas.addTab(tabMisDen);

        // ----------------------------------------------------------------

        fab = (FloatingActionButton) me.findViewById(R.id.btnAgregarAlerta);
        fab.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent (view.getContext(), DenunciaAgregarActivity.class );
//                intent.putExtra("idUsuario", USUARIO_ID);
//                startActivityForResult(intent, REQUEST_AgregarDenuncia);
            }
        });

        thAlertas.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("tabAlertas")) {
                    fab.hide();
                }
                if(tabId.equals("tabMisAlertas")) {
                    fab.show();
                }
            }
        });

        //====================== TAB DENUNCIAS ========================
        cbTipoDenuncias = (Spinner) me.findViewById(R.id.s_alerta_tipo);
        cargarTipoDenuncias();

        cbTipoDenuncias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String  tipoSel=cbTipoDenuncias.getSelectedItem().toString();
                cargarAlertas(tipoSel);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        listaAlertas = (ListView) me.findViewById(R.id.lvAlertas);

//        //====================== TAB MIS DENUNCIAS ========================
        cbTipoMisDenuncias = (Spinner) me.findViewById(R.id.s_mialerta_tipo);
        cargarTipoMisDenuncias();
//
//        cbTipoMisDenuncias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                String  tipoSel=cbTipoMisDenuncias.getSelectedItem().toString();
//                cargarMisDenuncias(tipoSel);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        listaMisDenuncias = (ListView) me.findViewById(R.id.lvMisDenuncias);

        return me;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_AgregarDenuncia) {
//            cargarMisDenuncias("");
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


    public void cargarAlertas(String filtro){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                String filtroInicio = "?f=listar";
//                filtro = filtro.equals("")?"":"&tipo=" + filtro;
                filtro = "";
                String protocolo = "http://";
                String ip = getResources().getString(R.string.ipweb);
                String puerto = getResources().getString(R.string.puertoweb);
                puerto = puerto.equals("") ? "" : ":" + puerto;

                String url = protocolo + ip + puerto +  "/happypet-web/funciones/admin_extraviado.php" +filtroInicio + filtro;
                System.out.println("direccion ------      " + url);

                new JsonTask().execute(new URL(url) );
            } else {
                Toast.makeText(getActivity(), "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class JsonTask extends AsyncTask<URL, Void, List<Extraviado>> {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "", "Cargando las Alertas...");
        }

        @Override
        protected List<Extraviado> doInBackground(URL... urls) {
            List<Extraviado> estraviados = null;

            try {
                // Establecer la conexión
                conAlertas = (HttpURLConnection)urls[0].openConnection();
                conAlertas.setConnectTimeout(15000);
                conAlertas.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = conAlertas.getResponseCode();

                if(statusCode!=200) {
                    estraviados = new ArrayList<>();
                    estraviados.add(new Extraviado());
                } else {
                    InputStream in = new BufferedInputStream(conAlertas.getInputStream());

                    JsonExtraviadosParser parser = new JsonExtraviadosParser();

                    estraviados = parser.leerFlujoJson(in);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                conAlertas.disconnect();
            }
            //System.out.println(estraviados);
            return estraviados;
        }

        @Override
        protected void onPostExecute(List<Extraviado> extraviados) {
            if(extraviados!=null) {
                adaptadorAlertas = new adaptadorExtraviados(getActivity().getBaseContext(), extraviados, AlertasFragment.this);
                listaAlertas.setAdapter(adaptadorAlertas);
            }else{
                Toast.makeText(
                        getActivity().getBaseContext(),
                        "Ocurrió un error de Parsing Json en Alertas",
                        Toast.LENGTH_LONG)
                        .show();
            }
            if (progress.isShowing()) { progress.dismiss(); }

        }
    }
//
//    public void cargarMisDenuncias(String filtro){
//        try {
//            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//
//            if (networkInfo != null && networkInfo.isConnected()) {
//                String filtroInicio = "?f=listar&id=" + USUARIO_ID;
//                filtro = filtro.equals("")?"":"&tipo=" + filtro;
//                String protocolo = "http://";
//                String ip = getResources().getString(R.string.ipweb);
//                String puerto = getResources().getString(R.string.puertoweb);
//                puerto = puerto.equals("") ? "" : ":" + puerto;
//
//                String url = protocolo + ip + puerto +  "/happypet-web/funciones/admin_denuncia.php" +filtroInicio + filtro;
//                System.out.println("direccion ------      " + url);
//
//                new JsonTaskMisDenuncias().execute(new URL(url) );
//            } else {
//                Toast.makeText(getActivity(), "Necesita activar su conexión a la RED…", Toast.LENGTH_LONG).show();
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public class JsonTaskMisDenuncias extends AsyncTask<URL, Void, List<Denuncia>> {
//        @Override
//        protected void onPreExecute() {
//            progressMisDenuncias = ProgressDialog.show(getActivity(), "", "Cargando Mis Denuncias...");
//        }
//
//        @Override
//        protected List<Denuncia> doInBackground(URL... urls) {
//            List<Denuncia> denuncias = null;
//
//            try {
//                // Establecer la conexión
//                conMisDenuncias = (HttpURLConnection)urls[0].openConnection();
//                conMisDenuncias.setConnectTimeout(15000);
//                conMisDenuncias.setReadTimeout(10000);
//
//                // Obtener el estado del recurso
//                int statusCode = conMisDenuncias.getResponseCode();
//
//                if(statusCode!=200) {
//                    denuncias = new ArrayList<>();
//                    denuncias.add(new Denuncia());
//                } else {
//                    InputStream in = new BufferedInputStream(conMisDenuncias.getInputStream());
//
//                    JsonDenunciasParser parser = new JsonDenunciasParser();
//
//                    denuncias = parser.leerFlujoJson(in);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }finally {
//                conDenuncias.disconnect();
//            }
//            return denuncias;
//        }
//
//        @Override
//        protected void onPostExecute(List<Denuncia> denuncias) {
//            if(denuncias!=null) {
//                adaptadorMisDenunciasA = new adaptadorMisDenuncias(getActivity().getBaseContext(), denuncias);
//                listaMisDenuncias.setAdapter(adaptadorMisDenunciasA);
//            }else{
//                Toast.makeText(
//                        getActivity().getBaseContext(),
//                        "Ocurrió un error de Parsing Json en Mis Denuncias",
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//            if (progressMisDenuncias.isShowing()) { progressMisDenuncias.dismiss(); }
//
//        }
//    }


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
