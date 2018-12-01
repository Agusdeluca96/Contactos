package app.agustindeluca.com.contactos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PersonAdapter extends BaseAdapter {
    Activity context;
    ArrayList<Person> contacts;
    ArrayList<Person> displayingContacts;
    private static LayoutInflater inflater = null;

    public PersonAdapter(Activity context, ArrayList<Person> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.displayingContacts = contacts;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return displayingContacts.size();
    }

    public Person getItem(int position) {
        return displayingContacts.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.single_contact, null): itemView;
        TextView textViewName = (TextView) itemView.findViewById(R.id.textViewName);
        TextView textViewFirstPhone = (TextView) itemView.findViewById(R.id.textViewFirstPhone);
        Person selectedContact = displayingContacts.get(position);
        textViewName.setText(selectedContact.getName());
        textViewFirstPhone.setText(selectedContact.getFirstPhone());
        return itemView;

    }

    public Filter getFilter() {
        return myFilter;
    }


    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Person> results = new ArrayList<Person>();

            if (constraint == null || constraint.length() == 0) {
                results.addAll(contacts);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Person contact: contacts) {
                    if ( contact.getName().toLowerCase().contains(filterPattern) ||
                            contact.getFirstPhone().toLowerCase().contains(filterPattern) ||
                            contact.getSecondPhone().toLowerCase().contains(filterPattern) ||
                            contact.getEmail().toLowerCase().contains(filterPattern) ||
                            contact.getContactType().toLowerCase().contains(filterPattern) ) {
                        results.add(contact);
                    }
                }
            }

            filterResults.values = results;
            filterResults.count = results.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            displayingContacts = (ArrayList<Person>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

}
