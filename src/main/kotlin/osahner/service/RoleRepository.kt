package osahner.service

import org.springframework.data.repository.CrudRepository
import osahner.domain.Role
import java.util.*

interface RoleRepository : CrudRepository<Role, Long> {
  fun findByRoleName(roleName: String): Optional<Role>
}
