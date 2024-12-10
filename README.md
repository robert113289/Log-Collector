# Log Collector

## Project Overview

Log Collector is a Spring Boot application designed to fetch and filter log entries from log files. The application
provides a RESTful API to retrieve logs based on specified criteria such as filename, number of entries, and keyword. It
also includes a simple web interface for interacting with the API.

## Project Design

### Architecture

The project follows a typical Spring Boot architecture with the following components:

- **Controller**: Handles HTTP requests and maps them to service methods.
- **Service**: Contains the business logic for fetching and filtering logs.
- **Model**: Represents the data structures used in the application.
- **Static Resources**: Contains the HTML, CSS, and JavaScript files for the web interface.

### API Design

The API consists of two main endpoints:

- **GET /logs**: Fetches logs from a specified file with optional filtering by the number of entries and keyword.
    - **Parameters**:
        - `filename` (String, required): The name of the log file.
        - `lastN` (Integer, optional, default: 20): The number of last log entries to fetch.
        - `keyword` (String, optional): A keyword to filter log entries.
    - **Response**: A `LogResponse` object containing the filtered log entries.

- **GET /files**: Retrieves a list of available log files.
    - **Response**: A list of filenames.

### Testing Strategy

The project includes unit tests for the service layer and end to end tests for the controller layer. The tests cover
various
scenarios such as fetching logs with and without keywords, limiting the number of log entries, and retrieving the list
of log files.

### Performance Testing

Performance testing was conducted using large log files downloaded
from [LogHub](https://github.com/logpai/loghub?tab=readme-ov-file). The tests involved searching for specific keywords
and fetching a large number of log entries to ensure the application performs efficiently with large files.

To optimize performance, the strategy involved reading the file in reverse order and stopping once the required number
of lines were collected. This approach avoids reading the entire file into memory and improves performance by
terminating early when enough logs are collected.

## Setup Instructions

### Prerequisites

- **Java**: Ensure you have Java 21 installed.
- **Gradle**: Ensure you have Gradle installed.
- **IntelliJ IDEA**: Recommended IDE for development.

### Steps

1. **Clone the Repository**

2. **Open the Project in IntelliJ IDEA**:
    - Open IntelliJ IDEA.
    - Select `File > Open` and navigate to the `log-collector` directory.
    - Click `OK` to open the project.

3. **Configure the Application**:
    - Set the `log.base.path` property in `application.properties` to the directory containing your log files. By
      default, this is the `/var/logs` directory.

4. **Build the Project**:
    - Open the terminal in IntelliJ IDEA.
    - Run the following command to build the project:
      ```sh
      ./gradlew build
      ```

5. **Run the Application**:
    - In IntelliJ IDEA, navigate to the `LogCollectorApplication` class.
    - Right-click and select `Run 'LogCollectorApplication'`.

6. **Access the Web Interface**:
    - Open a web browser and navigate to [http://localhost:8080](http://localhost:8080).
