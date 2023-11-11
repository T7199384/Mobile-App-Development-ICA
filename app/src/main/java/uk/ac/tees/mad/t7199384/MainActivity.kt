package uk.ac.tees.mad.t7199384

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme

var world = "Crystal"
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        var sharedPref = this@MainActivity.getSharedPreferences(getString(R.string.world_file_key), Context.MODE_PRIVATE)
        world = sharedPref.getString("world", "Empty").toString()

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                Scaffold(

                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = { World_Button() },
                    containerColor = MaterialTheme.colorScheme.background){

                    Greeting2(world)
                }


            }
        }
    }
}

@Composable
fun World_Button() {
    val context = LocalContext.current
    val array: Array<String> = context.resources.getStringArray(R.array.world_array)
    var current_world=world

    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setTitle("Choose world")
        .setPositiveButton("Confirm"){dialog, which ->
            world = current_world
            Toast.makeText(context, "Current world: $world", Toast.LENGTH_SHORT).show()}
        .setNegativeButton("Cancel"){dialog, which -> Toast.makeText(context, "Current world: $world", Toast.LENGTH_SHORT).show()}
        .setSingleChoiceItems(array,0,){ dialog, which -> current_world=array[which]}

    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Top) {
        FloatingActionButton(
            onClick = {
                val dialog: AlertDialog = builder.create()
                dialog.show()
            },
            shape = CircleShape,
            modifier = Modifier
                .size(56.dp)
        ) {
            Icon(painterResource(R.drawable.world_icon), "Change World Server")
        }
    }
}


@Composable
fun Greeting2(world: String) {
    Text(
        text = "Welcome to the $world marketboard!",
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)


@Composable
fun GreetingPreview2() {
    ICATheme {
        Greeting2(world)
        World_Button()
    }
}
