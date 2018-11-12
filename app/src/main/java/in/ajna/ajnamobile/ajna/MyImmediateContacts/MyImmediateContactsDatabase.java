package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {MyImmediateContactsEntity.class},version = 1)
public abstract class MyImmediateContactsDatabase extends RoomDatabase {

    private static MyImmediateContactsDatabase instance;

    public abstract MyImmediateContactsDao myImmediateContactsDao();

    public static synchronized MyImmediateContactsDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    MyImmediateContactsDatabase.class,"my_immediate_contacts_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{

        private MyImmediateContactsDao myImmediateContactsDao;

        private PopulateDbAsyncTask(MyImmediateContactsDatabase db){
            myImmediateContactsDao=db.myImmediateContactsDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {

            myImmediateContactsDao.insert(new MyImmediateContactsEntity("Atharva","9766760151"));
            myImmediateContactsDao.insert(new MyImmediateContactsEntity("Revati","8805007214"));
            myImmediateContactsDao.insert(new MyImmediateContactsEntity("Sudishnu","7887525675"));
            return null;

        }
    }

}
