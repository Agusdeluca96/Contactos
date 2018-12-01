package app.agustindeluca.com.contactos;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ModifyContact extends AppCompatActivity {

    private Toolbar toolbar;
    private Person contact;
    private List<Person> contacts;
    private Integer indexContact;
    private EditText name;
    private EditText firstPhone;
    private EditText secondPhone;
    private EditText email;
    private String contactType = "";
    private Spinner contactTypeSpinner;

    private SharedPreferences.Editor prefsEditor;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_contact);

        toolbar = (Toolbar) findViewById(R.id.toolbar_modify_contact);
        toolbar.setTitle("Contactos");
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        indexContact = -1;
        if (b != null) {
            indexContact = b.getInt("indexContact");
        }

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        prefsEditor = appSharedPrefs.edit();
        gson = new Gson();
        Type type = new TypeToken<ArrayList<Person>>(){}.getType();

        String contactsJson = appSharedPrefs.getString("Contacts", "");
        contacts = gson.fromJson(contactsJson, type);

        contact = contacts.get(indexContact);

        prepareContactForm(contact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modify_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                AlertDialog.Builder confirmAlertBuilder = new AlertDialog.Builder(this);
                confirmAlertBuilder.setMessage("¿Desea eliminar el contacto?")
                        .setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contacts.remove(contacts.get(indexContact));
                                String contactsJsonToSaveAfterDelete = gson.toJson(contacts);
                                prefsEditor.putString("Contacts", contactsJsonToSaveAfterDelete);
                                prefsEditor.commit();
                                ModifyContact.this.finish();
                            }
                        })
                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog confirmAlert = confirmAlertBuilder.create();
                confirmAlert.show();

                break;
            case R.id.confirm:
                contact.setName(name.getText().toString());
                contact.setFirstPhone(firstPhone.getText().toString());
                contact.setSecondPhone(secondPhone.getText().toString());
                contact.setEmail(email.getText().toString());
                contact.setContactType(contactType);

                contacts.set(indexContact, contact);
                String contactsJsonToSaveAfterModify = gson.toJson(contacts);
                prefsEditor.putString("Contacts", contactsJsonToSaveAfterModify);
                prefsEditor.commit();
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareContactForm(Person contact) {
        name = (EditText) findViewById(R.id.name);
        name.setText(contact.getName());
        firstPhone = (EditText) findViewById(R.id.firstPhone);
        firstPhone.setText(contact.getFirstPhone());
        secondPhone = (EditText) findViewById(R.id.secondPhone);
        secondPhone.setText(contact.getSecondPhone());
        email = (EditText) findViewById(R.id.email);
        email.setText(contact.getEmail());
        contactTypeSpinner = (Spinner) findViewById(R.id.contactTypeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.contact_type_options, R.layout.spinner_item);
        contactTypeSpinner.setAdapter(adapter);
        contactTypeSpinner.setSelection(adapter.getPosition(contact.getContactType()));

        contactTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString() == "Tipo de Contacto") {
                    contactType = null;
                } else {
                    contactType = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
