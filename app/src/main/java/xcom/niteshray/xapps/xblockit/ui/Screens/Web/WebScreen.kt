package xcom.niteshray.xapps.xblockit.ui.Screens.Web

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import xcom.niteshray.xapps.xblockit.R
import xcom.niteshray.xapps.xblockit.ui.theme.Blue
import xcom.niteshray.xapps.xblockit.ui.theme.PurpleGrey40

@Composable
fun WebScreen(webViewModel: WebViewModel = viewModel()) {
    val urltext = remember { mutableStateOf("") }
    val context = LocalContext.current
    val weblist = webViewModel.web
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color(0xFF1C1C1E), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Block Websites",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
                OutlinedTextField(
                    value = urltext.value,
                    onValueChange = {
                        urltext.value = it
                    },
                    label = {
                        Text(text = "Enter URL")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleGrey40,
                        unfocusedBorderColor = PurpleGrey40
                    )
                )

                Button(onClick = {
                    if (!webViewModel.validateWebsite(urltext.value)){
                        Toast.makeText(context,"Website Already Blocked",Toast.LENGTH_SHORT).show()
                        return@Button
                    }else{
                        webViewModel.addWebsite(urltext.value)
                        urltext.value = ""
                        Toast.makeText(context,"Website Blocked",Toast.LENGTH_SHORT).show()
                    }

                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    )
                    ) {
                    Text("Blockit", fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(4.dp)
                        )
                }
            }
        }
        Spacer(modifier =  Modifier.height(8.dp))
        LazyColumn {
            items(weblist){
                BlockWebItems(it){
                    webViewModel.deleteWebsite(it)
                }
            }
        }
    }
}

@Composable
fun BlockWebItems(url : String, ondelete : (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color(0xFF1C1C1E), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.web),
                contentDescription = "Web",
                modifier = Modifier.padding(8.dp).size(24.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
            Text(
                text = url,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete site",
                tint = Color.Red,
                modifier = Modifier.padding(8.dp).size(24.dp).clickable{
                    //delete website
                    ondelete(url)
                }
            )
        }
    }
}