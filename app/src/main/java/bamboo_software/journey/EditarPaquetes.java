package bamboo_software.journey;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static java.lang.Integer.parseInt;

public class EditarPaquetes extends Activity {

    private EditText mIdText;
    private EditText mNombre;
    private EditText mPrecioText;
    private EditText mDuracionText;
    private EditText mCalificacionText;
    private EditText mDescripcion;

    private Long mRowId;
    private int mPrecio;
    private int mDuracion;
    private int mCalificacion;
    private AdaptadorPaquetes paqueteDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paqueteDbHelper = new AdaptadorPaquetes(this);
        paqueteDbHelper.open();

        /*Cursor catCursor = paqueteDbHelper.listarPaquetes();
        if (catCursor.getCount() <= 0) {
            paqueteDbHelper.crearPaquete("",0,0,0,"");
        }*/

        setContentView(R.layout.editar_paquetes);
        setTitle(R.string.editar_paquetes);

        mIdText = (EditText) findViewById(R.id.ID);
        mNombre = (EditText) findViewById(R.id.nombre);
        mPrecioText = (EditText) findViewById(R.id.precio);
        mDuracionText = (EditText) findViewById(R.id.duracion);
        mCalificacionText = (EditText) findViewById(R.id.calificacion);
        mDescripcion = (EditText) findViewById(R.id.descripcion);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(paqueteDbHelper.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(paqueteDbHelper.KEY_ROWID)
                    : null;
        }

        populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor paquete = paqueteDbHelper.listarPaquete(mRowId);
            //startManagingCursor(category);
            mIdText.setText(paquete.getString(
                    paquete.getColumnIndexOrThrow(paqueteDbHelper.KEY_ROWID)));
            mNombre.setText(paquete.getString(
                    paquete.getColumnIndexOrThrow(paqueteDbHelper.KEY_NOMBRE)));
            mPrecioText.setText(paquete.getString(
                    paquete.getColumnIndexOrThrow(paqueteDbHelper.KEY_PRECIO)));
            mDuracionText.setText(paquete.getString(
                    paquete.getColumnIndexOrThrow(paqueteDbHelper.KEY_DURACION)));
            mCalificacionText.setText(paquete.getString(
                    paquete.getColumnIndexOrThrow(paqueteDbHelper.KEY_CALIFICACION)));
            mDescripcion.setText(paquete.getString(
                    paquete.getColumnIndexOrThrow(paqueteDbHelper.KEY_DESCRIPCION)));
        }
        else {
            mIdText.setText("***");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(paqueteDbHelper.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String nombre = mNombre.getText().toString();
        String Sprecio = mPrecioText.getText().toString();
        int precio = parseInt(Sprecio);
        String Sduracion = mDuracionText.getText().toString();
        int duracion = parseInt(Sduracion);
        String Scalificacion = mCalificacionText.getText().toString();
        int calificacion = parseInt(Scalificacion);
        String descripcion = mDescripcion.getText().toString();

        if (mRowId == null) {
                long id = paqueteDbHelper.crearPaquete(nombre,precio,duracion,calificacion,descripcion);
                if (id > 0) {
                    mRowId = id;
                 }
        }
        else {
                paqueteDbHelper.actualizarPaquete(mRowId,nombre,precio,duracion,calificacion,descripcion);
        }
        paqueteDbHelper.close();
    }

}

