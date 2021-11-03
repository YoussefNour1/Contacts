package com.ynour.contacts.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ynour.contacts.R
import com.ynour.contacts.model.ContactData

class ContactRecyclerView(
    private val context: Context,
    private val contactsList: ArrayList<ContactData>
) : RecyclerView.Adapter<ContactRecyclerView.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var name: TextView? = null
        var number: TextView? = null
        private var mMenu: ImageView? = null

        init {
            name = itemView.findViewById(R.id.mName)
            number = itemView.findViewById(R.id.mNumber)
            mMenu = itemView.findViewById(R.id.mMenu)
            mMenu?.setOnClickListener { popUpMenus(it) }
            itemView.setOnClickListener {
                onClick(itemView)
            }
        }

        @SuppressLint("DiscouragedPrivateApi", "NotifyDataSetChanged", "InflateParams")
        private fun popUpMenus(it: View?) {
            val popUpMenus = PopupMenu(context, it)
            val position = contactsList[adapterPosition]
            popUpMenus.inflate(R.menu.m)
            popUpMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        val v = LayoutInflater.from(context).inflate(R.layout.add_item, null)
                        val nName = v.findViewById<EditText>(R.id.name)
                        val nNumber = v.findViewById<EditText>(R.id.num)
                        nName.setText(position.name)
                        nNumber.setText(position.number)
                        AlertDialog.Builder(context).apply {
                            setView(v)
                            setPositiveButton("Edit") { dialog, _ ->
                                if (nName.text.isNotEmpty() && nNumber.text.isNotEmpty()) {
                                    position.name = nName?.text.toString()
                                    position.number = nNumber?.text.toString()
                                    notifyDataSetChanged()
                                    Toast.makeText(
                                        context,
                                        "Contact Information was edited successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                                dialog.dismiss()
                            }
                            setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()

                            }
                            create()
                            show()
                        }
                        true
                    }
                    R.id.delete -> {
                        AlertDialog.Builder(context).apply {
                            setTitle("Delete")
                            setIcon(R.drawable.ic_warning)
                            setMessage("Are you sure delete this Contact")
                            setPositiveButton("Yes") { dialog, _ ->
                                contactsList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(
                                    context,
                                    "Contact was deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.dismiss()
                            }
                            setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            create()
                            show()
                        }
                        true
                    }
                    else -> true
                }
            }
            popUpMenus.show()
            val popUp = PopupMenu::class.java.getDeclaredField("mPopup")
            popUp.isAccessible = true
            val menu = popUp.get(popUpMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }

        override fun onClick(v: View?) {
            Toast.makeText(
                context,
                "Name: ${name?.text.toString()}\nNumber: ${number?.text.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact: ContactData = contactsList[position]
        holder.name?.text = contact.name
        holder.number?.text = contact.number
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }
}