package com.mexcelle.xiacomemechinetest

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mexcelle.clavaxtechnologies.Endpoints
import com.mexcelle.clavaxtechnologies.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI


class DetailsActivity : AppCompatActivity() {

    var imageView:ImageView?=null
    var firstname:EditText?=null
    var lastname:EditText?=null
    var email:EditText?=null
    var number:EditText?=null
    var upload:Button?=null
    var bmp: Bitmap? = null
    var image:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        imageView=findViewById(R.id.image)
        firstname=findViewById(R.id.first_name)
        lastname=findViewById(R.id.last_name)
        email=findViewById(R.id.email)
        number=findViewById(R.id.phone)
        upload=findViewById(R.id.upload_btn)







        upload!!.setOnClickListener {

            if (firstname!!.text.length>0)
            {
                if (lastname!!.text.length>0)
                {
                    if (email!!.text.length>0)
                    {
                        if (number!!.text.length>0)
                        {
                            callApi(
                                firstname!!.text.toString(),
                                lastname!!.text.toString(),
                                email!!.text.toString(),
                                number!!.text.toString(),
                            )

                        }
                        else
                        {
                            Toast.makeText(
                                applicationContext,
                                "please enter number",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                    else
                    {
                        Toast.makeText(applicationContext, "please enter email", Toast.LENGTH_SHORT).show()

                    }
                }
                else
                {
                    Toast.makeText(applicationContext, "please enter last name", Toast.LENGTH_SHORT).show()

                }

            }
            else
            {
                Toast.makeText(applicationContext, "please enter first name", Toast.LENGTH_SHORT).show()
            }

        }



        Glide.with(this)
            .load(Uri.parse(intent.getStringExtra("url")))
            .into(imageView!!);
    }


    fun callApi(name: String, lastname: String, email: String, number: String) {
        val file: File = File(intent.getStringExtra("url"))


        /* try {
               bmp= MediaStore.Images.Media.getBitmap(
                        getContentResolver(), videoUri);
                Bundle b = data.getExtras();
                videoUri=data.getData();
                bmp = (Bitmap) b.get("data");
                videoUri=getImageUri(bmp);
                iv_userPic.setImageBitmap(bmp);
                iv_userPic.setBackgroundResource(R.drawable.profile_bg);
                iv_userPic.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }*/


        image = (intent.getStringExtra("url"))
        val myURI = URI(image)


        val selectedImage: String? = intent.getStringExtra("url")
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor: Cursor = this.getContentResolver().query(
            myURI,
            filePathColumn, null, null, null
        )!!
        cursor.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()

        val loadedBitmap = BitmapFactory.decodeFile(picturePath)


        try {
            bmp = getBitmapFromUri(this, myURI)
            val matrix = Matrix()
            val scaled: Bitmap
            scaled = if (loadedBitmap.width >= loadedBitmap.height) {
                matrix.setRectToRect(
                    RectF(
                        0F, 0F, loadedBitmap.width.toFloat(),
                        loadedBitmap.height.toFloat()
                    ), RectF(0F, 0F, 400F, 300F), Matrix.ScaleToFit.CENTER
                )
                Bitmap.createBitmap(
                    loadedBitmap,
                    0,
                    0,
                    loadedBitmap.width,
                    loadedBitmap.height,
                    matrix,
                    true
                )
            } else {
                matrix.setRectToRect(
                    RectF(
                        0F, 0F, loadedBitmap.width.toFloat(),
                        loadedBitmap.height.toFloat()
                    ), RectF(0F, 0F, 300F, 400F), Matrix.ScaleToFit.CENTER
                )
                Bitmap.createBitmap(
                    loadedBitmap,
                    0,
                    0,
                    loadedBitmap.width,
                    loadedBitmap.height,
                    matrix,
                    true
                )
            }
            val file: File = File(this.getExternalCacheDir(), "image.jpg")
            try {
                val out = FileOutputStream(file)
                scaled.compress(Bitmap.CompressFormat.JPEG, 50, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                Log.e("Image", "Convert")
            }
            bmp = scaled
            // Toast.makeText(getActivity(), "Getting Image Successfully", Toast.LENGTH_LONG).show();
        } catch (e: Exception) {
            e.printStackTrace()
        }




        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.getPostResponse(
            name,
            lastname,
            email,
            number,
            bmp.toString()
        )

        call!!.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {


                    Log.e("response", response.body().toString())


                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Toast.makeText(this@DetailsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }


    @Throws(IOException::class)
    fun getBitmapFromUri(context: Context, uri: Uri?): Bitmap? {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
            uri!!, "r"
        )
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }
}