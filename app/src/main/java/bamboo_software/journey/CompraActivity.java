package bamboo_software.journey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * La clase CompraActivity se corresponde con la pantalla de la
 * aplicacion en la que se muestra un listado de las compras realizadas
 * por el usuario. Se accede a ella mediante un boton del menu superior
 * de la pantalla de MainActivity.
 */
public class CompraActivity extends ActionBarActivity {

    private static final String USUARIO = "CorreoUsuario";
    private static final int VER_INFO = 0;
    private static final int REENVIAR = 1;

    AdaptadorCompras adCompras = new AdaptadorCompras(this);

    /**
     * Al crear la actividad, se carga en pantalla un listado con las
     * compras realizadas por el usuario.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);



        registerForContextMenu(findViewById(R.id.listaCompra));
        adCompras.open();
        fillData();
    }

    /**
     * Obtiene de la base de datos un listado con todas las compras que
     * ha realizado el usuario, y lo muestra por pantalla.
     */
    private void fillData() {
        String correo = getUsuario();
        /* Comprueba que se ha almacenado el correo del usuario */
        if (correo != null) {
            Cursor cursorCompras = adCompras.listarCompra(correo);
            //Mostrar compras a partir del cursor

            /*Create an array to specify the fields we want to display in the
              list ( Id, correo, fecha y personas)*/
            String[] from = new String[]{AdaptadorCompras.KEY_ID,AdaptadorCompras.KEY_NOMBRE,AdaptadorCompras.KEY_FECHA,
                    AdaptadorCompras.KEY_PERSONAS};

            // and an array of the fields we want to bind those fields to (in this case just text1)
            int[] to = new int[]{R.id.textID,R.id.textTitulo,R.id.textFecha,R.id.textPersonas};

            // Now create a simple cursor adapter and set it to display
            SimpleCursorAdapter compras =
                    new SimpleCursorAdapter(this, R.layout.fila_compras, cursorCompras, from, to, 1);

            ListView listView = (ListView) findViewById(R.id.listaCompra);
            listView.setAdapter(compras);
            listView.setEmptyView(findViewById(R.id.android_empty));
        }
    }

    /**
     * Declara los botones del menu contextual de la actividad.
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, VER_INFO, 0, R.string.ver_info);
        menu.add(0, REENVIAR, 1, R.string.reenviar);
    }

    /**
     * Establece el comportamiento de los botones del menu contextual de
     * la actividad.
     *
     * @param item
     * @return true, excepto en caso de error
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textViewName;
        String selectedName;
        long id;
        switch(item.getItemId()) {
            case VER_INFO:
                //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                /* Obtiene la id del paquete */
                textViewName = (TextView) findViewById(R.id.textID);
                selectedName = (String) textViewName.getText();
                id = Long.parseLong(selectedName);

                Intent paqueteIntent = new Intent(this, PaqueteActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putLong("clave", id);
                paqueteIntent.putExtras(mBundle);
                this.startActivity(paqueteIntent);
                return true;
            case REENVIAR:
                /* Obtiene la id del paquete */
                textViewName = (TextView) findViewById(R.id.textID);
                selectedName = (String) textViewName.getText();
                id = Long.parseLong(selectedName);

                /* Se obtiene la compra de la base de datos */
                Cursor crs = adCompras.listarCompra(getUsuario(), id);
                crs.moveToFirst();
                String titulo = crs.getString(crs.getColumnIndex("nombre"));
                int personas = Integer.parseInt(crs.getString(crs.getColumnIndex("personas")));
                String fecha = crs.getString(crs.getColumnIndex("fecha"));

                /* Envia el mail */
                Mail mail = new Mail(CompraActivity.this, titulo, personas, fecha);
                mail.enviar(getUsuario());

                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Devuelve el correo del usuario que esta utilizando la aplicacion, que se ha
     * almacenado en SharedPreferences en el momento de registrarse o hacer login.
     *
     * @return el correo del usuario que esta usando la aplicacion, o null en caso
     * de error.
     */
    private String getUsuario() {
        SharedPreferences settings = getSharedPreferences(USUARIO, 0);
        String correo = settings.getString("correo", null);
        return correo;
    }
}
