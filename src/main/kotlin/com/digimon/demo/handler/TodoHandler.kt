package com.digimon.demo.handler

import com.digimon.demo.domain.todo.Todo
import com.digimon.demo.domain.todo.TodoRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Mono
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@Component
class TodoHandler {
    private val repo: TodoRepository

    constructor(repo: TodoRepository) {
        this.repo = repo
    }

    fun getAll(req: ServerRequest): Mono<ServerResponse> = ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(repo.findAll(), Todo::class.java)
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())

    fun getById(req: ServerRequest): Mono<ServerResponse> = ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(repo.findById(req.pathVariable("id").toLong()), Todo::class.java)
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())

    fun save(req: ServerRequest): Mono<ServerResponse> =
            repo.saveAll(req.bodyToMono(Todo::class.java))
                    .flatMap { created(URI.create("/todos/${it.id}")).build() }
                    .next()

    fun done(req: ServerRequest): Mono<ServerResponse> =
            repo.findById(req.pathVariable("id").toLong())
                    .filter(Objects::nonNull)
                    .flatMap { todo ->
                        todo.done = true
                        todo.modifiedAt = LocalDateTime.now()
                        repo.save(todo)
                    }
                    .flatMap {
                        it?.let { ok().build() }
                    }
                    .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())

    fun delete(req: ServerRequest): Mono<ServerResponse> =
            repo.findById(req.pathVariable("id").toLong())
                    .filter(Objects::nonNull)
                    .flatMap { todo -> ok().build(repo.deleteById(todo.id!!)) }
                    .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())
}