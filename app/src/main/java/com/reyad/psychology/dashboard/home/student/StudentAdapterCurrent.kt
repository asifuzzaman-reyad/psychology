package com.reyad.psychology.dashboard.home.student

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.reyad.psychology.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class StudentAdapterCurrent(
    val context: Context,
    val data: List<StudentItemList>
) :
    RecyclerView.Adapter<StudentAdapterCurrent.MyViewHolder>() {

    // onCount
    override fun getItemCount(): Int = data.size

    // onCreateView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.model_student_current, parent, false)
        return MyViewHolder(view)
    }

    // onBind
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mPosition: StudentItemList = data[position]
        holder.textViewName.text = mPosition.name
        holder.textViewId.text = mPosition.id
        holder.textViewBlood.text = mPosition.blood
        holder.textViewMobile.text = mPosition.mobile
        holder.textViewHall.text = mPosition.hall

        //profile image
        if (mPosition.imageUrl.isEmpty()) {
            val url =
                "https://firebasestorage.googleapis.com/v0/b/psychology-95b39.appspot.com/o/Students%2Fimages.jpg?alt=media&token=eb31492c-1a2b-46da-ab00-db547a53b9eb"

            Picasso.get().load(url).placeholder(R.drawable.male_avatar)
                .into(holder.civProfile)

        } else {
            Picasso.get().load(mPosition.imageUrl).placeholder(R.drawable.male_avatar)
                .into(holder.civProfile)
        }


        //call button
        holder.buttonCall.setOnClickListener {
            if (mPosition.mobile == "") {
                Toast.makeText(context, "No Mobile No Found", Toast.LENGTH_SHORT).show()

            } else {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${mPosition.mobile}")
                context.startActivity(intent)
                Toast.makeText(context, "Open Dialer", Toast.LENGTH_SHORT).show()
            }
        }

        //message button
        holder.buttonMessage.setOnClickListener {
            if (mPosition.mobile == "") {
                Toast.makeText(context, "No Mobile No Found", Toast.LENGTH_SHORT).show()

            } else {
                val intentMessage = Intent(Intent.ACTION_VIEW)
                intentMessage.data = Uri.parse("sms:${mPosition.mobile}")
                context.startActivity(intentMessage)
                Toast.makeText(context, "Open Message", Toast.LENGTH_SHORT).show()

            }
        }
    }


    // view holder
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val civProfile: CircleImageView =
            itemView.findViewById(R.id.civ_profile_student_model_current)
        val textViewName: TextView = itemView.findViewById(R.id.tv_name_student_current)
        val textViewId: TextView = itemView.findViewById(R.id.tv_id_student_current)
        val textViewBlood: TextView = itemView.findViewById(R.id.tv_blood_student_current)
        val textViewMobile: TextView = itemView.findViewById(R.id.tv_mobile_student_current)
        val textViewHall: TextView = itemView.findViewById(R.id.tv_hall_student_current)
        val linearBlood: LinearLayout = itemView.findViewById(R.id.linear_blood_student_current)
        val linearHall: LinearLayout = itemView.findViewById(R.id.linear_hall_student_current)
        val buttonCall: ImageButton = itemView.findViewById(R.id.btn_call_student_current)
        val buttonMessage: ImageButton = itemView.findViewById(R.id.btn_message_student_current)

    }
}
