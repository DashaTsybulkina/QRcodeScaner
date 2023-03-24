package com.bignerdranch.android.qrcodescaner.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.qrcodescaner.R
import com.bignerdranch.android.qrcodescaner.db.DbHelperI
import com.bignerdranch.android.qrcodescaner.db.convertors.toFormattedDisplay
import com.bignerdranch.android.qrcodescaner.db.entities.QrResult
import com.bignerdranch.android.qrcodescaner.ui.dialogs.QrCodeResultDialog
import kotlinx.android.synthetic.main.single_item_qr_result.view.*

class ScannedResultListAdapter(
    var dbHelperI: DbHelperI,
    var context: Context,
    private var listOfScannedResult: MutableList<QrResult>,
    var status:String
) :
    RecyclerView.Adapter<ScannedResultListAdapter.ScannedResultListViewHolder>() {

    private var resultDialog: QrCodeResultDialog =
        QrCodeResultDialog(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedResultListViewHolder {
        return ScannedResultListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_item_qr_result,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfScannedResult.size
    }

    override fun onBindViewHolder(holder: ScannedResultListViewHolder, position: Int) {
        holder.bind(listOfScannedResult[position], position)
    }

    inner class ScannedResultListViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind(qrResult: QrResult, position: Int) {
            view.result.text = qrResult.result!!
            view.tvTime.text = qrResult.calendar.toFormattedDisplay()
            setResultTypeIcon(qrResult.resultType)
            view.favouriteIcon.visibility = View.GONE
            Log.d("ADAPTER", status)
            if(status =="All")
                setFavourite(qrResult.favourite)
            onClicks(qrResult, position)
        }

        private fun setResultTypeIcon(resultType: String?) {

        }

        private fun setFavourite(isFavourite: Boolean) {
            if (isFavourite && status == "All")
                view.favouriteIcon.visibility = View.VISIBLE
            else {
                view.favouriteIcon.visibility = View.GONE
                Log.d("ADAPTER", "EXIT")
            }
        }


        private fun onClicks(qrResult: QrResult, position: Int) {
            view.setOnClickListener {
                resultDialog.show(qrResult)
            }

            view.setOnLongClickListener {
                showDeleteDialog(qrResult, position)
                return@setOnLongClickListener true
            }
        }

        private fun showDeleteDialog(qrResult: QrResult, position: Int) {
            AlertDialog.Builder(context, R.style.CustomAlertDialog).setTitle(context.getString(R.string.delete))
                .setMessage(context.getString(R.string.want_to_delete))
                .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                    deleteThisRecord(qrResult, position)
                }
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }.show()
        }

        private fun deleteThisRecord(qrResult: QrResult, position: Int) {
            dbHelperI.deleteQrResult(qrResult.id!!)
            listOfScannedResult.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}