package adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.happypet.movil.happypet.R;

import java.util.List;

import clases.Denuncia;


public class adaptadorMisDenuncias extends ArrayAdapter<Denuncia> {

    public adaptadorMisDenuncias(Context context, List<Denuncia> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = convertView;

        if (null == convertView) {
            v = inflater.inflate(R.layout.activity_midenuncia_item,parent,false);
        }

        TextView titulo = (TextView)v.findViewById(R.id.tvMiDenunciaItem_titulo);
        TextView fecha = (TextView)v.findViewById(R.id.tvMiDenunciaItem_fecha);
        TextView estado = (TextView)v.findViewById(R.id.tvMiDenunciaItem_estado);
        TextView telefono = (TextView)v.findViewById(R.id.tvMiDenunciaItem_telefono);
        TextView descripcion = (TextView)v.findViewById(R.id.tvMiDenunciaItem_descripcion);
        ImageView imagen = (ImageView)v.findViewById(R.id.ivMiDenunciaItem_foto);

        final Denuncia item = getItem(position);

        titulo.setText(item.getTitulo());
        fecha.setText(item.getFecha());
        estado.setText(item.getEstado() ? "Atendido": "Nuevo");
        telefono.setText(item.getTelefono());
        descripcion.setText(item.getDescripcion());

        byte[] decodedString = Base64.decode(item.getFoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imagen.setImageBitmap(decodedByte);

        //Devolver al ListView la fila creada
        return v;

    }

}
