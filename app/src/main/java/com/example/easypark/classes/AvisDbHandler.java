package com.example.easypark.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AvisDbHandler extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "easyPark_db";
        private static final String AVIS_TABLE_NAME = "avis";
        private static final String AVIS_id = "id";
        private static final String AVIS_user = "user";
        private static final String AVIS_DATE_CREATION = "date";
        private static final String AVIS_content = "content";

        public AvisDbHandler(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_TICKETS_TABLE = "CREATE TABLE "+AVIS_TABLE_NAME+" ("
                    +AVIS_id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    +AVIS_content+" TEXT,"
                    +AVIS_user+" TEXT,"
                    +AVIS_DATE_CREATION+" TEXT"
                    +")";

            db.execSQL(CREATE_TICKETS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + AVIS_TABLE_NAME);

            // Create tables again
            onCreate(db);
        }

        public void addAvis(Avis avis) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(AVIS_content, avis.getContent());
            values.put(AVIS_user, avis.getUser());
            values.put(AVIS_DATE_CREATION, avis.getDate());
            // Inserting Row
            db.insert(AVIS_TABLE_NAME, null, values);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        }

        //get a single ticket
        public Avis getAvis(int id) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(AVIS_TABLE_NAME, new String[] { AVIS_id, AVIS_content, AVIS_user, AVIS_DATE_CREATION },
                    AVIS_id + "=?",
                    new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            Avis avis = new Avis(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3));
            // return contact
            return avis;
        }

        // code to get all tickets in a list view
        public ArrayList<Avis> getAllAvis() {
            ArrayList<Avis> avisList = new ArrayList<>();

            String selectQuery = "SELECT  * FROM " + AVIS_TABLE_NAME+ " ORDER BY id DESC;";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Avis avis = new Avis(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                    );
                   avisList.add(avis);
                } while (cursor.moveToNext());
            }

            // return contact list
            return avisList;
        }
}
