package com.example.easypark.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "easyPark_db";
    private static final String TICKET_TABLE_NAME = "ticket";
    private static final String TICKET_id = "id";
    private static final String TICKET_date = "date";
    private static final String TICKET_heureDebut = "heureDebut";
    private static final String TICKET_heureFin = "heureFin";
    private static final String TICKET_duree = "duree";
    private static final String TICKET_longitude = "longitude";
    private static final String TICKET_latitude = "latitude";

    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TICKETS_TABLE = "CREATE TABLE "+TICKET_TABLE_NAME+" ("
                +TICKET_id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                +TICKET_heureDebut+" TEXT,"
                +TICKET_heureFin+" TEXT,"
                +TICKET_duree+" TEXT,"
                +TICKET_longitude+" TEXT,"
                +TICKET_latitude+" TEXT,"
                +TICKET_date+" TEXT)";

        db.execSQL(CREATE_TICKETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TICKET_TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void addTicket(Ticket ticket) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TICKET_heureDebut, ticket.getHeureDebut());
        values.put(TICKET_heureFin, ticket.getHeureFin());
        values.put(TICKET_duree, ticket.getDuree());
        values.put(TICKET_longitude, ticket.getLongitude());
        values.put(TICKET_latitude, ticket.getLatitude());
        values.put(TICKET_date, ticket.getDate());

        // Inserting Row
        db.insert(TICKET_TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    //get a single ticket
    public Ticket getTicket(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TICKET_TABLE_NAME, new String[] { TICKET_id, TICKET_heureDebut, TICKET_heureFin, TICKET_duree, TICKET_longitude, TICKET_latitude, TICKET_date},
                TICKET_id + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Ticket ticket = new Ticket(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getDouble(4),
                cursor.getDouble(5),
                cursor.getString(6));
        // return contact
        return ticket;
    }

    // code to get all tickets in a list view
    public List<Ticket> getAllContacts() {
        List<Ticket> ticketList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TICKET_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Ticket ticket = new Ticket();
                ticket.setId(cursor.getInt(0));
                ticket.setHeureDebut(cursor.getString(1));
                ticket.setHeureFin(cursor.getString(2));
                ticket.setDuree(cursor.getString(3));
                ticket.setLongitude(cursor.getDouble(4));
                ticket.setLatitude(cursor.getDouble(5));
                ticket.setDate(cursor.getString(6));
                // Adding contact to list
                ticketList.add(ticket);
            } while (cursor.moveToNext());
        }

        // return contact list
        return ticketList;
    }

}
