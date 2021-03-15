package com.mexcelle.xiacomemechinetest

import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mexcelle.clavaxtechnologies.Endpoints
import com.mexcelle.clavaxtechnologies.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {


    var recyclerView: RecyclerView? = null
    var madapter: ImageAdapter? = null
    var responselist: ArrayList<ImageReposne>? = null
    var loadmore = true
    var currentItem: Int? = null
    var totalItem: Int? = null
    var ScroolOutItem: Int? = null
    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        button = findViewById(R.id.button)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        responselist = ArrayList<ImageReposne>()
        callApi()

        button!!.setOnClickListener {
            recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {


                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        loadmore = true
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // super.onScrolled(recyclerView, dx, dy);
                    currentItem = LinearLayoutManager(this@MainActivity).childCount
                    totalItem = LinearLayoutManager(this@MainActivity).itemCount
                    ScroolOutItem =
                        LinearLayoutManager(this@MainActivity).findFirstVisibleItemPosition()
                    if (loadmore && (currentItem!! + ScroolOutItem!! == totalItem)) {

                        loadmore = false
                       /* responselist!!.add(response.body()!!)
                        //  val imageReposne: ImageReposne = response as ImageReposne


                        madapter = ImageAdapter(this@MainActivity, responselist!!)
                       */
                        recyclerView!!.adapter = madapter
                        madapter!!.notifyDataSetChanged()
                    }
                }
            })
        }

    }


    fun callApi() {
        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.getImageList("108", "0", "popular")

        call.enqueue(object : Callback<ImageReposne> {
            override fun onResponse(call: Call<ImageReposne>, response: Response<ImageReposne>) {
                if (response.isSuccessful) {


                    Log.e("response", response.body().toString())
                    //  Log.e("response", response.body()!!.images.get(0).xt_image)

                     responselist!!.add(response.body()!!)
                    //  val imageReposne: ImageReposne = response as ImageReposne


                    madapter = ImageAdapter(this@MainActivity, response.body()!!)
                    recyclerView!!.adapter = madapter


                }
            }

            override fun onFailure(call: Call<ImageReposne>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}