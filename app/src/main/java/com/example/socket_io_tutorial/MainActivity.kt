package com.example.socket_io_tutorial

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socket_io_tutorial.adapter.MessageAdapter
import com.example.socket_io_tutorial.databinding.ActivityMainBinding
import com.example.socket_io_tutorial.model.Message
import com.example.socket_io_tutorial.model.TypeMessage
import com.example.socket_io_tutorial.other.Resource
import com.example.socket_io_tutorial.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private lateinit var messages: MutableList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        messages = mutableListOf()
        val linearLayoutManager = LinearLayoutManager(this)
        val messageAdapter = MessageAdapter(messages)


        binding.rvChat.apply {
            adapter = messageAdapter
            layoutManager = linearLayoutManager
        }
        viewModel.connect()



        binding.imageButton.setOnClickListener{
            if(binding.editTextText.text.isEmpty() || binding.editTextText.text.isBlank()){
                return@setOnClickListener
            } else {
                val message = binding.editTextText.text.toString()
                viewModel.sendMessage(message)

                binding.editTextText.text.clear()
            }
        }
        viewModel.textLiveData.observe(this){event->
            if (event.hasBeenHandled.not()){
                event.getContentIfNotHandled()?.let { resource->
                    when(resource){
                        is Resource.Success -> {
                            resource.data?.let {
                                messageAdapter.addMessage(
                                    Message(
                                        it.message,
                                        TypeMessage.TRUE
                                    )
                                )
                            }
                        }
                        is Resource.Error -> {
                            resource.data?.let {
                                messageAdapter.addMessage(
                                    Message(
                                        it.message,
                                        TypeMessage.FALSE
                                    )
                                )
                            }
                        }
                        else -> {}
                    } }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disconnect()
    }

}
