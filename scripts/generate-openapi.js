import { generate } from "openapi-typescript-codegen";

generate({
  input: "./contracts/openapi.yaml", // path to your snapshot
  output: "./apps/web/src/openapi",       // where generated code will live
  httpClient: "axios",                // or "axios" if you prefer
  useOptions: true,                   // use options object in methods
  useUnionTypes: true,                // better type safety
});