package com.reyad.psychology.dashboard.home.student

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reyad.psychology.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class StudentAdapter(
    val context: Context,
    val data: List<StudentItemList>
) :
    RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {

    // onCount
    override fun getItemCount(): Int = data.size

    // onCreateView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.model_student, parent, false)
        return MyViewHolder(view)
    }

    // onBind
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mPosition: StudentItemList = data[position]
        holder.fText.text = mPosition.name
        holder.lText.text = mPosition.id

        if (mPosition.imageUrl.isNotEmpty()) {
            Picasso.get().load(mPosition.imageUrl).placeholder(R.drawable.bg_call_btn)
                .into(holder.civProfile)
        }
    }

    // view holder
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fText: TextView = itemView.findViewById(R.id.f_name_tv)
        val lText: TextView = itemView.findViewById(R.id.l_name_tv)
        val civProfile: CircleImageView = itemView.findViewById(R.id.civ_profile_student_model)
    }
}