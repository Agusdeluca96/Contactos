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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddContact extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText name;
    private EditText firstPhone;
    private EditText secondPhone;
    private EditText email;
    private String contactType = null;
    private Spinner contactTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        toolbar = (Toolbar) findViewById(R.id.toolbar_add_contact);
        toolbar.setTitle("Contactos");
        setSupportActionBar(toolbar);

        prepateContactForm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cancel:
                this.finish();
                break;
            case R.id.confirm:

                if (name.getText().toString().equals("") || firstPhone.getText().toString().equals("")) {
                    String alertMessage;
                    if (name.getText().toString().equals("")) {
                        alertMessage = "El campo Nombre no debería estar vacío";
                    } else {
                        alertMessage = "El campo Teléfono Principal no debería estar vacío";
                    }
                    AlertDialog.Builder confirmAlertBuilder = new AlertDialog.Builder(this);
                    confirmAlertBuilder.setMessage(alertMessage)
                            .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog confirmAlert = confirmAlertBuilder.create();
                    confirmAlert.show();
                } else {
                    Person person = new Person(name.getText().toString(), firstPhone.getText().toString(), secondPhone.getText().toString(), email.getText().toString(), contactType);

                    SharedPreferences appSharedPrefs = PreferenceManager
                            .getDefaultSharedPreferences(this.getApplicationContext());
                    SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Person>>() {
                    }.getType();

                    String contactsJson = appSharedPrefs.getString("Contacts", "");
                    List<Person> contacts = gson.fromJson(contactsJson, type);
                    contacts.add(person);

                    String contactsJsonToSave = gson.toJson(contacts);
                    prefsEditor.putString("Contacts", contactsJsonToSave);
                    prefsEditor.commit();

                    this.finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepateContactForm() {
        name = (EditText) findViewById(R.id.name);
        firstPhone = (EditText) findViewById(R.id.firstPhone);
        secondPhone = (EditText) findViewById(R.id.secondPhone);
        email = (EditText) findViewById(R.id.email);
        contactTypeSpinner = (Spinner) findViewById(R.id.contactTypeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.contact_type_options, R.layout.spinner_item);
        contactTypeSpinner.setAdapter(adapter);

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
