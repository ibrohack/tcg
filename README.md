# Chaos & Monkeys TCG

A dynamic Trading Card Game (TCG) web application built with Spring Boot and Thymeleaf. 

Chaos & Monkeys allows players to collect, buy, and trade a wide variety of primate-themed cards. You can build custom decks, open booster packs, and optimize your collection while monitoring live server stats.

## 👥 Authors
Designed and developed by **Brayan, Adam, Oihan, and Asier**.

## 📚 Knowledgebase
For comprehensive documentation on the project architecture, features, and database design, please visit our [Project Knowledgebase](https://deepwiki.com/ibrohack/tcg).

## ✨ Features
*   **Player Authentication & Profiles:** Secure registration and login using jBCrypt. Players can customize their profile and upload avatars.
*   **Booster Packs:** Test your luck by opening card packs with dynamic rarity probabilities. Earn free packs periodically!
*   **Flash Shop:** A live-updating shop environment where players can buy specific cards. The shop inventory rotates based on server-side events.
*   **Deck Builder:** Create, manage, and edit your custom decks using the cards in your collection. 
*   **Card Collection System:** Over 100 unique monkey/primate cards with different rarities (Common, Rare, Epic, Legendary, Mythic, and the elusive Arok). Automatic duplicate-selling mechanics built right into the database.
*   **Economy System:** In-game currency ("Coins") to buy packs and individual cards. Contains simulated real-money coin bundles.
*   **Live Dashboard:** Real-time statistics including active players, most/least common cards, and latest additions.

## 🛠️ Technology Stack
*   **Backend:** Java 25, Spring Boot 4.0.x (WebMVC, JDBC)
*   **Frontend:** HTML5, Thymeleaf, Javascript, Vanilla CSS (styled using Tailwind CSS CLI)
*   **Database:** MySQL (Heavily relies on Stored Procedures, Triggers, and Events)
*   **Build Tools:** Maven & npm (for Tailwind compilation)

## 🚀 Getting Started

### Prerequisites
*   JDK 25 or higher
*   Node.js and npm (for managing frontend styling)
*   MySQL Server

### Database Setup
1. Create a MySQL database named `CHAOSMONKEYS`.
2. Execute the provided SQL script `database/Chaos&MonkeysDatabase.sql` to initialize the schema, populate the 100+ cards, insert dummy players, and create the required Stored Procedures, Triggers, and Events.
3. Keep the MySQL Event Scheduler running (`SET GLOBAL event_scheduler = ON;`). This is critical for the shop rotations and free packs.
4. Verify your database credentials in `src/main/resources/configDB.properties`:
   ```properties
   DB=chaosmonkeys
   Conn=jdbc:mysql://localhost:3306/chaosmonkeys?serverTimezone=Europe/Madrid&useSSL=false
   DBUser=root
   DBPass=YOUR_PASSWORD
   Driver=com.mysql.cj.jdbc.Driver
   ```

### Building & Running
1. Install frontend UI dependencies:
   ```bash
   npm install
   ```
2. Build the Tailwind CSS (Required if you make changes to HTML/CSS files):
   ```bash
   npm run build:css
   ```
   *(Use `npm run dev:css` to watch for file changes during development)*
3. Run the Spring Boot application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Access the application in your browser at `http://localhost:8080`.

## 📁 Project Structure Highlights
*   **`/src/main/java/com/dami/tcg/controller/`**: MVC endpoints routing to the views logic.
*   **`/src/main/java/com/dami/tcg/modelo/`**: Domain objects and standard interfaces with direct JDBC implementations.
*   **`/src/main/resources/templates/`**: Frontend Thymeleaf HTML views.
*   **`/src/main/resources/static/`**: Client side JS logic, raw CSS inputs, and game images.
*   **`/database/`**: Contains the critical `Chaos&MonkeysDatabase.sql` dump.
*   **`/data/images/players/`**: Managed external volume allowing safe storage for user avatars outside the classpath.


