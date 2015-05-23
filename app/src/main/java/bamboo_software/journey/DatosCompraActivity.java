package bamboo_software.journey;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Daniel on 05/05/2015.
 */
public class DatosCompraActivity extends ActionBarActivity {

    private EditText mTarjeta;
    private EditText mPersonas;
    private EditText mPagador;

    private static final String USUARIO = "CorreoUsuario";
    private AdaptadorUsuarios usuarioDbHelper;
    private AdaptadorCompras compraDbHelper;
    private AdaptadorPaquetes paqueteDbHelper;

    private long clave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_compra);

        compraDbHelper = new AdaptadorCompras(this);
        compraDbHelper.open();
        usuarioDbHelper = new AdaptadorUsuarios(this);
        usuarioDbHelper.open();
        paqueteDbHelper = new AdaptadorPaquetes(this);
        paqueteDbHelper.open();

        SpannableString s = new SpannableString("Introduce estos datos");
        s.setSpan(new bamboo_software.journey.TypefaceSpan(this, "futura.ttf"),
                0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);

        mTarjeta = (EditText) findViewById(R.id.tarjeta);
        mPersonas = (EditText) findViewById(R.id.personas);
        mPagador = (EditText) findViewById(R.id.pagador);


        clave = getIntent().getExtras().getLong("clave");

        Button comprar = (Button) findViewById(R.id.comprar);
        comprar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Se obtiene el nombre del paquete y el numero de personas */
                Cursor crs = paqueteDbHelper.listarPaquete(clave);
                crs.moveToFirst();
                String pers = mPersonas.getText().toString();
                int personas;
                if(!pers.equals("")) {
                    personas = Integer.parseInt(pers);
                } else {
                    personas = 0;
                }
                /* Se obtiene la fecha actual */
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String fecha = df.format(c.getTime());

                /* Se crea la compra en la base de datos*/
                String titulo = crs.getString(crs.getColumnIndex("nombre"));
                compraDbHelper.crearCompra(clave, titulo, getUsuario(), fecha, personas);

                /* Envia el mail */
                Mail mail = new Mail(DatosCompraActivity.this, titulo, personas, fecha);
                mail.enviar(getUsuario());

                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override protected void onPause() {
        super.onPause();
    }


    private String getUsuario() {
        SharedPreferences settings = getSharedPreferences(USUARIO, 0);
        String correo = settings.getString("correo", null);
        return correo;
    }
}
