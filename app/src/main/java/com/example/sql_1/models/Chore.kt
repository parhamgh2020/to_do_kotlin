package com.example.sqliteapplication.models

import java.text.SimpleDateFormat
import java.util.*

class Chore() {
    var id: Int? = null
    var choreName: String? = null
    var assignedBy: String? = null
    var assignedTo: String? = null
    var timeAssigned: Long? = null

    fun showHumanDate(chore: Chore): String {
        val simple = SimpleDateFormat("dd/MM/yyyy")
        val result = Date(chore.timeAssigned!!)
        return simple.format(result)
    }

    constructor(
        choreName: String,
        assignedBy: String,
        assignedTo: String,
        timeAssigned: Long,
        id: Int
    ) : this() {
        this.choreName = choreName
        this.assignedBy = assignedBy
        this.assignedTo = assignedTo
        this.timeAssigned = timeAssigned
        this.id = id
    }
}