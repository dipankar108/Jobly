package com.university_project.jobly.bottomsheetdialogm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.university_project.jobly.R

class UpdateText(val field: String, val preValue: String) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.updateprofiledialog, container, false)
        val titleView = view.findViewById<TextView>(R.id.tv_titleview_update_id)
        val takeInput = view.findViewById<EditText>(R.id.et_bottomFragment_Update_id)
        val submitBtn = view.findViewById<Button>(R.id.btn_submit_update_id)
        return view
    }
}