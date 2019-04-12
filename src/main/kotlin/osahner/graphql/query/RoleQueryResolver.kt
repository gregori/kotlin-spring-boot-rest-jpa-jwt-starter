package osahner.graphql.query

import osahner.service.RoleRepository

class RoleQueryResolver(
  private val roleRepository: RoleRepository
) {
  fun role(roleName: String) = roleRepository.findByRoleName(roleName)
}
