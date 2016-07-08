package com.example.dantesrevelion.mipedido;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.utils.ConnectionUtils;
import com.example.dantesrevelion.mipedido.utils.MyAnimationUtils;
import com.example.dantesrevelion.mipedido.utils.SQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    LinearLayout layoutUser;
    LinearLayout layoutPass;
    EditText inputTextUser=null;
    EditText inputTextPsw=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layoutUser=(LinearLayout)findViewById(R.id.layoutUser);
        layoutPass=(LinearLayout)findViewById(R.id.layoutPassword);
        inputTextUser=(EditText) findViewById(R.id.inputUser);
        inputTextPsw=(EditText) findViewById(R.id.inputPassword);
        layoutPass.setVisibility(View.INVISIBLE);

        SQLiteHelper sqlHelper=new SQLiteHelper(this, "miPedidoLite", null, 1);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        MyAnimationUtils.translateAnimation(layoutUser,800L,2.0f,1000,0,0,0);
    }

    public void nextStep(View view){

        try {
            String entry=inputTextUser.getText().toString();
            JSONArray taskResult= (JSONArray) new Connection().execute(entry).get();

            if(taskResult.length()!=0) {
                MyAnimationUtils.translateAnimation(layoutUser,800L,2.0f,0,-900,0,0);
                layoutUser.setVisibility(View.INVISIBLE);
                layoutPass.setVisibility(View.VISIBLE);
                MyAnimationUtils.translateAnimation(layoutPass,800L,2.0f,900,0,0,0);
            }else{
                Toast toast1 = Toast.makeText(getApplicationContext(),
                                "El usuario no existe", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
    public void loadMenu(View view){
        try {
            String entryUser=inputTextUser.getText().toString();
            String entryPass=inputTextPsw.getText().toString();
            System.out.println(entryUser+"---"+entryPass);
            JSONArray taskResult= (JSONArray) new ConnectionUserPass().execute(entryUser,entryPass).get();
            System.out.println("JSON "+taskResult);
            if(taskResult.length()!=0) {
                Intent intent=new Intent(Login.this,Menu.class);
                intent.putExtra("usuario",taskResult.getJSONObject(0).get("usuario").toString());
                intent.putExtra("correo",taskResult.getJSONObject(0).get("correo").toString());
                startActivity(intent);
            }else{
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Contraseña Incorrecta", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void loadUser(View view){
        MyAnimationUtils.translateAnimation(layoutPass,800L,2.0f,0,900,0,0);
        layoutUser.setVisibility(View.VISIBLE);
        layoutPass.setVisibility(View.INVISIBLE);
        MyAnimationUtils.translateAnimation(layoutUser,800L,2.0f,-900,0,0,0);
    }

    private class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;
            ConnectionUtils cn=new ConnectionUtils();
            if(param!=null){
                if(!param[0].toString().trim().equals("")){
                    response=cn.connect(ConnectionUtils.getUserParameter(param[0].toString()));
                }
            }
            return response;
        }

    }

    private class ConnectionUserPass extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;
            ConnectionUtils cn=new ConnectionUtils();
            if(param!=null){
                if(!param[0].toString().trim().equals("")){
                    if(!param[1].toString().trim().equals("")) {
                        response = cn.connect(ConnectionUtils.getIdentUserPassParameter
                                (param[0].toString(),param[1].toString()));
                    }
                }
            }
            return response;
        }

    }
}
