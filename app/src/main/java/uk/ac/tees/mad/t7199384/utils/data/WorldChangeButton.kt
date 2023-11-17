package uk.ac.tees.mad.t7199384.utils.data

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            Toast.makeText(context, "Current world: $currentWorld", Toast.LENGTH_SHORT).show()}
        .setNegativeButton("Cancel"){dialog, which -> Toast.makeText(context, "Current world: $world", Toast.LENGTH_SHORT).show()}
        .setSingleChoiceItems(array,0,){ dialog, which -> currentWorld=array[which]}

    ElevatedButton(
        onClick = {
            val dialog: AlertDialog = builder.create()
            dialog.show()
        },
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        colors = ButtonDefaults.elevatedButtonColors(contentColor = Color.White),
        modifier = Modifier
            .size(50.dp)
    ) {
        Icon(painterResource(R.drawable.world_icon), "Change World Server", tint= Color.DarkGray)
    }

}