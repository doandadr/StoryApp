package com.dicoding.doanda.storyapp.ui.addstory

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.doanda.storyapp.data.repository.Result
import com.dicoding.doanda.storyapp.databinding.ActivityAddStoryBinding
import com.dicoding.doanda.storyapp.ui.story.StoryActivity
import com.dicoding.doanda.storyapp.ui.utils.factory.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val addStoryViewModel by viewModels<AddStoryViewModel> { ViewModelFactory.getInstance(this) }

    private var imageFile: File? = null
    private var lat: Float? = null
    private var lon:  Float? = null
    private var bearerToken: String? = null
    private lateinit var photoPath: String
    private lateinit var fusedLocProvider: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Add Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (!getPermissionStatus()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_PERMISSIONS_CODE)
        }

        addStoryViewModel.getUser().observe(this) { user ->
            if (user.isLoggedIn) {
                bearerToken = user.bearerToken
            } else {
                Toast.makeText(this@AddStoryActivity, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btCamera.setOnClickListener { capturePhoto() }
        binding.btGallery.setOnClickListener { importGallery() }

        binding.swLoc.setOnCheckedChangeListener { _: CompoundButton?, isChecked ->
            if (isChecked) {
                getUserLocation()
            } else {
                lat = null
                lon = null
            }
        }
        binding.buttonAdd.setOnClickListener { uploadStory() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun capturePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.dicoding.doanda.storyapp",
                it
            )
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    fun createTempFile(context: Context): File =
        File.createTempFile(timeStamp, ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))

    val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.ENGLISH
    ).format(System.currentTimeMillis())
    private fun importGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadStory() {
        val file = imageFile ?: return Toast.makeText(this, "Pick an Image", Toast.LENGTH_SHORT).show()
        val reqImgFile = reduceImageSize(file).asRequestBody("image/*".toMediaTypeOrNull())
        val token = bearerToken ?: return Toast.makeText(this, "Token not found", Toast.LENGTH_SHORT).show()
        if (binding.edAddDescription.text.toString().isEmpty())
            return Toast.makeText(this, "Enter description", Toast.LENGTH_SHORT).show()
        val desc = "${binding.edAddDescription.text}".toRequestBody("text/plain".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, reqImgFile)
        addStoryViewModel.uploadStory(token, desc, imageMultipart, lat, lon).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    showLoading(false)
                    val intent = Intent(this@AddStoryActivity, StoryActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    Toast.makeText(this@AddStoryActivity, "Story Uploaded", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    Toast.makeText(this@AddStoryActivity, result.error, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }

    }
    fun reduceImageSize(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocProvider = LocationServices.getFusedLocationProviderClient(this)
            fusedLocProvider.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude.toFloat()
                    lon = location.longitude.toFloat()
                    Toast.makeText(this, "Location saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Location not saved", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) getUserLocation()
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(photoPath)
            imageFile = myFile
            val result = BitmapFactory.decodeFile(imageFile?.path)
            binding.ivAddDetailPhoto.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            imageFile = myFile
            binding.ivAddDetailPhoto.setImageURI(selectedImg)
        }
    }
    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        inputStream.copyTo(outputStream, 1024)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun getPermissionStatus() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbAddStory.visibility = View.VISIBLE
        } else {
            binding.pbAddStory.visibility = View.GONE
        }
    }


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_PERMISSIONS_CODE = 10
        private const val FILENAME_FORMAT = "dd-MMM-yyyy"
    }
}