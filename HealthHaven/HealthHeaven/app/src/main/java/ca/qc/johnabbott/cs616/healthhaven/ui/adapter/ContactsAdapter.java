package ca.qc.johnabbott.cs616.healthhaven.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.qc.johnabbott.cs616.healthhaven.R;
import ca.qc.johnabbott.cs616.healthhaven.model.Contact;
import ca.qc.johnabbott.cs616.healthhaven.ui.Contact.ContactsFragment;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsViewHolder> implements Filterable {

    public List<Contact> data;
    private List<Contact>exampleData;
    private Fragment fragment;
    private Context context;
    private FloatingActionButton fab;


    ContactsAdapter(List<Contact> exampleData){

    }

    public ContactsAdapter(List<Contact> data, Fragment fragment, Context context, FloatingActionButton fab){
        this.data=data;
        this.exampleData = new ArrayList<>(data);
        this.fragment=fragment;
        this.context=context;
        this.fab= fab;
    }
    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_contacts,parent,false);
        ContactsViewHolder holder = new ContactsViewHolder(root,data,fragment,context,this,fab);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.set(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<Contact> getData() {
        return data;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            updateData();
            List<Contact> filteredList = new ArrayList<>();
            if(constraint == null|| constraint.length() == 0){
                filteredList.addAll(exampleData);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Contact item: exampleData){
                    if(item.getName().toLowerCase().startsWith(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        private void updateData() {
            data=ContactsFragment.getData();
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data.clear();
            data.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
