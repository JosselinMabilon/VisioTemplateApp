package com.example.visioglobe.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import coil.annotation.ExperimentalCoilApi
import com.example.visioglobe.R
import com.example.visioglobe.domain.model.Furniture
import com.example.visioglobe.ui.components.CustomOutlinedButton
import com.example.visioglobe.ui.components.furniture.FurnitureList
import com.example.visioglobe.ui.components.incident.BoxIncidentImage
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.Executor

@AndroidEntryPoint
class CameraFragment : Fragment() {

    private val viewModel: CameraViewModel by viewModels()

    private lateinit var selectImageLauncher: ActivityResultLauncher<String>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var imageGalleryUri: MutableState<Uri?>
    private lateinit var imageCapture: ImageCapture

    @ExperimentalCoilApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        imageCapture = ImageCapture.Builder().build()
        imageGalleryUri = mutableStateOf(null)

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            )
            { isGranted: Boolean ->
                if (isGranted) {
                    selectImageLauncher.launch(GALLERY_PATH)
                } else {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.external_storage_permission_denied),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        selectImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    imageGalleryUri.value = uri
                }
            }

        return ComposeView(requireContext()).apply {
            setContent {
                VisioGlobeAppTheme {
                    CameraContentView()
                }
            }
        }
    }

    @ExperimentalCoilApi
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @Composable
    private fun CameraContentView() {

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (imageGalleryUri.value == null) {
                    SimpleCameraPreview()
                } else {
                    ImageGalleryPreview()
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                FloatingActionButton(
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(VisioGlobeAppTheme.dims.padding_small),
                    onClick = {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                )
                {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = ""
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                PredictionList()
            }
        }
    }

    @ExperimentalCoilApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun SimpleCameraPreview() {
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        val executor: Executor = ContextCompat.getMainExecutor(context)
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        val previewView = PreviewView(context).apply {
            this.scaleType = scaleType
            this.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
        val cameraProvider = cameraProviderFuture.get()

        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    captureImage(
                        executor = executor,
                        imageCapture = imageCapture
                    )
                }) {
                AndroidView(
                    factory = {
                        cameraProviderFuture.addListener({
                            bindPreview(
                                lifecycleOwner,
                                previewView,
                                cameraProvider,
                                imageCapture,
                            )
                        }, executor)
                        previewView
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                ) {
                    CustomOutlinedButton(
                        buttonTextColor = MaterialTheme.colors.primary,
                        backgroundColor = Color.Transparent,
                        borderWidth = VisioGlobeAppTheme.dims.border_width_large,
                        onClick = {
                            captureImage(
                                executor = executor,
                                imageCapture = imageCapture
                            )
                            Toast.makeText(
                                context,
                                resources.getString(R.string.running_analysis),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        buttonText = stringResource(id = R.string.analyse),
                        modifier = Modifier.padding(bottom = VisioGlobeAppTheme.dims.padding_small)
                    )
                }
            }
        }
    }

    @ExperimentalCoilApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun ImageGalleryPreview() {
        imageGalleryUri.value?.let { viewModel.runClassifier(it) }
        Card(
            modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_small)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                BoxIncidentImage(
                    imageGalleryUri.value.toString(),
                    VisioGlobeAppTheme.dims.incident_image_gallery_height_fraction,
                    ContentScale.Fit
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(VisioGlobeAppTheme.dims.padding_large),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { selectImageLauncher.launch(GALLERY_PATH) }) {
                        Text(
                            text = stringResource(R.string.camera_take_another_one),
                            style = MaterialTheme.typography.button.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
                            color = MaterialTheme.colors.primary
                        )
                    }
                    TextButton(onClick = { imageGalleryUri.value = null }) {
                        Text(
                            text = stringResource(id = R.string.dismiss_string),
                            style = MaterialTheme.typography.button.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
                PredictionList()
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun PredictionList() {
        val furnitureList: List<Furniture> by viewModel.prediction.observeAsState(emptyList())
        if (furnitureList.isNotEmpty()) {
            FurnitureList(furnitureList, findNavController(), viewModel.mapper)
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindPreview(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        cameraProvider: ProcessCameraProvider,
        imageCapture: ImageCapture,
    ) {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            imageCapture,
            preview
        )
    }

    private fun captureImage(
        executor: Executor,
        imageCapture: ImageCapture,
    ) {
        val photoFile = viewModel.getOutputDirectory()
        val outputFileOptions = ImageCapture.OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputFileOptions, executor, object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    val errorValue = requireContext().getString(R.string.camera_error)
                    Toast.makeText(context, errorValue, Toast.LENGTH_LONG).show()
                    Timber.tag(TAG).e("Photo capture failed: ${exc.message}")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    savedUri?.let {
                        viewModel.runClassifier(it)
                    }
                    Timber.tag(TAG).i("Photo capture succeeded: $savedUri")
                }
            })
    }

    companion object {
        private const val GALLERY_PATH = "image/*"
        private const val TAG = "CameraFragment"
    }
}

/////////////////////////////////////////////////////////////////

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun ButtonRowPreview() {
    VisioGlobeAppTheme() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box() {
                    CustomOutlinedButton(
                        buttonTextColor = MaterialTheme.colors.primary,
                        backgroundColor = Color.Transparent,
                        borderWidth = VisioGlobeAppTheme.dims.border_width_large,
                        onClick = { },
                        buttonText = stringResource(id = R.string.analyse),
                        modifier = Modifier.padding(bottom = VisioGlobeAppTheme.dims.padding_zero)
                    )

                    FloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(end = VisioGlobeAppTheme.dims.padding_small)
                            .align(Alignment.CenterEnd),
                        onClick = { },
                        elevation = FloatingActionButtonDefaults.elevation(VisioGlobeAppTheme.dims.padding_small),
                    )
                    {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}



