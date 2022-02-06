package com.r0th.shopping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DataBarang_Admin extends AppCompatActivity {
    private RecyclerView recyclerView;

    private data_item_histoy adapter;
    private List<data_item> items;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private FirebaseDatabase database;
    FirebaseRecyclerOptions<data_item> options;
    FirebaseRecyclerAdapter<data_item, viewholder> adapter2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        recyclerView = findViewById(R.id.search_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Data_aset");
        setHasOptionsMenu(true);
        showtask();
        
    }

    private void setHasOptionsMenu(boolean b) {
    }
    public void showtask(){
        options = new FirebaseRecyclerOptions.Builder<data_item>()
                .setQuery(reference, data_item.class)
                .build();

        adapter2 = new FirebaseRecyclerAdapter<data_item, viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull data_item data_item) {

                viewholder.rvnama.setText(data_item.getBnama());
                viewholder.rvjumlah.setText(Integer.toString(data_item.getCjumlah()));
                viewholder.rvHsatuan.setText("Rp."+data_item.getDhargasatuan());
                viewholder.rvTotal.setText("Rp."+data_item.getEtotal());
                viewholder.rvPenempatan.setText(data_item.getFtempat());
                viewholder.rvstatus.setText(data_item.getGstatus());

            }

            @NonNull
            @Override
            public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_lihat_aset, parent, false);
                return new viewholder(itemview);
            }
        };
        adapter2.startListening();
        recyclerView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Update")){
            showUpdateDialog(adapter2.getRef(item.getOrder()).getKey(), adapter2.getItem(item.getOrder()));


        } else if(item.getTitle().equals("Delete")){
            deleteTask(adapter2.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }
    private void deleteTask(String key) {
        reference.child(key).removeValue();
    }

    private void showUpdateDialog(final String key, data_item item) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update");
        builder.setMessage("Silahkan Update Data");
        View updateLayout = LayoutInflater.from(getContext()).inflate(R.layout.customedit_dataaset, null);
        //
        String[] arraySpinner = new String[] {
                "Ruang Dosen", "Ruang TU", "Ruang Dekan", "Ruang Wakil Dekan", "Ruang Sekertaris", "Ruang BPSI"
        };
        String[] arraystatus = new String[] {
                "Baik", "Normal", "Rusak", "Rusak Parah"
        };

        String[] arraynamaset = new String[] {
                "Meja Dosen", "Meja Rapat", "Meja Komputer", "Kursi", "Sapu", "Printer", "Stop Kontak","Dispenser"
        };
        //
        final TextView idaset =updateLayout.findViewById(R.id.edit_id);
        final TextView namabarang = updateLayout.findViewById(R.id.edit_anamabarang);
        final EditText editjumlah = updateLayout.findViewById(R.id.edit_bjumlahbarang);
        final EditText editharga = updateLayout.findViewById(R.id.edit_chargasatuan);
        final EditText edittotal = updateLayout.findViewById(R.id.edit_dtotal);
        final TextView tempatedit= updateLayout.findViewById(R.id.edit_etempat);
        final Spinner statusedit= updateLayout.findViewById(R.id.edit_fstatus);




        //
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        ArrayAdapter<String> adapterstatus = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arraystatus);
        ArrayAdapter<String> adapternamaaset = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, arraynamaset);
        adapterstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusedit.setAdapter(adapterstatus);
        //

        //final TextView namauser = updateLayout.findViewById(R.id.edit_anamabarang);

/*        final EditText edittempat = updateLayout.findViewById(R.id.edit_etempat);
        final EditText editstatus = updateLayout.findViewById(R.id.edit_fstatus);*/

       /* namabarang.setSelection(Integer.parseInt(item.getBnama()));
        statusedit.setSelection(Integer.parseInt(item.getGstatus()));
        tempatedit.setSelection(Integer.parseInt(item.getFtempat()));*/
        namabarang.setText(item.getBnama());
        idaset.setText(item.getAid());
        editjumlah.setText(Integer.toString(item.getCjumlah()));
        editharga.setText(item.getDhargasatuan());
        tempatedit.setText(item.getFtempat());
        edittotal.setText(item.getEtotal());

        builder.setView(updateLayout);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = idaset.getText().toString();
                String nm = namabarang.getText().toString();
                int jmlh = Integer.parseInt(editjumlah.getText().toString());
                String hrga = editharga.getText().toString();
                String Total = edittotal.getText().toString();
                String tmpt = tempatedit.getText().toString();
                String stts = statusedit.getSelectedItem().toString();

                data_item daitem3 = new data_item(id , nm, jmlh, hrga, tmpt,Total, stts);
                reference.child(key).setValue(daitem3);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
