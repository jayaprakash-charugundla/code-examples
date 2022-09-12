go mod init github.com/jc/slack-age-bot

go get github.com/shomali11/slacker


Go to api.slack.com/apps

1) Create an app

2) Create a token in Settings -> Basic Information -> App-Level Tokens
    
    **Token Name** - socket-token    
    **Scope** - connections:write
3) Enable Socket Mode in Settings -> Socket mode -> Connect using Socket Mode
4) Enable Events in Features -> Event Subscriptions
5) Add some bot events in Features -> Event Subscriptions -> Subscribe to bot events
6) Add some scopes in Features -> OAuth & Permissions -> Scopes



