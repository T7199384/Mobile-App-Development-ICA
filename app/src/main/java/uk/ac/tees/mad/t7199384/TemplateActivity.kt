package uk.ac.tees.mad.t7199384

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.WorldChangeButton

class TemplateActivity : ComponentActivity(),SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this@TemplateActivity.getSharedPreferences(getString(R.string.world_file_key), Context.MODE_PRIVATE)
        val world = sharedPref.getString("world", "Empty").toString()

        sharedPref.registerOnSharedPreferenceChangeListener(this)

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                val worldText by remember{mutableStateOf(world)}

                Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Box(Modifier.fillMaxSize().weight(1f)){
                            Greeting2(world)
                            //WorldChangeButton(world = "Crystal")
                        }
                        Box(modifier = Modifier.fillMaxSize().weight(0.15f)){
                            WorldChangeButton()
                        }
                    }
            }
        }
    }
}
@Composable
fun Greeting2(world: String) {
    val worldGreeting by remember{mutableStateOf(world)}

    Text(
        text = "Welcome to $worldGreeting's market!",
    )
}

@Preview(showBackground = true)


@Composable
fun GreetingPreview2() {
    val world = "Crystal"
    ICATheme {
        Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Box(Modifier.fillMaxSize().weight(1f)){
                    Greeting2(world)
                    //WorldChangeButton(world = "Crystal")
                }
                Box(modifier = Modifier.fillMaxSize().weight(0.15f)){
                    WorldChangeButton()
                }
            }
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recreate()
    }
}

