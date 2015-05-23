package bamboo_software.journey;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private EditText mDestino;
    private EditText mImagenText;

    private Long mRowId;
    private int mPrecio;
    private int mDuracion;
    private int mCalificacion;
    private AdaptadorPaquetes paqueteDbHelper;
    private boolean nuevaImagen;

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paqueteDbHelper = new AdaptadorPaquetes(this);
        paqueteDbHelper.open();

        /*Cursor catCursor = paqueteDbHelper.listarPaquetes();
        if (catCursor.getCount() <= 0) {
            paqueteDbHelper.crearPaquete("",0,0,0,"");
        }*/

        nuevaImagen = false;
        setContentView(R.layout.editar_paquetes);
        setTitle(R.string.editar_paquetes);

        mIdText = (EditText) findViewById(R.id.ID);
        mNombre = (EditText) findViewById(R.id.nombre);
        mPrecioText = (EditText) findViewById(R.id.precio);
        mDuracionText = (EditText) findViewById(R.id.duracion);
        mCalificacionText = (EditText) findViewById(R.id.calificacion);
        mDescripcion = (EditText) findViewById(R.id.descripcion);
        mDestino = (EditText) findViewById(R.id.destino);
        mImagenText = (EditText) findViewById(R.id.textImagen);

        Button botonImagen = (Button) findViewById(R.id.imagen);

        botonImagen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }

        });

        //mImagenText = (EditText) findViewById(R.id.imagen);

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
                guardarPaquete();
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
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
            mDestino.setText(paquete.getString(
                    paquete.getColumnIndexOrThrow(paqueteDbHelper.KEY_DESTINO)));
            if(!nuevaImagen){
                mImagenText.setText(paquete.getString(
                        paquete.getColumnIndexOrThrow(paqueteDbHelper.KEY_IMAGEN)));
            }
        }
        else {
            mIdText.setText("***");
        }

    }

    /*@Override
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
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            nuevaImagen = true;

            mImagenText.setText(picturePath);
        }
    }

    private void guardarPaquete() {
        String nombre = mNombre.getText().toString();
        String Sprecio = mPrecioText.getText().toString();
        int precio = 0;
        if (!mPrecioText.getText().toString().equals("")) {
            precio = parseInt(Sprecio);
        }
        String Sduracion = mDuracionText.getText().toString();
        int duracion = 0;
        System.out.println(mDuracionText.getText().toString());
        if (!mDuracionText.getText().toString().equals("")) {
            duracion = parseInt(Sduracion);
        }
        String Scalificacion = mCalificacionText.getText().toString();
        int calificacion = 0;
        if (!mCalificacionText.getText().toString().equals("")) {
            calificacion = parseInt(Scalificacion);
        }
        String descripcion = mDescripcion.getText().toString();
        String destino = mDestino.getText().toString();
        //String Simagen = mCalificacionText.getText().toString();
        //int imagen = parseInt(Scalificacion);
        //int imagen = R.drawable.zaragoza1;
        String imagen = mImagenText.getText().toString();

        System.out.println("GUARDANDO EN BD IMAGE PATH:" + imagen);

        if (mRowId == null) {
            //if (nombre!=null && destino!=null && Sprecio!=null && Sduracion!=null && Scalificacion!=null) {
                long id = paqueteDbHelper.crearPaquete(nombre, destino, precio, duracion, calificacion, descripcion, imagen);
                if (id > 0) {
                    mRowId = id;
                }
            //}
        }
        else {
                paqueteDbHelper.actualizarPaquete(mRowId,nombre,destino,precio,duracion,calificacion,descripcion,imagen);
        }
        //paqueteDbHelper.close();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

}

