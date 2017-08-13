package cardiac.general.hospital.medicare.Database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MySqliteDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAMES = "MedicareDB_v1.1";
    private static final int DATABASE_VERSION = 1;

    public MySqliteDatabase(Context context) {
        super(context, DATABASE_NAMES, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

}
