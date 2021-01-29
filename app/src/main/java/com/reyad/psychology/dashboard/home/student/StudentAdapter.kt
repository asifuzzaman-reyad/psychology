package com.reyad.psychology.dashboard.home.student

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
        holder.textViewName.text = mPosition.name
        holder.textViewId.text = mPosition.id
        holder.textViewBlood.text = mPosition.blood
        holder.textViewHall.text = mPosition.hall

        if (mPosition.imageUrl.isEmpty()) {
            val url =
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS3UGtvN8kbZmwI7RN5oxWpSA3G63Xne8KIZQ&usqp=CAU"
//                "https://firebasestorage.googleapis.com/v0/b/psychology-95b39.appspot.com/o/Students%2Fmale_avatar.webp?alt=media&token=f68fc7ca-bbbb-49dd-b7d9-5d287bb1c598"

            Picasso.get().load(url).placeholder(R.drawable.male_avatar)
                .into(holder.civProfile)

        } else {
            Picasso.get().load(mPosition.imageUrl).placeholder(R.drawable.male_avatar)
                .into(holder.civProfile)
        }
    }

    // view holder
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.tv_name_student_m)
        val textViewId: TextView = itemView.findViewById(R.id.tv_id_student_m)
        val textViewBlood: TextView = itemView.findViewById(R.id.tv_blood_student_m)
        val textViewHall: TextView = itemView.findViewById(R.id.tv_hall_student_m)
        val linearBlood: LinearLayout = itemView.findViewById(R.id.linear_blood_student_m)
        val linearHall: LinearLayout = itemView.findViewById(R.id.linear_hall_student_m)
        val civProfile: CircleImageView = itemView.findViewById(R.id.civ_profile_student_model)
    }
}