package osahner.graphql.query

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import osahner.service.UserRepository

@Component
class UserQueryResolver(
  private val userRepository: UserRepository
) : GraphQLQueryResolver {
  @PreAuthorize("hasAuthority('STANDARD_USER')")
  fun user(id: String) =
    userRepository.findByUsername(id)
}
