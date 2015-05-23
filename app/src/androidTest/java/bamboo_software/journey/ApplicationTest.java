package bamboo_software.journey;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.*;

import com.robotium.solo.Solo;

public class ApplicationTest extends
        ActivityInstrumentationTestCase2<InicioActivity> {
    private static final String NOMBRE_DEL_TEST = "Robotium";
    private static final String TITULO_DE_PRUEBA = "Prueba";
    private static final String CUERPO_DE_PRUEBA = NOMBRE_DEL_TEST + " Body ";

    private Solo solo;

    public ApplicationTest() {
        super(InicioActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        // setUp() is run before a test case is started.
        // This is where the solo object is created.
        solo = new Solo(super.getInstrumentation(), super.getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        // tearDown() is run after a test case has finished.
        // finishOpenedActivities() will finish all the activities that have
        // been opened during the test execution.
        solo.finishOpenedActivities();
    }

    public void testAAcrearPaquete() {
        solo.goBackToActivity("InicioActivity");
        solo.enterText(0, "admin");
        solo.enterText(1, "bamboo-software");
        solo.clickOnButton("INICIAR SESIÓN");
        solo.assertCurrentActivity("Esperada la actividad InicioPaquetes",
                "InicioPaquetes");
        View b = solo.getView(R.id.fab);
        solo.clickOnView(b);
        solo.assertCurrentActivity("Esperada la actividad EditarPaquetes",
                "EditarPaquetes");
        solo.enterText(1, "robotium");
        solo.enterText(2, "robotium");
        solo.enterText(3, "30");
        solo.enterText(4, "30");
        solo.enterText(5, "30");
        solo.enterText(7, "30");
        Button c = (Button) solo.getView(R.id.confirm);
        solo.clickOnView(c);
        solo.goBackToActivity("InicioActivity");
    }

    public void testAregistrarse() {
        solo.clickOnButton("REGISTRARSE");
        solo.assertCurrentActivity("Esperada la actividad RegistroActivity",
                "RegistroActivity");
        solo.enterText(0, "robotium@gmail.com");
        solo.enterText(1, "robotium");
        solo.enterText(2, "robotium");
        solo.enterText(3, "robotium");
        solo.enterText(4, "Avenida de robotium");
        solo.enterText(5, "123456789");
        Button a = (Button) solo.getView(R.id.confirmar);
        solo.clickOnView(a);
        solo.goBackToActivity("InicioActivity");
    }

    public void logearse() {
        solo.enterText(0, "robotium");
        solo.enterText(1, "robotium");
        solo.clickOnButton("INICIAR SESIÓN");
        solo.assertCurrentActivity("Esperada la actividad MainActivity",
                "MainActivity");
    }

    public void testcomprar() {
        logearse();
        solo.clickOnText("robotium");
        solo.clickOnButton("COMPRAR");
        solo.enterText(0, "11111111111111111111");
        solo.enterText(1, "1");
        solo.enterText(2, "robotium");
        Button a = (Button) solo.getView(R.id.comprar);
        solo.clickOnView(a);
        solo.assertCurrentActivity("Esperada la actividad PaqueteActivity",
                "PaqueteActivity");
        solo.goBackToActivity("InicioActivity");
    }

    public void testBuscarcompras() {
        logearse();
        View b = solo.getView(R.id.action_filter);
        solo.clickOnView(b);
        solo.enterText(0, "robotium");
        Button a = (Button) solo.getView(R.id.search_boton_buscar);
        solo.clickOnView(a);
        solo.assertCurrentActivity("Esperada la actividad MainActivity",
                "MainActivity");
        solo.goBackToActivity("InicioActivity");
    }

    public void testListarcompras() {
        logearse();
        View b = solo.getView(R.id.compras);
        solo.clickOnView(b);
        solo.clickLongOnText("robotium");
        solo.clickOnText("Ver mas informacion");
        solo.assertCurrentActivity("Esperada la actividad PaqueteActivity",
                "PaqueteActivity");
        solo.goBackToActivity("InicioActivity");
    }

}