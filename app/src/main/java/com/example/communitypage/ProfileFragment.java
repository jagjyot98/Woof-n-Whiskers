package com.example.communitypage;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.communitypage.adapters.AdapterPost;
import com.example.communitypage.model.ModelPost;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import com.squareup.picasso.Picasso;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Objects;


public class ProfileFragment extends Fragment implements RecyclerViewInterface {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    //storage
    StorageReference storageReference;
    // path where pics of user profile and cover pic will be stored
    String storagePath = "Users_Profile_Cover_Imgs/";
    DatabaseReference databaseReference;

    //views from xml
    TextView nametv,emailtv,phonetv,petTypetv;
    ImageView displaypictv,coverIv;
    FloatingActionButton editbtn;
    RecyclerView postsRecyclerView, servicesRecyclerView;
    ProgressDialog pd;

    // Permissions constants
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY_CODE=300;
    private static final int IMAGE_PICK_CAMERA_CODE=400;
    //ARRAYof permissions to  be requested
    String[] cameraPermissions;
    String[] storagePermissions;
    ArrayList<ModelPost> postList;
    ArrayList<ServiceClass> serviceList;
    AdapterPost adapterPost;
    ServiceAdapter adapterServices;
    String uid;

    //uri of picked image
    Uri image_uri;

    // checking profile or coverpic
    String profileOrCoverPhoto;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        postList = new ArrayList<>();
        serviceList = new ArrayList<>();

        //init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init views
        displaypictv = view.findViewById(R.id.displayPic);
        nametv = view.findViewById(R.id.nameFrbs);
        emailtv= view.findViewById(R.id.emailFrbs);
        phonetv= view.findViewById(R.id.phoneFrbs);
        petTypetv=view.findViewById(R.id.petTypeFrbs);
        coverIv=view.findViewById(R.id.petPic);
        editbtn =view.findViewById(R.id.fab);
        postsRecyclerView=view.findViewById(R.id.recyclerviewPosts);
        servicesRecyclerView=view.findViewById(R.id.recyclerviewServices);

        //init progress dialog
        pd = new ProgressDialog(getActivity());

        //show the details of nodes whose key email is equal to current signed email.
        Query query = databaseReference.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data is recieved
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //get data
                    String Name = ""+ds.child("Name").getValue();
                    String email = ""+ds.child("email").getValue();
                    String image = ""+ds.child("image").getValue();
                    String petType = ""+ds.child("petType").getValue();
                    String phone = ""+ds.child("phone").getValue();
                    String cover = ""+ds.child("cover").getValue();

                    //set data
                    nametv.setText(Name);
                    emailtv.setText(email);
                    phonetv.setText(phone);
                    petTypetv.setText(petType);

                    //for profile pic
                    try {
                        //if image received then set
                        Picasso.get().load(image).into(displaypictv);
                    }
                    catch (Exception e){
                        //set default if there is exception while getting image.
                        Picasso.get().load(R.drawable.ic_displaypic).into(displaypictv);
                    }
                    //for cover pic
                    try {
                        //if image received then set
                        Picasso.get().load(cover).into(coverIv);
                    }
                    catch (Exception e){
                        //set default if there is exception while getting image.
                        //Picasso.get().load(R.drawable.ic_displaypic).into(coverIv);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //floating action edit btton click
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditProfileDialog();
            }
        });


        LinearLayoutManager layoutManagerServices = new LinearLayoutManager(getActivity());
        //show newest post first, for this load from last
        layoutManagerServices.setStackFromEnd(true);
        layoutManagerServices.setReverseLayout(true);

        //linear layout for recyclerview
        LinearLayoutManager layoutManagerPosts = new LinearLayoutManager(getActivity());
        //show newest post first, for this load from last
        layoutManagerPosts.setStackFromEnd(true);
        layoutManagerPosts.setReverseLayout(true);

        adapterServices = new ServiceAdapter(getActivity(), serviceList, this);
        adapterPost = new AdapterPost(getActivity(), postList);

        //set this adapter to recyclerview
        servicesRecyclerView.setAdapter(adapterServices);
        postsRecyclerView.setAdapter(adapterPost);

        //set this layout to recyclerview
        servicesRecyclerView.setLayoutManager(layoutManagerServices);
        postsRecyclerView.setLayoutManager(layoutManagerPosts);

        //init posts list
        DatabaseReference refServices = FirebaseDatabase.getInstance().getReference("Services");
        DatabaseReference refPosts = FirebaseDatabase.getInstance().getReference("Posts");

        //qurey to load posts
        /*whenever user publishes a post the uid of this user is also saved as info of post
         * so we're retrieving posts having uid equals to uid of current user*/

        query = refServices.orderByChild("userID").equalTo(user.getUid());
        //get all data from this ref
        query.addValueEventListener(new ValueEventListener() {          //For Services
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ServiceClass myServices = ds.getValue(ServiceClass.class);

                    //add to list
                    serviceList.add(myServices);
                }
                    //adapter
                adapterServices.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        query = refPosts.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {          //For Posts
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost myPosts = ds.getValue(ModelPost.class);

                    //add to list
                    postList.add(myPosts);
                }
                    //adapter
                adapterPost.notifyDataSetChanged();
            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        checkUserStatus();
        return view;
    }
//    private void loadMyPosts() {        ///////////////////////////////////////////////////////////
//        //linear layout for recyclerview
////        LinearLayoutManager layoutManagerPosts = new LinearLayoutManager(getActivity());
////        //show newest post first, for this load from last
////        layoutManagerPosts.setStackFromEnd(true);
////        layoutManagerPosts.setReverseLayout(true);
//        //set this layout to recyclerview
////        postsRecyclerView.setLayoutManager(layoutManagerPosts);
//
//        //init posts list
////        DatabaseReference refPosts = FirebaseDatabase.getInstance().getReference("Posts");
//        //qurey to load posts
//        /*whenever user publishes a post the uid of this user is also saved as info of post
//         * so we're retrieving posts having uid equals to uid of current user*/
////        Query query = refPosts.orderByChild("uid").equalTo(uid);
//        //get all data from this ref
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                postList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    ModelPost myPosts = ds.getValue(ModelPost.class);
//
//                    //add to list
//                    postList.add(myPosts);
//
//                    //adapter
//                    adapterPost = new AdapterPost(getActivity(), postList);
//                    //set this adapter to recyclerview
//                    postsRecyclerView.setAdapter(adapterPost);
//                }
//            }
////
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void loadMyServices() {                             //////////////////////////////////////////////////////////////////
//        //linear layout for recyclerview
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        //show newest post first, for this load from last
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(true);
//        //set this layout to recyclerview
//        servicesRecyclerView.setLayoutManager(layoutManager);
//
//        //init posts list
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Services");
//        //qurey to load posts
//        /*whenever user publishes a post the uid of this user is also saved as info of post
//         * so we're retrieving posts having uid equals to uid of current user*/
//        Query query = ref.orderByChild("uid").equalTo(uid);
//        //get all data from this ref
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                serviceList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    ServiceClass myServices = ds.getValue(ServiceClass.class);
//
//                    //add to list
//                    serviceList.add(myServices);
//
//                    //adapter
//                    adapterServices = new ServiceAdapter(getActivity(), serviceList, this);
//                    //set this adapter to recyclerview
//                    servicesRecyclerView.setAdapter(adapterServices);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private boolean checkStoragePermission(){
        //check if storage permission isenabled or not
        //return true if enabled
        //return false if not enabled
        return ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
    }
    private void requestStoragePermission(){
        //request runtime storage permission
        requestPermissions(storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        //check if storage permission isenabled or not
        //return true if enabled
        //return false if not enabled
        return ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
    }
    private void requestCameraPermission(){
        //request runtime storage permission
       requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
    }
    private void showEditProfileDialog(){
        //edit profile pic/cover pic/name/phone/pet type

        //options to show in dialog
        String[] options ={"Edit Profile Picture","Edit cover Photo","Edit Name","Edit phone number","Edit Pet Type"};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Choose Action");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //handle dialog items click
                if(i==0){
                    //Edit profile pic clicked
                    pd.setMessage("Updating Profile Picture");
                    profileOrCoverPhoto = "image";//changing profile picture ,make sure to assign same value
                    showImagePicDialog();
                } else if (i==1) {
                    //Edit cover pic clicked
                    pd.setMessage("Updating PetPicture Picture");
                    profileOrCoverPhoto = "cover";//changing cover ,make sure to assign same value
                    showImagePicDialog();
                } else if (i==2) {
                    //Edit name clicked
                    pd.setMessage("Updating Name");
                    //passing name to upate its value is db
                    showNamePhoneUpdateDialog("Name");
                } else if (i==3) {
                    //edit phone number clicked
                    pd.setMessage("Updating PhoneNumber");
                    showNamePhoneUpdateDialog("phone");
                } else if (i==4) {
                   //Edit pet type clicked
                    pd.setMessage("Updating Pet Type");
                    showNamePhoneUpdateDialog("petType");
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void showNamePhoneUpdateDialog(String key){
        //custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update"+ key);
        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        EditText editText = new EditText(getActivity());
        editText.setHint("Enter"+key);
        linearLayout.addView(editText);
        builder.setView(linearLayout);

        //add buttons to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //input text
                String value = editText.getText().toString().trim();
                //validate
                if (!TextUtils.isEmpty(value)) {
                    pd.show();
                    HashMap<String,Object> result = new HashMap<>();
                    result.put(key,value);
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated...", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    // if user edit his name then change it from  hist  posts

                    if (key.equals("Name")) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                        Query query = ref.orderByChild("uid").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String child = ds.getKey();

                                    dataSnapshot.getRef().child(child).child("uName").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Please enter "+key, Toast.LENGTH_SHORT).show();
                }


            }
        });
        //add buttons to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        //create and show dialog
        builder.create().show();

    }
    private void showImagePicDialog(){
        //show dialog containing options camera and gallery to pic the image

        //options to show in dialog
        String[] options ={"Camera","Gallery"};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Pic Image From");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //handle dialog items click
                if(i==0){
                    //Camera clicked
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                } else if (i==1) {
                    //Gallery clicked
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // this method is called when users  press allow or  deny from permission request dialog
        //here we will handle permission cases (allowed or denied)
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                //picking from Camera, first check if camera and storage permissions allowed or not
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        //permissions enabled
                        pickFromCamera();
                    }
                    else {
                        //permissions denied
                        Toast.makeText(getActivity(), "Please enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{

                //picking from Gallery, first check if camera and storage permissions allowed or not
                if (grantResults.length>0){

                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if ( writeStorageAccepted){
                        //permissions enabled
                        pickFromGallery();
                    }
                    else {
                        //permissions denied
                        Toast.makeText(getActivity(), "Please enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this method will be called after picking image from Camera and Gallery
        if(resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery,get uri of image
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);
            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //image is picked from camera,get uri of image
                uploadProfileCoverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void uploadProfileCoverPhoto(Uri uri){
        pd.show();
        /*instead of creating seperate functions for profile pic and cover photo
        we aredoing the work for both in same function
        we will create one string varibale profileOrCoverPhoto and assign its value as "image"
        when user clicks on edit profile pic. and assign it as "cover" when user clicks on "edit cover
        pic".
        "image_uri" contains the uri of image picked either from camer or gallery. we are using user id as
        name for image so that one user can only have one profile pic and one cover pic
         */

        //path and name of image to be stored  in firebase storage
        String filePathAndName = storagePath+""+profileOrCoverPhoto+"_"+user.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image  is uploaded to storage
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        //check if image is uploaded or not and url is recieved
                        if (uriTask.isSuccessful()){
                            //image uploaded
                            //add/update url in users DB
                            HashMap<String, Object> results =new HashMap<>();
                            results.put(profileOrCoverPhoto,downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Image Updated...", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Error Updating Image...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            // if chnage its dp also change in hist posts
                            if (profileOrCoverPhoto.equals("image")) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                Query query = ref.orderByChild("uid").equalTo(uid);
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            String child = ds.getKey();
                                            dataSnapshot.getRef().child(child).child("uDp").setValue(downloadUri.toString());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        else {
                            //error
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pickFromCamera(){
        //intent of picking image from device camers
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        //put image uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }
    private void pickFromGallery(){
        // pick from camera
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }
    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user !=null){
            //User is signed in stay in my profile
            //set email of logged in user
            // mProfile.setText(user.getEmail());
            uid= user.getUid();
        }
        else {
            //user not signed in, go to login/register
            startActivity(new Intent(getActivity(), MainActivity.class));
            requireActivity().finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);// to show menu options in fragment
        super.onCreate(savedInstanceState);
    }

    //inflate menu options
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        //inflating menu
        inflater.inflate(R.menu.menu_main,menu);
        //hide search from action bar
        menu.findItem(R.id.action_search).setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }
    //handle menu item click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        if (id == R.id.action_add_post) {
            startActivity(new Intent(getActivity(),AddPostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

    }
}