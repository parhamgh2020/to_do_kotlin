package com.example.sql_1.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.sql_1.R
import com.example.sqliteapplication.DataLayer.ChoresDatabaseHandler
import com.example.sqliteapplication.models.Chore

class ChoreListAdapter(private val context: Context, private val list: ArrayList<Chore>) :
    RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoreListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view, context, list)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    inner class ViewHolder(itemView: View, context: Context, choreList: ArrayList<Chore>) :
        RecyclerView.ViewHolder(itemView) {

        var mContext = context
        var mList = choreList

        var choreName = itemView.findViewById<TextView>(R.id.choreNameId)
        var assignedBy = itemView.findViewById<TextView>(R.id.assignedById)
        var assignedTo = itemView.findViewById<TextView>(R.id.assignedToId)
        var assignedDate = itemView.findViewById<TextView>(R.id.dateId)
        var deleteButton = itemView.findViewById<Button>(R.id.deleteId)
        var editButton = itemView.findViewById<Button>(R.id.editId)

        fun bindView(chore: Chore) {
            choreName.text = chore.choreName
            assignedBy.text = chore.assignedBy
            assignedTo.text = chore.assignedTo
            assignedDate.text = chore.showHumanDate(chore)

            var mPosition: Int = adapterPosition
            var chore: Chore = mList[mPosition]

            deleteButton.setOnClickListener {
                deleteChore(chore.id!!.toInt())
                mList.removeAt(mPosition)
                notifyItemRemoved(mPosition)
            }

            editButton.setOnClickListener {
                updateChore(chore)
            }
        }
        fun deleteChore(id: Int) {
            val db = ChoresDatabaseHandler(mContext)
            db.deleteChore(id)
        }

        fun updateChore(chore: Chore) {

            val alertDialogBuilder: AlertDialog.Builder?
            val alertDialog: AlertDialog?
            val dbHandler = ChoresDatabaseHandler(mContext)

            val view = LayoutInflater.from(mContext).inflate(R.layout.popup, null)
            val choreName = view.findViewById<TextView>(R.id.editChoreNamePopId)
            val assignedBy = view.findViewById<TextView>(R.id.editAssignedByPopId)
            val assignedTo = view.findViewById<TextView>(R.id.editAssignedToPopId)
            val saveButton = view.findViewById<Button>(R.id.saveButtonPopId)

            choreName.setText(chore.choreName)
            assignedBy.setText(chore.assignedBy)
            assignedTo.setText(chore.assignedTo)

            alertDialogBuilder = AlertDialog.Builder(mContext).setView(view)
            alertDialog = alertDialogBuilder!!.create()
            alertDialog?.show()

            saveButton.setOnClickListener {
                val name = choreName.text.toString().trim()
                val aTo = assignedTo.text.toString().trim()
                val aBy = assignedBy.text.toString().trim()

                if (
                    !TextUtils.isEmpty(name) &&
                    !TextUtils.isEmpty(aTo) &&
                    !TextUtils.isEmpty(aBy)
                ) {
                    chore.choreName = name
                    chore.assignedTo = aTo
                    chore.assignedBy = aBy

                    dbHandler?.updateChore(chore)
                    notifyItemChanged(adapterPosition,chore)
                    alertDialog!!.dismiss()

                    // getAllDataFromDatabase()

                } else {

                }
            }
        }

    }
}