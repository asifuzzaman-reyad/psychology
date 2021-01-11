package com.reyad.psychology.messenger.users

class ChatItems(
    val fromId: String,
    val toId: String,
    val message: String,
    val messageId: String,
    val timeStamp: Long,
    val isSeen: Boolean,
) {
    constructor() : this("", "", "", "", -1, false)
}