package bamboo_software.journey;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class InicioPaquetes extends ActionBarActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CardData> cards;
    static View.OnClickListener myOnClickListener;
    private AdaptadorPaquetes adPaquetes = new AdaptadorPaquetes(this);
    int id = 0;
    FloatingActionButton fab;

    protected static final String SEARCH_DESTINO = "DESTINO";
    protected static final String SEARCH_DURACION = "DURACION";
    protected static final String SEARCH_PRECIO = "PRECIO";
    protected static final String SEARCH_VALORACION = "VALORACION";
    protected static final int RESULT_LOAD_IMAGE = 1;

    private static final int SEARCH_ACTIVITY = 0;
    private static final int COMPRA_ACTIVITY = 1;
    private static final int EDITA_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        SpannableString s = new SpannableString("Administrador");
        s.setSpan(new bamboo_software.journey.TypefaceSpan(this, "futura.ttf"),
                0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);

        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Crea los objetos que permiten poblar el cardView a partir de los datos de DatosInterfaz

        cards = new ArrayList<CardData>();


        adapter = new AdaptadorAdmin(cards, getResources());
        recyclerView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(InicioPaquetes.this, EditarPaquetes.class);
                startActivityForResult(i, EDITA_ACTIVITY);
                overridePendingTransition  (R.anim.pull_in_right, R.anim.push_out_left);
            }

        });


        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    CardData cardBorrar = cards.get(position);
                                    adPaquetes.borrarPaquete(cardBorrar.getId());
                                    removeItem(position);

                                }
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    //mItems.remove(position);
                                    //mAdapter.notifyItemRemoved(position);
                                }
                                //mAdapter.notifyDataSetChanged();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);

        adPaquetes.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String destino = (String) extras.get(SEARCH_DESTINO);
            int duracion = extras.getInt(SEARCH_DURACION);
            float precio = extras.getFloat(SEARCH_PRECIO);
            float valoracion = extras.getFloat(SEARCH_VALORACION);
            listarPaquetes(destino, duracion, precio, valoracion);
        } else listarPaquetes(null, 0, 0, 0);
    }

    private void removeItem(int position) {
        cards.remove(position);
        adapter.notifyItemRemoved(position);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        System.out.println("ONACTIVITYRESULT");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String destino = (String) extras.get(SEARCH_DESTINO);
            int duracion = extras.getInt(SEARCH_DURACION);
            float precio = extras.getFloat(SEARCH_PRECIO);
            float valoracion = extras.getFloat(SEARCH_VALORACION);
            listarPaquetes(destino, duracion, precio, valoracion);
        } else listarPaquetes(null, 0, 0, 0);
    }

    /**
     * Se encarga de lo que ocurre al ser pulsado uno de los CardViews del RecyclerView
     */

    private class MyOnClickListener implements View.OnClickListener  {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            System.out.println("ON CLICK");
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);

            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.cardId);
            String selectedName = (String) textViewName.getText();
            long cardId = Long.parseLong(selectedName);


            Intent paqueteIntent = new Intent(InicioPaquetes.this, EditarPaquetes.class);
            //Bundle mBundle = new Bundle();
            paqueteIntent.putExtra(AdaptadorPaquetes.KEY_ROWID, cardId);
            //i.putExtra(AdaptadorPaquetes.KEY_ROWID, id);
            //mBundle.putLong("clave", cardId);
            //paqueteIntent.putExtras(mBundle);

            /*adPaquetes.listarPaquete(cardId);

            Cursor cur = adPaquetes.listarPaquete(cardId);
            startManagingCursor(cur);
            System.out.println(cur.getString(cur.getColumnIndex(adPaquetes.KEY_NOMBRE)));

            System.out.println(cardId);*/

            InicioPaquetes.this.startActivityForResult(paqueteIntent, EDITA_ACTIVITY);

            overridePendingTransition  (R.anim.pull_in_right, R.anim.push_out_left);
        }

    }


    /**
     * A partir de unos datos parametro, busca en la base de datos y crea lo necesario para
     * mostrar por pantalla
     */

    private void listarPaquetes(String destino, int duracion, float precio, float valoracion) {
        Cursor paquetes;
        if (destino == null && precio == 0 && valoracion == 0 && duracion == 0) {
            paquetes = adPaquetes.listarPaquetes();
        } else {
            paquetes = adPaquetes.listarPaquetes(destino, duracion, precio, valoracion);
        }
        cards = new ArrayList<CardData>();
        if (paquetes != null) {
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
        adapter = new AdaptadorAdmin(cards, getResources());
        recyclerView.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Controla la accion de los botones del menu superior
        switch (item.getItemId()){
            //boton de filtro-busqueda
            case R.id.action_filter:
                Log.d("SEARCH", "Lanzando Search Activity");
                Intent filterIntent = new Intent(this, SearchActivity.class);
                this.startActivity(filterIntent);
                return true;
            case R.id.compras:
                Intent comprasIntent = new Intent(this, CompraActivity.class);
                this.startActivityForResult(comprasIntent, COMPRA_ACTIVITY);
                return true;
            case R.id.logout:
                Intent inicioIntent = new Intent(this, InicioActivity.class);
                inicioIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(inicioIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

/*public class InicioPaquetes extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERTAR_PAQUETE = Menu.FIRST;
    private static final int BORRAR_PAQUETE = Menu.FIRST + 1;
    private static final int LISTAR_PAQUETES = Menu.FIRST + 2;

    private AdaptadorPaquetes mDbHelper;
    private int positionView = 0;

    /** Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.lista_paquetes);
        setTitle(R.string.MenuAdministrador);

        Button botonRegistrar = (Button) findViewById(R.id.registrar);



        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(InicioPaquetes.this, EditarPaquetes.class);
                InicioPaquetes.this.startActivityForResult(i, ACTIVITY_CREATE);
            }

        });
        super.onCreate(savedInstanceState);

        mDbHelper = new AdaptadorPaquetes(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
        this.setSelection(positionView);
    }

    private void fillData() {
        Cursor cursorPaquetes = mDbHelper.listarPaquetes();
        //startManagingCursor(notesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{AdaptadorPaquetes.KEY_NOMBRE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter paquetes =
                new SimpleCursorAdapter(this, R.layout.fila_paquetes, cursorPaquetes, from, to, 1);
        setListAdapter(paquetes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERTAR_PAQUETE, 0, R.string.menu_insertar);
        menu.add(0, LISTAR_PAQUETES, 0, R.string.menu_listar_paquetes);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERTAR_PAQUETE:
                crearPaquete();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, BORRAR_PAQUETE, 0, R.string.menu_borrar);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case BORRAR_PAQUETE:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.borrarPaquete(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void crearPaquete() {
        Intent i = new Intent(this, EditarPaquetes.class);
        startActivityForResult(i, ACTIVITY_CREATE);
        positionView = this.getListView().getCount();
    }

    private void listarPaquetes() {
        fillData();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, EditarPaquetes.class);
        i.putExtra(AdaptadorPaquetes.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
        positionView = position;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
        this.setSelection(positionView);
    }
}*/