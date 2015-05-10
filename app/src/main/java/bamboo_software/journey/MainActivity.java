package bamboo_software.journey;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CardData> cards;
    static View.OnClickListener myOnClickListener;
    private AdaptadorPaquetes adPaquetes = new AdaptadorPaquetes(this);
    int id = 0;

    protected static final String SEARCH_DESTINO = "DESTINO";
    protected static final String SEARCH_DURACION = "DURACION";
    protected static final String SEARCH_PRECIO = "PRECIO";
    protected static final String SEARCH_VALORACION = "VALORACION";
    protected static final int RESULT_LOAD_IMAGE = 1;

    private static final int SEARCH_ACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Crea los objetos que permiten poblar el cardView a partir de los datos de DatosInterfaz

        cards = new ArrayList<CardData>();


        adapter = new AdaptadorRecycleView(cards, getResources());
        recyclerView.setAdapter(adapter);

        adPaquetes.open();

        //SI NO APARECE NADA DESCOMENTA ESTO PARA QUE SE AÑADA A LA BD, SOLO UNA EJECUCION LUEGO COMENTALO O HABRA VARIOS PAQUETES IGUALES
        //SI AL CONTRARIO APARECEN REPETIDOS, BORRA LA BASE DE DATOS
        //adPaquetes.crearPaquete("Teide Enigmático", "Tenerife", 300, 10, 3, "El Teide es un volcán situado en la isla de Tenerife (Islas Canarias, España). Con una altitud de 3718 metros.",R.drawable.india);
        //adPaquetes.crearPaquete("Malvinas de ensueño", "Malvinas", 1000, 10, 3, "Las islas Malvinas (en inglés: Falkland Islands, AFI: [ˈfuckland ˈiland]; del francés iles Malouines",R.drawable.sheychelles);
        //adPaquetes.crearPaquete("Teide Enigmático", "Tenerife", 300, 10, 3, "El Teide es un volcán situado en la isla de Tenerife (Islas Canarias, España). Con una altitud de 3718 metros.",R.drawable.paris);
        //adPaquetes.crearPaquete("Piramides Faraónicas", "Egipto", 9900, 10, 3, "Las pirámides de Egipto son, de todos los vestigios legados por egipcios de la Antigüedad, los más portentosos.",R.drawable.piramides);

       listarPaquetes(null, 0, 0, 0);
    }

    /**
     * Se encarga de lo que ocurre al ser pulsado uno de los CardViews del RecyclerView
     */

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);

            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.cardId);
            String selectedName = (String) textViewName.getText();
            long cardId = Long.parseLong(selectedName);

            Intent paqueteIntent = new Intent(MainActivity.this, PaqueteActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putLong("clave", cardId);
            paqueteIntent.putExtras(mBundle);

            /*adPaquetes.listarPaquete(cardId);

            Cursor cur = adPaquetes.listarPaquete(cardId);
            startManagingCursor(cur);
            System.out.println(cur.getString(cur.getColumnIndex(adPaquetes.KEY_NOMBRE)));

            System.out.println(cardId);*/
            MainActivity.this.startActivity(paqueteIntent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_ACTIVITY && resultCode == RESULT_OK) {
            Log.d("SEARCH","Ha vuelto correctamente");
            Bundle extras = data.getExtras();
            String destino = (String) extras.get(SEARCH_DESTINO);
            int duracion = extras.getInt(SEARCH_DURACION);
            float precio = extras.getFloat(SEARCH_PRECIO);
            float valoracion = extras.getFloat(SEARCH_VALORACION);
            listarPaquetes(destino, duracion, precio, valoracion);
        }
    }

    /**
     * A partir de unos datos parametro, busca en la base de datos y crea lo necesario para
     * mostrar por pantalla
     */

    private void listarPaquetes(String destino, int duracion, float precio, float valoracion) {
        Cursor paquetes;
        if (destino == null && precio == 0 && valoracion == 0 && duracion == 0){
            paquetes = adPaquetes.listarPaquetes();
        }
        else {
            paquetes = adPaquetes.listarPaquetes(destino, duracion, precio, valoracion);
        }
        cards = new ArrayList<CardData>();
        if(paquetes!=null) {
            if (paquetes.moveToFirst()) {
                cards.add(new CardData(
                        paquetes.getString(paquetes.getColumnIndex("nombre")),
                        paquetes.getString(paquetes.getColumnIndex("destino")),
                        paquetes.getString(paquetes.getColumnIndex("imagen")),
                        paquetes.getLong(paquetes.getColumnIndex("_id"))
                ));
                while (paquetes.moveToNext()) {
                    cards.add(new CardData(
                            paquetes.getString(paquetes.getColumnIndex("nombre")),
                            paquetes.getString(paquetes.getColumnIndex("destino")),
                            paquetes.getString(paquetes.getColumnIndex("imagen")),
                            paquetes.getLong(paquetes.getColumnIndex("_id"))
                    ));

                }
            }
        }
        adapter = new AdaptadorRecycleView(cards, getResources());
        recyclerView.setAdapter(adapter);
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        //Controla la accion de los botones del menu superior
        switch (item.getItemId()){
            //boton de filtro-busqueda
            case R.id.action_filter:
                Log.d("SEARCH","Lanzando Search Activity");
                Intent filterIntent = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivityForResult(filterIntent, SEARCH_ACTIVITY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
