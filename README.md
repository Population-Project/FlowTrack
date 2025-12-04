# FlowTrack

A Spring Boot application for collecting, processing, and providing real-time traffic flow data and congestion information for Seoul roads, highways, and trade areas.

## Project Overview

FlowTrack is a research internship project (25-2) that integrates multiple traffic data sources to provide comprehensive traffic information:

- **Seoul Roads**: Real-time traffic speed and travel time data from Seoul's road network
- **Highways**: Traffic information from Korean expressways via EX Corporation API
- **Trade Areas**: Congestion data for commercial areas via SK Open API
- **Seoul Areas**: Area-based traffic and demographic data

## Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Build Tool**: Gradle
- **Database**: H2 (in-memory, with PostgreSQL mode)
- **ORM**: JPA/Hibernate
- **Additional Libraries**:
  - Spring Data JPA
  - Spring WebFlux
  - Spring WebSocket
  - Jackson (JSON/XML processing)
  - Apache POI (Excel processing)
  - Lombok

## Project Structure

```
src/main/java/com/example/flowtrack/
├── FlowTrackApplication.java          # Main application entry point
├── GithubRunner.java                  # GitHub integration runner
├── common/
│   ├── config/
│   │   ├── ExProps.java              # EX Corporation API configuration
│   │   ├── RestClientConfig.java     # REST client configuration
│   │   └── SeoulProps.java           # Seoul API configuration
│   └── util/
│       └── ExcelReader.java          # Excel file reading utility
├── highwayroad/                       # Highway traffic information
│   ├── controller/
│   │   └── HighwayController.java
│   ├── dto/
│   │   ├── HighwayInfoDto.java
│   │   └── HighwayResponse.java
│   └── service/
│       └── HighwayService.java
├── seoulroad/                         # Seoul road traffic information
│   ├── controller/
│   │   └── RoadController.java
│   ├── dto/
│   │   ├── LiveRoadResponse.java
│   │   ├── RoadInfoDto.java
│   │   └── RoadMetaResponse.java
│   └── service/
│       ├── RoadService.java
│       ├── RoadCsvWriter.java
│       └── RoadDataProcessor.java
├── seoulArea/                         # Seoul area-based data
│   ├── controller/
│   │   └── AreaController.java
│   ├── dto/
│   │   ├── AreaDto.java
│   │   └── AreaResponse.java
│   └── service/
│       ├── AreaService.java
│       └── AreaDataProcessor.java
└── sk/
    └── tradeArea/                    # SK trade area congestion data
        ├── controller/
        │   ├── TradeAreaController.java
        │   └── TradeAreaMetaController.java
        ├── dto/
        │   ├── TradeArea.java
        │   ├── TradeAreaMetaResponse.java
        │   └── TradeAreaResponse.java
        ├── repository/
        │   └── TradeAreaRepository.java
        └── service/
            ├── TradeAreaService.java
            └── TradeAreaMetaService.java
```

## Configuration

### Application Properties

Configuration files are located in `src/main/resources/`:

- **application.yml**: Main configuration file
- **application-cli.yml**: CLI-specific configuration

### Key Configuration Items

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:traffic;MODE=PostgreSQL;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver

seoul:
  base-url: http://openapi.seoul.go.kr:8088
  api-key: ${TOPIS_API_KEY}

ex:
  base-url: https://data.ex.co.kr/openapi/
  api-key: 7730526404
```

## Setup and Installation

### Prerequisites

- Java 21 or higher
- Gradle (included via gradle wrapper)

### Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd FlowTrack
   ```

2. **Set environment variables**
   ```bash
   set TOPIS_API_KEY=your_api_key_here
   ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Seoul Roads
- **GET** `/road/{linkId}` - Get real-time traffic information for a specific road link
- **GET** `/road/all` - Get all road traffic data

### Highways
- **GET** `/highway/info` - Get highway traffic information
- **POST** `/highway/export` - Export highway data to CSV

### Trade Areas
- **GET** `/tradearea/{areaId}` - Get congestion information for a trade area
- **GET** `/tradearea/meta` - Get trade area metadata

### Seoul Areas
- **GET** `/area/{areaId}` - Get area-based data
- **GET** `/area/all` - Get all area data

## Features

### Traffic Data Collection
- Real-time Seoul road traffic data (speed, travel time)
- Expressway traffic information via EX Corporation API
- Trade area congestion levels via SK Open API
- Seoul area demographic and traffic data

### Data Processing
- CSV export functionality for highway data
- Excel file reading and processing
- XML/JSON data transformation and mapping
- Real-time data streaming via WebSocket

### H2 Database Console
Access the H2 database console at:
```
http://localhost:8080/h2-console
```

## Development

### Running Tests
```bash
./gradlew test
```

### Code Style
- Project uses Lombok for reducing boilerplate code
- Follows Spring Boot best practices

## Database

The application uses an H2 in-memory database in PostgreSQL-compatible mode:

- **URL**: `jdbc:h2:mem:traffic;MODE=PostgreSQL;DB_CLOSE_DELAY=-1`
- **Console**: Available at `/h2-console` (when enabled in configuration)
- **Auto-update**: Hibernate automatically creates/updates schema on startup

## API Keys and Credentials

The following external API keys are required:

| Service | Environment Variable | Configuration |
|---------|---------------------|-----------------|
| Seoul OpenAPI | `TOPIS_API_KEY` | `seoul.api-key` |
| EX Corporation | - | Embedded in `application.yml` |
| SK Open API | `sk.api.appkey` | Profile-specific config |

## Common Tasks

### Export Highway Data
```java
highwayService.fetchAndSave("output/path/highways.csv");
```

### Get Seoul Road Information
```java
RoadInfoDto roadInfo = roadService.getRoadInfo("linkId");
```

### Fetch Trade Area Congestion
```java
TradeAreaResponse congestion = tradeAreaService.getCongestion("areaId");
```

## Troubleshooting

### Database Connection Issues
- Ensure H2 database mode is set to PostgreSQL-compatible
- Check that Spring JPA is properly configured

### API Connection Errors
- Verify API keys are set as environment variables
- Check API endpoints are accessible from your network
- Ensure correct API endpoint URLs in configuration

### Port Already in Use
If port 8080 is already in use, modify `server.port` in `application.yml`:
```yaml
server:
  port: 8081  # Use a different port
```

## License

This project is a research internship project for academic purposes.

## Contact and Support

For issues and questions, please contact the project maintainers through the Population-Project repository.
