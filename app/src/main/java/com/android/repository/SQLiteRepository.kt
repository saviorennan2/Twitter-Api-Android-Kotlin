package com.android

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.android.model.Tweet
import com.android.model.User


class SQLiteRepository(ctx: Context): TweetRepository {

    private  val helper: TweetSqlHelper = TweetSqlHelper(ctx)

    private fun insert(tweet : Tweet){

        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_USERNAME, tweet.user?.name)
            put(COLUMN_CONTENT, tweet.text)
        }

        val id = db.insert(TABLE_NAME, null, cv)
        if (id != -1L){
            tweet.id = id
        }
        db.close()
    }

    private fun update(tweet : Tweet){
        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_USERNAME, tweet.user?.name)
            put(COLUMN_CONTENT, tweet.text)
        }

        db.update(
            TABLE_NAME,
            cv,
            "$COLUMN_ID = ? ",
            arrayOf(tweet.id.toString())
        )

        db.close()

    }


    override fun save(tweet : Tweet) {
        Log.d("identificador", tweet.id.toString())

        insert(tweet)

//        }else{
//            update(tweet)
//        }
    }

    override fun remove(tweet : Tweet) {
        val db = helper.writableDatabase

        db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ? ",
            arrayOf(tweet.id.toString())
        )

        db.close()
    }

    fun cleanTables(){
        val db = helper.writableDatabase

        db.execSQL("delete from "+ TABLE_NAME);

        db.close()
    }


    override  fun list(callbacks: (MutableList<Tweet>)->Unit){

        var sql = "SELECT * FROM $TABLE_NAME"
        var args: Array<String>? = null

        sql += " ORDER BY $COLUMN_ID"
        val db = helper.readableDatabase
        val cursor = db.rawQuery(sql, args)
        val tweets = ArrayList<Tweet>()
        while (cursor.moveToNext()){
            val tweet = tweetFromCursor(cursor)
            tweets.add(tweet)
        }
        cursor.close()
        db.close()

        callbacks(tweets)
    }

    private fun tweetFromCursor(cursor: Cursor): Tweet {
        val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
        val content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))

        return Tweet(title,content,title,title,title, User("1", title, title, title ))
    }

}