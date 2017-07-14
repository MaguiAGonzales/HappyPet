package adaptadores;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.happypet.movil.happypet.R;

import java.util.List;

import clases.Denuncia;
import fragmentos.DenunciasFragment;


public class adaptadorDenuncias extends ArrayAdapter<Denuncia> {

    private Context mContext;
    private DenunciasFragment fragment;

    public adaptadorDenuncias(Context context, List<Denuncia> objects,  DenunciasFragment fragment) {
        super(context, 0, objects);
        this.mContext=context;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = convertView;

        if (null == convertView) {
            v = inflater.inflate(R.layout.activity_denuncia_item,parent,false);
        }

        TextView titulo = (TextView)v.findViewById(R.id.tvDenunciaItem_titulo);
        TextView fecha = (TextView)v.findViewById(R.id.tvDenunciaItem_fecha);
        TextView estado = (TextView)v.findViewById(R.id.tvDenunciaItem_estado);
        TextView telefono = (TextView)v.findViewById(R.id.tvDenunciaItem_telefono);
        TextView descripcion = (TextView)v.findViewById(R.id.tvDenunciaItem_descripcion);
        ImageView imagen = (ImageView)v.findViewById(R.id.ivDenunciaItem_foto);

        final Denuncia item = getItem(position);

        titulo.setText(item.getTitulo());
        fecha.setText(item.getFecha());
        estado.setText(item.getEstado() ? "Nuevo": "Atendido");
        telefono.setText(item.getTelefono());
        descripcion.setText(item.getDescripcion());

        byte[] decodedString = Base64.decode(item.getFoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imagen.setImageBitmap(decodedByte);

        ImageButton btnLlamar = (ImageButton) v.findViewById(R.id.btnDenunciaItem_llamar);

        if(item.getTelefono().equals("")){
            btnLlamar.setVisibility(View.GONE);
        }else{
            btnLlamar.setVisibility(View.VISIBLE);
        }

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.llamar(item.getTelefono());
            }
        });

        //Devolver al ListView la fila creada
        return v;

    }

}
