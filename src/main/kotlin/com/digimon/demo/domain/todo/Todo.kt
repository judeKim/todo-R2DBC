package com.digimon.demo.domain.todo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("todos")
class Todo {

    @Id
    var id: Long? = null

    @Column("content")
    var content: String? = null

    @Column("done")
    var done: Boolean = false

    @Column("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column("modified_at")
    var modifiedAt: LocalDateTime = createdAt
}
