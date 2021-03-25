/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.customs.rosmfrontend.forms.models.subscription

import play.api.libs.json.Json
import uk.gov.hmrc.customs.rosmfrontend.domain.messaging.Address
import uk.gov.hmrc.customs.rosmfrontend.domain.subscription.AddressDetails

case class AddressViewModel(street: String, city: String, postcode: Option[String], countryCode: String) {
  val addressDetails = AddressDetails(street, city, postcode, countryCode)
  def sanitise(): AddressViewModel =
    this.copy(street = this.street.trim, city = this.city.trim)
}

object AddressViewModel {
  implicit val jsonFormat = Json.format[AddressViewModel]

  val sixLineAddressLine1MaxLength = 35
  val sixLineAddressLine2MaxLength = 34
  val townCityMaxLength = 35

  def apply(sixLineAddress: Address): AddressViewModel = {
    val line1 = (sixLineAddress.addressLine1.trim.take(sixLineAddressLine1MaxLength) + " " + sixLineAddress.addressLine2
      .getOrElse("")
      .trim
      .take(sixLineAddressLine2MaxLength)).trim
    val townCity = sixLineAddress.addressLine3.getOrElse("").trim.take(townCityMaxLength)
    val postCode = sixLineAddress.postalCode
    val countryCode = sixLineAddress.countryCode
    AddressViewModel(line1, townCity, postCode, countryCode)
  }

  def apply(contactDetailsModel: ContactDetailsModel): AddressViewModel =
    AddressViewModel(street=contactDetailsModel.street.getOrElse(""),
      city = contactDetailsModel.city.getOrElse(""),
      postcode = contactDetailsModel.postcode,
      countryCode = contactDetailsModel.countryCode.getOrElse(""))
}
