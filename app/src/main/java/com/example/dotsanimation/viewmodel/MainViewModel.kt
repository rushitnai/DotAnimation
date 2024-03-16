package com.example.dotsanimation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

     var redDotIndex = MutableLiveData<Int>()
     var blueDotIndex = MutableLiveData<Int>()
     var greenDotIndex = MutableLiveData<Int>()
}