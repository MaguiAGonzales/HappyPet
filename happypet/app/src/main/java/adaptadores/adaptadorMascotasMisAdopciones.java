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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import clases.AdopcionMascota;
import clases.Mascota;

public class adaptadorMascotasMisAdopciones extends ArrayAdapter<AdopcionMascota> {
    public adaptadorMascotasMisAdopciones(Context context, List<AdopcionMascota> objects) {
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
            v = inflater.inflate(R.layout.activity_mis_adopciones_item,parent,false);
        }

        //Obteniendo instancias de los elementos
        TextView nombre = (TextView)v.findViewById(R.id.tvMisAdopciones_NombreMascota);
        TextView tipo = (TextView)v.findViewById(R.id.tvMisAdopciones_TipoMascota);
        TextView fecha = (TextView)v.findViewById(R.id.tvMisAdopciones_Fecha);
        TextView estado = (TextView)v.findViewById(R.id.tvMisAdopciones_Estado);
        ImageView imagen = (ImageView)v.findViewById(R.id.ivMisAdopciones_MascotaMini);

        //Obteniendo instancia de la Tarea en la posición actual
        final AdopcionMascota item = getItem(position);

        nombre.setText(item.getNombre());
        tipo.setText(item.getTipo());

//        String dtStart = "2010-10-15 09:27:37";

        String fechaAdo = item.getFecha();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        try {
            Date fechaInicio = format.parse(fechaAdo);
            format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            fechaAdo = format.format(fechaInicio);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fecha.setText(fechaAdo);

        String estadoLabel = "";
        switch (item.getEstado()){
            case "F1":
                estadoLabel = "PENDIENTE : Fase 1";
                break;
            case "F2":
                estadoLabel = "PENDIENTE : Fase 2";
                break;
            case "F3":
                estadoLabel = "PENDIENTE : Fase 3";
                break;
            case "TE":
                estadoLabel = "COMPLETO";
                break;
            case "NP":
                estadoLabel = "NO PASÓ";
                break;
        }
        estado.setText(estadoLabel);

        byte[] decodedString = Base64.decode(item.getImagen(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imagen.setImageBitmap(decodedByte);

        //Devolver al ListView la fila creada
        return v;

    }
}
