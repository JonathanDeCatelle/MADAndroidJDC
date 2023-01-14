package com.example.delawaretrackandtraceapp.screens.adres

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.delawaretrackandtraceapp.databinding.FragmentAdresBinding
import kotlinx.coroutines.withTimeoutOrNull

class AdresViewModel : ViewModel() {
    private var _straat = MutableLiveData<String>()
        val straat : LiveData<String>
        get() = _straat
    private var _huisNr = MutableLiveData<String>()
        val huisNr : LiveData<String>
        get() = _huisNr
    private var _postCod = MutableLiveData<String>()
        val postCod : LiveData<String>
        get() = _postCod
    private var _stad = MutableLiveData<String>()
        val stad : LiveData<String>
        get() = _stad

    fun leesFormulierIn(bind : FragmentAdresBinding): Boolean {
        return if (bind.textStraat.text.isBlank() || bind.textHuisNr.text.isBlank() || bind.textPostcode.text.isBlank() || bind.textStad.text.isBlank()) {
            false
        } else {
            _straat.value = bind.textStraat.text.toString()
            _huisNr.value = bind.textHuisNr.text.toString()
            _postCod.value = bind.textPostcode.text.toString()
            _stad.value = bind.textStad.text.toString()
            true
        }
    }

    fun geefAdresAlsString(): String {
        return String.format(_straat.value + " " + _huisNr.value + ", " + _postCod.value + " " + _stad.value)
    }


}