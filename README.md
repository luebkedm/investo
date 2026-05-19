# Investo - Investment Simulator

A JavaFX application that simulates investment growth over time with tax considerations.

## Features

- **LineChart Visualization**: Large, interactive chart displaying investment growth from now until 2045
- **Customizable Parameters**:
  - Initial investment amount
  - Annual interest rate
- **Tax Handling**: Automatically applies 25% tax on investment gains
- **Real-time Updates**: Recalculate and update the chart instantly with new parameters

## Technology Stack

- **Java 17**
- **JavaFX 21.0.2**
- **Maven**

## Prerequisites

- Java 17 or higher installed
- Maven 3.6+ installed

## Build Instructions

1. Navigate to the project directory:
   ```bash
   cd e:\prog\java\Investo
   ```

2. Build the project:
   ```bash
   mvn clean package
   ```

## Run Instructions

### Option 1: Using Maven Plugin (Recommended)
```bash
mvn javafx:run
```

### Option 2: Running the JAR
```bash
mvn clean javafx:run
```

## How to Use

1. Enter the **Start Amount** (initial investment in euros)
2. Enter the **Annual Interest Rate** (percentage)
3. Click **Update Chart** to recalculate and visualize the growth
4. The chart displays the investment value from the current year until 2045
5. A 25% tax is automatically applied to all investment gains

## Calculation Formula

- For each year: `Interest = CurrentAmount × InterestRate`
- Tax Applied: `TaxOnInterest = Interest × 25%`
- Net Interest: `NetInterest = Interest - TaxOnInterest`
- Next Year Amount: `CurrentAmount + NetInterest`

## Example

- Starting Amount: €10,000
- Annual Interest Rate: 5%
- Tax Rate: 25%

The chart will show the compounded growth year by year, accounting for the annual 25% tax on gains.

## Project Structure

```
Investo/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/investo/
│   │   │       ├── InvestoApp.java
│   │   │       ├── model/
│   │   │       │   ├── Investment.java
│   │   │       │   └── InvestmentDataPoint.java
│   │   │       └── ui/
│   │   │           └── InvestmentController.java
│   │   └── resources/
│   └── test/
│       └── java/
└── README.md
```

## Notes

- The application uses the current year as the starting point for calculations
- Data points are calculated for each year up to and including 2045
- The 25% tax rate is applied to the interest earned, not the principal
