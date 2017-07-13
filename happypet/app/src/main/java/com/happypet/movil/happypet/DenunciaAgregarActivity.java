package com.happypet.movil.happypet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

public class DenunciaAgregarActivity extends AppCompatActivity {

    String USUARIO_ID;

    String[] tiposDenuncias = {"TODAS", "MALTRATOS", "ACCIDENTES", "OTROS"};
    private Spinner cbTipoDenuncias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_agregar);
    }
}
