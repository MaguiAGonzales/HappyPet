package adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.happypet.movil.happypet.R;

import java.util.List;

import clases.Denuncia;
import clases.Extraviado;
import fragmentos.AlertasFragment;
import fragmentos.DenunciasFragment;

/**
 * Created by Farly on 14/07/2017.
 */

public class adaptadorExtraviados extends ArrayAdapter<Extraviado> {

    private Context mContext;
    private AlertasFragment fragment;

    public adaptadorExtraviados(Context context, List<Extraviado> objects, AlertasFragment fragment) {
        super(context, 0, objects);
        this.mContext=context;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = convertView;

        if (null == convertView) {
            v = inflater.inflate(R.layout.activity_alerta_item,parent,false);
        }

        TextView titulo = (TextView)v.findViewById(R.id.tvExtraviadoItem_titulo);
        TextView fecha = (TextView)v.findViewById(R.id.tvExtraviadoItem_fecha);
        TextView estado = (TextView)v.findViewById(R.id.tvExtraviadoItem_estado);
        TextView detalle = (TextView)v.findViewById(R.id.tvExtraviadoItem_detalle);
        TextView telefono = (TextView)v.findViewById(R.id.tvExtraviadoItem_telefono);
        ImageView imagen = (ImageView)v.findViewById(R.id.ivExtraviadoItem_foto);

        final Extraviado item = getItem(position);

        titulo.setText( "Se Busca : " + item.getNombre());
        fecha.setText(item.getFecha());
        estado.setText(item.getEncontrado().equals("0") ? "Nuevo": "Atendido");
        detalle.setText(item.getDetalle());
        telefono.setText(item.getCelular());

        byte[] decodedString = Base64.decode(item.getImagen(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imagen.setImageBitmap(decodedByte);

        ImageButton btnLlamar = (ImageButton) v.findViewById(R.id.btnExtraviadoItem_llamar);

        if(item.getCelular().equals("")){
            btnLlamar.setVisibility(View.GONE);
        }else{
            btnLlamar.setVisibility(View.VISIBLE);
        }

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.llamar(item.getCelular());
            }
        });

        //Devolver al ListView la fila creada
        return v;

    }

}