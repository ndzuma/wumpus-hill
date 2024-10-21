import os
import time
import platform
from itertools import count
from typing import List, Optional
from display import draw_grid, draw, clearScreen, drawEndGame, drawStartGame
import random


class GameState():
    playersLocation = (0, 0)
    newPlayersLocation = (0, 0)
    playersPoints = 100
    movesTaken = 0
    playersGold = 0
    playersAvailableShots = 0
    hurdleCount = 0
    goldCount = 0
    hintCount = 0

    gridSize: int
    grid: List[List]

class Position():
    isPlayer = False
    isWumpus = False
    isGold = False
    isPit = False
    isBreeze = False
    isStench = False
    isStartPosition = False
    hasVisited = False

    def __init__(
        self,
        isPlayer: Optional[bool] = False,
        isWumpus: Optional[bool] = False,
        isGold: Optional[bool] = False,
        isPit: Optional[bool] = False,
        isBreeze: Optional[bool] = False,
        isStench: Optional[bool] = False,
        isStartPosition: Optional[bool] = False,
        hasVisited: Optional[bool] = False
    ):
        self.isPlayer = isPlayer
        self.isWumpus = isWumpus
        self.isGold = isGold
        self.isPit = isPit
        self.isBreeze = isBreeze
        self.isStench = isStench
        self.isStartPosition = isStartPosition
        self.hasVisited = hasVisited

    def hasBeenVisited(self):
        self.hasVisited = True

    def placePlayer(self):
        self.isPlayer = True

    def removePlayer(self):
        self.isPlayer = False

    def removeGold(self):
        self.isGold = False

    def addStench(self):
        self.isStench = True

    def addBreeze(self):
        self.isBreeze = True

    def isClear(self, semiClear: Optional[bool]=False):
        # semiClear is used to check if the position is clear excluding the stench and breeze attributes
        if semiClear:
            return not self.isPlayer and not self.isWumpus and not self.isGold and not self.isPit  and not self.isStartPosition
        return not self.isPlayer and not self.isWumpus and not self.isGold and not self.isPit and not self.isBreeze and not self.isStench and not self.isStartPosition

def generateGrid(size):
    grid = [[Position() for _ in range(size)] for _ in range(size)]
    xAxis = random.randint(0, size - 1)
    if xAxis == 0 or xAxis == size - 1:
        yAxis = random.randint(0, size - 1)
    else:
        yAxis = random.choice([0, size - 1])
    grid[xAxis][yAxis] = Position(isPlayer=True, isStartPosition=True, hasVisited=True)
    playersLocation = (xAxis, yAxis)
    return grid, playersLocation

def generateHurdles(grid, gridSize):
    # 25% of the grid will have a hurdle
    maxHurdles = int((gridSize**2)*0.25)
    hurdleCount = 0
    clearZone = []

    for i in range(maxHurdles):
        while True:
            xAxis = random.randint(0, gridSize - 1)
            yAxis = random.randint(0, gridSize - 1)
            if grid[xAxis][yAxis].isClear():
                hurdleCount += 1
                break

        hurdle = random.randint(1, 2)
        if hurdle== 1:
            grid[xAxis][yAxis] = Position(isWumpus=True)
            if xAxis > 0:
                grid[xAxis-1][yAxis].addStench()
            if xAxis < gridSize - 1:
                grid[xAxis+1][yAxis].addStench()
            if yAxis > 0:
                grid[xAxis][yAxis-1].addStench()
            if yAxis < gridSize - 1:
                grid[xAxis][yAxis+1].addStench()
        else:
            grid[xAxis][yAxis] = Position(isPit=True)
            if xAxis > 0:
                grid[xAxis-1][yAxis].addBreeze()
            if xAxis < gridSize - 1:
                grid[xAxis+1][yAxis].addBreeze()
            if yAxis > 0:
                grid[xAxis][yAxis-1].addBreeze()
            if yAxis < gridSize - 1:
                grid[xAxis][yAxis+1].addBreeze()

    return grid, hurdleCount

def generateGold(grid, gridSize):
    maxGold = int((gridSize**2)*0.05)
    if maxGold <= 1:
        # At least 1 gold should be present
        maxGold = random.randint(1, 2)
    goldCount = 0

    for i in range(maxGold):
        while True:
            xAxis = random.randint(0, gridSize - 1)
            yAxis = random.randint(0, gridSize - 1)
            if grid[xAxis][yAxis].isClear(True):
                goldCount += 1
                break

        grid[xAxis][yAxis] = Position(isGold=True)

    return grid, goldCount

def getCurrentOS():
    type = platform.system()
    if type == "Windows":
        return "w"
    elif type == "Linux":
        return "l"
    else:
        return "m"

def getUserInput():
    # Get immediate user input, depending on the OS
    currentOs = getCurrentOS()
    if currentOs == "w":
        import msvcrt
        while True:
                if msvcrt.kbhit():  # Check if a key has been pressed
                    first_char = msvcrt.getch()
                    if first_char == b'\xe0':  # Special keys (like arrow keys)
                        second_char = msvcrt.getch()
                        if second_char == b'H':  # Up arrow key
                            return 'UP'
                        elif second_char == b'P':  # Down arrow key
                            return 'DOWN'
                    else:
                        return first_char.decode('utf-8').upper()  # Return uppercase for consistency
    else:
        import sys, tty, termios
        fd = sys.stdin.fileno()
        old_settings = termios.tcgetattr(fd)
        try:
            tty.setraw(fd)
            ch = sys.stdin.read(1)
            if ch == '\x1b':  # Escape sequence (arrow keys)
                ch += sys.stdin.read(2)  # Read the next two characters
                if ch == '\x1b[A':  # Up arrow key
                    return 'UP'
                elif ch == '\x1b[B':  # Down arrow key
                    return 'DOWN'
            else:
                return ch.upper()
        finally:
            termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)

def movePlayer(State, move):
    # Game controls
    # w = up, s = down, a = left, d = right
    if move == "W":
        if State.playersLocation[0] == 0:
            print("You can't move up")
        else:
            State.newPlayersLocation = (State.playersLocation[0] - 1, State.playersLocation[1])
    elif move == "S":
        if State.playersLocation[0] == State.gridSize - 1:
            print("You can't move down")
        else:
            State.newPlayersLocation = (State.playersLocation[0] + 1, State.playersLocation[1])
    elif move == "A":
        if State.playersLocation[1] == 0:
            print("You can't move left")
        else:
            State.newPlayersLocation = (State.playersLocation[0], State.playersLocation[1] - 1)
    elif move == "D":
        if State.playersLocation[1] == State.gridSize - 1:
            print("You can't move right")
        else:
            State.newPlayersLocation = (State.playersLocation[0], State.playersLocation[1] + 1)
    elif move == "Q":
        print("Are you sure you want to quit? (y/n)")
        quitGame = getUserInput()
        if quitGame == "\r" or quitGame == "\n" or quitGame == "Y":
            print("Game Over!")
            os._exit(1)
    elif move == "R":
        pass
    elif move == "E":
        print("Hints:")
        print(f"Number of dangers left: {State.hurdleCount}")
        print(f"Number of gold left: {State.goldCount}")
        State.hintCount += 1
        time.sleep(2)
    else:
        print("Invalid move")

def update(State):
    newPlayerLocation = State.newPlayersLocation
    playerLocation = State.playersLocation
    grid = State.grid

    def move():
        grid[playerLocation[0]][playerLocation[1]].removePlayer()
        grid[newPlayerLocation[0]][newPlayerLocation[1]].placePlayer()
        grid[newPlayerLocation[0]][newPlayerLocation[1]].hasBeenVisited()
        State.playersLocation = newPlayerLocation
        State.playersPoints -= 1
        State.movesTaken += 1

    if grid[newPlayerLocation[0]][newPlayerLocation[1]].isClear(True):
        move()
    elif grid[newPlayerLocation[0]][newPlayerLocation[1]].isWumpus:
        move()
        print("You were eaten by the Wumpus!")
        State.playersPoints -= 10000
        drawEndGame(State)
        os._exit(1)
    elif grid[newPlayerLocation[0]][newPlayerLocation[1]].isPit:
        move()
        print("You fell into a pit!")
        State.playersPoints -= 10000
        drawEndGame(State)
        os._exit(1)
    elif grid[newPlayerLocation[0]][newPlayerLocation[1]].isGold:
        move()
        grid[newPlayerLocation[0]][newPlayerLocation[1]].removeGold()
        State.playersPoints += 1000
        State.goldCount -= 1
        State.playersGold += 1
        draw(State, True)
    elif grid[newPlayerLocation[0]][newPlayerLocation[1]].isStartPosition and not grid[playerLocation[0]][playerLocation[1]].isStartPosition:
        move()
        print("You're back to the start!")
        print("You just quit mid game, mateee!")
        drawEndGame(State)
        os._exit(1)
    else:
        print("You can't move there, mate!")

def start(state):
    options = ["3X3", "5X5", "10X10", "15X15"]
    option = 0
    while True:
        drawStartGame(state, options, option)
        input = getUserInput()
        if input == "W" or input == "UP":
            if option == 0:
                option = len(options) - 1
            else:
                option -= 1
        elif input == "S" or input == "DOWN":
            if option == len(options) - 1:
                option = 0
            else:
                option += 1
        elif input == "Q":
            print("Are you sure you want to quit? (y/n)")
            quitGame = getUserInput()
            if quitGame == "\r" or quitGame == "\n" or quitGame == "Y":
                print("Game Over!")
                os._exit(1)
        elif input == "\r" or input == "\n":
            state.gridSize = int(options[option].split("X")[0])
            break

def main():
    state = GameState()
    start(state)
    # make sure the grid has at least a 3x3 grid size, for it to work properly
    state.grid, playerLocation = generateGrid(state.gridSize)
    state.grid, state.goldCount = generateGold(state.grid, state.gridSize)
    state.playersLocation, state.newPlayersLocation = playerLocation, playerLocation
    state.grid, state.hurdleCount = generateHurdles(state.grid, state.gridSize)

    while True:
        draw(state)
        move = getUserInput()
        movePlayer(state, move)
        update(state)


if __name__ == "__main__":
    main()
