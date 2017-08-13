package cardiac.general.hospital.medicare.Database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DbBackend extends DbObject {

    private String SpecialitiesJson;
    private int BookmarkParaPosition;
    public DbBackend(Context context) {
        super(context);
    }
    public String[] surah_arabic() {
        String query = "Select * from Surah_Names";
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        ArrayList<String> surah_names_array = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String surah_name = cursor.getString(cursor.getColumnIndexOrThrow("names"));
                surah_names_array.add(surah_name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        String[] surah_arabic_names = new String[surah_names_array.size()];
        surah_arabic_names = surah_names_array.toArray(surah_arabic_names);
        return surah_arabic_names;
    }
    public void SetSpecialitiesJson(String SpecJson) {
        String query = "update SpecialitiesJSON set specialitiesJson ='"+SpecJson+"' where id=1";
        db.execSQL(query);
        this.SpecialitiesJson = SpecJson;
    }
    public String getSpecialitiesJson() {
        String query = "Select * from SpecialitiesJSON where _id=1";
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                SpecialitiesJson = cursor.getString(cursor.getColumnIndexOrThrow("specialitiesJson"));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return SpecialitiesJson;
    }
    public void insertINTObookmarkpara(int bookmark_para_no,String bookmark_para_arabic,String bookmark_para_roman,int bookmark_scroll,String bookmark_date) {
        String query = "INSERT INTO bookmark_para (para_no,para_arabic,para_english,position,date) VALUES ('"+bookmark_para_no+"','"+bookmark_para_arabic+"','"+bookmark_para_roman+"','"+bookmark_scroll+"','"+bookmark_date+"')";
        db.execSQL(query);
    }
    public void deleteBookmarkPara(int index) {
        String query = "DELETE FROM bookmark_para WHERE _id in (SELECT _id FROM bookmark_para LIMIT 1 OFFSET "+index+")";
        db.execSQL(query);
    }
    public int getBookmarkParaPosition(int index) {
        String query = "SELECT position FROM bookmark_para ORDER BY _id LIMIT 1 OFFSET "+index;
        Cursor cursor = this.getDbConnection().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                BookmarkParaPosition= cursor.getInt(cursor.getColumnIndexOrThrow("position"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return BookmarkParaPosition;
    }
}