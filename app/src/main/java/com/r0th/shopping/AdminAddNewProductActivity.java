package com.r0th.shopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime,stock,subcat,disc,barc,hrgawl;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, inputstock, InputProductPrice,discount,barcode,hargabeli;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    private Spinner spinner,spinner2;
    String spinnertext = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        spinner2 = (Spinner) findViewById(R.id.spinner1);

        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        barcode = findViewById(R.id.barcode);
        //discount = findViewById(R.id.productdiscount);
        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        //InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        hargabeli=findViewById(R.id.product_OG_price);
        inputstock=findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);

        spinner();

//        InputProductImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                OpenGallery();
//            }
//        });


        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });
    }


    private void OpenGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }
    private void ValidateProductData() {
         stock= inputstock.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
        subcat = spinner.getSelectedItem().toString();
        hrgawl = hargabeli.getText().toString();
        //disc = discount.getText().toString();
        barc = barcode.getText().toString();
        if (TextUtils.isEmpty(barc))
        {
            Toast.makeText(this, "Barcode Produk Dibutuhkan", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(stock))
        {
            Toast.makeText(this, "Mohon masukan jumlah stok.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Mohon masukan harga", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Mohon masukan nama produk", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(hrgawl)){
            Toast.makeText(this, "Mohon masukan Harga Beli", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SaveProductInfoToDatabase();
            //StoreProductInformation();
        }

    }
    private void StoreProductInformation()
    {
        loadingBar.setTitle("Tambah produk baru");
        loadingBar.setMessage("Sedang memasukan produk ke dalam database..");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Gambar produk sukses di upload..", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();



                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    protected void spinner() {

        spinner = (Spinner) findViewById(R.id.spinner1);
        String resultcat = getIntent().getExtras().get("category").toString();

        switch (resultcat){
            case "Pakaian" :
                String[] arraymodel = new String[] {
                        "Top", "Bottom", "Dress"

                };
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, arraymodel);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            break;
            case "Aksesoris" :
                String[] arraymodel1 = new String[] {
                        "Brooch", "Gelang", "Cincin","Strap Masker","Ring Hijab"
                };
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, arraymodel1);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter1);
                break;
            case "Hijab" :
                String[] arraymodel2 = new String[] {
                        "Segi Empat", "Syari", "Bergo"
                };
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, arraymodel2);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter2);
                break;
            case "Alat Shalat" :
                String[] arraymodel3 = new String[] {
                        "Mukena", "Sajadah", "Tasbih"
                };
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, arraymodel3);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter3);
                break;
        }


    }

    private void SaveProductInfoToDatabase()
    {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", subcat);
        productMap.put("image", "Null");
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("ogprice", hrgawl);
        productMap.put("barcode", barc);
        productMap.put("discount","0");
        productMap.put("pname", Pname);
        productMap.put("stock",stock);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Mendaftarkan produk berhasil", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
