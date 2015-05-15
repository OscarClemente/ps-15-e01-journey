package bamboo_software.journey;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import static bamboo_software.journey.AdaptadorRecycleView.decodeSampledBitmapFromFile;


public class PaqueteActivity extends ActionBarActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CardData> cards;
    private TextView nombre;
    private TextView duracion;
    private TextView descripcion;
    private TextView precio;
    private RatingBar nota;
    ImageView imageViewIcon;

    private Long clave;

    private AdaptadorPaquetes db;
    static View.OnClickListener myOnClickListener;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new AdaptadorPaquetes(this);
        db.open();

        setContentView(R.layout.paquete);
        setTitle(R.string.paquete);

        nombre = (TextView) findViewById(R.id.texto_nombre);	//nuevo
        duracion = (TextView) findViewById(R.id.texto_duracion);
        descripcion = (TextView) findViewById(R.id.texto_descripcion);
        nota = (RatingBar) findViewById(R.id.nota);
        Drawable estrella = nota.getProgressDrawable();
        nota.setClickable(false);
        nota.setFocusable(false);
        estrella.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        precio = (TextView) findViewById(R.id.texto_precio);
        imageViewIcon = (ImageView) findViewById(R.id.vista_foto_paquete);
        Button botonCompra = (Button) findViewById(R.id.comprar);



        clave = getIntent().getExtras().getLong("clave");

        /*clave = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(AdaptadorPaquetes.KEY_ROWID);
        if (clave == null) {
            Bundle extras = getIntent().getExtras();
            clave = extras != null ? extras.getLong(AdaptadorPaquetes.KEY_ROWID)
                    : null;
        }*/

        populateFields();
        final Context c = getBaseContext();
        botonCompra.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(c, DatosCompraActivity.class);
                i.putExtra("clave", clave);
                startActivity(i);
            }

        });

    }

    private void populateFields() {
            Cursor cur = db.listarPaquete(clave);
            startManagingCursor(cur);
            nombre.setText(cur.getString(cur.getColumnIndex(db.KEY_NOMBRE)));
            duracion.setText("" + cur.getInt(cur.getColumnIndex(db.KEY_DURACION)));
            descripcion.setText(cur.getString(cur.getColumnIndex(db.KEY_DESCRIPCION)));
            nota.setRating(cur.getInt(cur.getColumnIndex(db.KEY_CALIFICACION)));
            precio.setText(Integer.toString(cur.getInt(cur.getColumnIndex(db.KEY_PRECIO))));
            Bitmap imagen = decodeSampledBitmapFromFile((cur.getString(cur.getColumnIndex(db.KEY_IMAGEN))), 200, 200);
            imageViewIcon.setImageBitmap(imagen);
    }




    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}