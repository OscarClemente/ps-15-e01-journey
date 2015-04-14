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

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CardData> cards;
    static View.OnClickListener myOnClickListener;
    private AdaptadorPaquetes adPaquetes;
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
        for (int i = 0; i < DatosInterfaz.nameArray.length; i++) {
            cards.add(new CardData(
                    DatosInterfaz.nameArray[i],
                    DatosInterfaz.infoArray[i],
                    "/res/drawable/android.png",
                    DatosInterfaz.id_[i]
            ));
        }


        adapter = new AdaptadorRecycleView(cards, getResources());
        recyclerView.setAdapter(adapter);

        AdaptadorPaquetes db = new AdaptadorPaquetes(this);
        db.open();
        long id4 = db.crearPaquete("Malvinas de ensueño", "Malvinas", 1000, 10, 3, "Las islas Malvinas5 (en inglés: Falkland Islands, AFI: [ˈfɔːlklənd ˈaɪləndz]; del francés îles Malouines","/res/drawable/android.png");
        long id2 = db.crearPaquete("Teide Enigmático", "Tenerife", 300, 10, 3, "El Teide es un volcán situado en la isla de Tenerife (Islas Canarias, España). Con una altitud de 3718 metros.","/res/drawable/android.png");
        long id3 = db.crearPaquete("Piramides Faraónicas", "Egipto", 9900, 10, 3, "Las pirámides de Egipto son, de todos los vestigios legados por egipcios de la Antigüedad, los más portentosos.","/res/drawable/android.png");

        Cursor paquetes = adPaquetes.listarPaquetes();

        //PRUEBA
        /*AdaptadorPaquetes db = new AdaptadorPaquetes(this);
        db.open();
        long id4 = db.crearPaquete("Malvinas de ensueño", "Malvinas", 1000, 10, 3, "Las islas Malvinas5 (en inglés: Falkland Islands, AFI: [ˈfɔːlklənd ˈaɪləndz]; del francés îles Malouines","/res/drawable/android.png");
        long id2 = db.crearPaquete("Teide Enigmático", "Tenerife", 300, 10, 3, "El Teide es un volcán situado en la isla de Tenerife (Islas Canarias, España). Con una altitud de 3718 metros.","/res/drawable/android.png");
        long id3 = db.crearPaquete("Piramides Faraónicas", "Egipto", 9900, 10, 3, "Las pirámides de Egipto son, de todos los vestigios legados por egipcios de la Antigüedad, los más portentosos.","/res/drawable/android.png");

        listarPaquetes (null, 0, 0, 0);*/
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
                        //paquetes.getInt(paquetes.getColumnIndex("imagen")),
                        paquetes.getString(paquetes.getColumnIndex("imagen")),
                        id++
                ));
            }
        }
        adapter = new AdaptadorRecycleView(cards, getResources());
        recyclerView.setAdapter(adapter);
    }

}

/*
 private void listarPaquetes(String destino, int duracion, float precio, float valoracion) {
        Cursor paquetes = adPaquetes.listarPaquetes(destino, duracion, precio, valoracion);
        cards = new ArrayList<Paquete>();
        if(paquetes.moveToFirst()){
            while(paquetes.moveToNext()){
                cards.add(new Paquete(
                        id++,
                        paquetes.getString(paquetes.getColumnIndex("nombre")),
                        paquetes.getString(paquetes.getColumnIndex("destino")),
                        paquetes.getInt(paquetes.getColumnIndex("precio")),
                        paquetes.getInt(paquetes.getColumnIndex("duracion")),
                        paquetes.getInt(paquetes.getColumnIndex("calificacion")),
                        paquetes.getString(paquetes.getColumnIndex("descripcion")),
                        paquetes.getString(paquetes.getColumnIndex("imagen"))
                ));
            }
        }
    }
 */