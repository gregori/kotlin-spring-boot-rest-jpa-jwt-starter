package tech.gregori.service

import org.springframework.data.repository.CrudRepository
import tech.gregori.domain.User
import java.util.*

interface UserRepository : CrudRepository<User, Long> {
  fun findByUsername(username: String): Optional<User>
}
