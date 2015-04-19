package bamboo_software.journey;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class InicioPaquetes extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERTAR_PAQUETE = Menu.FIRST;
    private static final int BORRAR_PAQUETE = Menu.FIRST + 1;
    private static final int LISTAR_PAQUETES = Menu.FIRST + 2;

    private AdaptadorPaquetes mDbHelper;
    private int positionView = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_paquetes);
        mDbHelper = new AdaptadorPaquetes(this);
        mDbHelper.open();
        mDbHelper.crearPaquete("islas malvinas", 1000, 10, 5, "mu bonitooooo");
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
}