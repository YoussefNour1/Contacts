package com.ynour.contacts

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ynour.contacts.adapter.ContactRecyclerView
import com.ynour.contacts.databinding.ActivityMainBinding
import com.ynour.contacts.model.ContactData

class MainActivity : AppCompatActivity() {
    private lateinit var contacts: ArrayList<ContactData>
    private lateinit var contactRecyclerView: ContactRecyclerView
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /* Set ArrayList */
        contacts = ArrayList()
        /* Set Adapter */
        contactRecyclerView = ContactRecyclerView(this, contacts)
        binding.myRecycler.adapter = contactRecyclerView
        binding.myRecycler.layoutManager = LinearLayoutManager(this)
        /*Set OnClickListener for FloatingButton */
        binding.addingContact.setOnClickListener { addContact() }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addContact() {
        val inflater = LayoutInflater.from(this).inflate(R.layout.add_item, null)
        val editName = inflater.findViewById<EditText>(R.id.name)
        val editNumber = inflater.findViewById<EditText>(R.id.num)
        /* set the dialog*/
        val dialog = AlertDialog.Builder(this)
        dialog.setView(inflater)
        dialog.setPositiveButton("Add") { e,
                                          _ ->
            if (editName.text.isEmpty() || editNumber.text.isEmpty()) {
                Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    contacts.add(ContactData(editName.text.toString(), editNumber.text.toString()))
                    Toast.makeText(this, "New Contact Added Successfully", Toast.LENGTH_SHORT)
                        .show()
                    e.dismiss()
                } catch (e: Exception) {
                    Log.d("zzzzz", "addContact: $e")
                }
            }
        }
        dialog.setNegativeButton("Cancel") { e, _ ->
            e.dismiss()
        }

        dialog.create()
        dialog.show()
    }
}