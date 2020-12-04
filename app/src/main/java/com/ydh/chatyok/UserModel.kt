package com.ydh.chatyok

class UserModel() {
    var uid: String = ""
    var email: String = ""
    var token: String = ""

    constructor(uid: String, email: String, token: String = "") : this() {
        this.uid = uid
        this.email = email
        this.token = token
    }
}