# Crusalis utils server

NOTE: use nodes with this plugin, or it will implode

## COMMANDS
`/crusalis_utils help` - unused as of writing

`/crusalis_utils commstick` (OP only) - gives the player a "commstick" which is the server-side alternative to location pings
the player right/left clicks and the block they are looking at gets the glow effect for every nation member within render distance.
The duration of the blocks glow can be changed in the `config.yml`

`/crusalis_utils teaminfo` (OP only) -  DEPRECATED gives the player an item that when held creates coloured particles above every player with a colour:
RED = enemy, GREEN = nation , BLUE = ally, YELLOW = NEUTRAL  (This has been replaced with the shift to highlight function)

`/na <message>` (Nation leaders only) Sends the message to every online nation member as a hotbar message

`/income_summary` (Officer and above) Shows the player all the contents of their town's income in a chat message

## Shift to highlight
When a player shifts the game automatically adds coloured client sided glow to every player within their line of sight 
the colour corresponds to the players relation ie: ENEMY = RED, NATION = GREEN etc.
The duration of the glow effect is `5 seconds` by default but can be changed in the `config.yml`