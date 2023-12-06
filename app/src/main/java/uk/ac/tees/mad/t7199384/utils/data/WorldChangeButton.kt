package uk.ac.tees.mad.t7199384.utils.data

import android.app.AlertDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.t7199384.R
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme

@Composable
fun WorldChangeButton() {

    var dialog: AlertDialog? = null

    fun dialogSet(dialog_: AlertDialog){
        dialog = dialog_
    }

    fun dialogGet(): AlertDialog? {
        return dialog
    }

    fun dismiss(dialog: AlertDialog){
        dialog.dismiss()
    }

    val context = LocalContext.current
    val array: Array<String> = context.resources.getStringArray(R.array.world_array)

    var currentWorld:String

    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setTitle("Choose world")
        .setNegativeButton("Cancel"){_, _ -> }
        .setSingleChoiceItems(array,0,){ _, which ->
            currentWorld=array[which]
            val rLocation = "${array[which]}_array".lowercase()
            val rID = context.resources.getIdentifier(rLocation,"array",context.packageName)
            dialog=dialogGet()
            dialog?.dismiss()
            showAdditionalOptionsDialog(context,rID,currentWorld)
        }

    IconButton( modifier=Modifier.size(80.dp),
        onClick = {
            val firstDialog = builder.create()
            dialogSet(firstDialog)
            firstDialog.show()
        },
    ) {
        Box(modifier= Modifier
            .aspectRatio(1f)
            .size(80.dp)
            .background(Color.White, shape= CircleShape)
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
        Icon(painterResource(R.drawable.world_icon), "Change World Server", tint= Color.DarkGray,modifier=Modifier.clip(CircleShape))
        }
    }

}

private fun showAdditionalOptionsDialog(context: Context, worldOption: Int, world:String) {
    val array: Array<String> = context.resources.getStringArray(worldOption)
    var currentWorld=world

    AlertDialog.Builder(context)
        .setTitle("Additional Options for $world")
        .setPositiveButton("Confirm"){_, _ ->
            var sharedPref = context.getSharedPreferences(context.resources.getString(R.string.world_file_key), Context.MODE_PRIVATE)
            var edit = sharedPref.edit()
            edit.putString("world",currentWorld)
            edit.apply()
        }
        .setSingleChoiceItems(array,0,){ _, which -> currentWorld=array[which]}
        .show()
}

@Preview(showBackground = true)

@Composable
fun WorldButtonPreview() {
    ICATheme() {
        WorldChangeButton()
    }
}