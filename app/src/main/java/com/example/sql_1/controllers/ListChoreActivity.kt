package com.example.sql_1.controllers

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sql_1.R
import com.example.sql_1.adapters.ChoreListAdapter
import com.example.sqliteapplication.DataLayer.ChoresDatabaseHandler
import com.example.sqliteapplication.models.Chore

class ListChoreActivity : AppCompatActivity() {
    private var adapter: ChoreListAdapter? = null
    private var choreList: ArrayList<Chore>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    var dbHandler: ChoresDatabaseHandler? = null
    private var alertDialogBuilder: AlertDialog.Builder? = null
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_chore)

        choreList = ArrayList()
        layoutManager = LinearLayoutManager(this)
        dbHandler = ChoresDatabaseHandler(this)
        getAllDataFromDatabase()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.addId) {
            // Toast.makeText(this, "add button selected", Toast.LENGTH_LONG).show()
            createPopupDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createPopupDialog() {
        val view = layoutInflater.inflate(R.layout.popup, null)
        val choreName = view.findViewById<TextView>(R.id.editChoreNamePopId)
        val assignedBy = view.findViewById<TextView>(R.id.editAssignedToPopId)
        val assignedTo = view.findViewById<TextView>(R.id.editAssignedByPopId)
        val saveButton = view.findViewById<Button>(R.id.saveButtonPopId)

        alertDialogBuilder = AlertDialog.Builder(this).setView(view)
        alertDialog = alertDialogBuilder!!.create()
        alertDialog?.show()

//        alertDialog?.setOnDismissListener {
//            Toast.makeText(this, "Close Alert Dialog", Toast.LENGTH_LONG).show()
//            choreList = dbHandler!!.readChores()
//            choreList?.reverse()
//            adapter = ChoreListAdapter(choreList!!, this)
//            recyclerViewId.layoutManager = layoutManager
//            recyclerViewId.adapter = adapter
//            adapter!!.notifyDataSetChanged()
//        }

        saveButton.setOnClickListener {
            val name = choreName.text.toString().trim()
            val aTo = assignedTo.text.toString().trim()
            val aBy = assignedBy.text.toString().trim()

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(aTo) && !TextUtils.isEmpty(aBy)) {

                val chore: Chore = Chore()
                chore.choreName = name
                chore.assignedTo = aTo
                chore.assignedBy = aBy

                dbHandler?.createChore(chore)
                alertDialog!!.dismiss()
//                startActivity(Intent(this,ChoreListActivity::class.java))
//                finish()
                getAllDataFromDatabase()

            } else {
                Toast.makeText(this, "Please Enter Chore Details !!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getAllDataFromDatabase() {
        choreList = dbHandler!!.readChores()
        choreList?.reverse()
        adapter = ChoreListAdapter(this, choreList!!)
        val recyclerViewId = findViewById<RecyclerView>(R.id.recyclerViewId)
        recyclerViewId.layoutManager = layoutManager
        recyclerViewId.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }
}