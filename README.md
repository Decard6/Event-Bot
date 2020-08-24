# Event-Bot setup guide for Heroku
1. Fork repository
2. Set buildpack to default gradle buildpack.
3. Get DB addon for your dyno.
4. Execute query from Database folder. (MySQL)
5. Set env vars <br />
  BOT_PREFIX - Only first character of the string will be counted <br />
  COMMAND_CHANNEL - Channel name of the channel you want commands to be used in. <br />
  MOD_RANK - Role in discord that allows you to use commands. <br />
  TOKEN - Discord bot token. <br />
  DB_DIALECT - https://www.javatpoint.com/dialects-in-hibernate <br />
  DB_URL <br />
  DB_USER <br />
  DB_PASSWORD <br />
6. Deploy / Set auto-deployment.
7. Type "{prefix}event help" or "{prefix}char help" in your command channel to get a list of available commands. 
