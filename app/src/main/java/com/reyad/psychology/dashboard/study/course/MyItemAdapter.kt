package com.reyad.psychology.dashboard.study.course

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.reyad.psychology.R
import com.reyad.psychology.dashboard.study.PdfViewerActivity
import com.squareup.picasso.Picasso

class MyItemAdapter(
    private val context: Context,
    private val itemList:List<ItemData>
): RecyclerView.Adapter<MyItemAdapter.MyViewHolder>() {

    inner class MyViewHolder(view:View): RecyclerView.ViewHolder(view), View.OnClickListener{
       var txtTitle: TextView = view.findViewById(R.id.tvTitle) as TextView
        var imgItem: ImageView = view.findViewById(R.id.itemImage) as ImageView

        lateinit var iItemClickListener: ItemClickListener

       fun setClick(iItemClickListener: ItemClickListener){
           this.iItemClickListener = iItemClickListener
       }

       init {

           view.setOnClickListener(this)
       }
        override fun onClick(view: View?) {
            iItemClickListener.onItemClickListener(view!!, adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtTitle.text = itemList[position].name

        holder.setClick(object : ItemClickListener {
            override fun onItemClickListener(view: View, position: Int) {
                val intent = Intent(context, PdfViewerActivity::class.java)
                intent.putExtra("lesson", itemList[position].name)
                intent.putExtra("pdfLink", itemList[position].image)
                context.startActivity(intent)
            }

        })
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}