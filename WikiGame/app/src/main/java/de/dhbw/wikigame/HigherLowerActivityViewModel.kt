package de.dhbw.wikigame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HigherLowerActivityViewModel : ViewModel() {

    var firstWikiArticleThumbnailURL = ""
    var secondWikiArticleThumbnailURL = ""


    val currentFirstArticleThumbnailURL: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val currentSecondArticleThumbnailURL: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


}