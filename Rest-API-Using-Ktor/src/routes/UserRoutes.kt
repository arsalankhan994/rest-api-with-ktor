package com.erselankhan.routes

import com.erselankhan.constant.RoutesConstant
import com.erselankhan.model.UserEntity
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlin.random.Random

fun Application.registerUserRoutes() {
    routing {
        userRouting()
    }
}

fun Route.userRouting() {
    val testUserList = mutableListOf<UserEntity>()
    for (i in 1..5) {
        testUserList.add(UserEntity(id = i.toString(), firstName = "User $i", lastName = "Khan",
        phoneNumber = Random.nextInt(0, 100).toString(), emailAddress = "email $i",
        password = "12345", confirmPassword = "12345"))
    }

    route(RoutesConstant.USER_ROUTES) {
        get {
            if (testUserList.isNotEmpty()) {
                call.respond(status = HttpStatusCode.OK, testUserList)
            } else {
                userNotFoundResponse()
            }
        }
        get("{id}") {
            if (testUserList.isNotEmpty()) {
                val id = call.parameters["id"] ?: return@get missingIdParameter()
                val userEntity = testUserList.find { it.id == id } ?: return@get userNotFoundResponse()
                call.respond(status = HttpStatusCode.OK, userEntity)
            } else {
                userNotFoundResponse()
            }
        }
        delete("{id}") {
            if(testUserList.isNotEmpty()) {
                val id = call.parameters["id"] ?: return@delete missingIdParameter()
                val userEntity = testUserList.find { it.id == id } ?: return@delete userNotFoundResponse()
                call.respond(status = HttpStatusCode.OK, userEntity)
            }
        }
        post {
            val userEntity = call.receive<UserEntity>()
            if (!testUserList.contains(userEntity)) {
                if (userEntity.password == userEntity.confirmPassword) {
                    testUserList.add(userEntity)
                    call.respond(status = HttpStatusCode.OK, "User Created Successfully")
                } else {
                    call.respond(status = HttpStatusCode.OK, "Some message")
                }
            } else {
                call.respond(status = HttpStatusCode.BadRequest, "User Already Exist")
            }
        }
        put {
            val userEntity = call.receive<UserEntity>()
            val oldUserEntity = testUserList.find { it.id == userEntity.id }
            testUserList.replace(oldUserEntity, userEntity)
            call.respond(status = HttpStatusCode.OK, "User Updated Successfully")
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.userNotFoundResponse() {
    call.respondText(text = "Users not found", status = HttpStatusCode.NotFound)
}

fun <E> Iterable<E>.replace(old: E, new: E) = map { if (it == old) new else it }

private suspend fun PipelineContext<Unit, ApplicationCall>.missingIdParameter() {
    call.respondText(text = "Missing id parameter", status = HttpStatusCode.BadRequest)
}