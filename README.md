# Tax Calculation thread-pooling .

multi-threaded web server with thread-pooling implemented in Java/Spring.

the application simulate long-time calculations and handles the requests in a thread poll.


## API

# find tax request information
Returns all information of the given calculation

Request

GET tax/calculation?clientID=2001010306&year=2019 HTTP/1.1 Accept: application/json

Response Success

HTTP/1.1 200 OK Content-Type: application/xml; charset=utf-8
```json
 {
     "id": 1,
     "clientID": "2001010306",
     "year": 2019,
     "annualIncome": 1000,
     "status": "PROCESSED",
     "tax": 300
 }
```

# Request Tax calculation
Request a stubbed calculation given information on year income value

Request

POST /tax/calculation HTTP/1.1 Accept: application/json

Request body
```json
 {
     "clientID":2001010306,
     "year":2019,
     "annualIncome":1000.0
 }
```

Response Success

HTTP/1.1 200 OK Content-Type: application/xml; charset=utf-8
```json
{
    "id": 1,
    "clientID": "2001010306",
    "year": 2019,
    "annualIncome": 1000,
    "status": "POSTED",
}
```

## How to run
This is a spring boot application, so all you have to do is run the app as a regular java application
to expose the API in your local environment, by default spring will boot your application under localhost:8080/.


## Poll size
the pool size is configurable on the property executor.pool.size under the ./application.properties file