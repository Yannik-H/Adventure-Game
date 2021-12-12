### About/Overview

This project provides user options for using text based version or GUI version when playing the game. The text based version is basically implemented in project4. In this project 5, I accomplished the design of the GUI version.

Users can set the size of the dungeon, the interconnectivity, whether it is wrapping or not, the percentage of caves that have treasure, and the number of Otyughs through one or more items on a `JMenu`. Furthermore, I provide an option for restarting the game as a new game with a new dungeon or reusing the same dungeon through two items on a `JMenuBar`. In the GUI, user can see the structure of the dungeon on the screen. The view begins with a mostly blank screen and display only the pieces of the maze that have been revealed by the user's exploration of the caves and tunnels. Dungeons that are bigger than the area allocated it to the screen, and I provide the ability to scroll the view by applying `JScrollPane`.



Users can move through the dungeon using a mouse click on the screen in addition to the keyboard arrow keys. A click on an invalid space in the game would not advance the player. The player can press specific key to shoot an arrow or pick up what they found.

### List of features

- Consise and user-friendly GUI;
- Prompt message in a conspicuous text area for telling users about what's going on;
- Scrolling pane for both text area and game area;
- User can have straight feelings by viewing the stucture of the dungeon(visited part).

### How To Run

```bash
cd /res/artifacts/Dungeon_jar
java -jar Dungeon.jar text // for text based game
java -jar Dungeon.jar gui // for GUI based game
```

**How to Use the Program.** 

- In gui verision, you have to press setting buttion in the menu, and input parameterss to create the dungeon you like;
- If your input is legal and create the dungeon successfully, you can se the game board on the right side of the window;
- The you can click units of dungeon near to where you are to move the warrior, or you can press arrow keys to move;
- If you found something, you can press P to pick them up;
- If you are closed to Otyugh, you can see stench(green and smoke-like stuff) around the unit. There are two kind of stench here, if the color is deeper green, meaning that you are very dangerous;
- You can press S to shoot an arrow, then press an arrow key to denote the direction for the arrow, then you can see a window poping up asking for how far the arrows should go;
- If you precisely hit a Otyugh, you can see messags in the text area saying that you hear a great howl, otherwise you can only see message like the arrow goes in to darkness;
- If the game is over, the text area will tell you.

### Description of Examples

Run 1 -- res/Example_Pictures:

1. The Starting image shows what it looks like originally;
1. The Setting shows the setting window;
1. The GameInitialized shows what it looks like after setting;
1. The MoveAndPick shows the player move east and pick up an arrow;
1. The SeveralMove shows the player is very closed to the Otyugh after several step;
1. The Shooting shows the player is shooting an arrow;
1. The AfterShooting shows the player hit a Otyugh.

### Design/Model Changes

- Add GUI controller;
- Add GUI viewer, which is the main part.

### Assumptions

- I assume that when players choose to pickup what they found, they pickup everything without choosing what to pick.
- I assume the player will input only one string, except for deciding the size of the dungeon.
- I assume the whole program is over when the player is eaten or reach at the goal.

### Limitations

The code is not concise enough. For example, I should use filter or map or reduce or folder that I learning to simplify the codes between console based controller and GUI controller. Currently, differences between two controller generate lots of redundant methods.

### Citation

- Effective Java item 34: Use enmus instead of int constants
- Effective Java item 64: Refer to objects by their interfaces
- Head First Design Patterns: Command pattern in Chapter 7, pp 191 -- 233
- Head First Design Patterns: Model View Controller pattern in Ch 12, pp 526 - 531
