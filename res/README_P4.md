### About/Overview

The solution mainly contains two parts: dungeon, player. Specifically, the dungeon has tunnels/caves and tunnels/caves have directions which is an Enum with four types. Future controller can interact with the model by using ModelFacade in the the dungeon package.

For the controller part, `DungeonConsoleController` is the center of the whole controller, and it will call `Move`, `PickUp`, `Shoot` in controller.command. By seperating three actions from the controller, the `DungeonConsoleControlle`r is more readable and concise. Furthermore, if we would like to add more actions in the future, We can just write more class in the controller.command, and add more cases to the `DungeonConsoleController` class without changing the controller to much.

### List of features

- Making treasures as Enum to simplify the model;
- Using Direction as the direction signs, and there are four Enum types of directions;
- Apply facade as the connection between the controller and the model;
- Seperating actions from the controller.

### How To Run

```bash
cd /res/artifacts/Dungeon_jar
java -jar Dungeon.jar
```

**How to Use the Program.** 

- Run the jar
- Than create the dungeon by following the instructions
- After creating a dungeon successfully, input C to continue
- Do something by following the instructions
- Input S to shoot an arrow. Input M to move the player. Input P to pick items up.

### Description of Examples

Run 1 -- res/EatenDriverRun:

1. The player first move west and pick up a arrow
1. Then move toward east and north
1. Then pick up one treasure
1. Then move toward north by two steps
1. The player is eaten by Otyugh!

Run2 -- res/SuccessDriverRun:

1. The player first move west and pick up one arrow
1. Than travels through part of the dungeon and smells someting
1. Shoot toward north by two steps to kill the monster
1. Goes west and smell smoething terribile again
1. shoot toward north, the arrow is crooked and hit an Otyugh
1. Then shoot another arrow to kill the injured Otyugh
1. Then go toward noth and west
1. The player finally find the way to the goal.

### Design/Model Changes

- Add Monster interface and its concrete class: Otyugh
- Add controller interface and concrete class and three actions classes:Move, PickUp, Shoot. I seperate actions from the console controller class.
- Add arrows and monsters in the location node.
- Add arrow in the player class.
- Add shooting method in the concrete dungeon class.

### Assumptions

- I assume that when players choose to pickup what they found, they pickup everything without choosing what to pick.
- I assume the player will input only one string, except for deciding the size of the dungeon.
- I assume the whole program is over when the player is eaten or reach at the goal.

### Limitations

The controller is not taking the whole line of the user input, but implemented the same as what we did in the lab. This means that if the player input a sequence of string, and the following will be consider for following steps' inputs, which is not reasonable.

### Citation

- Effective Java item 34: Use enmus instead of int constants
- Effective Java item 64: Refer to objects by their interfaces
- Head First Design Patterns: Command pattern in Chapter 7, pp 191 -- 233
