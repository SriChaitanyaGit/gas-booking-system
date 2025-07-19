**Create Booking**
*URL:* /create-booking
*Method:* POST
*Request Body:* BookingDTO (JSON)
*Response:* ResponseData (JSON) containing the created booking details

**Get All Bookings**
*URL:* /fetch-all-bookings
*Method:* GET
*Response:* ResponseData (JSON) containing a list of booking details

**Get Booking by ID**
*URL:* /fetch-booking/{id}
*Method:* GET
*Path Variable:* id (integer)
*Response:* ResponseData (JSON) containing the booking detail if found, or a failure message

**Update Booking Status**
*URL:* /update-booking-status/{id}
*Method:* PUT
*Path Variable:* id (integer)
*Request Parameter:* status (string)
*Response:* ResponseData (JSON) containing the updated booking details

**Delete Booking**
*URL:* /delete-booking/{id}
*Method:* DELETE
*Path Variable:* id (integer)
*Response:* ResponseData (JSON) with a success or error message

**PAYMENT API'S**


