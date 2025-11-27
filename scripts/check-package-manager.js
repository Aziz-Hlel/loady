const exec = require('child_process').execSync;

const pkgManager = process.env.npm_config_user_agent || '';
if (!pkgManager.startsWith('pnpm')) {
  console.error(`
    ❌ You must use pnpm to install dependencies.
    Detected: ${pkgManager || 'unknown'}
  `);
  process.exit(1);
}
console.log('✅ pnpm detected as package manager. (Good choice!)');
// ! doesnt acutally work