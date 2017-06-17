package app.vidas.salvando.com.cajamarca.salvandovidas;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class CrearCuenta extends AppCompatActivity implements View.OnClickListener {

    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private ImageView mSetImage;
    private Button mOptionButton;
    private LinearLayout mRlView;

    Spinner opciones;


    private String mPath; //en que ruta se guardo

    //Guardar
    EditText txtNombres, txtApellidos, txtEmail, txtContrasenia,txtId;
    Spinner txtCiudad;
    Button btnGuardar,btnCancelar;
    HttpClient cliente;
    HttpPost post;
    List<NameValuePair> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        mSetImage = (ImageView) findViewById(R.id.set_picture);
        mOptionButton = (Button) findViewById(R.id.show_options_button);
        mRlView = (LinearLayout) findViewById(R.id.rl_view);

        //Combobox
        opciones=(Spinner)findViewById(R.id.spCiudad);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.opcionesCiudad,android.R.layout.simple_spinner_item);
        opciones.setAdapter(adapter);

        if(mayRequestStoragePermission())
            mOptionButton.setEnabled(true);
        else
            mOptionButton.setEnabled(false);


        mOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });

        //guardar
        //txtId=(EditText)findViewById(R.id.txtId);
        txtNombres=(EditText)findViewById(R.id.txtNombres);
        txtApellidos=(EditText)findViewById(R.id.txtApellidos);
        txtCiudad=(Spinner)findViewById(R.id.spCiudad);
        txtEmail=(EditText)findViewById(R.id.txtEmail);
        txtContrasenia=(EditText)findViewById(R.id.txtContrasenia);
        btnGuardar=(Button) findViewById(R.id.btnGuardar);
        btnCancelar=(Button) findViewById(R.id.btnCancelar);

        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    private boolean mayRequestStoragePermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galería", "Cancelar"}; //vector de arrays
        final AlertDialog.Builder builder = new AlertDialog.Builder(CrearCuenta.this);
        builder.setTitle("Elije una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Tomar foto"){
                    openCamera();
                }else if(option[which] == "Elegir de galería"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                     intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona imagen"), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY); //ruta del almacenamiento
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    mSetImage.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    mSetImage.setImageURI(path);
                    break;

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(CrearCuenta.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                mOptionButton.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CrearCuenta.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnGuardar){
            if(txtNombres.getText().toString().equals("")){
                Toast.makeText(CrearCuenta.this,"Nombre no puede ser vacío",Toast.LENGTH_SHORT).show();
            }else{
                if(txtApellidos.getText().toString().equals("")){
                    Toast.makeText(CrearCuenta.this,"Apellidos no puede ser vacío",Toast.LENGTH_SHORT).show();
                }else{
                    if(txtEmail.getText().toString().equals("")){
                        Toast.makeText(CrearCuenta.this,"Email no puede ser vacío",Toast.LENGTH_SHORT).show();
                }else{
                        if(txtContrasenia.getText().toString().equals("")){
                            Toast.makeText(CrearCuenta.this,"Contrasenia no puede ser vacío",Toast.LENGTH_SHORT).show();
                    }else
                            new EnviarDatos(CrearCuenta.this).execute();
                    }
                }
            }

        }
        if(v.getId()==R.id.btnCancelar){
            Intent i = new Intent(getApplicationContext(),InicioRegistro.class);
            startActivity(i);
        }
    }

    class EnviarDatos extends AsyncTask<String,String,String>{
        private Activity contexto;
        EnviarDatos(Activity context){
            this.contexto=context;
        }
        @Override
        protected String doInBackground(String... params) {
            if(datos()){
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(contexto,"Datos Enviados Exitosamente",Toast.LENGTH_SHORT).show();
                        //txtNombres.setText("");
                        //txtApellidos.setText("");
                        //txtEmail.setText("");
                        //txtContrasenia.setText("");
                        Intent i = new Intent(getApplicationContext(),Home.class);
                        startActivity(i);
                    }
                });
                Intent i = new Intent(getApplicationContext(),Home.class);
                startActivity(i);
            }
            else
            {
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(contexto,"Datos no Enviados",Toast.LENGTH_SHORT).show();
                     }
                });
            }

            return null;
        }
    }

    private boolean datos(){
        cliente=new DefaultHttpClient();
        post=new HttpPost("http://karinacabrera.idesoft.co/apis/happypet/app/insertarUsuario.php");
        lista=new ArrayList<NameValuePair>(5);
        lista.add(new BasicNameValuePair("idUsuario",txtId.getText().toString().trim()));
        lista.add(new BasicNameValuePair("nombres",txtNombres.getText().toString().trim()));
        lista.add(new BasicNameValuePair("apellidos",txtApellidos.getText().toString().trim()));
        lista.add(new BasicNameValuePair("coreo",txtEmail.getText().toString().trim()));
        lista.add(new BasicNameValuePair("contrasenia",txtContrasenia.getText().toString().trim()));

        try {
            post.setEntity(new UrlEncodedFormEntity(lista));
            cliente.execute(post);
            return true;
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (ClientProtocolException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
}