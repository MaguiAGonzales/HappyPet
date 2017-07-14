package com.happypet.movil.happypet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import clases.DataConnection;
import clases.GuardarDenuncia;

public class DenunciaAgregarActivity extends AppCompatActivity  implements View.OnClickListener{

    String USUARIO_ID;

    String[] tiposDenuncias = {"MALTRATOS", "ACCIDENTES", "OTROS"};
    private Spinner cbTipoDenuncias;

    ImageView img;
    private final int PICKER = 1;
    String encodedImage, foto, funcion;
    GuardarDenuncia dc;

    private EditText tbFecha, tbTitulo, tbDescripcion, tbTelefono;

    static DenunciaAgregarActivity me;

    Intent intentPadre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_agregar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        USUARIO_ID = getIntent().getStringExtra("idUsuario");

        img = (ImageView) findViewById(R.id.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickFile();
            }
        });

        tbFecha = (EditText) findViewById(R.id.et_da_Fecha);
        tbFecha.requestFocus();

        cbTipoDenuncias = (Spinner) findViewById(R.id.s_da_Tipo);
        cargarTipos();

        tbTitulo = (EditText) findViewById(R.id.et_da_Titulo);
        tbDescripcion = (EditText) findViewById(R.id.et_da_Descripcion);
        tbTelefono = (EditText) findViewById(R.id.et_da_Telefono);


        me = this;

        Button btnEnviar = (Button) findViewById(R.id.btn_da_Guardar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcion = "insertar";
                String fecha = tbFecha.getText().toString();
                String tipo = cbTipoDenuncias.getSelectedItem().toString();
                String titulo = tbTitulo.getText().toString();
                String descripcion = tbDescripcion.getText().toString();
                String telefono = tbTelefono.getText().toString();

                dc = new GuardarDenuncia(DenunciaAgregarActivity.this, funcion, encodedImage, fecha, tipo, titulo, descripcion, telefono, "0", USUARIO_ID);
            }
        });


        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        tbFecha.setText(sdf.format(c.getTime()));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cargarTipos(){
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tiposDenuncias );
        cbTipoDenuncias.setAdapter(ad);
    }

    @Override
    public void onClick(View v) {
        PickFile();
    }

    private void PickFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(intent.CATEGORY_OPENABLE);
        try{
            startActivityForResult(
                    Intent.createChooser(intent,"Seleccione la imagen"),
                    PICKER);
        }catch (android.content.ActivityNotFoundException ex){
            System.out.println("Error al abrir el Selector :"+ex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICKER:
                if(resultCode == RESULT_OK){
                    foto = "foto";
                    Bitmap photobmp;
                    Uri selectedImageUri = data.getData();
                    String dataFU = getRealPathFromURI(selectedImageUri);
                    photobmp = BitmapFactory.decodeFile(dataFU);
                    img.setImageBitmap(photobmp);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    photobmp.compress(Bitmap.CompressFormat.PNG, 100,baos);
                    photobmp.compress(Bitmap.CompressFormat.JPEG, 50,baos);
                    byte[] imageBytes = baos.toByteArray();
                    encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                }
                break;
        }
    }
    public String getRealPathFromURI(Uri contentUri){

        Cursor cursor = null;
        try{
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getApplicationContext().getContentResolver().query(contentUri, proj, null, null, null);
            int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(colum_index);
        }finally{
            if(cursor != null){
                cursor.close();
            }
        }
    }


}
