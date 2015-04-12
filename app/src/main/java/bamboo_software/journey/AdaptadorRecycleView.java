package bamboo_software.journey;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Oscar on 5/04/15.
 */
public class AdaptadorRecycleView extends RecyclerView.Adapter<AdaptadorRecycleView.MyViewHolder>{

    private ArrayList<CardData> cardDataSet;
    private Resources resource;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewInfo;
        ImageView imageViewIcon;

        //Asocia variables de la clase a cada elemento de los cardViews

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewInfo = (TextView) itemView.findViewById(R.id.textViewInfo);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
        }


    }

    public AdaptadorRecycleView(ArrayList<CardData> cards, Resources resource) {
        this.cardDataSet = cards;
        this.resource = resource;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    /**
     * Prepara los datos que han sido requeridos
     */

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewInfo = holder.textViewInfo;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(cardDataSet.get(listPosition).getName());
        textViewInfo.setText(cardDataSet.get(listPosition).getInfo());
        //imageView.setImageResource(cardDataSet.get(listPosition).getImage());
        imageView.setImageBitmap(decodeSampledBitmapFromResource(resource, cardDataSet.get(listPosition).getImage(), 200, 150));
    }

    @Override
    public int getItemCount() {
        return cardDataSet.size();
    }

    /**
     * Devuelve el numero de veces que hay que reducir la imagen para evitar desbordamientos de memoria de android
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Tamaño original de la imagen
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Transforma una imagen original en una version reducida en bitmap pequeño para evitar desbordamiento de memoria y la devuelve
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // Preparar para testear tamaño de la imagen
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate reduccion de la imagen
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decodificar la imagen
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}

