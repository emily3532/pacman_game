# pacman_game
INFO1113 Assignment
Similar to the PacMan arcade game, this game utilises the same design and AI of the ghosts.

The mode the Ghost is in determines what the target location is:
- If the Ghost is in SCATTER mode, the target location is the closest corner of the map
- If the Ghost is in CHASE mode, the target location is the grid space occupied by Waka

# to run
gradle build
gradle run


move around the screen using up, down, left and right arrow keys
press the space bar for DEBUG mode which draws a white line from the ghost to their target locations 