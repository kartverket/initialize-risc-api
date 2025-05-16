# Initialize RiSc
`initialize-risc-api` is a micro-service that generates default-RiSc's based on default RiScs in Airtable and security metrics from [sikkerhetsmetrikker](https://github.com/kartverket/sikkerhetsmetrikker).

## Run the application
`intialize-risc-api` needs some environment variables to be runned.

| Env var                      | Description                                                  | Required? |
|------------------------------|--------------------------------------------------------------|-----------|
| AIRTABLE_API_TOKEN           | API token used to authenticate requests to the Airtable API. | ✅         |
| AIRTABLE_BASE_ID             | Identifier for the specific Airtable base being accessed.    | ✅         |
| AIRTABLE_RECORD_ID           | ID of a particular record in the Airtable base.              | ✅         |
| AIRTABLE_TABLE_ID            | ID of a particular table in the Airtable base.               | ✅         |

---

## 🧪 Running the application locally

1. Copy the example env file and fill in required values:

   ```bash
   cp .env.example .env
   ```


2. Start the application using Docker Compose:

   ```bash
   docker compose up --build
   ```

3. Test that the application is running:

   ```bash
   curl http://localhost:8085/health
   ```


You should get a response: `All good!`
