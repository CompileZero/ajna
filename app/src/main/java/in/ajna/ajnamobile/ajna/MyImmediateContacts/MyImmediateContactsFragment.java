package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.provider.ContactsContract;

import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import androidx.loader.content.Loader;
import in.ajna.ajnamobile.ajna.R;

import static in.ajna.ajnamobile.ajna.R.layout.my_immediate_contacts_list_view;

public class MyImmediateContactsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener{


    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.LOLLIPOP ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME

            };

    /*
     * Defines an array that contains column names to move from
     * the Cursor to the ListView.
     */
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.LOLLIPOP ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };

    /*
     * Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id"
     */
    private final static int[] TO_IDS = {
            android.R.id.text1
    };


    // Define global mutable variables
    // Define a ListView object
    ListView mContactsList;


    // Define variables for the contact the user selects
    // The contact's _ID value
    long mContactId;

    // The contact's LOOKUP_KEY
    String mContactKey;

    // A content URI for the selected contact
    Uri mContactUri;

    // An adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter mCursorAdapter;

    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the CONTACT_KEY column
    private static final int CONTACT_KEY_INDEX = 1;


    // Defines the text expression
    @SuppressLint("InlinedApi")
    private static final String SELECTION =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
    // Defines a variable for the search string
    private String mSearchString;
    // Defines the array to hold values that replace the ?
    private String[] mSelectionArgs = { mSearchString };



    private MyImmediateContactsViewModel mViewModel;

    public static MyImmediateContactsFragment newInstance() {
        return new MyImmediateContactsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_immediate_contacts_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initializes the loader
       // getLoaderManager().initLoader(0, null, this);

        mViewModel = ViewModelProviders.of(this).get(MyImmediateContactsViewModel.class);

        // Set the item click listener to be the current fragment.
        mContactsList.setOnItemClickListener(this);

        // Gets the ListView from the View list of the parent activity

        //mContactsList =(ListView) getActivity().findViewById(R.layout.my_immediate_contacts_list_view);
        //mContactsList = (ListView) getActivity().findViewById(R.id.list);


        // Gets a CursorAdapter
        mCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.my_immediate_contacts_list_item,
                null,
                FROM_COLUMNS, TO_IDS,
                0);
        // Sets the adapter for the ListView
        mContactsList.setAdapter(mCursorAdapter);
        // TODO: Use the ViewModel
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        // Get the Cursor

        Cursor cursor = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();

        // Get the _ID value
        mContactId = cursor.getLong(CONTACT_ID_INDEX);
        // Get the selected LOOKUP KEY
        mContactKey = cursor.getString(CONTACT_KEY_INDEX);
        // Create the contact's content Uri
        mContactUri = ContactsContract.Contacts.getLookupUri(mContactId, mContactKey);

    }
}
