package tech.gregori.api.address

import tech.gregori.toArray
import tech.gregori.toMap
import tech.gregori.writeValueAsString
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Lob

@Entity
data class Address(
  @Id
  @GeneratedValue
  var id: Int?,

  var name: String?,

  var street: String?,

  var zip: String?,

  var city: String?,

  var email: String?,

  var tel: String?,

  var enabled: Boolean?,

  var lastModified: LocalDateTime?,

  @Lob var options: String?,

  @Lob var things: String?
) {
  fun toDTO() = AddressDto(
    id,
    name,
    street,
    zip,
    city,
    email,
    tel,
    enabled,
    lastModified,
    options.toMap(),
    things.toArray()
  )

  companion object {
    fun fromDTO(dto: AddressDto) = Address(
      dto.id,
      dto.name,
      dto.street,
      dto.zip,
      dto.city,
      dto.email,
      dto.tel,
      dto.enabled,
      LocalDateTime.now(),
      dto.options.writeValueAsString(),
      dto.things.writeValueAsString()
    )
  }
}
