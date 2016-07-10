package com.example.dantesrevelion.mipedido.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.ExecutionException;

/**
 * Created by Dantes Revelion on 08/07/2016.
 */
public class CheckIn {

    public static void checkInProcess(Context context){
        SQLiteHelper sqlHelper=new SQLiteHelper(context, "miPedidoLite", null, 1);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        try {
            String task=new ConectionTask().execute(db).get().toString();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("check In End");
    }


}
