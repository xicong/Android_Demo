package com.cong.demo.mergeadapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cong.demo.R
import com.cong.demo.databinding.ItemStudentBinding
import com.cong.demo.mergeadapter.data.Student

class StudentAdapter : ListAdapter<Student, StudentViewHolder>(StudentDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(
            ItemStudentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StudentViewHolder(private val binding: ItemStudentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(student: Student) {
        binding.studentIcon.setImageResource(student.iconId)
        binding.studentName.text = student.name
    }
}

class StudentDiffCallBack : DiffUtil.ItemCallback<Student>() {
    override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
        return oldItem.iconId == newItem.iconId &&
                oldItem.name == newItem.name
    }
}