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

        //adPaquetes.crearPaquete("Malvinas de ensueño", "Malvinas", 1000, 10, 3, "Las islas Malvinas5 (en inglés: Falkland Islands, AFI: [ˈfɔːlklənd ˈaɪləndz]; del francés îles Malouines","/res/drawable/sheychelles.jpg");
        //adPaquetes.crearPaquete("Teide Enigmático", "Tenerife", 300, 10, 3, "El Teide es un volcán situado en la isla de Tenerife (Islas Canarias, España). Con una altitud de 3718 metros.","/res/drawable/india.jpg");
        //adPaquetes.crearPaquete("Piramides Faraónicas", "Egipto", 9900, 10, 3, "Las pirámides de Egipto son, de todos los vestigios legados por egipcios de la Antigüedad, los más portentosos.","/res/drawable/piramides.jpg");

        listarPaquetes (null, 0, 0, 0);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_ACTIVITY && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String destino = (String) extras.get(SEARCH_DESTINO);
            int duracion = (int) extras.get(SEARCH_DURACION);
            float precio = (float) extras.get(SEARCH_PRECIO);
            float valoracion = (float) extras.get(SEARCH_VALORACION);
            listarPaquetes(destino, duracion, precio, valoracion);
        }
    }

    private void listarPaquetes(String destino, int duracion, float precio, float valoracion) {
        //Cursor paquetes = adPaquetes.listarPaquetes(destino, duracion, precio, valoracion);
        Cursor paquetes = adPaquetes.listarPaquetes();
        cards = new ArrayList<CardData>();
        if(paquetes.moveToFirst()){
            while(paquetes.moveToNext()){
                cards.add(new CardData(
                        paquetes.getString(paquetes.getColumnIndex("nombre")),
                        paquetes.getString(paquetes.getColumnIndex("destino")),
                        paquetes.getString(paquetes.getColumnIndex("imagen")),
                        id++
                ));
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
                Intent filterIntent = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivity(filterIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
