package com.example.artbookudemy

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.icu.util.Freezable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.artbookudemy.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddFragment : Fragment() {

    private lateinit var binding : FragmentAddBinding
    //activity and permission launchers defined
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedBitmap : Bitmap? = null
    private lateinit var database : SQLiteDatabase
    private val args : AddFragmentArgs by navArgs()
    private var selectedId : Int = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_delete,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.deleteMenuItem){
            try {
                val database =
                    requireActivity().openOrCreateDatabase("Arts", Context.MODE_PRIVATE, null)

                database.execSQL("DELETE FROM arts WHERE id = ?", arrayOf(selectedId.toString()))

                findNavController().navigate(R.id.action_addFragment_to_homeFragment)

            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialize database
        database = requireActivity().openOrCreateDatabase("Arts",Context.MODE_PRIVATE,null)

        //register launchers
        registerLaunchers()

        //if an item clicked from home
        selectedId = args.id
        if(selectedId != -1){
            binding.addButton.visibility = View.INVISIBLE
            try {
                val database =
                    requireActivity().openOrCreateDatabase("Arts", Context.MODE_PRIVATE, null)

                val cursor = database.rawQuery(
                    "SELECT * FROM arts WHERE id = ?",
                    arrayOf(selectedId.toString())
                )
                val artNameIx = cursor.getColumnIndex("artname")
                val artistNameIx = cursor.getColumnIndex("artistname")
                val artYear = cursor.getColumnIndex("artyear")
                val artImageIx = cursor.getColumnIndex("artimage")

                while (cursor.moveToNext()) {
                    //get values from db
                    binding.artName.setText(cursor.getString(artNameIx))
                    binding.artistName.setText(cursor.getString(artistNameIx))
                    binding.artYear.setText(cursor.getString(artYear))

                    //convert byte array to bitmap to show on image view
                    val imageByteArray = cursor.getBlob(artImageIx)
                    val imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)

                    binding.artImage.setImageBitmap(imageBitmap)

                }



                cursor.close()
            }catch (e : Exception){
                e.printStackTrace()
            }

        }



        //select image button
        binding.artImage.setOnClickListener {
            selectImage()
        }

        //save button
        binding.addButton.setOnClickListener {
            //save and go to home screen
            save()
            findNavController().navigate(R.id.action_addFragment_to_homeFragment)
        }
    }

    //saving art details function for save button
    private fun save(){
        //get edit text's inputs
        val artName = binding.artName.text.toString()
        val artistName = binding.artistName.text.toString()
        val artYear = binding.artYear.text.toString()

        //convert image bitmap tp byte array for SQL
        if(selectedBitmap != null){
            val smallerBitmap = makeSmallerBitmap(selectedBitmap!!,400)
            val outputStream = ByteArrayOutputStream()
            smallerBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            try {
                database.execSQL("CREATE TABLE IF NOT EXISTS arts (id INTEGER PRIMARY KEY, artname VARCHAR, artistname VARCHAR, artyear VARCHAR, artimage BLOB )")
                val sqlString = "INSERT INTO arts (artname, artistname, artyear,artimage) VALUES (?,?,?,?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1,artName)
                statement.bindString(2,artistName)
                statement.bindString(3,artYear)
                statement.bindBlob(4,byteArray)

                statement.execute()


            }catch (e: Exception){
                e.printStackTrace()
            }


        }


    }

    //select image view's function for selecting image from gallery with needed permissions
    private fun selectImage(){
        activity?.let {
            //above API 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                //if permission is not granted: ask permission
                if(ContextCompat.checkSelfPermission(requireActivity().applicationContext,READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                    //check if permission rationale should be shown
                    if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),READ_MEDIA_IMAGES)) {
                        Snackbar.make(
                            requireView(),
                            "Permission needed for accessing the gallery!",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction("Give Permission"
                            ) {
                                //request permission with launcher
                                permissionLauncher.launch(READ_MEDIA_IMAGES)
                            }
                            .show()
                        //request permission
                        permissionLauncher.launch(READ_MEDIA_IMAGES)

                    } else{
                        //request permission
                        permissionLauncher.launch(READ_MEDIA_IMAGES)
                    }


                }
                //permission is granted : go to gallery
                else{
                    val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                }
            }

            //below API 13
            else{
                //same permission process but with READ_EXTERNAL_STORAGE for blow API 13
                //permission is not granted : ask permission
                if(ContextCompat.checkSelfPermission(requireActivity().applicationContext, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
                    //should android show permission rationale
                    if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), READ_EXTERNAL_STORAGE)){
                        //show snackbar for asking permission
                        Snackbar.make(requireView(),
                            "Permission needed for accessing the gallery!",Snackbar.LENGTH_INDEFINITE)
                            .setAction("Give Permission"
                            ) {
                                //request permission
                                permissionLauncher.launch(READ_EXTERNAL_STORAGE)
                            }.show()
                    }
                    else{
                    //request permission
                        permissionLauncher.launch(READ_EXTERNAL_STORAGE)
                    }
                }
                //permission is granted: go to gallery
                else{
                    //go to gallery
                    val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                 }
            }
        }
    }

    //register activity and permission launchers
    private fun registerLaunchers(){
        //going to gallery and selecting image processes
        activityResultLauncher  = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result->
            // if result code is ok that means we can go to gallery
            if(result.resultCode == AppCompatActivity.RESULT_OK){
                val intentFromResult = result.data
                //if not null we can get image data from gallery
                if(intentFromResult != null){
                    // get (image uri)
                    val galleryImage = intentFromResult.data
                    try {
                        //try-catch block for error handling
                        //converting bitmap(for saving to sql) above API 28
                        if(Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                galleryImage!!
                            )
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            //show picture in image view
                            binding.artImage.setImageBitmap(selectedBitmap)
                        }
                        //below API 28
                        else{
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,galleryImage
                            )
                            //show picture in image view
                            binding.artImage.setImageBitmap(selectedBitmap)
                        }

                    }catch (e : IOException){
                        e.printStackTrace()
                    }
                }
            }
        }

        //asking permission processes
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
        { success->
            //if permission is granted go to gallery
            if(success){
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            //permission denied
            else{
                Toast.makeText(requireContext(),"Permission Needed!",Toast.LENGTH_LONG).show()
            }
        }
    }

    //make image data smaller for saving to SQL
    private fun makeSmallerBitmap(image : Bitmap, maxSize: Int) : Bitmap{
        //course instructor's algorithm for making rational bitmaps
        //whether they are landscape or vertical images
        var width = image.width
        var height = image.height

        //get ratio to decide if image portrait or landscape
        val bitmapRatio = width.toDouble() / height.toDouble()
        if(bitmapRatio > 0){
            //landscape
            width = maxSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        }
        else{
            //portrait
            height = maxSize
            val scaledHeight = height * bitmapRatio
            height = scaledHeight.toInt()
        }

        return Bitmap.createScaledBitmap(image, width,height,true)
    }






}