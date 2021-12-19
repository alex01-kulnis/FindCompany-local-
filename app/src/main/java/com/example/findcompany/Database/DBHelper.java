package com.example.findcompany.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "FindCompanyDB.db";
    private static final int SCHEMA = 1; // версия базы данных

    public DBHelper(Context context) {
        super(context, DBNAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
   /*     db.execSQL("create Table Users ("
                + "id_user integer primary key autoincrement not null,"
                + "firstname text not null,                           "
                + "secondname text not null,                          "
                + "login text unique,                                 "
                + "password text not null                           );"
        );
        db.execSQL("create table Events ("
                + "id_event integer primary key autoincrement not null,"
                + "id_user integer not null,                           "
                + "name_event text not null,                           "
                + "place_event text not null,                          "
                + "dataAndtime_event text not null,                    "
                + "maxParticipants_event integer not null,             "
                + " foreign key(id_user) references Users(id_user)     "
                + " on delete cascade on update cascade              );"
        );
        db.execSQL("create table ParticipationInEvents ("
                + "id_event integer primary key autoincrement not null,           "
                + "id_user integer not null,                                      "
                + "name_event text not null,                                      "
                + "place_event text not null,                                     "
                + "dataAndtime_event Date not null,                               "
                + "maxParticipants_event integer not null,                        "
                + "IsConfirm boolean not null,                                    "
                + " foreign key(id_user) references Users(id_user)                "
                + " on delete cascade on update cascade              );           "
        );*/

       db.execSQL(" create Table HistoryVisiting ("
               +" id_event integer primary key autoincrement not null,"
               +"id_user INTEGER not null,"
               +"name_event text not null,"
               +"place_event text not null,"
               +"dataAndTime_event text unique,"
               +"maxParticipants text not null,"
               +" foreign key(id_user) references Users(id_user));"
               );
        db.execSQL(" create Table Recording ("
                +" id_event integer primary key autoincrement UNIQUE,"
                +"id_user INTEGER not null,"
                +"name_event text not null,"
                +"place_event text not null,"
                +"dataAndTime_event text unique,"
                +"maxParticipants text not null,"
                +" foreign key(id_user) references Users(id_user));"
        );
        db.execSQL("create Table Users ("
                + "id_user integer primary key autoincrement not null,"
                + "firstname text not null,                           "
                + "secondname text not null,                          "
                + "login text unique,                                 "
                + "password text not null                           );"
        );
        db.execSQL("create Table VisitorsGroup ("
                + "id_event integer  not null,"
                + "id_user integer not null,                           "
                + "PRIMARY KEY (`id_event`, `id_user`),                     "
                + "FOREIGN KEY (id_user) REFERENCES Users(id_user) ON DELETE CASCADE,"
                + "FOREIGN KEY (id_event) REFERENCES Events(id_event) ON DELETE CASCADE);"
        );

        db.execSQL("create table Events ("
                + "id_event integer primary key autoincrement not null,"
                + "id_user integer not null,                           "
                + "name_event text not null,                           "
                + "place_event text not null,                          "
                + "dataAndtime_event text not null,                    "
                + "maxParticipants_event integer not null);         "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists Users");
        onCreate(MyDB);
    }

    //registration
    public Boolean insertData(String login, String password, String firstame, String secondname){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("login", login);
        contentValues.put("password", password);
        contentValues.put("firstname", firstame);
        contentValues.put("secondname", secondname);
        long result = MyDB.insert("Users",null, contentValues);
        if (result == 1) return  false;
        else return true;
    }

    public Boolean CheckUser(String login){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Users where login = ?", new String[] {login});
        if(cursor.getCount()>0)
            return  true;
        else
            return false;
    }

    public Boolean CheckPassword(String login, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Users where login = ? and password = ?",new String[] {login,password});
        if(cursor.getCount()>0)
            return  true;
        else
            return false;
    }

    //currentUser
    public int CurrentUser(String login) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select id_user from Users where login = ?", new String[] {login});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        return -1;
    }

    //createEvent
    public void CreateEvent(Integer id_user, String name_event, String place_event, String evnt_date, Integer maxParticipacion) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_user", id_user);
        contentValues.put("name_event", name_event);
        contentValues.put("place_event", place_event);
        contentValues.put("dataAndtime_event", evnt_date);
        contentValues.put("maxParticipants_event", maxParticipacion);
        MyDB.insert("Events",null, contentValues);
    }

    public void CreateEventAdmin(Integer id_user, String name_event, String place_event, String evnt_date, Integer maxParticipacion) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_user", id_user);
        contentValues.put("name_event", name_event);
        contentValues.put("place_event", place_event);
        contentValues.put("dataAndtime_event", evnt_date);
        contentValues.put("maxParticipants_event", maxParticipacion);
        MyDB.insert("HistoryVisiting",null, contentValues);
    }
    //
    public Cursor getExpenses(SQLiteDatabase db) {
        return db.rawQuery("select id_user, firstname, secondname from " + "USERS" + ";", null);
    }
}