# Initialize RiSc
`initialize-risc-api` is a micro-service that generates default-RiSc's based on default RiScs in Airtable and security metrics from [sikkerhetsmetrikker](https://github.com/kartverket/sikkerhetsmetrikker).

## Run the application
`intialize-risc-api` needs some environment variables to be runned.

| Env var  | Description | Required? |
| ------------- | ------------- |-----------|
| AIRTABLE_API_TOKEN | API token used to authenticate requests to the Airtable API. | ✅ |
| AIRTABLE_BASE_ID | Identifier for the specific Airtable base being accessed. | ✅ |
| AIRTABLE_RECORD_ID | ID of a particular record in the Airtable base. | ✅ |
| CLIENT_ID | Entra ID client ID used for authenticating requests made towards `initialize-risc-api`. | ❌ |
| CLIENT_SECRET | Entra ID client secret used for authenticating requests made towards `sikkerhetsmetrikker`. | ❌ |
| SIKKERHETSMETRIKKER_BASE_URL | Base URL for the Sikkerhetsmetrikker (Security Metrics) API. | ❌ |
| SIKKERHETSMETRIKKER_CLIENT_ID | Client ID specifically used for accessing the Sikkerhetsmetrikker API. | ❌ |
| TENANT_ID | Identifier for the Microsoft Entra ID tenant used in authentication. | ❌ |
