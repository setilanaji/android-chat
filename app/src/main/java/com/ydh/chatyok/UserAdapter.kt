package com.ydh.chatyok

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ydh.chatyok.databinding.ItemChatListBinding

class UserAdapter(private val context: Context, private val listener: UserListener) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    interface UserListener {
        fun onSend(userModel: UserModel, message: String)
    }

    inner class ViewHolder(val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(userModel: UserModel) {
            binding.tvUsername.text = userModel.email
        }
    }

    var list = mutableListOf<UserModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.bindData(model)
        holder.binding.btSend.setOnClickListener {
            listener.onSend(model, holder.binding.etMsg.text.toString())

            holder.binding.etMsg.setText("")
        }
    }

    override fun getItemCount(): Int = list.size
}