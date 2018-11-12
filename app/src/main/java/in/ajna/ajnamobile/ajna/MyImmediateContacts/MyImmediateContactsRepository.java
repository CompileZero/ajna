package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MyImmediateContactsRepository {

    private MyImmediateContactsDao myImmediateContactsDao;
    private LiveData<List<MyImmediateContactsEntity>> allImmediateContacts;


    public MyImmediateContactsRepository(Application application){
        MyImmediateContactsDatabase database = MyImmediateContactsDatabase.getInstance(application);
        myImmediateContactsDao=database.myImmediateContactsDao();
        allImmediateContacts=myImmediateContactsDao.getAllImmediateContacts();
    }

    public void insert(MyImmediateContactsEntity myImmediateContactsEntity){

        new InsertImmediateContactAsyncTask(myImmediateContactsDao)
                .execute(myImmediateContactsEntity);

    }

    public void delete(MyImmediateContactsEntity myImmediateContactsEntity){
        new DeleteImmediateContactAsyncTask(myImmediateContactsDao)
                .execute(myImmediateContactsEntity);

    }

    public LiveData<List<MyImmediateContactsEntity>> getAllImmediateContacts() {
        return allImmediateContacts;
    }




    private static class InsertImmediateContactAsyncTask extends AsyncTask<MyImmediateContactsEntity, Void,Void>{

        private MyImmediateContactsDao myImmediateContactsDao;

        private InsertImmediateContactAsyncTask(MyImmediateContactsDao myImmediateContactsDao){

            this.myImmediateContactsDao=myImmediateContactsDao;
        }
        @Override
        protected Void doInBackground(MyImmediateContactsEntity... myImmediateContactsEntities) {

            myImmediateContactsDao.insert(myImmediateContactsEntities[0]);
            return null;
        }
    }



    private static class DeleteImmediateContactAsyncTask extends AsyncTask<MyImmediateContactsEntity, Void,Void>{

        private MyImmediateContactsDao myImmediateContactsDao;

        private DeleteImmediateContactAsyncTask(MyImmediateContactsDao myImmediateContactsDao){

            this.myImmediateContactsDao=myImmediateContactsDao;
        }
        @Override
        protected Void doInBackground(MyImmediateContactsEntity... myImmediateContactsEntities) {

            myImmediateContactsDao.delete(myImmediateContactsEntities[0]);
            return null;
        }
    }
}
