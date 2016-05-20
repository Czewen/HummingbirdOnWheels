package hummingbird.android.mobile_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import android.database.MatrixCursor;

import android.database.SQLException;

/**
 * Created by CzeWen on 2016-05-19.
 */
//We use a singleton approach to ensure we have at only 1 open connection to the db at any point in time
public class DBStore extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HummingbirdWheels.db";

    private static DBStore instance;

    private DBStore(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    public static DBStore getInstance(Context context){
        if(instance==null)
            instance = new DBStore(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //create table for caching library entries by id and anime id
        //So that we can check whether an anime search result is in the user's library
        db.execSQL(
                "create table library_entries " +
                        "(id integer primary key, anime_id integer, username text)"
        );
        db.execSQL("create table users "+"(username text primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion){
        db.execSQL("DROP TABLE IF EXISTS library_entries");
        onCreate(db);
    }

    public void insertLibraryEntry(int library_entry_id, int anime_id, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", library_entry_id);
        contentValues.put("anime_id", anime_id);
        contentValues.put("username", username.toLowerCase());
        db.insert("library_entries", null, contentValues);
    }

    public void insertUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username.toLowerCase());
        db.insert("users", null, contentValues);
    }

    public boolean userExistsInDB(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from users where username=\""+username.toLowerCase()+"\"", null);
        if(res.getCount()==0)
            return false;
        return true;
    }

    public int getLibraryEntryByAnimeId(int id, String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select id from library_entries " +
                "where anime_id="+id+" AND username=\""+username+"\"", null);

        //No matching records
        if(res.getCount()==0){
            res.close();
            return -1;
        }
        res.moveToFirst();
        //first column of the result should be id,
        //since there should only be one matching tuple
        //and we're only returning one column
        int result = res.getInt(0);
        res.close();
        return result;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }



}
