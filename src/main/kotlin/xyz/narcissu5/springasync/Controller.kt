package xyz.narcissu5.springasync

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    lateinit var userDAO: UserDAO

    @GetMapping("{id}")
    suspend fun sayHello(@PathVariable id: Int): User {
        return userDAO.getUser(id)
    }
}