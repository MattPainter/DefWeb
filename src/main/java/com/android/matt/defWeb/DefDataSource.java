package com.android.matt.defWeb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 09/11/2014.
 * Class to help manage data in database
 */
public class DefDataSource {
  /* internal variables and columns array*/
  private SQLiteDatabase database;
  private DatabaseHelper dbHelper;
  private String allColumns[] = {DatabaseHelper.COLUMN_ID,
      DatabaseHelper.COLUMN_NAME_TEXT,
      DatabaseHelper.COLUMN_IMG_LOC};

  public DefDataSource(Context context) {
    dbHelper = new DatabaseHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  /* Method adds a definition to database defined by two strings */
  public Definition createDefinition(String defName, String imgLoc) {
    /* Set up content values - looks like row of database */
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.COLUMN_NAME_TEXT, defName);
    values.put(DatabaseHelper.COLUMN_IMG_LOC, imgLoc);

    /* Insert the content values and get cursor to this new row */
    long insertId = database.insert(DatabaseHelper.TABLE_NAME, null, values);
    Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,
        allColumns,
        DatabaseHelper.COLUMN_ID + " = " + insertId,
        null, null, null, null);

    /* Get and return definition */
    cursor.moveToFirst();
    Definition definition = cursorToDef(cursor);
    cursor.close();

    return definition;
  }

  /* Method adds an already created definition to database*/
  public Definition createDefinition(Definition definition) {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.COLUMN_NAME_TEXT, definition.getDefName());
    values.put(DatabaseHelper.COLUMN_IMG_LOC, definition.getImgLoc());

    /* Insert the content values and get cursor to this new row */
    long insertId = database.insert(DatabaseHelper.TABLE_NAME, null, values);
    Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,
        allColumns,
        DatabaseHelper.COLUMN_ID + " = " + insertId,
        null, null, null, null);

    /* Get and return definition */
    cursor.moveToFirst();
    Definition def = cursorToDef(cursor);
    cursor.close();

    return def;
  }

  /* Method deletes a definition from database */
  public void deleteDefinition(Definition def) {
    long id = def.getId();
    System.out.println("Definition deleted with id: " + id);
    database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + " = " + id, null);
  }

  public List<Definition> getAllDefinitions() {
    /* Create array list and get cursor to first row */
    List<Definition> definitions = new ArrayList<Definition>();
    Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,
        allColumns, null, null, null, null, null);
    cursor.moveToFirst();

    /* Loops through rows and adds definitions to list */
    while (!cursor.isAfterLast()) {
      Definition def = cursorToDef(cursor);
      definitions.add(def);
      cursor.moveToNext();
    }
    cursor.close();
    return definitions;
  }

  /* Method sends request to helper to drop and remake the table - clears all entries*/
  public void dropAndRemakeTable() {
    dbHelper.dropAndRemakeTable(database);
  }

  private Definition cursorToDef(Cursor cursor) {
    /* Moves values of columns to appropriate places in definition object */
    Definition definition = new Definition();
    definition.setId(cursor.getLong(0));
    definition.setDefName(cursor.getString(1));
    definition.setImgLoc(cursor.getString(2));
    return definition;
  }
}
