package bamboo_software.journey;

import android.content.Context;
import android.content.Intent;
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
        //long id4 = db.crearPaquete("islas malvinas", 1000, 10, 5, "mu bonitooooo");
        //long id2 = db.crearPaquete("islas CANARIAS", 1000, 10, 5, "muCHISMO bonitooooo");
        //long id3 = db.crearPaquete("egipto enigmatico", 1000, 10, 3, "mu bonitooooo");
        Intent i = new Intent(this, PaqueteActivity.class);
        long id = 6;
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

}