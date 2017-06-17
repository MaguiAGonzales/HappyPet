package com.happypet.movil.happypet;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import clases.DataConnection;

public class MascotaAgregarActivity extends AppCompatActivity implements View.OnClickListener {
    String[] tipos = {"Perro", "Gato"};
    private Spinner cbTipo;

    String[] sexos = {"Hembra", "Macho"};
    private Spinner cbSexo;

    ImageView img;
    private final int PICKER = 1;
    String encodedImage, foto, funcion;
    DataConnection dc;

    private EditText tbNombre, tbAnio, tbParti ,tbSalud;
    private RadioButton rbAdoptadoSI;

    static MascotaAgregarActivity me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota_agregar);

        img = (ImageView) findViewById(R.id.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickFile();
            }
        });

        tbNombre = (EditText) findViewById(R.id.etNombreMascota);
        tbNombre.requestFocus();

        cbTipo = (Spinner) findViewById(R.id.sTipoMascota);
        cargarTipos();

        cbSexo = (Spinner) findViewById(R.id.sSexoMascota);
        cargarSexos();

        tbAnio = (EditText) findViewById(R.id.etFechaNacimiento);
        tbParti = (EditText) findViewById(R.id.etParticularidadMacota);
        tbSalud = (EditText) findViewById(R.id.etSaludMascota);
        rbAdoptadoSI = (RadioButton) findViewById(R.id.rbAdoptadoSi);

        me = this;

        Button btnEnviar = (Button) findViewById(R.id.btnRegistrarMascota);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcion = "setImage";
                String nombre = tbNombre.getText().toString();
                String tipo = cbTipo.getSelectedItem().toString();
                String sexo = cbSexo.getSelectedItem().toString();
                String anio = tbAnio.getText().toString();
                String parti = tbParti.getText().toString();
                String salud = tbSalud.getText().toString();
                String adoptado = rbAdoptadoSI.isChecked() ? "1":"0";

                dc = new DataConnection(MascotaAgregarActivity.this, funcion, encodedImage, nombre, tipo, sexo, anio, parti, salud, adoptado);

                me.finish();
            }
        });

    }
    public void cargarTipos(){
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipos );
        cbTipo.setAdapter(ad);
    }

    public void cargarSexos(){
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sexos );
        cbSexo.setAdapter(ad);
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
