package uk.ac.tees.mad.t7199384

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background){
                    World_Button()
                    Greeting2("Android")
                }


            }
        }
    }
}

@Composable
fun World_Button() {
    val context = LocalContext.current
    Box(contentAlignment = Alignment.TopEnd)
    {
        FloatingActionButton(
            onClick = {
                Toast.makeText(context, "Clicked on world button", Toast.LENGTH_SHORT).show()
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
fun Greeting2(name: String) {
    Text(
        text = "Hello $name!",
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)


@Composable
fun GreetingPreview2() {
    ICATheme {
        Greeting2("Android")
        World_Button()
    }
}
