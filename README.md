# PulseDesk Triage MVP - IBM Internship Challenge 

A backend application built with Spring Boot that acts as a triage system for PulseDesk. It collects user comments and utilizes the **Hugging Face Inference API** (Mistral-7B-Instruct) to intelligently decide if a comment should be converted into a structured support ticket.

##  Features Implemented
- **RESTful API:** Endpoints to submit comments and retrieve generated tickets.
- **AI Integration:** Prompts a Hugging Face LLM to analyze the sentiment/context of the comment and outputs a strict JSON format.
- **Automated Ticket Generation:** Extracts `title`, `category`, `priority`, and a `summary` if the comment is deemed actionable.
- **Embedded Database:** Uses H2 in-memory database for zero-config data persistence.
- ** OPTIONAL BONUS INCLUDED:** A built-in graphical User Interface (UI) to easily submit comments and view generated tickets without needing Postman.

##  Tech Stack
- **Java 17** 
- **Spring Boot 3** (Web, Data JPA)
- **H2 Database** (In-memory)
- **Hugging Face Serverless Inference API** (`mistralai/Mistral-7B-Instruct-v0.3`)
- **HTML/JS + Water.css** (For the frontend bonus)

---

##  How to Run the Application

### 1. Prerequisites
- Java 17 or higher installed.
- Maven installed (or you can use the provided `mvnw` wrapper).

### 2. API Token Configuration
The Hugging Face API token is already included in `src/main/resources/application.properties` for testing purposes (allowed via GitHub Secret Scanning for this assessment). 
If you wish to use your own token, replace the value here:
```properties
hf.api.token=YOUR_HUGGINGFACE_API_TOKEN
```

### 3. Build and Run
Open your terminal in the project root directory and execute:
```bash
# For Windows
.\mvnw spring-boot:run

# For Mac/Linux
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

---

##  Testing the App (The Easy Way)

Since I completed the **Optional Bonus**, the easiest way to evaluate this task is via the embedded UI!

1. Run the application.
2. Open your browser and navigate to: **[http://localhost:8080](http://localhost:8080)**
3. Type a comment (e.g., *"The app crashes every time I try to update my profile picture."*) and hit submit. 
4. The AI will process it and generate a bug ticket with High/Medium priority automatically!

---

##  API Endpoints Reference

If you prefer testing via `cURL` or Postman, here are the available REST endpoints:

### 1. Submit a New Comment
**POST** `/api/comments`
```json
{
  "content": "I can't seem to find the billing history anywhere, please help!",
  "channel": "Web Form"
}
```

### 2. View All Comments
**GET** `/api/comments`

### 3. View All Generated Tickets
**GET** `/api/tickets`
*Example Response:*
```json
[
  {
    "id": 1,
    "title": "Missing Billing History",
    "category": "billing",
    "priority": "medium",
    "summary": "User is unable to locate their billing history on the platform.",
    "originComment": {
      "id": 1,
      "content": "I can't seem to find the billing history anywhere, please help!",
      "channel": "Web Form"
    }
  }
]
```

### 4. View a Specific Ticket
**GET** `/api/tickets/{ticketId}`

---

##  AI Prompting Strategy
To ensure the LLM strictly returns a parsable JSON object instead of conversational text, I implemented a strict system prompt overriding the `return_full_text` parameter and manually stripping any potential Markdown code block ticks (` ```json `) in the service layer. This ensures backend stability when parsing the AI's response.

Thank you for reviewing my code!
