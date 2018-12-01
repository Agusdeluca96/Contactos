package app.agustindeluca.com.contactos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView contactsList;
    private ArrayList<Person> contacts;
    private PersonAdapter adapter;
    private SearchView searchView;
    private Type arrayContactsType;
    private Gson gson;
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private static final String STATE_SAVED_CONTACTS = MainActivity.class.getName() + ".SAVED_CONTACTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        gson = new Gson();
        appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        prefsEditor = appSharedPrefs.edit();
        arrayContactsType = new TypeToken<ArrayList<Person>>(){}.getType();

        Bundle b = getIntent().getExtras();
        String contactsJson = "";

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String savedContacts = (String) savedInstanceState.get(STATE_SAVED_CONTACTS);
            contacts = gson.fromJson(savedContacts, arrayContactsType);
        } else if (b != null) {
            contactsJson = b.getString("Contacts");
            contacts = gson.fromJson(contactsJson, arrayContactsType);
        } else {
            contacts = new ArrayList<Person>();
            // Cargo contactos para que la agenda no este vacia, de desearse se pueden borrar pero no habra contactos al iniciarse
            fillContacts(contacts);
            String json = gson.toJson(contacts);
            prefsEditor.putString("Contacts", json);
            prefsEditor.commit();
        }

        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("Contactos");
        setSupportActionBar(toolbar);

        contactsList = (ListView) findViewById(R.id.contactsList);

        adapter = new PersonAdapter(this, contacts);
        contactsList.setAdapter(adapter);
        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent modifyContact = new Intent(view.getContext(), ModifyContact.class);
                Bundle b = new Bundle();
                b.putInt("indexContact", contacts.indexOf(adapter.getItem(position)));
                modifyContact.putExtras(b);
                startActivityForResult(modifyContact, 5);
            }
        });

        searchView = findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String contactsJson = appSharedPrefs.getString("Contacts", "");
        outState.putSerializable(STATE_SAVED_CONTACTS, contactsJson);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.create:
                Intent addContact = new Intent(this, AddContact.class);
                startActivityForResult(addContact, 5);
                break;
            case R.id.switchMode:
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        searchView.setQuery("", false);
        searchView.clearFocus();
        String contactsJson = appSharedPrefs.getString("Contacts", "");
        contacts = gson.fromJson(contactsJson, arrayContactsType);
        adapter = new PersonAdapter(this, contacts);
        contactsList.setAdapter(adapter);
    }

    private void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        Bundle b = new Bundle();
        String contactsJson = appSharedPrefs.getString("Contacts", "");
        b.putString("Contacts", contactsJson);
        i.putExtras(b);
        startActivity(i);
        finish();
    }

    private ArrayList<Person> fillContacts(ArrayList<Person> arrayContacts) {
        arrayContacts.add(new Person("Agustin De Luca", "2214335566", "2214880909", "agustin.deluca@gba.gob.ar", "Familiar"));
        arrayContacts.add(new Person("Rodrigo Pait", "221345678", "2214880909", "rodrigo.pait@gba.gob.ar", "Amigo"));
        arrayContacts.add(new Person("Ramiro Cortes", "221567890", "2214880909", "ramiro.cortes@gba.gob.ar", "Otro"));
        arrayContacts.add(new Person("Agustin Vignolo", "221474302", "2214880909", "agustin.vignolo@gba.gob.ar", "Otro"));
        return arrayContacts;
    }
}
