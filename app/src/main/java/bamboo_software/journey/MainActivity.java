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
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CardData> cards;
    static View.OnClickListener myOnClickListener;
    private AdaptadorPaquetes adPaquetes;

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
        for (int i = 0; i < DatosInterfaz.nameArray.length; i++) {
            cards.add(new CardData(
                    DatosInterfaz.nameArray[i],
                    DatosInterfaz.infoArray[i],
                    DatosInterfaz.drawableArray[i],
                    DatosInterfaz.id_[i]
            ));
        }


        adapter = new AdaptadorRecycleView(cards, getResources());
        recyclerView.setAdapter(adapter);

        //PRUEBA
        AdaptadorPaquetes db = new AdaptadorPaquetes(this);
        db.open();
        long id4 = db.crearPaquete("Malvinas de ensueño", "Malvinas", 1000, 10, 3, "Las islas Malvinas5 (en inglés: Falkland Islands, AFI: [ˈfɔːlklənd ˈaɪləndz]; del francés îles Malouines, nombre dado por el explorador Bougainville) es un archipiélago situado en la plataforma continental de América del Sur, dentro del sector de mar epicontinental del océano Atlántico Sur que Argentina denomina mar Argentino. La menor distancia de las islas Malvinas al continente americano se encuentra entre la roca Mintay al sudoeste de la isla Beauchene y el cabo San Juan en la isla de los Estados, a 356,4 km. La principal localidad de las islas Malvinas es Puerto Argentino (Stanley en inglés), ésta está ubicada en la costa oriental de la isla Soledad.");
        long id2 = db.crearPaquete("Teide Enigmático", "Tenerife", 300, 10, 3, "El Teide es un volcán situado en la isla de Tenerife (Islas Canarias, España). Con una altitud de 3718 metros sobre el nivel del mar y 7500 metros sobre el lecho oceánico, es el pico más alto de España, el de cualquier tierra emergida del océano Atlántico y el tercer mayor volcán de la Tierra desde su base en el lecho oceánico, después del Mauna Kea y el Mauna Loa, ambos en la isla de Hawái.1 La altitud del Teide convierte además a la isla de Tenerife en la décima isla más alta de todo el mundo.");
        long id3 = db.crearPaquete("Piramides Faraónicas", "Egipto", 9900, 10, 3, "Las pirámides de Egipto son, de todos los vestigios legados por egipcios de la Antigüedad, los más portentosos y emblemáticos monumentos de esta civilización, y en particular, las tres grandes pirámides de Guiza, las tumbas o cenotafios de los faraones Keops, Kefrén y Micerino, cuya construcción se remonta, para la gran mayoría de estudiosos, al periodo denominado Imperio Antiguo de Egipto. La Gran Pirámide de Guiza, construida por Keops (Jufu), es una de las Siete Maravillas del Mundo Antiguo, además de ser la única que aún perdura.");
        Intent i = new Intent(this, PaqueteActivity.class);
        long id = 2;
        i.putExtra(AdaptadorPaquetes.KEY_ROWID,id);
        startActivityForResult(i, 1);

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
        Cursor paquetes = adPaquetes.listarPaquetes(destino, duracion, precio, valoracion);
        /* FALTA MOSTRAR PAQUETES */
    }

}