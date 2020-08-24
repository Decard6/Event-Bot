# Event-Bot setup guide for Heroku
1. Fork repository
2. Set buildpack to default gradle buildpack.
3. Get DB addon for your dyno.
4. Execute query from Database folder. (MySQL)
5. Set env vars
  BOT_PREFIX - Only first character of the string will be counted
  COMMAND_CHANNEL - Channel name of the channel you want commands to be used in.
  MOD_RANK - Role in discord that allows you to use commands.
  TOKEN - Discord bot token.
  DB_DIALECT - https://www.javatpoint.com/dialects-in-hibernate
  DB_URL
  DB_USER
  DB_PASSWORD
6. Deploy / Set auto-deployment.
