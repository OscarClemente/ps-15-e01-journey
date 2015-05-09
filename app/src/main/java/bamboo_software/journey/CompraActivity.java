package bamboo_software.journey;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

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
        //Faltan cosas posiblemente
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);  //Rellenar xml

        adCompras.open();
        listarPaquetes();
    }

    /**
     * Obtiene de la base de datos un listado con todas las compras que
     * ha realizado el usuario, y lo muestra por pantalla.
     */
    private void listarPaquetes() {
        String correo = getUsuario();
        /* Comprueba que se ha almacenado el correo del usuario */
        if (correo != null) {
            Cursor compras = adCompras.listarCompra(correo);
            //Mostrar compras a partir del cursor
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
        switch(item.getItemId()) {
            case VER_INFO:
                //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                //Llamar a PaqueteActivity
                return true;
            case REENVIAR:
                //Llamar al modulo de mail
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
