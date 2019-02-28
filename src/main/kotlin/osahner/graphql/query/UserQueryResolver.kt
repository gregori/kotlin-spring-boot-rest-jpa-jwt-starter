package osahner.graphql.query

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import osahner.service.UserRepository

@Component
class UserQueryResolver(
  private val userRepository: UserRepository
) : GraphQLQueryResolver {
  fun user(id: String) =
    userRepository.findByUsername(id)
}
