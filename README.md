**Create Admin**
*URL:* `/create-admin`
*Method:* `POST`
*Request Body:* `AdminDTO` (JSON)
 *Response:* `ResponseData` (JSON) containing the created admin details

**Delete Admin**
*URL:* `/delete-admin/{id}`
*Method:* `DELETE`
*Path Variable:* `id` (integer)
 *Response:* `ResponseData` (JSON) with a success message

Customer APIs
**Add Customer**
*URL:* `/admin/add-customer`
*Method:* `POST`
*Request Body:* `CustomerDTO` (JSON)
*Response:* `ResponseData` (JSON) containing the created customer details

**Delete Customer**
*URL:* `/admin/delete-customer/{id}`
*Method:* `DELETE`
*Path Variable:* `id` (integer)
*Response:* `ResponseData` (JSON) with a success message

**Get All Customers**
*URL:* `/admin/all-customers`
*Method:* `GET`
*Response:* `ResponseData` (JSON) containing a list of customer details

Booking APIs
**Update Booking Status**
*URL:* `/admin/update-booking-status/{id}`
*Method:* `PUT`
*Path Variable:* `id` (integer)
*Request Parameter:* `status` (string)
*Response:* `ResponseData` (JSON) containing the updated booking details

**Delete Booking**
*URL:* `/admin/delete-booking/{id}`
*Method:* `DELETE`
*Path Variable:* `id` (integer)
*Response:* `ResponseData` (JSON) with a success message

**Get All Bookings**
*URL:* `/admin/all-bookings`
*Method:* `GET`
*Response:* `ResponseData` (JSON) containing a list of booking details

Payment APIs
**Delete Payment**
**URL**: `/admin/delete-payment/{id}`
*Method:* `DELETE`
*Path Variable:* `id` (integer)
*Response:* `ResponseData` (JSON) with a success message

**Get All Payments**
*URL:* `/admin/all-payments`
*Method:* `GET`
*Response:* `ResponseData` (JSON) containing a list of payment details

Gas Agency APIs
**Update Gas Agency**
*URL:* `/admin/update-gas-agency/{id}`
*Method:* `PUT`
*Path Variable:* `id` (integer)
*Request Parameters:* `name` (string), `location` (string)
*Response:* `ResponseData` (JSON) containing the updated gas agency details

**Delete Gas Agency**
*URL:* `/admin/delete-gas-agency/{id}`
*Method:* `DELETE`
*Path Variable:* `id` (integer)
*Response:* `ResponseData` (JSON) with a success message

**Get All Gas Agencies**
*URL:* `/admin/all-gas-agencies`
*Method:* `GET`
*Response:* `ResponseData` (JSON) containing a list of gas agency details
