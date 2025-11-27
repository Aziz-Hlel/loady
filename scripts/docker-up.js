#!/usr/bin/env node
import { execSync } from 'child_process';
import { existsSync, writeFileSync, readFileSync } from 'fs';
import { join } from 'path';

// Colors
const GREEN = '\x1b[32m';
const YELLOW = '\x1b[33m';
const NC = '\x1b[0m';

// Get env argument: dev / stage / prod
const envArg = process.argv[2] || 'dev'; // default to dev
if (!['dev', 'stage', 'prod'].includes(envArg)) {
  console.error(`Invalid environment: ${envArg}`);
  process.exit(1);
}

// Paths
const ROOT = execSync('git rev-parse --show-toplevel').toString().trim();
const DOCKER_ROOT = join(ROOT, 'docker');

const ENV_LOCAL = join(ROOT, '.env.local');
const ENV_ROOT = join(ROOT, '.env');
const ENV_MAP = {
  dev: join(ROOT, 'config', '.env.dev'),
  stage: join(ROOT, 'config', '.env.stage'),
  prod: join(ROOT, 'config', '.env.prod'),
};
const DOCKER_COMPOSE_MAP = {
  dev: join(DOCKER_ROOT, 'compose.dev.yml'),
  stage: join(DOCKER_ROOT, 'compose.stage.yml'),
  prod: join(DOCKER_ROOT, 'compose.prod.yml'),
};

// Ensure env files exist
[ENV_LOCAL, ENV_ROOT, ENV_MAP[envArg]].forEach((f) => {
  if (!existsSync(f)) writeFileSync(f, '');
});

// Merge env files
const env = Object.assign(
  {},
  process.env,
  parseEnvFile(ENV_MAP[envArg]),
  parseEnvFile(ENV_LOCAL),
  parseEnvFile(ENV_ROOT),
);
env.PROJECT_ROOT = ROOT;

// Logs
console.log(`${YELLOW}🚀 Starting Docker in ${envArg.toUpperCase()} Env...${NC}`);

// Run Docker Compose
execSync(`docker compose -f ${DOCKER_COMPOSE_MAP[envArg]} up --build`, {
  stdio: 'inherit',
  env,
});

console.log(`${GREEN}✅ Done!${NC}`);

// Helper: parse .env
function parseEnvFile(filePath) {
  const envObj = {};
  if (!existsSync(filePath)) return envObj;
  const lines = readFileSync(filePath, 'utf8').split('\n');
  lines.forEach((line) => {
    if (!line || line.startsWith('#')) return;
    const [key, ...vals] = line.split('=');
    envObj[key] = vals.join('=');
  });
  return envObj;
}
