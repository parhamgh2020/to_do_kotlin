package com.example.sqliteapplication.DataLayer

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.sqliteapplication.models.*
import java.text.DateFormat
import java.util.*

class ChoresDatabaseHandler(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createChoreTable = """
            CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER KEY ID,
                                      $KEY_CHORE_NAME TEXT NOT NULL,
                                      $KEY_CHORE_ASSIGNED_BY TEXT NOT NULL,
                                      $KEY_CHORE_ASSIGNED_TO TEXT NOT NULL,
                                      $KEY_CHORE_ASSIGNED_TIME LONG NOT NULL)
        """.toString()
        db?.execSQL(createChoreTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun createChore(chore: Chore) {
        val db: SQLiteDatabase = writableDatabase
        val contentValue = ContentValues()
        contentValue.put(KEY_CHORE_NAME, chore.choreName)
        contentValue.put(KEY_CHORE_ASSIGNED_BY, chore.assignedBy)
        contentValue.put(KEY_CHORE_ASSIGNED_TO, chore.assignedTo)
        contentValue.put(KEY_CHORE_ASSIGNED_TIME, System.currentTimeMillis())
        db.insert(TABLE_NAME, null, contentValue)
        db.close()
        Log.d("DATA INSERTED", "SUCCESS")
    }

    fun getChore(id: Int): Chore? {
        val db: SQLiteDatabase = readableDatabase
        val cursor = db.query(
            TABLE_NAME, arrayOf(
                KEY_ID,
                KEY_CHORE_NAME,
                KEY_CHORE_ASSIGNED_TO,
                KEY_CHORE_ASSIGNED_BY,
                KEY_CHORE_ASSIGNED_TIME
            ), KEY_ID + "=?", arrayOf(id.toString()),
            null,
            null,
            null
        )

        if (cursor != null) {
            cursor.moveToFirst()
            val chore = Chore()
            val choreName = cursor.getColumnIndex(KEY_CHORE_NAME)
            val assignedBy = cursor.getColumnIndex(KEY_CHORE_ASSIGNED_BY)
            val assignedTo = cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TO)
            val timeAssigned = cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TIME)
            chore.choreName = cursor.getString(choreName)
            chore.assignedBy = cursor.getString(assignedBy)
            chore.assignedTo = cursor.getString(assignedTo)
            chore.timeAssigned = cursor.getLong(timeAssigned)

            val dateFormat: DateFormat = DateFormat.getDateInstance()
            var formattedDate = dateFormat.format(Date(cursor.getLong(timeAssigned)).time)
            return chore
        }
        return null
    }

    fun readChores(): ArrayList<Chore> {
        val db: SQLiteDatabase = readableDatabase
        val list: ArrayList<Chore> = ArrayList()
        val selectAll = "SELECT * FROM " + TABLE_NAME

        val cursor: Cursor = db.rawQuery(selectAll, null)

        if (cursor.moveToFirst()) {
            do {
                val chore = Chore()
                val choreName = cursor.getColumnIndex(KEY_CHORE_NAME)
                val id = cursor.getColumnIndex(KEY_ID)
                val assignedTo = cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TO)
                val assignedBy = cursor.getColumnIndex(KEY_CHORE_ASSIGNED_BY)
                val timeAssigned = cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TIME)
                chore.choreName = cursor.getString(choreName)
                chore.id = cursor.getInt(id)
                chore.assignedTo = cursor.getString(assignedTo)
                chore.assignedBy = cursor.getString(assignedBy)
                chore.timeAssigned = cursor.getLong(timeAssigned)
                list.add(chore)
            } while (cursor.moveToNext())
        }

        return list
    }

    fun updateChore(chore: Chore): Int {
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()
        values.put(KEY_CHORE_NAME, chore.choreName)
        values.put(KEY_CHORE_ASSIGNED_BY, chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TO, chore.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_TIME, System.currentTimeMillis())
        return db.update(TABLE_NAME, values, KEY_ID + "=?", arrayOf(chore.id.toString()))
    }

    fun deleteChore(chore: Chore) {
        val db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME, KEY_ID + "=?", arrayOf(chore.id.toString()))
        db.close()
    }

    fun deleteChore(id: Int) {
        val db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME, KEY_ID + "=?", arrayOf(id.toString()))
        db.close()
    }

    fun getChoresCount(): Int {
        val db: SQLiteDatabase = readableDatabase
        val countQuery = """SELECT * FROM $TABLE_NAME""".toString()
        val cursor: Cursor = db.rawQuery(/* sql = */ countQuery, /* selectionArgs = */ null)
        return cursor.count
    }
}