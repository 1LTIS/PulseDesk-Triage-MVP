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
