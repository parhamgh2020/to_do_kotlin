package com.example.sql_1.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.sql_1.R
import com.example.sqliteapplication.DataLayer.ChoresDatabaseHandler
import com.example.sqliteapplication.models.Chore

class MainActivity : AppCompatActivity() {

    var dbHandler: ChoresDatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val enterChore = findViewById<TextView>(R.id.editChoreNameId)
        val assignedBy = findViewById<TextView>(R.id.editAssignedById)
        val assignedTo = findViewById<TextView>(R.id.editAssignedToId)
        val saveButton = findViewById<Button>(R.id.saveButtonId1)

        dbHandler = ChoresDatabaseHandler(this)

        checkDB()

        saveButton.setOnClickListener {
            if (
                !TextUtils.isEmpty(enterChore.text.toString()) &&
                !TextUtils.isEmpty(assignedBy.text.toString()) &&
                !TextUtils.isEmpty(assignedTo.text.toString())
            ) {
                // save data to database
                val chore = Chore()
                chore.choreName = enterChore.text.toString()
                chore.assignedBy = assignedBy.text.toString()
                chore.assignedTo = assignedTo.text.toString()

                saveChoreToDatabase(chore)
                Toast.makeText(this, "your Chore has been saved successfully", Toast.LENGTH_LONG)
                    .show()

                startActivity(Intent(this, ListChoreActivity::class.java))

            } else {
                Toast.makeText(this, "please enter a chore !!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun checkDB() {
        if (dbHandler!!.getChoresCount() > 0) {
            startActivity(Intent(this, ListChoreActivity::class.java))
        }
    }

    fun saveChoreToDatabase(chore: Chore) {
        dbHandler!!.createChore(chore)
    }
}