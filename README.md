# Initialize RiSc
`initialize-risc-api` is a micro-service that generates default-RiSc's based on default RiScs in Airtable and security metrics from [sikkerhetsmetrikker](https://github.com/kartverket/sikkerhetsmetrikker).

## Run the application
`intialize-risc-api` needs some environment variables to be runned.

| Env var                        | Description                                                                 | Required? |
|-------------------------------|-----------------------------------------------------------------------------|-----------|
| AIRTABLE_API_TOKEN            | API token used to authenticate requests to the Airtable API.               | ✅         |
| AIRTABLE_BASE_ID              | Identifier for the specific Airtable base being accessed.                  | ✅         |
| AIRTABLE_RECORD_ID            | ID of a particular record in the Airtable base.                            | ✅         |
| CLIENT_ID                     | Entra ID client ID used for authentication. Dummy is used in dev.         | ❌         |
| CLIENT_SECRET                 | Entra ID client secret used for authentication. Dummy is used in dev.     | ❌         |
| TENANT_ID                     | Identifier for the Microsoft Entra ID tenant. Dummy is used in dev.       | ❌         |
| SECURITY_TEAM_PUBLIC_KEY      | Public key for encryption used by the security team.                       | ✅         |
| SECURITY_PLATFORM_PUBLIC_KEY  | Public key for encryption used by the platform.                            | ✅         |
| BACKEND_PUBLIC_KEY            | Public key for backend encryption.                                         | ✅         |

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


You should get a response: `All good!`
