# gas-booking-system

**Create Gas Agency**
*URL:* /create-gas-agency
*Method:* POST
*Request Body:* GasAgencyDTO (JSON)
*Response:* ResponseData (JSON) containing the created gas agency details

**Get All Gas Agencies**
*URL:* /fetch-all-gas-agencies
*Method:* GET
*Response:* ResponseData (JSON) containing a list of gas agency details

**Get Gas Agency by ID**
*URL:* /fetch-gas-agency/{id}
*Method:* GET
*Path Variable:* id (integer)
*Response:* ResponseData (JSON) containing the gas agency details if found, or a failure message

**Update Gas Agency**
*URL:* /update-gas-agency/{id}
*Method:* PUT
*Path Variable:* id (integer)
*Request Body:* GasAgencyDTO (JSON)
*Response:* ResponseData (JSON) containing the updated gas agency details

**Delete Gas Agency**
*URL:* /delete-gas-agency/{id}
*Method:* DELETE
*Path Variable:* id (integer)
*Response:* ResponseData (JSON) with a success or error message
