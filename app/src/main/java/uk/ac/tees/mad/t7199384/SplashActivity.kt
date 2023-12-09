package uk.ac.tees.mad.t7199384

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this@SplashActivity.getSharedPreferences(getString(R.string.world_file_key), Context.MODE_PRIVATE)
        val world = sharedPref.getString("world", "Empty").toString()

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    SplashGreeting("Kupo Mart")
                }
            }
        }

        val context = this@SplashActivity
        val array: Array<String> = context.resources.getStringArray(R.array.world_array)

        var dialog: AlertDialog? = null

        fun dialogSet(dialog_: AlertDialog){
            dialog = dialog_
        }

        fun dialogGet(): AlertDialog? {
            return dialog
        }

        var currentWorld:String

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setTitle("Choose world")
            .setSingleChoiceItems(array,0){ _, which ->
                currentWorld=array[which]
                val rLocation = "${array[which]}_array".lowercase()
                val rID = context.resources.getIdentifier(rLocation,"array",context.packageName)
                dialog=dialogGet()
                dialog?.dismiss()
                showAdditionalOptionsDialog(context,rID,currentWorld)
            }

        val firstDialog = builder.create()
        dialogSet(firstDialog)

        if(world!="Empty"){
            callIntent()
        }
        else{
            firstDialog.show()
        }
    }

    private fun showAdditionalOptionsDialog(context: Context, worldOption: Int, world:String) {
        val array: Array<String> = context.resources.getStringArray(worldOption)
        var currentWorld=world

        AlertDialog.Builder(context)
            .setTitle("Additional Options for $world")
            .setPositiveButton("Confirm"){_, _ ->
                val sharedPref = context.getSharedPreferences(context.resources.getString(R.string.world_file_key), Context.MODE_PRIVATE)
                val edit = sharedPref.edit()
                edit.putString("world",currentWorld)
                edit.apply()
                callIntent()
            }
            .setSingleChoiceItems(array,0){ _, which -> currentWorld=array[which]}
            .show()
    }


    private fun callIntent(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity,MainActivity::class.java)
            startActivity(intent)
        }, 3000)
    }
}


@Composable
fun SplashGreeting(title: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row {
            Spacer(modifier = Modifier.height(30.dp))
            Image (
                painter = painterResource(R.drawable.app_icon),
                contentDescription = "Splash Screen Image",
                modifier = Modifier
                    .size(300.dp)
            )
        }
        Row{
            Image (
                painter = painterResource(R.drawable.app_title),
                contentDescription = "Splash Screen Title",
                modifier = Modifier
                    .size(220.dp)
            )
        }
        Column {
            Spacer(modifier = Modifier.height(150.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(0.1f), contentAlignment = Alignment.BottomCenter) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Universalis v2, based on Mogboard v2.2\n" +
                                "FINAL FANTASY XIV © 2010 - 2020 SQUARE ENIX CO., LTD. All Rights Reserved.\n" +
                                "$title is not affliated with Universalis.app or FINAL FANTASY XIV",
                        textAlign = TextAlign.Center,
                        style = TextStyle.Default.copy(fontSize = 9.sp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashGreetingPreview() {
    ICATheme {
        Surface(modifier=Modifier.fillMaxSize()) {
            SplashGreeting("Kupo Mart")
        }
    }
}
