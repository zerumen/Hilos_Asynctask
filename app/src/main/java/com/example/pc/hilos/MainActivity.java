package com.example.pc.hilos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    EditText entrada;
    TextView salida;
    class MiThread extends Thread {
        private int n, res;

        public MiThread(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            res = factorial(n);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("2 - " + n);
                    System.out.println("Res - " + res);
                    salida.append(n + "! = ");
                    salida.append(res + "\n");

                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrada=(EditText)findViewById(R.id.editText);
        salida=(TextView)findViewById(R.id.salida);
    }

    public void calcularOperacion(View view){
        int n=Integer.parseInt(entrada.getText().toString());
        salida.setText("");
       MiTarea tarea=new MiTarea();
        tarea.execute(n);
    }
    public int factorial(int n){
        int res=1;
        for(int i=1;i<n;i++){
            res*=i;
            SystemClock.sleep(1000);
        }
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class MiTarea extends AsyncTask<Integer,Integer,Integer>{
        private ProgressDialog progreso;
        @Override protected  void onPreExecute(){
            progreso=new ProgressDialog(MainActivity.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progreso.setMessage("Calculando");
            progreso.setCancelable(true);
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MiTarea.this.cancel(true);
                }
            });
            progreso.setMax(100);
            progreso.setProgress(0);
            progreso.show();
        }
        @Override protected Integer doInBackground(Integer...n){
            int res=1;
            for (int i = 1; i <= n[0] && !isCancelled(); i++) {

                res *= i;

                SystemClock.sleep(1000);

                publishProgress(i*100 / n[0]);

            }

            return res;
        }
        @Override protected void onProgressUpdate(Integer... porc) {

            progreso.setProgress(porc[0]);

        }

        @Override protected void onPostExecute(Integer res) {

            progreso.dismiss();

            salida.append(res + "\n");

        }

        @Override protected void onCancelled() {

            salida.append("cancelado\n");

        }
    }

}

