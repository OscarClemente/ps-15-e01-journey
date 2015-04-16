package bamboo_software.journey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

/**
 *
 */
public class SearchActivity extends ActionBarActivity {

    private EditText editDestino;
    private EditText editDuracion;
    private EditText editPrecio;
    private RatingBar editValoracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editDestino = (EditText) findViewById(R.id.search_destino);
        editDuracion = (EditText) findViewById(R.id.search_duracion);
        editPrecio = (EditText) findViewById(R.id.search_precio);
        editValoracion = (RatingBar) findViewById(R.id.search_valoracion);

        Button botonBuscar = (Button) findViewById(R.id.search_boton_buscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent extras = new Intent();
                extras.putExtra(MainActivity.SEARCH_DESTINO, editDestino.getText().toString());
                extras.putExtra(MainActivity.SEARCH_DURACION, editDuracion.getText().toString());
                extras.putExtra(MainActivity.SEARCH_PRECIO, editPrecio.getText().toString());
                extras.putExtra(MainActivity.SEARCH_VALORACION, editValoracion.getRating());
                setResult(RESULT_OK, extras);
                finish();
            }

        });
    }
}
