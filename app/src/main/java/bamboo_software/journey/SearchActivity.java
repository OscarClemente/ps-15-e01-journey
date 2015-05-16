package bamboo_software.journey;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

/**
 *
 */
public class SearchActivity extends Activity {

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
        LayerDrawable stars = (LayerDrawable) editValoracion.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FAFAFA"), PorterDuff.Mode.SRC_ATOP);    //FAFAFA?

        Button botonBuscar = (Button) findViewById(R.id.search_boton_buscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                if (!editDestino.getText().toString().equals("")) {
                    intent.putExtra(MainActivity.SEARCH_DESTINO, editDestino.getText().toString());
                }
                if (!editDuracion.getText().toString().equals("")) {
                    intent.putExtra(MainActivity.SEARCH_DURACION,
                            Integer.parseInt(editDuracion.getText().toString()));
                }
                if (!editPrecio.getText().toString().equals("")) {
                    intent.putExtra(MainActivity.SEARCH_PRECIO,
                            Float.parseFloat(editPrecio.getText().toString()));
                }
                intent.putExtra(MainActivity.SEARCH_VALORACION, editValoracion.getRating());
                startActivity(intent);
            }

        });
    }
}
