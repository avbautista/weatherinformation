# Weather Log Project

This application is a weather monitoring and logging API service.

# Configuring application.properties

  - The application.properties file should be updated to match the current saved settings for MySQL, and the schema that is needed for the project is 'weather'
  - You could also use your own API Key for https://openweathermap.org/api

# Running the Application

 - In the command line, go to the topmost folder (where the pom.xml is located), then input the following commands (assuming Maven is installed, and make sure nothing is running on the selected port! 8080 is the default port on the application properties):
 
```sh
$ mvn clean install
$ mvn spring-boot:run
```

## API Documentation
### Get and Save Location Weather (/weather)

This Get API allows you to input a weather then get the actual weather and temperature and save this to the application

| Parameter | Description | Required? |
| ------ | ------ | ------ |
| q | City name, state code and country code divided by comma, use ISO 3166 country codes. (ex. London, Prague and San Francisco)  | Yes
| units | Units of measurement. standard, metric and imperial units are available. If you do not use the units parameter, standard units will be applied by default | No
| lang | You can use this parameter to get the output in your language. | No

Sample GET Request (deployed in local, port 8080)
```sh
http://localhost:8080/weather?q=Prague
```

Sample JSON Response
```sh
{
    "code": 200,
    "message": "Successfully retrieved weather data.",
    "location": "Prague",
    "actualWeather": "broken clouds ",
    "temperature": "279.19"
}
```

Common Response Codes
| Parameter | Description |
| ------ | ------ |
| 200 | Ok Response |
| 404 | No city found |

### Get Latest Weather Logs (/weather/getlatest)

This Get API allows you to get the top 5 latest weather logs that was saved using the Get and Save Location Weather API

Sample GET Request (deployed in local, port 8080)
```sh
http://localhost:8080/weather/getlatest
```

Sample JSON Response
```sh
{
    "code": 200,
    "message": "Successfully retrieved weather data.",
    "weatherList": [
        {
            "responseId": "6b4f1c4c-040b-4a82-81a8-fde64648138f",
            "location": "Prague",
            "actualWeather": "broken clouds ",
            "temperature": "279.05"
        },
        {
            "responseId": "c38410fa-63be-4030-b6e4-0f02fd8fe55b",
            "location": "Prague",
            "actualWeather": "scattered clouds ",
            "temperature": "279.04"
        },
        {
            "responseId": "e2860f07-1c47-4e7c-a1b8-256a91caf0d3",
            "location": "San Francisco",
            "actualWeather": "clear sky ",
            "temperature": "282.63"
        },
        {
            "responseId": "ba468f46-224b-492b-9931-712fd3430b26",
            "location": "Quezon",
            "actualWeather": "light rain ",
            "temperature": "297.69"
        },
        {
            "responseId": "faba8f73-2fe1-48f8-997f-2ef352dfa06b",
            "location": "London",
            "actualWeather": "overcast clouds ",
            "temperature": "280.72"
        }
    ]
}
```

Common Response Codes
| Parameter | Description |
| ------ | ------ |
| 200 | Ok Response |

**End of ReadMe**
