package com.bignerdranch.android.qrcodescaner.ui.generate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.bignerdranch.android.qrcodescaner.R
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.fragment_generate.*

class GenerateFragment : Fragment() {

	var imQr: ImageView? = null
	var bGenerate: Button? = null
	var editText: EditText? = null
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		val view = inflater.inflate(R.layout.fragment_generate, container, false)
		imQr = view.findViewById(R.id.imQR)
		bGenerate = view.findViewById(R.id.generateButton)
		editText = view.findViewById(R.id.textGenerate)
		bGenerate?.setOnClickListener {
			generateQR(editText!!.text.toString())
		}
		return view
	}


	private fun generateQR(text: String) {
		val qrGenerator = QRGEncoder(text, null, QRGContents.Type.TEXT, 500)
		try {
			val bMap = qrGenerator.encodeAsBitmap()
			imQr!!.setImageBitmap(bMap)
		}catch (e:WriterException){

		}
	}

	companion object {
		@JvmStatic
		fun newInstance() = GenerateFragment()
	}
}