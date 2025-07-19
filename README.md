# gas-booking-system

**Create Customer**
*URL:* /create-customer
*Method:* POST
*Request Body:* CustomerDTO (JSON)
*Response:* ResponseData (JSON) containing the created customer details

**Get All Customers**
*URL:* /fetch-all-customers
*Method:* GET
*Response:* ResponseData (JSON) containing a list of customer details

**Get Customer by ID**
*URL:* /fetch-customer/{id}
*Method:* GET
*Path Variable:* id (integer)
*Response:* ResponseData (JSON) containing the customer detail if found, or a failure message

**Delete Customer**
*URL:* /delete-customer/{id}
*Method:* DELETE
*Path Variable:* id (integer)
*Response:* ResponseData (JSON) with a success or error message
