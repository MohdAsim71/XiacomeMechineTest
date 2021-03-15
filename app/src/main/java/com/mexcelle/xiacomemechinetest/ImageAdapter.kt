package com.mexcelle.xiacomemechinetest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.ArrayList



class ImageAdapter(
    private val context: Context,
    responseData: ImageReposne
) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private val responseData: ImageReposne
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v: View =
            LayoutInflater.from(this.context).inflate(R.layout.list_items, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val mCurrentItem: ImageReposne = responseData
        val url: String = mCurrentItem.images[position].xt_image


        Glide.with(context)
            .load(Uri.parse(url))
            .into(holder.imageView);

        holder.imageView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("url",url)


            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return responseData.images.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView

        init {

            imageView= itemView.findViewById(R.id.image_id)
        }
    }

    init {
        this.responseData = responseData
    }
}