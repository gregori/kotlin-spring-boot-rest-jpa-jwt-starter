package tech.gregori.api.address

import com.opencsv.bean.CsvBindByName
import java.time.LocalDateTime

class AddressImportDto {
  @CsvBindByName(required = true)
  var id: Int? = null

  @CsvBindByName(required = true)
  var name: String? = null

  @CsvBindByName(required = true)
  var street: String? = null

  @CsvBindByName(required = true)
  var zip: String? = null

  @CsvBindByName(required = true)
  var city: String? = null

  @CsvBindByName(required = true)
  var email: String? = null

  @CsvBindByName(required = true)
  var tel: String? = null

  @CsvBindByName(required = false)
  var options: String? = null

  @CsvBindByName(required = false)
  var things: String? = null

  fun toAddress() = Address(id, name, street, zip, city, email, tel, false, LocalDateTime.now(), options, things)
}
