package com.example.cloneinstagramudemy.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cloneinstagramudemy.databinding.ActivityPostBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.Date
import java.util.UUID

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedImage : Uri? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize launchers
        registerLaunchers()

        //initialize firebase auth, storage, firestore
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        //select image functionality
        binding.artImage.setOnClickListener {
            selectImage()
        }
        //post button functionality
        binding.addButton.setOnClickListener {
            val description = binding.postDescription.text .toString()
            post(description)
        }

    }

    //function to upload a post for post button
    private fun post(description : String){
        val uuid = UUID.randomUUID()
        val imageName = "${uuid}.jpeg"
        //get reference to storage for uploading images and getting urls
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)

        if(selectedImage != null){
            //save image to storage
            imageReference.putFile(selectedImage!!).addOnSuccessListener { it->
                //if saving image succeeds : download url for firestore
                val postedPictureReference = storage.reference.child("images").child(imageName)
                postedPictureReference.downloadUrl.addOnSuccessListener { url ->
                    //get image url to upload to the feed
                    val imageUrl = url.toString()

                    if(auth.currentUser != null){
                        //if user is logged in: Create Post model for saving to database
                        val postMap = hashMapOf<String,Any>()
                        postMap.put("imageUrl",imageUrl)
                        postMap.put("userEmail",auth.currentUser!!.email!!)
                        postMap.put("description",description)
                        postMap.put("date",Timestamp.now().toDate())

                        firestore.collection("Posts").add(postMap).addOnSuccessListener {
                            //create collection, add data to db and finish activity
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this@PostActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }.addOnFailureListener {
                //if saving image fails
                Toast.makeText(this@PostActivity,it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    //function to select image from gallery
    private fun selectImage(){
        //above API 13+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //if permission is not granted: request permission
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED){
                //check if permission rationale should be shown
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )){
                    Snackbar.make(binding.root,
                        "Permission needed for accessing the gallery!",
                        Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                        //request permission
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                    }.show()
                }else{
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
            //permission is granted : go to gallery
            else{
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }
        }
        //below API 13
        else{
            //same permission process but with READ_EXTERNAL_STORAGE for blow API 13
            //permission is not granted : ask permission
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED){
                //should show permission rationale
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )){
                    //show snackbar for asking permission
                    Snackbar.make(binding.root,
                        "Permission needed for accessing the gallery!",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Give Permission"){
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                        }.show()
                }
                else{
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                }
            }
            //permission is granted : go to gallery
            else{
                //go to gallery
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }

        }

    }

    //register activity and permission launchers
    private fun registerLaunchers(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            //if result code is ok that means we can go to the gallery
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                //if not null we can get image data from gallery
                if(intentFromResult != null){
                    //get image uri
                    selectedImage = intentFromResult.data
                    binding.artImage.setImageURI(selectedImage)
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
                success ->
            //if permission is granted go to gallery
            if(success){
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            //permission denied
            else{
                Toast.makeText(this,"Permission needed for gallery!", Toast.LENGTH_LONG).show()
            }
        }

    }

}