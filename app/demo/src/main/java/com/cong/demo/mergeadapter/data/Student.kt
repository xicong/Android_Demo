package com.cong.demo.mergeadapter.data

data class Student(
    val id:Int,
    val name:String = "Student : $id",
    val iconId:Int
)