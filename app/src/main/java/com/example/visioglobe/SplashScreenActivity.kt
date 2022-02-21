package com.example.visioglobe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme

class SplashScreenActivity : AppCompatActivity() {

    private val TIME_OUT: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisioGlobeAppTheme {
                SplashScreen()
            }
            loadSplashScreen()
        }
    }

    @Composable
    private fun SplashScreen() {
        Surface(color = MaterialTheme.colors.surface) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SetImage()
                SetAppName()
                SetContributor()
            }
        }
    }

    @Composable
    fun SetImage() {
        Image(
            painter = painterResource(id = R.drawable.globe),
            contentDescription = null,
            modifier = Modifier
                .width(VisioGlobeAppTheme.dims.obs_logo_width)
                .height(VisioGlobeAppTheme.dims.obs_logo_height)
        )
    }

    @Composable
    fun SetAppName() {
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_extra_large,
                bottom = VisioGlobeAppTheme.dims.padding_large
            ),
            style = MaterialTheme.typography.h5.copy(
                fontSize = VisioGlobeAppTheme.dims.text_large
            ),
            color = MaterialTheme.colors.onPrimary
        )
    }

    @Composable
    fun SetContributor() {
        Text(
            text = stringResource(id = R.string.made_by),
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_extra_large,
                bottom = VisioGlobeAppTheme.dims.padding_extra_small
            ),
            style = MaterialTheme.typography.h6.copy(
                fontSize = VisioGlobeAppTheme.dims.text_normal
            ),
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = stringResource(id = R.string.contributor),
            modifier = Modifier.padding(
                bottom = VisioGlobeAppTheme.dims.padding_large
            ),
            style = MaterialTheme.typography.h6.copy(
                fontSize = VisioGlobeAppTheme.dims.text_normal
            ),
            color = MaterialTheme.colors.onPrimary
        )
    }

    private fun loadSplashScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, TIME_OUT)
    }

    @Preview("default")
    @Composable
    private fun SplashScreenPreview() {
        VisioGlobeAppTheme {
            SplashScreen()
        }
    }
}