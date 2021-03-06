package com.example.findcompany.Provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.findcompany.Database.DBHelper;

public class Provider extends ContentProvider {

    final String LOG_TAG = "myLogs";

    // // Константы для БД
    // БД
    static final String DB_NAME = "FindCompanyDB.db";
    static final int DB_VERSION = 1;

    // Таблица
    static final String FINDCOMPANY_TABLE = "USERS";

    // Поля
    static final String USERS_ID = "id_user";
    static final String USER_FIRSTNAME = "firstname";
    static final String USER_SECONDNAME = "secondname";

    // // Uri
    // authority
    static final String AUTHORITY = "com.example.findcompany.Provider.Provider";

    // path
    static final String FINDCOMPANYDb_TABLE = "USERS";

    // Общий Uri
    public static final Uri FINDCOMPANY_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + FINDCOMPANYDb_TABLE);

    // Типы данных
    // набор строк
    static final String FINDCOMPANY_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + FINDCOMPANYDb_TABLE;

    // одна строка
    static final String FINDCOMPANY_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + FINDCOMPANYDb_TABLE;

    //// UriMatcher
    // общий Uri
    static final int URI_FINDCOMPANY = 1;

    // Uri с указанным ID
    static final int URI_FINDCOMPANY_ID = 2;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, FINDCOMPANYDb_TABLE, URI_FINDCOMPANY);
        uriMatcher.addURI(AUTHORITY, FINDCOMPANYDb_TABLE + "/#", URI_FINDCOMPANY_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    // чтение
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_FINDCOMPANY: // общий Uri
                Log.d(LOG_TAG, "URI_FINDCOMPANY");
                // если сортировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = USER_FIRSTNAME + " ASC";
                }
                break;
            case URI_FINDCOMPANY_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_FINDCOMPANY_ID, " + id);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = USERS_ID + " = " + id;
                } else {
                    selection = selection + " AND " + USERS_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = dbHelper.getUsers(db);
        // просим ContentResolver уведомлять этот курсор
        // об изменениях данных в CONTACT_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(),
                FINDCOMPANY_CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_FINDCOMPANY)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(FINDCOMPANY_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(FINDCOMPANY_CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_FINDCOMPANY:
                Log.d(LOG_TAG, "URI_FINDCOMPANY");
                break;
            case URI_FINDCOMPANY_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_FINDCOMPANY_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = USERS_ID + " = " + id;
                } else {
                    selection = selection + " AND " + USERS_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(FINDCOMPANY_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_FINDCOMPANY:
                Log.d(LOG_TAG, "URI_FINDCOMPANY");

                break;
            case URI_FINDCOMPANY_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_FINDCOMPANY_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = USERS_ID + " = " + id;
                } else {
                    selection = selection + " AND " + USERS_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(FINDCOMPANY_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_FINDCOMPANY:
                return FINDCOMPANY_CONTENT_TYPE;
            case URI_FINDCOMPANY_ID:
                return FINDCOMPANY_CONTENT_ITEM_TYPE;
        }
        return null;
    }
}