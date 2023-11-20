package uk.ac.tees.mad.t7199384.utils.data

import android.app.AlertDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.t7199384.R

@Composable
fun WorldChangeButton(world: String) {
    val context = LocalContext.current
    val array: Array<String> = context.resources.getStringArray(R.array.world_array)
    var currentWorld=world

    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setTitle("Choose world")
        .setPositiveButton("Confirm"){dialog, which ->
            var sharedPref = context.getSharedPreferences(context.resources.getString(R.string.world_file_key), Context.MODE_PRIVATE)
            var edit = sharedPref.edit()
            edit.putString("world",currentWorld)
            edit.apply()
        }
        .setNegativeButton("Cancel"){dialog, which -> ""}
        .setSingleChoiceItems(array,0,){ dialog, which -> currentWorld=array[which]}

    IconButton( modifier=Modifier.size(80.dp),
        onClick = {
            val dialog: AlertDialog = builder.create()
            dialog.show()
        },
    ) {
        Box(modifier= Modifier
            .aspectRatio(1f)
            .size(80.dp)
            .background(Color.White, shape= CircleShape)
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
        Icon(painterResource(R.drawable.world_icon), "Change World Server", tint= Color.DarkGray,modifier=Modifier.clip(CircleShape).padding(4.dp))
        }
    }

}