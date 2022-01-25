package com.alperendiler.mine.view


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alperendiler.mine.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.IOException
import java.util.*

class UploadActivity : AppCompatActivity() {
    var selectedVoice : Uri? = null
    var selectedBitmap : Bitmap? = null
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)

        registerLauncher()

        auth =Firebase.auth
        db = Firebase.firestore

    }

    fun selectImage(view: View){
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) !=PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
//
//                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                ActivityCompat.requestPermissions(this, permissions,0)
//
//                Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
//                    //request permission
//                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//                }.show()
//            }else{
//                //request permission
//                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//            }
//        }else{
//            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
//            //Start activity  for result
//            activityResultLauncher.launch(intentToGallery)
//        }
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
//
//                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                ActivityCompat.requestPermissions(this, permissions,0)
//
//                Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
//                    //request permission
//                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//                }.show()
//            }else{
//                //request permission
//                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//            }
//        }else{
//            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//            //Start activity  for result
//            activityResultLauncher.launch(intentToGallery)
//        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                    View.OnClickListener {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)

        }
    }

    fun uploadClicked(view: View){

        val uuid = UUID.randomUUID()
        val voiceName = "$uuid.jpg"

        val stroge = Firebase.storage
        val reference =stroge.reference
        val voiceReferance = reference.child("voice").child(voiceName)


        if (selectedVoice != null){
            voiceReferance.putFile(selectedVoice!!).addOnSuccessListener {
                //download url -> firestore
                val uploadVoiceReference = stroge.reference.child("voice").child(voiceName)
                uploadVoiceReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    val postMap = hashMapOf<String,Any>()
                    postMap.put("downloadUrl",downloadUrl)
                    postMap.put("userEmail",auth.currentUser!!.email.toString())
                    postMap.put("comment",binding.uploadCommentText.text.toString())
                    postMap.put("date",Timestamp.now())


                    db.collection( "Posts").add(postMap).addOnCompleteListener{task ->

                        if (task.isComplete && task.isSuccessful) {
                            //back
                            finish()

                        }

                    }.addOnFailureListener{exception ->
                        Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }



                }.addOnFailureListener{
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }




    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedVoice = intentFromResult.data
                   binding.uploadVideoView
                }
            }
        }
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (result) {
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(this@UploadActivity, "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

     fun turnBackUpload(view: View) {
        val intent = Intent(applicationContext, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }
}