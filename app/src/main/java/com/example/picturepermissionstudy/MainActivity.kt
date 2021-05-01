package com.example.picturepermissionstudy

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var add : Button
    lateinit var image : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        add = findViewById(R.id.add)
       image = findViewById<ImageView>(R.id.image)



        add.setOnClickListener {
            when{
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )==PackageManager.PERMISSION_GRANTED ->{
                    // 권한이 잘 부여 되었을 때 갤러리에서 사진을 선택하는 기능
                    navigatesPhotos()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)->{
                    // 교육용 팝 확인 후 권한 팝업 띄우는 기능

                    showContextPopupPermission()
                }
                    else ->{
                       requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                    }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2000) {
            var path = data?.getStringExtra("image")
            println("##DDD${path}")
            if(data!=null){
                Glide.with(applicationContext).load(path).into(image)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode){
            1000->{
                    if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        // 권한이 부여되었으므로

                        navigatesPhotos()
                    }else{
                        Toast.makeText(applicationContext,"권한이 거부되었습니다!",Toast.LENGTH_SHORT).show()
                    }
            }
            else->{

            }
        }
    }
    private fun navigatesPhotos(){
        //여기에서는 contentsProvider에서 storage acess framework 개념을 이용(saf)

//        val  intent  = Intent(Intent.ACTION_PICK)
//
//        intent.type = "image/*"
//
//        startActivityForResult(intent,2000)

        val intent = Intent(this,imagePickerActivity::class.java)
        startActivityForResult(intent,2000)


    }

    fun showContextPopupPermission(){
         AlertDialog.Builder(this).setTitle("권한이 필요합니다")
             .setMessage("사진을 불러오기 위해 권한이 필요합니다")
             .setPositiveButton("동의하기") { _, _ ->
                 requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
             }
             .setNegativeButton("취소하기") { _, _ ->}
             .create()
             .show()

    }
}