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
import com.squareup.picasso.Picasso;

import java.util.List;

import clases.Mascota;

/**
 * Created by Farly on 18/06/2017.
 */

public class adaptadorMascotas extends ArrayAdapter<Mascota> {

    public adaptadorMascotas(Context context, List<Mascota> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View v = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            v = inflater.inflate(R.layout.activity_mascota_item,parent,false);
        }

        //Obteniendo instancias de los elementos
        TextView nombre = (TextView)v.findViewById(R.id.tvNombreMascotaItem);
        TextView tipo = (TextView)v.findViewById(R.id.tvTipoMascota);
        TextView sexo = (TextView)v.findViewById(R.id.tvSexoMascota);
        TextView anio = (TextView)v.findViewById(R.id.tvAnioMascota);
        ImageView imagen = (ImageView)v.findViewById(R.id.ivMascotaMini);

        //Obteniendo instancia de la Tarea en la posición actual
        final Mascota item = getItem(position);

//        System.out.println("----------- NOMBRE ---------->     " + item.getNombre());
        nombre.setText(item.getNombre());
        tipo.setText(item.getTipo());
        sexo.setText(item.getSexo());
        anio.setText(item.getAnio());

        byte[] decodedString = Base64.decode(item.getImagen(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imagen.setImageBitmap(decodedByte);

        //Devolver al ListView la fila creada
        return v;

    }


}
