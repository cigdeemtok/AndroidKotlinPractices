package com.example.artbookudemy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.versionedparcelable.VersionedParcelize
import com.example.artbookudemy.databinding.FragmentHomeBinding
import java.lang.Exception


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var artAdapter: ArtAdapter
    private lateinit var artList : ArrayList<Art>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialize art array
        artList = ArrayList<Art>()


        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        artAdapter = ArtAdapter(artList){ art->

            val direction = HomeFragmentDirections.actionHomeFragmentToAddFragment().setId(art.id)
            findNavController().navigate(direction)
        }
        binding.recyclerViewHome.adapter = artAdapter

        try {
            val database = requireActivity().openOrCreateDatabase("Arts", Context.MODE_PRIVATE,null)

            val cursor = database.rawQuery("SELECT * FROM arts", null)
            val artNameIx = cursor.getColumnIndex("artname")
            val artIdIx = cursor.getColumnIndex("id")
            val artImageIx = cursor.getColumnIndex("artimage")

            while(cursor.moveToNext()){
                //get values from db
                val name = cursor.getString(artNameIx)
                val id = cursor.getInt(artIdIx)

                //convert byte array to bitmap to show on image view
                val imageByteArray = cursor.getBlob(artImageIx)
                val imageBitmap = BitmapFactory.decodeByteArray(imageByteArray,0,imageByteArray.size)

                val art = Art(id,name,imageBitmap)
                artList.add(art)
            }

            artAdapter.notifyDataSetChanged()

            cursor.close()

        }catch (e : Exception){
            e.printStackTrace()
        }

        if(artList.isEmpty()){
            binding.recyclerViewHome.visibility = View.GONE
            binding.alertTextHome.visibility = View.VISIBLE
        }else{
            binding.recyclerViewHome.visibility = View.VISIBLE
            binding.alertTextHome.visibility = View.GONE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_options,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.addMenuItem){
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}


