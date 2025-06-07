package xcom.niteshray.xapps.xblockit.ui.Screens.Web

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xcom.niteshray.xapps.xblockit.util.BlockWesiteUtil

class WebViewModel(application: Application) : AndroidViewModel(application) {
    private val _web = mutableStateListOf<String>()
    val web : List<String> get() = _web

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val list = BlockWesiteUtil.GetWebsite(application)
            list.map { _web.add(it) }
        }
    }

    fun addWebsite(url : String){
        viewModelScope.launch(Dispatchers.IO) {
            BlockWesiteUtil.AddWebsite(getApplication(),url)
            _web.add(url)
        }
    }

    fun deleteWebsite(url : String){
        viewModelScope.launch(Dispatchers.IO) {
            BlockWesiteUtil.RemoveWebsite(getApplication(),url)
            _web.remove(url)
        }
    }

    fun validateWebsite(url : String) : Boolean{
        if (url.isEmpty()) return false
        return !web.contains(url)
    }
}