package com.example.socket_io_tutorial.model

data class Message(
    val message : String,
    val messageType : TypeMessage
)

enum class TypeMessage{
    TRUE,FALSE
}