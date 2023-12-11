/**
 * Project Name : YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * DatabaseManager : This class provdies operations related to database
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package `in`.ymsli.ymlibrary.database

import `in`.ymsli.ymlibrary.utils.CommonUtils
import `in`.ymsli.ymlibrary.utils.StringUtils
import android.content.ContentValues
import android.database.Cursor.*
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*

class DatabaseManager(sqLiteOpenHelperArg: SQLiteOpenHelper) {

    private var database: SQLiteDatabase? = null
    private val oWriteLock = Any()
    private var sqLiteOpenHelper: SQLiteOpenHelper? = null

    init {

       this.sqLiteOpenHelper=sqLiteOpenHelperArg;

        }


    /**
     * Convenience method for updating rows in the database.
     *
     * @param table_name     the table name in which record will be updated.
     * @param oContentValues a map of column names to new column values. null is a valid
     * value that will be translated to NULL.
     * @param condition      the optional condition to apply when updating. Passing null
     * will update all rows.
     * @return the number of rows affected
     */
    fun update(table_name: String, oContentValues: ContentValues,
               condition: String?): Int? {
        this.openForWrite()
        var row:Int?=null;
        try {
           row = database!!.update(table_name, oContentValues, condition, null)

        }
        catch (e:Exception){
            CommonUtils.showLog(Log.ERROR, e.message)
        }
        finally {
            this.close()
        }
        return row
    }

    /**
     * Convenience method for deleting rows in the database.
     *
     * @param table_name the table name in which record will be deleted.
     * @param condition  the optional WHERE clause to apply when deleting. Passing null
     * will delete all rows.
     */
    fun delete(table_name: String, condition: String?):Int {
        this.openForWrite()
       var isDeleted:Int=-1
       try {

          isDeleted= database!!.delete(table_name, condition,null)

       }
       catch (e: Exception) {
            CommonUtils.showLog(Log.ERROR, e.message)

       }
        this.close()
     return isDeleted
    }

    /**
     * Convenience method for inserting a row into the database.
     *
     * @param table_name     the table name in which record will be inserted.
     * @param oContentValues this map contains the initial column values for the row. The
     * keys should be the column names and the values the column
     * values
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    fun insert(table_name: String, oContentValues: ContentValues): Long? {
        this.openForWrite()
        var row:Long?=null
        try {
             row = database!!.insertWithOnConflict(table_name, null,
                    oContentValues, SQLiteDatabase.CONFLICT_REPLACE)

        }
        catch (e:Exception){
            CommonUtils.showLog(Log.ERROR, e.message)
        }

        this.close()
        return row

    }

    /**
     * Convenience method for selecting records from database.
     *
     * @param table_name the table name in which record will be fetched.
     * @param fields     comma separated list of columns name required in result set.
     * Pass * for fetching all records.
     * @param condition  the optional WHERE condition for filter the result. Passing
     * null will select all rows.
     * @return result will be return in List of LinkedHashMap where keys are
     * column names.
     */
    fun getQueryResult(
            table_name: String, fields: String, condition: String?): List<LinkedHashMap<String, String>> {

        var list: List<LinkedHashMap<String, String>> = ArrayList()
        try {

            var sql = "select $fields from $table_name "
            if (condition != null) {
                sql += condition
            }
            list = getDBResult(sql)

        } catch (e: Exception) {
            CommonUtils.showLog(Log.ERROR, e.message)

        }

        return list
    }


    /**
     * Convenience method for selecting records from database.
     * @param  query raw query to get result from db
     * @return result will be return in List of LinkedHashMap where keys are
     * column names.
     */
    fun getDBResult(
            query: String): List<LinkedHashMap<String, String>> {
        this.openForRead()

        val listResult = ArrayList<LinkedHashMap<String, String>>()
        try {
        val oCursor = database!!.rawQuery(query, null)
        val columnNames = oCursor.columnNames
        val columncount = oCursor.columnCount

        if (oCursor.moveToFirst()) {
            do {
                val record = LinkedHashMap<String, String>()
                for (i in 0 until columncount) {
                    if (oCursor.getType(i) == FIELD_TYPE_STRING)
                    // if(oCursor.getString(i)!=null)
                        record[columnNames[i]] = oCursor.getString(i)
                    else if (oCursor.getType(i) == FIELD_TYPE_INTEGER) {
                        record[columnNames[i]] =   oCursor.getInt(i).toString()
                    } else if (oCursor.getType(i) == FIELD_TYPE_FLOAT) {
                        record[columnNames[i]] =  oCursor.getFloat(i).toString()
                    }
                    else if(oCursor.getType(i) == FIELD_TYPE_BLOB){
                        record[columnNames[i]] = oCursor.getBlob(i).toString()
                    }

                }
                listResult.add(record)
            } while (oCursor.moveToNext())
        }

        oCursor.close()
        this.close()
    } catch (e: Exception) {
        CommonUtils.showLog(Log.ERROR, e.message)

    }
        return listResult
    }



    /**
     * Provide synchronized write lock for sequential write operation.
     */
    private fun openForWrite() {
        synchronized(oWriteLock) {
            this.database = sqLiteOpenHelper!!.writableDatabase
        }
    }

    /**
     * Open database to read only
     */

    private fun openForRead() {

            this.database = sqLiteOpenHelper!!.readableDatabase

    }

    private fun close() {
        this.database!!.close()
    }

    private fun getInsertQuery(table_name: String, keyArray: List<String>): String {
        var insertSQL = "INSERT OR REPLACE INTO "
        insertSQL = insertSQL + table_name
        insertSQL = "$insertSQL ("

        for (i in keyArray.indices) {
            insertSQL = insertSQL + keyArray[i]

            if (i < keyArray.size - 1)
                insertSQL = "$insertSQL,"
        }

        insertSQL = "$insertSQL) VALUES("

        for (i in keyArray.indices) {
            insertSQL = "$insertSQL?"

            if (i < keyArray.size - 1)
                insertSQL = "$insertSQL,"
        }

        insertSQL = "$insertSQL);"
        CommonUtils.showLog(Log.VERBOSE, "insertSQL : $insertSQL")

        return insertSQL
    }

     fun getCreateQuery(table_name: String, keyArray: List<String>): String {
        var query = "CREATE TABLE IF NOT EXISTS "
        query = query + table_name
        query = "$query("

        for (i in keyArray.indices) {
            query = query + keyArray[i]

            if (i < keyArray.size - 1)
                query = "$query Long,"
        }

        query = "$query Long)"

        CommonUtils.showLog(Log.VERBOSE, "createSQL : $query")
        return query
    }

    /**
     * @param table_name     the table name in which record will be updated.
     * @param columns        a map of column names to new column values. null is a valid
     * value that will be translated to NULL.
     * @param result         map of values which needs to be inserted
     */
    fun saveApiResult(table_name: String, columns: String, result: ArrayList<Map<String, String?>>) {
        if (result != null && result.size > 0) {
            try {
                val columnsArray = ArrayList(Arrays.asList(*columns.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
                val insertSQL = getInsertQuery(table_name, columnsArray)
                CommonUtils.showLog(Log.VERBOSE, "Bulk Data Insertion Start")


                openForWrite()

              //  database!!.beginTransaction()
                database!!.beginTransactionNonExclusive();  //Non-exclusive mode allows database file to be in readable by other threads executing queries.
                val insert = database!!.compileStatement(insertSQL)

                for (data in result) {
                    try {

                        var j = 1
                        for (column in columnsArray) {
                            var value: String? = data[column].toString()
                            if(StringUtils.isNumeric(value!!))
                                if (value == null) value = ""

                            insert.bindString(j, value)

                            j++
                        }

                        insert.execute()
                    } catch (e: Exception) {

                        CommonUtils.showLog(Log.ERROR, e.localizedMessage)

                    }

                }

                database!!.setTransactionSuccessful()
                CommonUtils.showLog(Log.VERBOSE, "Insertion Done")

            } catch (e: Exception) {

                CommonUtils.showLog(Log.ERROR, e.localizedMessage)
            } finally {
                database!!.endTransaction()
                close()
            }
        }
    }


}
