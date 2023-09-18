package com.example.socket_io_tutorial.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socket_io_tutorial.R
import com.example.socket_io_tutorial.databinding.ReceiveMessageBinding
import com.example.socket_io_tutorial.databinding.SendMessageBinding
import com.example.socket_io_tutorial.model.Message
import com.example.socket_io_tutorial.model.TypeMessage

class MessageAdapter(private val messages : MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            MESSAGE_TYPE_SEND -> MessageSendViewHolder(SendMessageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))

            else -> MessageReceiveViewHolder(ReceiveMessageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
        }
    }

    fun addMessage(message: Message){
        messages.add(message)
        notifyItemInserted(messages.size)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return if(getItemViewType(position) == MESSAGE_TYPE_SEND){
            (holder as MessageSendViewHolder).bind(messages[position])
        } else {
            (holder as MessageReceiveViewHolder).bind(messages[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].messageType == TypeMessage.TRUE) MESSAGE_TYPE_SEND else MESSAGE_TYPE_RECEIVE
    }


    inner class MessageSendViewHolder(private val binding : SendMessageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : Message){
            binding.sendMessage.text = data.message
        }
    }

    inner class MessageReceiveViewHolder(private val binding : ReceiveMessageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Message){
            binding.receiveMessage.text = data.message
        }
    }

    companion object {
        private const val MESSAGE_TYPE_SEND = 1
        private const val MESSAGE_TYPE_RECEIVE = 2
    }
}