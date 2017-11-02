package com.yadaniil.blogchain.screens.home

import android.app.Dialog
import android.content.Context
import it.gmariotti.changelibs.library.view.ChangeLogRecyclerView
import android.view.LayoutInflater
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.yadaniil.blogchain.R


/**
 * Created by danielyakovlev on 10/11/17.
 */


class ChangelogDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val chgList = layoutInflater.inflate(R.layout.changelog_fragment_dialogmaterial, null) as ChangeLogRecyclerView

        return AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
                .setTitle(R.string.whats_new)
                .setView(chgList)
                .setPositiveButton(R.string.ok, { dialog, whichButton -> dialog.dismiss() })
                .create()
    }

}