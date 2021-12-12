### About/Overview

The solution mainly contains two parts: dungeon, player. Specifically, the dungeon has tunnels/caves and tunnels/caves have directions which is an Enum with four types. Future controller can interact with the model by using ModelFacade in the the dungeon package.

### List of features

- Making treasures as Enum to simplify the model;
- Using Direction as the direction signs, and there are four Enum types of directions;
- Apply facade as the connection between future controller and current model

### How To Run

```bash
cd /res/artifacts/Dungeon_jar
java -jar Dungeon.jar
```

**How to Use the Program.** 

- Run the jar
- Than a player will travel from the start point to the goal point and collect treasures along the way;
- The result of the run should be the same as the res/wrappingDungeonRun.

### Description of Examples

Run 1 -- res/nonWrappingDungeonRun:

1. Creating a 4 x 6 non-wrapping dungeon with interconnectivity 8 and display it;
2. Showing Treasures in corresponding caves;
3. The player starts moving from the start point toward the goal point;
4. Once the player reaches at the goal point, the game is over

Run2 -- res/wrappingDungeonRun:

1. Creating a 4 x 6 wrapping dungeon with interconnectivity 8 and display it;
2. Showing Treasures in corresponding caves;
3. The player starts moving from the start point toward the goal point;
4. Once the player reaches at the goal point, the game is over

Run3 -- res/travelThroughRun:

1. Creating a 4 x 4 wrapping dungeon with interconnectivity 8 and display it;
2. Showing Treasures in corresponding caves;
3. The player start moving from the start point toward the goal point, but the player will visit every caves/tunnels deliberately this time;
4. The player will reach at the goal point finally.

### Design/Model Changes

- Add facade to connect controller and current model;
- Add interface for Dungeon;
- Add interface for Player;
- Modify Treasure to Enum;

### Assumptions

- I assume that controller will only interact with the model by facade;
- I assume that the structure of the dungeon will be create and fixed as soon as the dungeon object is instantiated;
- I assume everything is set when constructing the dungeon object, likes what percentage of caves should have treasures.

### Limitations

Although the Treasure is Enum type and it can simplify the model, the refactoring will be compicated if future needs required to add some methods for Treasure. Also, putting Treasure in the package dungeon is a bit strange. As for the interactive part, users will blow up the running program if they input a non-exist direction in the cave/tunnel.

### Citation

- Effective Java item 34: Use enmus instead of int constants
- Effective Java item 64: Refer to objects by their interfaces
