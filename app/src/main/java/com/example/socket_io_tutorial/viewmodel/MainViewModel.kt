package com.example.socket_io_tutorial.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socket_io_tutorial.model.Message
import com.example.socket_io_tutorial.model.TypeMessage
import com.example.socket_io_tutorial.other.Event
import com.example.socket_io_tutorial.other.Resource
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException

class MainViewModel : ViewModel() {
    private val _textLiveData = MutableLiveData<Event<Resource<Message>>>()
    val textLiveData get() = _textLiveData

    private var socket : Socket

    init {
        try {
            socket = IO.socket("https://2b29-2402-800-6273-7a35-b9d3-25fb-1bad-1f67.ngrok-free.app")
            socket.on("receiveMessage"){args->
                if(args[0] != null){
                    val data = args[0] as JSONObject
                    val msg = data.getString("message")
                    val id = data.getString("id")
                    if(id == socket.id()){
                        _textLiveData.postValue(Event(Resource.Success(
                            Message(msg, TypeMessage.TRUE)
                        )))
                    } else {
                        _textLiveData.postValue(Event(Resource.Error(
                            Message(msg, TypeMessage.FALSE)
                        )))
                    }
                }
            }
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    fun connect(){
        socket.connect()
    }

    fun disconnect(){
        socket.disconnect()
    }

    fun sendMessage(message : String){
        socket.emit("sendMessage",message)
    }

}