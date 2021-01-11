package com.reyad.psychology.messenger.users

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.reyad.psychology.R
import com.reyad.psychology.dashboard.MainActivity
import com.reyad.psychology.messenger.UserItems

class AllUserAdapter(
    val context: Context, private val userItemsList: List<UserItems>
//    private val teacherItemListener: TeacherItemListener
) :
    RecyclerView.Adapter<AllUserViewHolder>() {

    //  all user ->> item count
    override fun getItemCount(): Int = userItemsList.size

    //inflate ->> model layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllUserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.model_all_user, parent, false)
        return AllUserViewHolder(view)
    }

    // view binding
    override fun onBindViewHolder(holder: AllUserViewHolder, position: Int) {
        val userItems: UserItems = userItemsList[position]
        holder.textViewName.text = userItems.name
        holder.textViewId.text = userItems.id

        when (userItems.batch) {
            "Batch 14" -> {
                holder.imageViewBadge.setBackgroundColor(Color.RED)
            }
            "Batch 13" -> {
                holder.imageViewBadge.setBackgroundColor(Color.GREEN)
            }
        }

//        Picasso.get().load(itemPosition.imageUrl)
//            .placeholder(R.drawable.male_avatar)
//            .into(holder.imageViewProfile)

//        val userId = getItemId(position).

        holder.constraintAllUser.setOnClickListener {
            val userProfileIntent = Intent(context, UserProfile::class.java).apply {
                putExtra("userId", userItems.userId)
            }
            context.startActivity(userProfileIntent)
        }
    }

}


// viewHolder
class AllUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var textViewId: TextView = itemView.findViewById(R.id.tv_id_all_user_model)
    var textViewName: TextView = itemView.findViewById(R.id.tv_name_all_user_model)
    var imageViewProfile: ImageView = itemView.findViewById(R.id.civ_profile_all_user_model)
    var imageViewBadge: ImageView = itemView.findViewById(R.id.iv_badge_all_user_model)
    var constraintAllUser: ConstraintLayout = itemView.findViewById(R.id.constraint_all_user)

}

//    interface TeacherItemListener {
//        fun onItemClick(item: TeacherItemList)
//    }