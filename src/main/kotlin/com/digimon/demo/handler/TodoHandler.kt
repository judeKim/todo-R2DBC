package com.digimon.demo.handler

import com.digimon.demo.domain.todo.Todo
import com.digimon.demo.domain.todo.TodoRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status
import reactor.core.publisher.Mono


@Service
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

    fun save(req: ServerRequest): Mono<ServerResponse> {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(req.bodyToFlux(Todo::class.java)
                        .flatMap { todo ->
                            Mono.fromCallable {
                                repo.save(todo)
                            }.then(Mono.just(todo))
                        }
                ).switchIfEmpty(status(HttpStatus.NOT_FOUND).build())

    }
//
//    fun done(req: ServerRequest): Mono<ServerResponse> = ok()
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(Mono.justOrEmpty(repo.findById(req.pathVariable("id").toLong()))
//                    .switchIfEmpty(Mono.empty())
//                    .filter(Objects::nonNull)
//                    .flatMap { todo ->
//                        Mono.fromCallable {
//                            todo.done = true
//                            todo.modifiedAt = LocalDateTime.now()
//                            repo.save(todo)
//                        }.then(Mono.just(todo))
//                    }
//            ).switchIfEmpty(notFound().build())
//
//    fun delete(req: ServerRequest): Mono<ServerResponse> = ok()
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(Mono.justOrEmpty(repo.findById(req.pathVariable("id").toLong()))
//                    .switchIfEmpty(Mono.empty())
//                    .filter(Objects::nonNull)
//                    .flatMap { todo ->
//                        Mono.fromCallable {
//                            repo.delete(todo)
//                        }.then(Mono.just(todo))
//                    }
//            )
//            .switchIfEmpty(notFound().build())
}