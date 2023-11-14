package uk.ac.tees.mad.t7199384

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var sharedPref = this@MainActivity.getSharedPreferences(getString(R.string.world_file_key), Context.MODE_PRIVATE)
        var world = sharedPref.getString("world", "Empty").toString()

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                var worldText by remember{mutableStateOf(world)}

                Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                    Row(Modifier.fillMaxWidth()){
                        Greeting2(worldText)
                        Spacer(Modifier.weight(1f,true).fillMaxWidth().background(MaterialTheme.colorScheme.background))
                        World_Button(world = worldText)
                    }


            }
        }
    }
}

@Composable
fun World_Button(world: String) {
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


@Composable
fun Greeting2(world: String) {
    var worldGreeting by remember{mutableStateOf(world)}
    Text(
        text = "Welcome to $worldGreeting's market!",
    )
}

@Preview(showBackground = true)


@Composable
fun GreetingPreview2() {
    var world = "Crystal"
    ICATheme {
        Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
            Row(Modifier.fillMaxWidth()){
                Greeting2(world)
                Spacer(Modifier.weight(1f,true).fillMaxWidth().background(MaterialTheme.colorScheme.background))
                World_Button(world = "Crystal")
                }
            }
        }
    }
}

