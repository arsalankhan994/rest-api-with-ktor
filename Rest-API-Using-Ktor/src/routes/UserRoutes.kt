package com.erselankhan.routes

import com.erselankhan.constant.RoutesConstant
import com.erselankhan.model.UserEntity
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun Application.registerUserRoutes() {
    routing {
        userRouting()
    }
}

fun Route.userRouting() {
    var testUserList = mutableListOf<UserEntity>()

    route(RoutesConstant.USER_ROUTES) {
        get() {
            if (testUserList.isEmpty()) {
                userNotFoundResponse()
            } else {
                call.respondText(text = "User List!!!", status = HttpStatusCode.OK)
            }
        }
        get("{id}") {
            if (testUserList.isNotEmpty()) {
                val id = call.parameters["id"] ?: return@get userNotFoundResponse()
                val userEntity = testUserList.find { it.id == id } ?: return@get userNotFoundResponse()
                call.respond(userEntity)
            } else {
                userNotFoundResponse()
            }
        }
        delete("{id}") {

        }
        post {

        }
        put {

        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.userNotFoundResponse() {
    call.respondText(text = "Users not found", status = HttpStatusCode.NotFound)
}