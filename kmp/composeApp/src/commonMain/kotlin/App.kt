import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import net.servicestack.client.JsonServiceClient
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {

        trustSelfSignedSSL()
        val client = JsonServiceClient("https://localhost:5001")
        var textFieldState by remember { mutableStateOf(TextFieldValue()) }
        var searchResults by remember { mutableStateOf(listOf<String>()) }
        val coroutineScope = rememberCoroutineScope()

        // Function to call ServiceStack API
        fun callService() {
            coroutineScope.launch {
                val request = SearchFiles()
                request.pattern = textFieldState.text
                val response: SearchFilesResponse = client.get(request)
                searchResults = response.results
            }
        }
        
        callService()

        val lightBlue = Color(0xFF2196F3)

        Column(
            modifier = Modifier.fillMaxSize().background(Color.White),
            verticalArrangement = Arrangement.Top  // Aligns children to the top
        ) {
            // Light blue banner with white title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(lightBlue),  // Use custom light blue color
                contentAlignment = Alignment.Center
            ) {
                Text("My Application", color = Color.White, style = TextStyle(fontSize = 2.em))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),  // Padding on the top
                contentAlignment = Alignment.Center  // Centers the content horizontally
            ) {
                Text("Search Files", color = Color.Black,
                     modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                     style = TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),  // Padding on the top, start, and end
                contentAlignment = Alignment.Center  // Centers the content horizontally
            ) {
                // Search Box
                BasicTextField(
                    value = textFieldState,
                    onValueChange = {
                        textFieldState = it
                        callService()
                    },
                    textStyle = TextStyle(color = Color.Black, fontSize = 2.em),
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.8f)
                        .height(40.dp)
                        .padding(top = 4.dp, start = 4.dp)
                        .border(2.dp, lightBlue, RoundedCornerShape(4.dp))
                        .clip(RoundedCornerShape(4.dp))
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(searchResults) { filePath ->
                    // Display each file path
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Blue)
                        Spacer(modifier = Modifier.width(8.dp)) // Gives some space between the icon and the text
                        Text(filePath,
                             color = Color.Black,
                             style = TextStyle(fontSize = 16.sp)
                        )
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp)) // Adds a divider between items
                }
            }
        }
        
    }
}

fun trustSelfSignedSSL() {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate>? {
            return null
        }

        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {
        }

        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {
        }
    })

    try {
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
